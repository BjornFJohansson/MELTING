
/*REF: Allawi and SantaLucia (1997). Biochemistry 36: 10581-10594. 
	REF: Allawi and SantaLucia (1998). Biochemistry 37: 2170-2179.
	REF: Allawi and SantaLucia (1998). Nuc Acids Res 26: 2694-2701. 
	REF: Allawi and SantaLucia (1998). Biochemistry 37: 9435-9444.
	REF: Peyret et al. (1999). Biochemistry 38: 3468-3477*/

package melting.patternModels.tandemMismatches;

import java.util.HashMap;
import java.util.logging.Level;


import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterMethods;
import melting.methodInterfaces.PatternComputationMethod;
import melting.patternModels.PatternComputation;
import melting.sequences.NucleotidSequences;

public class AllawiSantaluciaPeyret97_98_99tanmm extends PatternComputation{
	
	public static String defaultFileName = "AllawiSantaluciaPeyret1997_1998_1999tanmm.xml";
	
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
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for tandem mismatches is from Allawi, Santalucia and Peyret (1997, 1998, 1999)");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		Thermodynamics mismatchValue;
		for (int i = pos1; i < pos2; i++){
			mismatchValue = this.collector.getMismatchValue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i));
			
			OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequenceNNPair(pos1 + i) + "/" + sequences.getComplementaryNNPair(pos1 + i) + " : enthalpy = " + mismatchValue.getEnthalpy() + "  entropy = " + mismatchValue);

			enthalpy += mismatchValue.getEnthalpy();
			entropy += mismatchValue.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "\n The tandem mismatch parameters of " +
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
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameter for " + newSequences.getSequenceNNPair(i) + "/" + newSequences.getComplementaryNNPair(i) + " are missing. Check the parameters for tandem mismatches.");
				return true;
			}
		}
		return super.isMissingParameters(newSequences, pos1, pos2);
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);
		
		String singleMismatchName = options.get(OptionManagement.singleMismatchMethod);
		RegisterMethods register = new RegisterMethods();
		PatternComputationMethod singleMismatch = register.getPartialCalculMethod(OptionManagement.singleMismatchMethod, singleMismatchName);
		singleMismatch.initializeFileName(singleMismatchName);
		String fileSingleMismatch = singleMismatch.getDataFileName(singleMismatchName);
		
		loadFile(fileSingleMismatch, this.collector);
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
