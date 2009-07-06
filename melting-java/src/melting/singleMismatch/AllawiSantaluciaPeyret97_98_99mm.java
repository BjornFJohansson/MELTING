/*REF: Allawi and SantaLucia (1997). Biochemistry 36: 10581-10594. 
	REF: Allawi and SantaLucia (1998). Biochemistry 37: 2170-2179.
	REF: Allawi and SantaLucia (1998). Nuc Acids Res 26: 2694-2701. 
	REF: Allawi and SantaLucia (1998). Biochemistry 37: 9435-9444.
	REF: Peyret et al. (1999). Biochemistry 38: 3468-3477*/

package melting.singleMismatch;

import java.util.logging.Level;

import Sequences.NucleotidSequences;

import melting.Environment;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class AllawiSantaluciaPeyret97_98_99mm extends PartialCalcul{
	

	public static String defaultFileName = "AllawiSantaluciaPeyret1997_1998_1999mm.xml";
	
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
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for single mismatches is from Allawi, Santalucia and Peyret. (1997, 1998, 1999) : ");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		Thermodynamics mismatchValue;
		for (int i = pos1; i < pos2; i++){
			mismatchValue =  collector.getMismatchValue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i));
			
			OptionManagement.meltingLogger.log(Level.FINE, newSequences.getSequenceNNPair(i) + "/" + newSequences.getComplementaryNNPair(i) + " : enthalpy = " + mismatchValue.getEnthalpy() + "  entropy = " + mismatchValue.getEntropy());

			enthalpy += mismatchValue.getEnthalpy();
			entropy += mismatchValue.getEntropy();		
		}

		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1, int pos2) {

		if (environment.getHybridization().equals("dnadna") == false){
				
			OptionManagement.meltingLogger.log(Level.WARNING, "The single mismatch parameters of " +
					"Allawi, Santalucia and Peyret are originally established " +
					"for DNA duplexes.");
		}
		
		return super.isApplicable(environment, pos1, pos2);
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		for (int i = pos1; i < pos2; i++){
			if (this.collector.getMismatchValue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i)) == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + newSequences.getSequenceNNPair(i) + "/" + newSequences.getComplementaryNNPair(i) + " are missing. Check the single mismatch parameters");
				return true;
			}
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
