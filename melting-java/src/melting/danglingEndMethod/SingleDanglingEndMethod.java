package melting.danglingEndMethod;

import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;

public abstract class SingleDanglingEndMethod extends PartialCalcul {

	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		Thermodynamics parameter = this.collector.getDanglingValue(seq.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1));
			
		result.setEnthalpy(result.getEnthalpy() + parameter.getEnthalpy());
		result.setEntropy(result.getEntropy() + parameter.getEntropy());
		
		return result;
	}
	
	public boolean isMissingParameters(String seq1, String seq2, int pos1, int pos2){
		if (this.collector.getDanglingValue(seq1.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1)) == null){
			return true;
		}
		return super.isMissingParameters(seq1, seq2, pos1, pos2);
	}
}
