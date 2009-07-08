package melting.patternModels.singleMismatch;


import java.util.logging.Level;


import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.patternModels.PatternComputation;
import melting.sequences.NucleotidSequences;

public abstract class ZnoskoMethod extends PatternComputation{
	
	protected static String formulaEnthalpy = "delat H = H(single mismatch N/N) + number AU closing x H(closing AU) + number GU closing x H(closing GU) + H(NNN intervening)";
	
	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		Thermodynamics mismatchValue = this.collector.getMismatchParameterValue(sequences.getSequence(pos1 + 1, pos1 + 1), sequences.getComplementary(pos1 + 1, pos1 + 1));
		if (mismatchValue == null){
			mismatchValue = new Thermodynamics(0,0);
		}
		Thermodynamics NNNeighboringValue = this.collector.getMismatchValue(NucleotidSequences.convertToPyr_Pur(sequences.getSequence(pos1, pos2)), NucleotidSequences.convertToPyr_Pur( sequences.getComplementary(pos1, pos2)));
		if (NNNeighboringValue == null){
			NNNeighboringValue = new Thermodynamics(0,0);
		}
		OptionManagement.meltingLogger.log(Level.FINE, "N/N mismatch " + sequences.getSequence(pos1 + 1, pos1 + 1) + "/" + sequences.getComplementary(pos1 + 1, pos1 + 1) + " : enthalpy = " + mismatchValue.getEnthalpy() + "  entropy = " + mismatchValue.getEntropy());
		OptionManagement.meltingLogger.log(Level.FINE, "NNN intervening  " + NucleotidSequences.convertToPyr_Pur(sequences.getSequence(pos1, pos2)) + "/" + NucleotidSequences.convertToPyr_Pur(sequences.getComplementary(pos1, pos2)) + " : enthalpy = " + NNNeighboringValue.getEnthalpy() + "  entropy = " + NNNeighboringValue.getEntropy());
		
		double enthalpy = result.getEnthalpy() + mismatchValue.getEnthalpy() + NNNeighboringValue.getEnthalpy();
		double entropy = result.getEntropy() + mismatchValue.getEntropy() + NNNeighboringValue.getEntropy();
		
		NucleotidSequences mismatch = sequences.getEquivalentSequences("rna");
		
		double numberAU = mismatch.calculateNumberOfTerminal("A", "U", pos1, pos2);
		double numberGU = mismatch.calculateNumberOfTerminal("G", "U", pos1, pos2);
		
		if (numberAU > 0){
			Thermodynamics closingAU = this.collector.getClosureValue("A", "U");
			
			OptionManagement.meltingLogger.log(Level.FINE, numberAU + " x AU closing : enthalpy = " + closingAU.getEnthalpy() + "  entropy = " + closingAU.getEntropy());

			enthalpy += numberAU * closingAU.getEnthalpy();
			entropy += numberAU * closingAU.getEntropy();
		}
		if (numberGU > 0){
			Thermodynamics closingGU = this.collector.getClosureValue("G", "U");

			OptionManagement.meltingLogger.log(Level.FINE, numberGU + " x GU closing : enthalpy = " + closingGU.getEnthalpy() + "  entropy = " + closingGU.getEntropy());

			enthalpy += numberAU * closingGU.getEnthalpy();
			entropy += numberAU * closingGU.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		if (environment.getHybridization().equals("rnarna") == false){

			OptionManagement.meltingLogger.log(Level.WARNING, "The single mismatches parameter of " +
					"Znosco et al. are originally established " +
					"for RNA duplexes.");
		}
		
		return super.isApplicable(environment, pos1, pos2);
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences mismatch = sequences.getEquivalentSequences("rna");
		
			if (mismatch.calculateNumberOfTerminal("A", "U", pos1, pos2) > 0){
				if (this.collector.getClosureValue("A", "U") == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The parameters for AU closing base pair are missing. Check the single mismatch parameters.");

					return true;
				}
			}
			
			if (mismatch.calculateNumberOfTerminal("G", "U", pos1, pos2) > 0){
				if (this.collector.getClosureValue("G", "U") == null){
					OptionManagement.meltingLogger.log(Level.WARNING, "The parameters for GU closing base pair are missing. Check the single mismatch parameters.");

					return true;
				}
			}
		return super.isMissingParameters(mismatch, pos1, pos2);
	}

	protected int[] correctPositions(int pos1, int pos2, int duplexLength){
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
