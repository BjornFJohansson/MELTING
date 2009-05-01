package singleMismatchMethods;

import java.util.HashMap;

import melting.DataCollect;
import melting.ThermoResult;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;

public abstract class ZnoscoMethod implements PartialCalculMethod{

	protected DataCollect collector;
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		return null;
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
			
		return false;
	}
	
	private String convertToPyr_Pur(String sequence){
		String newSeq = "";
		
		for (int i = 0; i < sequence.length(); i++){
			switch (sequence.charAt(i)) {
			case 'A':
				newSeq += 'R';
				break;
			case 'G':
				newSeq += 'R';
				break;
			case 'U':
				newSeq += 'Y';
				break;
			case 'C':
				newSeq += 'Y';
				break;

			default:
				System.err.println("There are non watson crick bases in the sequence.");
				break;
			}
		}
		return newSeq;
	}

}
