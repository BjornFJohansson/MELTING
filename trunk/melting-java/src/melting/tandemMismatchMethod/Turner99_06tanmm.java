package melting.tandemMismatchMethod;

import java.util.HashMap;

import melting.DataCollect;
import melting.Helper;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;

public class Turner99_06tanmm implements PartialCalculMethod{

	/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924.
	REF: Douglas M Turner et al (1999). J.Mol.Biol.  288: 911_940 */
	
private DataCollect collector;
	
	public Turner99_06tanmm() {
		Helper.loadData("Turner1999_2006tanmm.xml", this.collector);
	}
	
	public DataCollect getCollector(){
		return this.collector;
	}

	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		String seq1 = seq.substring(pos1, pos2+1);
		String complementarySeq = seq2.substring(pos1, pos2+1);
		StringBuffer closing = new StringBuffer();
		Thermodynamics parameter = new Thermodynamics(0,0);
		
		if (isSymetric(seq1, complementarySeq)){
			closing.append(seq.charAt(0));
			closing.append("/");
			closing.append(complementarySeq.charAt(0));
			
			parameter = this.collector.getMismatchValue(seq1, complementarySeq, closing.toString());
			result.setEnthalpy(result.getEnthalpy() + parameter.getEnthalpy());
			result.setEntropy(result.getEntropy() + parameter.getEntropy());
			
			return result;
		}
		
		String symetricSequence1 = buildSymetricSequence(seq1, complementarySeq);
		String symetricSequence2 = buildSymetricComplementary(seq1, complementarySeq);
		String symetricComplementary1 = buildSymetricSequence(Helper.getInversedSequence(complementarySeq), Helper.getInversedSequence(seq1));
		String symetricComplementary2 = buildSymetricComplementary(Helper.getInversedSequence(complementarySeq), Helper.getInversedSequence(seq1));

		
		Thermodynamics penaltyGG = this.collector.getPenalty("G/G_adjacent_AA_or_nonCanonicalPyrimidine");
		Thermodynamics penaltyAG = this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA");
		ThermoResult result1 = new ThermoResult(0,0,0);
		ThermoResult result2 = new ThermoResult(0,0,0);
		result1 = calculateThermodynamics(symetricSequence1, symetricComplementary1, 0, 3, result1);
		result1 = calculateThermodynamics(symetricSequence2, symetricComplementary2, 0, 3, result2);
		
		result.setEnthalpy(result.getEnthalpy() + (result1.getEnthalpy() + result2.getEnthalpy()) / 2 + penaltyAG.getEnthalpy() + penaltyGG.getEnthalpy());
		result.setEntropy(result.getEntropy() + (result1.getEntropy() + result2.getEntropy()) / 2 + penaltyAG.getEntropy() + penaltyGG.getEntropy());
		
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = true;
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (hybridization.equals("rnarna") == false){
			System.out.println("WARNING : the tandem mismatch parameters of " +
					"Turner (1999-2006) are originally established " +
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
		String seq = seq1.substring(pos1, pos2+1);
		String complementarySeq = seq2.substring(pos1, pos2+1);
		StringBuffer closing = new StringBuffer();
		
		closing.append(seq.charAt(0));
		closing.append("/");
		closing.append(complementarySeq.charAt(0));
		
		if (this.collector.getMismatchValue(seq, complementarySeq, closing.toString()) == null){
			return true;
		}
		if (isSymetric(seq1, seq2) == false){
			if (this.collector.getPenalty("G/G_adjacent_AA_or_nonCanonicalPyrimidine") == null){
				return true;
			}
			if (this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA") == null){
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isSymetric(String seq1, String seq2){
		for (int i = 0; i < Math.max(seq1.length(), seq2.length()); i++){
			if (seq1.charAt(i) == seq2.charAt(seq2.length() - i - 1)){
				return true;
			}
		}
		return false;
	}
	
	private String buildSymetricSequence(String sequence, String complementary){
		StringBuffer symetricSequence = new StringBuffer(sequence.length());
		
		symetricSequence.append(sequence.substring(0, 2));
		symetricSequence.append(complementary.charAt(1));
		symetricSequence.append(complementary.charAt(0));
	
		return symetricSequence.toString();
	}
	
	private String buildSymetricComplementary(String sequence, String complementary){
		StringBuffer symetricComplementary = new StringBuffer(complementary.length());
		
		symetricComplementary.append(complementary.substring(0, 2));
		symetricComplementary.append(sequence.charAt(1));
		symetricComplementary.append(sequence.charAt(0));
	
		return symetricComplementary.toString();
	}
}
