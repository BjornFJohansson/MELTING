package melting.singleMismatchMethods;

import java.util.HashMap;

import melting.Helper;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public abstract class ZnoscoMethod extends PartialCalcul{
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		String seq1 = Helper.convertToPyr_Pur(seq.substring(pos1, pos2));
		String complementarySeq = Helper.convertToPyr_Pur(seq.substring(pos1, pos2));
		Thermodynamics parameter = new Thermodynamics(0,0);
		Thermodynamics NNInteraction = new Thermodynamics(0,0);
		Thermodynamics AUPenalty = new Thermodynamics(0,0);
		Thermodynamics GUPenalty = new Thermodynamics(0,0);
		int numberAU = 0;
		int numberGU = 0;
		
		if ((seq.charAt(pos1) == 'A' || seq.charAt(pos1) == 'U') && Helper.isComplementaryBasePair(seq.charAt(pos1), seq2.charAt(pos1))){
			numberAU++;
		}
		else if ((seq.charAt(pos1) == 'G' && complementarySeq.charAt(pos1) == 'U') || (seq.charAt(pos1) == 'G' && complementarySeq.charAt(pos1) == 'U')){
			numberGU++;
		}
		
		if ((seq.charAt(pos2) == 'A' || seq.charAt(pos2) == 'U') && Helper.isComplementaryBasePair(seq.charAt(pos2), seq2.charAt(pos2))){
			numberAU++;
		}
		else if ((seq.charAt(pos2) == 'G' && complementarySeq.charAt(pos2) == 'U') || (seq.charAt(pos2) == 'G' && complementarySeq.charAt(pos2) == 'U')){
			numberGU++;
		}
		 
		parameter = collector.getMismatchParameterValue(seq1.substring(1, 2), complementarySeq.substring(1, 2));
		NNInteraction = collector.getMismatchvalue(seq1, complementarySeq);
		AUPenalty = collector.getClosureValue("A", "U");
		AUPenalty = collector.getClosureValue("G", "U");
		
		result.setEnthalpy(result.getEnthalpy() + parameter.getEnthalpy() + NNInteraction.getEnthalpy() + numberAU * AUPenalty.getEnthalpy() + numberGU * GUPenalty.getEnthalpy());
		result.setEntropy(result.getEntropy() + parameter.getEntropy() + NNInteraction.getEntropy() + numberAU * AUPenalty.getEntropy() + numberGU * GUPenalty.getEntropy());
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = super.isApplicable(options, pos1, pos2);
		
		if (hybridization.equals("dnadna") == false){
			System.out.println("WARNING : the single mismatches parameter of " +
					"Znosco et al. are originally established " +
					"for RNA duplexes.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		
		String seq = Helper.convertToPyr_Pur(seq1.substring(pos1, pos2+1));
		String complementarySeq = Helper.convertToPyr_Pur(seq2.substring(pos1, pos2+1));
			
			if (this.collector.getMismatchvalue(seq, complementarySeq) == null){
				return true;
			}
			
			if (this.collector.getMismatchParameterValue(seq.substring(pos1+1,pos1+2), complementarySeq.substring(pos1+1, pos1+2)) == null){
				return true;
			}
			
			if (((seq.charAt(pos1) == 'A' || seq.charAt(pos1) == 'U') && Helper.isComplementaryBasePair(seq.charAt(pos1), complementarySeq.charAt(pos1))) || ((seq.charAt(pos2) == 'A' || seq.charAt(pos2) == 'U') && Helper.isComplementaryBasePair(seq.charAt(pos2), complementarySeq.charAt(pos2)))){
				if (this.collector.getClosureValue("A", "U") == null){
					return true;
				}
			}
			
			if (((seq.charAt(pos1) == 'G' && complementarySeq.charAt(pos1) == 'U') || (seq.charAt(pos1) == 'U' && complementarySeq.charAt(pos1) == 'G')) || ((seq.charAt(pos2) == 'G' && complementarySeq.charAt(pos2) == 'U') ||( seq.charAt(pos2) == 'U' && complementarySeq.charAt(pos2) == 'G'))){
				if (this.collector.getClosureValue("G", "U") == null){
					return true;
				}
			}
			
		return super.isMissingParameters(seq1, seq2, pos1, pos2);
	}

}
