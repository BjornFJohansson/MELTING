package melting.InternalLoopMethod;

import java.util.HashMap;

import melting.Helper;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Znosco071x2Loop extends PartialCalcul {

	/*REF: Brent M Znosko et al (2007). Biochemistry 46: 14715-14724. */
	
	public Znosco071x2Loop(){
		loadData("Znosco20071x2loop.xml", this.collector);
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		Thermodynamics loopInitiation = this.collector.getInitiationLoopValue();
		Thermodynamics firstMismatch = this.collector.getFirstMismatch(seq.substring(pos1 + 1, pos1 + 2), seq2.substring(pos1 + 1, pos1 + 2), "1x2");
		
		if ((seq.charAt(pos1 + 1) == 'G' && seq2.charAt(pos1 + 1) == 'A') || (seq.charAt(pos1 + 1) == 'A' && seq2.charAt(pos1 + 1) == 'G')){
			firstMismatch = this.collector.getFirstMismatch("A", "G_not_RA/YG", "1x2");
		}
		
		if (((seq.charAt(pos1) == 'A' || seq.charAt(pos1) == 'U') && Helper.isComplementaryBasePair(seq.charAt(pos1), seq2.charAt(pos1))) || ((seq.charAt(pos2) == 'A' || seq.charAt(pos2) == 'U') && Helper.isComplementaryBasePair(seq.charAt(pos2), seq2.charAt(pos2)))){
			Thermodynamics closure = this.collector.getClosureValue("A", "U");
			
			result.setEnthalpy(result.getEnthalpy() + closure.getEnthalpy());
			result.setEntropy(result.getEntropy() + closure.getEnthalpy());
		}
		
		if (((seq.charAt(pos1) == 'G' && seq2.charAt(pos1) == 'U') || (seq.charAt(pos1) == 'U' && seq2.charAt(pos1) == 'G')) || ((seq.charAt(pos2) == 'G' && seq2.charAt(pos2) == 'U') ||( seq.charAt(pos2) == 'U' && seq2.charAt(pos2) == 'G'))){
			Thermodynamics closure = this.collector.getClosureValue("G", "U");

			result.setEnthalpy(result.getEnthalpy() + closure.getEnthalpy());
			result.setEntropy(result.getEntropy() + closure.getEnthalpy());
		}
		
		result.setEnthalpy(result.getEnthalpy() + loopInitiation.getEnthalpy() + firstMismatch.getEnthalpy());
		result.setEntropy(result.getEntropy() + loopInitiation.getEnthalpy() + firstMismatch.getEnthalpy());
		
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = true;
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		String loopType = getLoopType(seq1.substring(pos1,pos2 + 1), seq2.substring(pos1, pos2 + 1));
		
		if (hybridization.equals("rnarna") == false){
			System.out.println("WARNING : the internal 1x2 loop parameters of " +
					"Znosco et al. (2007) are originally established " +
					"for RNA sequences.");
			
			isApplicable = false;
		}
		
		if (loopType.equals("1x2") == false){
			System.out.println("WARNING : The thermodynamic parameters of Znosco et al. (2007) are" +
					"established only for 1x2 internal loop");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		
		if (this.collector.getInitiationLoopValue() == null){
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
		
		Thermodynamics firstMismatch = this.collector.getFirstMismatch(seq1.substring(pos1 + 1, pos1 + 2), seq2.substring(pos1 + 1, pos1 + 2), "1x2");
		
		if ((seq1.charAt(pos1 + 1) == 'G' && seq2.charAt(pos1 + 1) == 'A') || (seq1.charAt(pos1 + 1) == 'A' && seq2.charAt(pos1 + 1) == 'G')){
			firstMismatch = this.collector.getFirstMismatch("A", "G_not_RA/YG", "1x2");
		}
		if (firstMismatch == null){
			return true;
		}
		return false;
	}
	
	private String getLoopType(String seq1, String seq2){
		
		if (Helper.isAsymetricLoop(seq1, seq2)){
			int internalLoopSize1 = 0;
			int internalLoopSize2 = 0;
			
			for (int i = 1; i < seq1.length() - 1; i++ ){
				if (seq1.charAt(i) != '-'){
					internalLoopSize1 ++;
				}
				
				if (seq2.charAt(i) != '-'){
					internalLoopSize2 ++;
				}
			}
			return Integer.toString(internalLoopSize1) + "x" + Integer.toString(internalLoopSize2);
		}
		else{
			String internalLoopSize = Integer.toString(seq1.length() - 2);
			return internalLoopSize + "x" + internalLoopSize;
		}
	}

}
