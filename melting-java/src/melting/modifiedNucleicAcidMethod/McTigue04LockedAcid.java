package melting.modifiedNucleicAcidMethod;

import java.util.HashMap;

import melting.Helper;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class McTigue04LockedAcid extends PartialCalcul{
	
	/*McTigue et al.(2004). Biochemistry 43 : 5388-5405 */
	
	public McTigue04LockedAcid(){
		loadData("McTigue2004lockedmn.xml", this.collector);
	}

	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		result = calculateThermodynamicsNoModifiedAcid(seq, seq2, pos1, pos2, result);
		
		Thermodynamics increment1 = this.collector.getLockedAcidValue(seq.substring(pos1, pos1 + 2), seq2.substring(pos1, pos1 + 2));
		Thermodynamics increment2 = this.collector.getLockedAcidValue(seq.substring(pos1 + 1, pos2 + 1), seq2.substring(pos1 + 1, pos2 + 1));
		
		result.setEnthalpy(result.getEnthalpy() + increment1.getEnthalpy() + increment2.getEnthalpy());
		result.setEntropy(result.getEntropy() + increment1.getEntropy() + increment2.getEntropy());
		
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = super.isApplicable(options, pos1, pos2);
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (hybridization.equals("dnadna") == false) {
			System.err.println("WARNING : The thermodynamic parameters for locked acid nucleiques of" +
					"McTigue et al. (2004) are established for DNA sequences.");
			isApplicable = false;
		}
		
		if ((pos1 == 0 && (seq1.charAt(pos1 + 1) == 'L' || seq2.charAt(pos1) == 'L')) || (pos2 == Math.min(seq1.length(), seq2.length()) && (seq1.charAt(pos2) == 'L' || seq2.charAt(pos2) == 'L'))){
			System.err.println("WARNING : The thermodynamics parameters for locked acid nucleiques of " +
					"McTigue (2004) are not established for terminal locked acid nucleiques.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		
		if (this.collector.getLockedAcidValue(seq1.substring(pos1, pos2),seq2.substring(pos1, pos2)) == null){
			return true;
		}
		
		if (this.collector.getLockedAcidValue(seq1.substring(pos1 + 1, pos2 + 1),seq2.substring(pos1 + 1, pos2 + 1)) == null){
			return true;
		}
		return super.isMissingParameters(seq1, seq2, pos1, pos2);
	}

	private ThermoResult calculateThermodynamicsNoModifiedAcid(String seq, String seq2,
			int pos1, int pos2, ThermoResult result){
		
		String sequence = seq.substring(pos1, pos2 + 1);
		String complementary= seq2.substring(pos1, pos2 + 1) ;
		
		if (sequence.contains("L")){
			sequence.replace("L", "");
			complementary.replace("-", "");
		}
		
		if (complementary.contains("L")){
			complementary.replace("L", "");
			sequence.replace("-", "");
		}
		
		Thermodynamics crickParameter1 = collector.getNNvalue(sequence.substring(0, 2), complementary.substring(0, 2));
		Thermodynamics crickParameter2 = collector.getNNvalue(sequence.substring(1, 3), complementary.substring(1, 3));
		
		result.setEnthalpy(result.getEnthalpy() + crickParameter1.getEnthalpy() + crickParameter2.getEnthalpy());
		result.setEntropy(result.getEntropy() + crickParameter1.getEntropy() + crickParameter2.getEntropy());
		
		return result;
	}
	
}
