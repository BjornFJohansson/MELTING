package melting.modifiedNucleicAcidMethod;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Asanuma05Azobenzene extends PartialCalcul{

	/*Asanuma et al. (2005). Nucleic acids Symposium Series 49 : 35-36 */
	
	public static String defaultFileName = "Asanuma2005azobenmn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		Thermodynamics azobenzeneValue = this.collector.getAzobenzeneValue(sequences.getSequence(pos1, pos2,"dna"), sequences.getComplementary(pos1, pos2,"dna"));
		
		OptionManagement.meltingLogger.log(Level.FINE, "The azobenzene thermodynamic parameters are from Asanuma et al. (2005) : ");
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " : enthalpy = " + azobenzeneValue.getEnthalpy() + "  entropy = " + azobenzeneValue.getEntropy());

		double enthalpy = result.getEnthalpy() + azobenzeneValue.getEnthalpy();
		double entropy = result.getEnthalpy() + azobenzeneValue.getEntropy();
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		
		NucleotidSequences modified = new NucleotidSequences(environment.getSequences().getSequence(pos1, pos2), environment.getSequences().getComplementary(pos1, pos2));
		
		if (environment.getHybridization().equals("dnadna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for azobenzene of" +
					"Asanuma (2005) are established for DNA sequences.");
			
			environment.modifieSequences(environment.getSequences().getSequence(pos1, pos2, "dna"), environment.getSequences().getSequence(pos1, pos2, "dna"));

		}
		
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (modified.calculateNumberOfTerminal('X', '-') > 0){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamics parameters for azobenzene of " +
					"Asanuma (2005) are not established for terminal benzenes.");
			isApplicable = false;
		}
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		if (this.collector.getAzobenzeneValue(sequences.getSequence(pos1, pos2),sequences.getComplementary(pos1, pos2)) == null){
			return true;
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}

}
