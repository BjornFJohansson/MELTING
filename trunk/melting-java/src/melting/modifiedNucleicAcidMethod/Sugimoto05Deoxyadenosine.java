package melting.modifiedNucleicAcidMethod;

import java.util.HashMap;

import melting.Helper;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Sugimoto05Deoxyadenosine extends PartialCalcul{
	
	public Sugimoto05Deoxyadenosine(){
		Helper.loadData("Sugimoto2005LdeoxyAmn.xml", this.collector);
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		result = calculateThermodynamicsNoModifiedAcid(seq, seq2, pos1, pos2, result);
		
		Thermodynamics increment = this.collector.getModifiedvalue(seq.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1));
		
		result.setEnthalpy(result.getEnthalpy() + increment.getEnthalpy());
		result.setEntropy(result.getEntropy() + increment.getEntropy());
		
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = super.isApplicable(options, pos1, pos2);
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (hybridization.equals("dnadna") == false) {
			System.err.println("WARNING : The thermodynamic parameters for L-deoxyadenosine of" +
					"Sugimoto et al. (2005) are established for DNA sequences.");
			isApplicable = false;
		}
		
		if ((pos1 == 0 && (seq1.substring(pos1, pos1 + 2).equals("-A") || seq2.substring(pos1, pos1 + 2).equals("-A") || seq1.substring(pos1, pos1 + 2).equals("-X") || seq2.substring(pos1, pos1 + 2).equals("-X"))) || (pos2 == Math.min(seq1.length(), seq2.length()) && (seq1.substring(pos2 - 1, pos2 + 1).equals("-A") || seq2.substring(pos2 - 1, pos2 + 1).equals("-A") || seq1.substring(pos2 - 1, pos2 + 1).equals("-X") || seq2.substring(pos2 - 1, pos2 + 1).equals("-X")))){
			System.err.println("WARNING : The thermodynamics parameters for L-deoxyadenosine of " +
					"Santaluciae (2005) are not established for terminal L-deoxyadenosine.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		
		if (this.collector.getDeoxyadenosineValue(seq1.substring(pos1, pos2 + 1),seq2.substring(pos1, pos2 + 1)) == null){
			return true;
		}
		
		return super.isMissingParameters(seq1, seq2, pos1, pos2);
	}
	
	private ThermoResult calculateThermodynamicsNoModifiedAcid(String seq, String seq2,
			int pos1, int pos2, ThermoResult result){
		
		String sequence = seq.substring(pos1, pos2 + 1);
		String complementary= seq2.substring(pos1, pos2 + 1) ;
		
		sequence.replace("-", "");
		complementary.replace("-", "");
		
		if (complementary.contains("X")){
			complementary.replace("X", "T");
		}
		if (sequence.contains("X")){
			sequence.replace("X", "T");
		}
		
		Thermodynamics crickParameter1 = collector.getNNvalue(sequence.substring(0, 2), complementary.substring(0, 2));
		Thermodynamics crickParameter2 = collector.getNNvalue(sequence.substring(1, 3), complementary.substring(1, 3));
		
		result.setEnthalpy(result.getEnthalpy() + crickParameter1.getEnthalpy() + crickParameter2.getEnthalpy());
		result.setEntropy(result.getEntropy() + crickParameter1.getEntropy() + crickParameter2.getEntropy());
		
		return result;
	}

}
