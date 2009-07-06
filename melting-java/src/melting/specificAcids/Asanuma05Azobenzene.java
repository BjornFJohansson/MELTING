	
/*Asanuma et al. (2005). Nucleic acids Symposium Series 49 : 35-36 */

package melting.specificAcids;

import java.util.logging.Level;

import Sequences.NucleotidSequences;

import melting.Environment;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Asanuma05Azobenzene extends PartialCalcul{
	
	public static String defaultFileName = "Asanuma2005azobenmn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		Thermodynamics azobenzeneValue = this.collector.getAzobenzeneValue(sequences.getSequence(pos1, pos2,"dna"), sequences.getComplementary(pos1, pos2,"dna"));
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The azobenzene model is from Asanuma et al. (2005) : ");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " : enthalpy = " + azobenzeneValue.getEnthalpy() + "  entropy = " + azobenzeneValue.getEntropy());

		double enthalpy = result.getEnthalpy() + azobenzeneValue.getEnthalpy();
		double entropy = result.getEntropy() + azobenzeneValue.getEntropy();
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);

		int [] positions = correctPositions(pos1, pos2, environment.getSequences().getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences modified = environment.getSequences().getEquivalentSequences("dna");
		
		if (environment.getHybridization().equals("dnadna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for azobenzene of" +
					"Asanuma (2005) are established for DNA sequences.");
		}

		if (modified.calculateNumberOfTerminal("X", " ", pos1, pos2) > 0){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamics parameters for azobenzene of " +
					"Asanuma (2005) are not established for terminal benzenes.");
			isApplicable = false;
		}
		return isApplicable;
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		if (this.collector.getAzobenzeneValue(newSequences.getSequence(pos1,pos2),newSequences.getComplementary(pos1,pos2)) == null){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + newSequences.getSequence(pos1,pos2) + "/" + newSequences.getComplementary(pos1,pos2)+ 
			" are missing. Check the azobenzene parameters.");
			return true;
		}
		return super.isMissingParameters(newSequences, pos1, pos2);
	}

	private int[] correctPositions(int pos1, int pos2, int duplexLength){
		if (pos1 > 0){
			pos1 --;
		}
		if (pos2 < duplexLength - 1){
			pos2 ++;
		}
		int [] positions = {pos1, pos2};
		return positions;
	}
}
