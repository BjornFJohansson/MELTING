package melting.modifiedNucleicAcidMethod;

import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;

public abstract class InosineNNMethod extends PartialCalcul{
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		String seq1 = "";
		String complementarySeq = "";
		Thermodynamics parameter = new Thermodynamics(0,0);
		 
		for (int i = pos1; i <= pos2 - 1; i++){
			seq1 = seq.substring(i, i+2);
			complementarySeq = seq2.substring(i, i+2);
			parameter = collector.getModifiedvalue(seq1, complementarySeq);
			
			result.setEnthalpy(result.getEnthalpy() + parameter.getEnthalpy());
			result.setEntropy(result.getEntropy() + parameter.getEntropy());
		}
		return result;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		Thermodynamics parameter = new Thermodynamics(0,0);
		String seq;
		String complementarySeq; 
		for (int i = pos1; i < pos2; i++){
			seq = seq1.substring(i, i+2);
			complementarySeq = seq2.substring(i, i+2);
			parameter = collector.getModifiedvalue(seq, complementarySeq);
			
			if (parameter == null) {
				return true;
			}
		}
		return super.isMissingParameters(seq1, seq2, pos1, pos2);
	}

}
