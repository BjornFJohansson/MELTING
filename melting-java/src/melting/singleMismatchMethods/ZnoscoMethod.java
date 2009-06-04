package melting.singleMismatchMethods;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public abstract class ZnoscoMethod extends PartialCalcul{
	
	protected static String formulaEnthalpy = "delat H = H(single mismatch N/N) + number AU closing x H(closing AU) + number GU closing x H(closing GU) + H(NNN intervening)";
	protected static String formulaEntropy = "delat S = S(single mismatch N/N) + number AU closing x S(closing AU) + number GU closing x S(closing GU) + S(NNN intervening)";
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		Thermodynamics mismatchValue = this.collector.getMismatchParameterValue(sequences.getSequence(pos1 + 1, pos2), sequences.getComplementary(pos1 + 1, pos2));
		Thermodynamics NNNeighboringValue = this.collector.getMismatchvalue(NucleotidSequences.convertToPyr_Pur(sequences.getSequence(pos1, pos2)), sequences.getComplementary(pos1, pos2));
		
		OptionManagement.meltingLogger.log(Level.INFO, "N/N mismatch " + sequences.getSequence(pos1 + 1, pos2) + "/" + sequences.getComplementary(pos1 + 1, pos2) + " : enthalpy = " + mismatchValue.getEnthalpy() + "  entropy = " + mismatchValue.getEntropy());
		OptionManagement.meltingLogger.log(Level.INFO, "NNN intervening  " + NucleotidSequences.convertToPyr_Pur(sequences.getSequence(pos1, pos2)) + "/" + sequences.getComplementary(pos1, pos2) + " : enthalpy = " + NNNeighboringValue.getEnthalpy() + "  entropy = " + NNNeighboringValue.getEntropy());
		
		double enthalpy = result.getEnthalpy() + mismatchValue.getEnthalpy() + NNNeighboringValue.getEnthalpy();
		double entropy = result.getEntropy() + mismatchValue.getEntropy() + NNNeighboringValue.getEntropy();
		
		NucleotidSequences mismatch = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		int numberAU = mismatch.calculateNumberOfTerminal('A', 'U');
		int numberGU = mismatch.calculateNumberOfTerminal('G', 'U');
		
		if (numberAU > 0){
			Thermodynamics closingAU = this.collector.getClosureValue("A", "U");
			
			OptionManagement.meltingLogger.log(Level.INFO, numberAU + " x AU closing : enthalpy = " + closingAU.getEnthalpy() + "  entropy = " + closingAU.getEntropy());

			enthalpy += numberAU * closingAU.getEnthalpy();
			entropy += numberAU * closingAU.getEntropy();
		}
		if (numberGU > 0){
			Thermodynamics closingGU = this.collector.getClosureValue("G", "U");

			OptionManagement.meltingLogger.log(Level.INFO, numberGU + " x GU closing : enthalpy = " + closingGU.getEnthalpy() + "  entropy = " + closingGU.getEntropy());

			enthalpy += numberAU * closingGU.getEnthalpy();
			entropy += numberAU * closingGU.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		
		if (environment.getHybridization().equals("dnadna") == false){

			OptionManagement.meltingLogger.log(Level.WARNING, "The single mismatches parameter of " +
					"Znosco et al. are originally established " +
					"for RNA duplexes.");
		}
		
		return super.isApplicable(environment, pos1, pos2);
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences mismatch = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		
			if (this.collector.getMismatchvalue(NucleotidSequences.convertToPyr_Pur(sequences.getSequence(pos1, pos2)), NucleotidSequences.convertToPyr_Pur(sequences.getComplementary(pos1, pos2))) == null){
				return true;
			}
			
			if (this.collector.getMismatchParameterValue(sequences.getSequence(pos1 + 1, pos2), sequences.getComplementary(pos1 + 1, pos2)) == null){
				return true;
			}
			
			if (mismatch.calculateNumberOfTerminal('A', 'U') > 0){
				if (this.collector.getClosureValue("A", "U") == null){
					return true;
				}
			}
			
			if (mismatch.calculateNumberOfTerminal('G', 'U') > 0){
				if (this.collector.getClosureValue("G", "U") == null){
					return true;
				}
			}
			
		return super.isMissingParameters(sequences, pos1, pos2);
	}

}
