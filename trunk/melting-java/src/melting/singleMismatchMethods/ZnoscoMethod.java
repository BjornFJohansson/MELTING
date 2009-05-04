package melting.singleMismatchMethods;

import java.util.HashMap;

import melting.DataCollect;
import melting.Helper;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;

public abstract class ZnoscoMethod implements PartialCalculMethod{

	protected DataCollect collector;
	
	public DataCollect getCollector(){
		return this.collector;
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		String seq1 = convertToPyr_Pur(seq.substring(pos1, pos2));
		String complementarySeq = convertToPyr_Pur(seq.substring(pos1, pos2));
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
		boolean isApplicable = true;
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (hybridization.equals("dnadna") == false){
			System.out.println("WARNING : the single mismatches parameter of " +
					"Znosco et al. are originally established " +
					"for RNA duplexes.");
			
			isApplicable = false;
		}
		
		if (isMissingParameters(seq1, seq2, pos1, pos2)){
			System.out.println("WARNING : some thermodynamic parameters are missing.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		
		String seq = convertToPyr_Pur(seq1.substring(pos1, pos2+1));
		String complementarySeq = convertToPyr_Pur(seq2.substring(pos1, pos2+1));
			
			if (this.collector.getMismatchvalue(seq, complementarySeq) == null){
				return true;
			}
			
			if (this.collector.getMismatchParameterValue(seq.substring(pos1+1,pos1+2), complementarySeq.substring(pos1+1, pos1+2)) == null){
				return true;
			}
			
			//if (this.collector.getClosureValue(seq.su)){
				
			//}
			
		return false;
	}
	
	private String convertToPyr_Pur(String sequence){
		StringBuffer newSeq = new StringBuffer(sequence.length());
		
		for (int i = 0; i < sequence.length(); i++){
			switch (sequence.charAt(i)) {
			case 'A':
				newSeq.append('R');
				break;
			case 'G':
				newSeq.append('R');
				break;
			case 'U':
				newSeq.append('Y');
				break;
			case 'C':
				newSeq.append('Y');
				break;

			default:
				System.err.println("There are non watson crick bases in the sequence.");
				break;
			}
		}
		return newSeq.toString();
	}

}
