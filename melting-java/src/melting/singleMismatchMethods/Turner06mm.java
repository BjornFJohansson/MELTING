package melting.singleMismatchMethods;

import java.util.HashMap;

import melting.DataCollect;
import melting.Helper;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;

public class Turner06mm implements PartialCalculMethod{
	
	/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924.*/

	
	private DataCollect collector;
	
	public Turner06mm(){
		Helper.loadData("Turner1999_2006longmm.xml", this.collector);
	}

	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		Thermodynamics loopInitiation = this.collector.getInitiationLoopValue("2");
		
		if (((seq.charAt(pos1) == 'A' || seq.charAt(pos1) == 'U') && Helper.isComplementaryBasePair(seq.charAt(pos1), seq2.charAt(pos1))) || ((seq.charAt(pos2) == 'A' || seq.charAt(pos2) == 'U') && Helper.isComplementaryBasePair(seq.charAt(pos2), seq2.charAt(pos2)))){
			Thermodynamics closure = this.collector.getClosureValue("A", "U");
			result.setEnthalpy(result.getEnthalpy() + closure.getEnthalpy());
			result.setEntropy(result.getEntropy() + closure.getEntropy());
		}
		
		if (((seq.charAt(pos1) == 'G' && seq2.charAt(pos1) == 'U') || (seq.charAt(pos1) == 'U' && seq2.charAt(pos1) == 'G')) || ((seq.charAt(pos2) == 'G' && seq2.charAt(pos2) == 'U') ||( seq.charAt(pos2) == 'U' && seq2.charAt(pos2) == 'G'))){
			Thermodynamics closure = this.collector.getClosureValue("G", "U");
			result.setEnthalpy(result.getEnthalpy() + closure.getEnthalpy());
			result.setEntropy(result.getEntropy() + closure.getEntropy());
		}
		
		if (seq.charAt(pos1+1) == 'G' && seq2.charAt(pos1+1) == 'G'){
			Thermodynamics mismatch = this.collector.getFirstMismatch("G", "G", "1x1");
			result.setEnthalpy(result.getEnthalpy() + mismatch.getEnthalpy());
			result.setEntropy(result.getEntropy() + mismatch.getEntropy());
		}
		
		if ((Helper.convertToPyr_Pur(seq.substring(pos1, pos1+1)) + seq.substring(pos1 + 1, pos1+2)).equals("RU") && (Helper.convertToPyr_Pur(seq2.substring(pos1, pos1+1)) + seq2.substring(pos1+1, pos1+2)).equals("U")){
			Thermodynamics mismatch = this.collector.getFirstMismatch("RU", "YU", "1x1");
			result.setEnthalpy(result.getEnthalpy() + mismatch.getEnthalpy());
			result.setEntropy(result.getEntropy() + mismatch.getEntropy());
		}
		result.setEnthalpy(result.getEnthalpy() + loopInitiation.getEnthalpy());
		result.setEntropy(result.getEntropy() + loopInitiation.getEntropy());
		
		return result;
	}

	public DataCollect getCollector() {
		return this.collector;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = true;
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (hybridization.equals("rnarna") == false){
			System.out.println("WARNING : the single mismatches parameter of " +
					"Turner et al. (2006) are originally established " +
					"for RNA sequences.");
			
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
		
		if (this.collector.getInitiationLoopValue("2") == null){
			return true;
		}
		
		if (((seq1.charAt(pos1) == 'A' || seq1.charAt(pos1) == 'U') && Helper.isComplementaryBasePair(seq1.charAt(pos1), seq2.charAt(pos1))) || ((seq1.charAt(pos2) == 'A' || seq1.charAt(pos2) == 'U') && Helper.isComplementaryBasePair(seq1.charAt(pos2), seq2.charAt(pos2)))){
			if (this.collector.getClosureValue("A", "U") == null){
				return true;
			}
		}
		
		if (((seq1.charAt(pos1) == 'G' && seq2.charAt(pos1) == 'U') || (seq1.charAt(pos1) == 'U' && seq2.charAt(pos1) == 'G')) || ((seq1.charAt(pos2) == 'G' && seq2.charAt(pos2) == 'U') ||( seq1.charAt(pos2) == 'U' && seq2.charAt(pos2) == 'G'))){
			if (this.collector.getClosureValue("G", "U") == null){
				return true;
			}
		}
		
		if (seq1.charAt(pos1+1) == 'G' && seq2.charAt(pos1+1) == 'G'){
			if (this.collector.getFirstMismatch("G", "G", "1x1") == null){
				return true;
			}
		}
		
		if ((Helper.convertToPyr_Pur(seq1.substring(pos1, pos1+1)) + seq1.substring(pos1 + 1, pos1+2)).equals("RU") && (Helper.convertToPyr_Pur(seq2.substring(pos1, pos1+1)) + seq2.substring(pos1+1, pos1+2)).equals("U")){
			if (this.collector.getFirstMismatch("RU", "YU", "1x1") == null){
				return true;
			}
		}
		return false;
	}

}
