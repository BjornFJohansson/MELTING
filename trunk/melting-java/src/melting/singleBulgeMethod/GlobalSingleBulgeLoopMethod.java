package melting.singleBulgeMethod;

import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;

public abstract class GlobalSingleBulgeLoopMethod extends PartialCalcul{
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		Thermodynamics bulge = this.collector.getSingleBulgeLoopvalue(seq.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1));
		
		result.setEnthalpy(result.getEnthalpy() + bulge.getEnthalpy());
		result.setEntropy(result.getEntropy() + bulge.getEntropy());

		return result;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		if (this.collector.getSingleBulgeLoopvalue(seq1.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1)) == null){
			return true;
		}
		return super.isMissingParameters(seq1, seq2, pos1, pos2);
	}

}
