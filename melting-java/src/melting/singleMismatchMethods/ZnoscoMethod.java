package melting.singleMismatchMethods;

import java.util.HashMap;

import melting.Environment;
import melting.Helper;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public abstract class ZnoscoMethod extends PartialCalcul{
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		double enthalpy = result.getEnthalpy() + this.collector.getMismatchParameterValue(sequences.getSequence(pos1 + 1, pos2), sequences.getComplementary(pos1 + 1, pos2)).getEnthalpy() + collector.getMismatchvalue(sequences.convertToPyr_Pur(sequences.getSequence(pos1, pos2)), sequences.getComplementary(pos1, pos2)).getEnthalpy();
		double entropy = result.getEntropy() + this.collector.getMismatchParameterValue(sequences.getSequence(pos1 + 1, pos2), sequences.getComplementary(pos1 + 1, pos2)).getEnthalpy() + collector.getMismatchvalue(sequences.convertToPyr_Pur(sequences.getSequence(pos1, pos2)), sequences.getComplementary(pos1, pos2)).getEnthalpy();
		
		NucleotidSequences mismatch = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		int numberAU = mismatch.calculateNumberOfTerminal('A', 'U');
		int numberGU = mismatch.calculateNumberOfTerminal('G', 'U');
		
		if (numberAU > 0){
			enthalpy += numberAU * this.collector.getClosureValue("A", "U").getEnthalpy();
			entropy += numberAU * this.collector.getClosureValue("A", "U").getEntropy();
		}
		if (numberGU > 0){
			enthalpy += numberAU * this.collector.getClosureValue("G", "U").getEnthalpy();
			entropy += numberAU * this.collector.getClosureValue("G", "U").getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("WARNING : the single mismatches parameter of " +
					"Znosco et al. are originally established " +
					"for RNA duplexes.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences mismatch = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		
			if (this.collector.getMismatchvalue(sequences.convertToPyr_Pur(sequences.getSequence(pos1, pos2)), sequences.convertToPyr_Pur(sequences.getComplementary(pos1, pos2))) == null){
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
