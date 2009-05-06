package melting.InternalLoopMethod;

import java.util.HashMap;

import melting.DataCollect;
import melting.Helper;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;

public class Turner06InternalLoop implements PartialCalculMethod{

	/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924.*/
	
	private DataCollect collector;
	
	public Turner06InternalLoop(){
		Helper.loadData("Turner1999_2006longmm.xml", this.collector);
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		Thermodynamics loopInitiation = this.collector.getInitiationLoopValue(Integer.toString(Helper.calculateLoopLength(seq.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1))));
		Thermodynamics firstMismatch = this.collector.getFirstMismatch(seq.substring(pos1 + 1, pos1 + 2), seq2.substring(pos1 + 1, pos1 + 2), getLoopType(seq.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1)));
		String loopType = getLoopType(seq.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1));
		
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
		
		if (Helper.isAsymetricLoop(seq.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1))){
			Thermodynamics asymetry = this.collector.getAsymetry();
				
			result.setEnthalpy(result.getEnthalpy() + asymetry.getEnthalpy());
			result.setEntropy(result.getEntropy() + asymetry.getEnthalpy());
		}
		
		if (loopType.charAt(0) == '1' && Integer.getInteger(loopType.substring(2, 3)) > 2){
			result.setEnthalpy(result.getEnthalpy() + loopInitiation.getEnthalpy());
			result.setEntropy(result.getEntropy() + loopInitiation.getEnthalpy());
			
			return result;
		}
		
		result.setEnthalpy(result.getEnthalpy() + loopInitiation.getEnthalpy() + firstMismatch.getEnthalpy());
		result.setEntropy(result.getEntropy() + loopInitiation.getEnthalpy() + firstMismatch.getEnthalpy());
		
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
		String loopType = getLoopType(seq1.substring(pos1,pos2 + 1), seq2.substring(pos1, pos2 + 1));
		
		if (hybridization.equals("rnarna") == false){
			System.out.println("WARNING : the internal loop parameters of " +
					"Turner et al. (2006) are originally established " +
					"for RNA sequences.");
			
			isApplicable = false;
		}
		
		if (loopType.charAt(0) == '3' && loopType.charAt(2) == '3' && ((seq1.charAt(pos1 + 2) == 'G' && seq2.charAt(pos1 + 2) == 'A') || (seq1.charAt(pos1 + 2) == 'A' && seq2.charAt(pos1 + 2) == 'G'))){
			System.out.println("WARNING : The thermodynamic parameters of Turner (2006) excluded" +
					"3 x 3 internal loops with a middle GA pair. The middle GA pair is shown to enhance " +
					"stability and this extra stability cannot be predicted by this nearest neighbor" +
					"parameter set.");
			
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
		
		if (this.collector.getInitiationLoopValue(Integer.toString(Helper.calculateLoopLength(seq1.substring(pos1,pos2), seq2.substring(pos1, pos2)))) == null){
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
		
		if (Helper.isAsymetricLoop(seq1.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1))){
			if (this.collector.getAsymetry() == null){
				return true;
			}
		}
		
		if (this.collector.getFirstMismatch(seq1.substring(pos1 + 1, pos1 + 2), seq2.substring(pos1 + 1, pos1 + 2), getLoopType(seq1.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1))) == null){
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
