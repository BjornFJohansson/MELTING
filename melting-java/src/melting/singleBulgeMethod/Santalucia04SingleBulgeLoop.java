package melting.singleBulgeMethod;

import melting.DataCollect;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.longBulgeMethod.Santalucia04LongBulgeLoop;

public class Santalucia04SingleBulgeLoop extends Santalucia04LongBulgeLoop{

	/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */

	
	private DataCollect collector;
	
	public Santalucia04SingleBulgeLoop(){
		super();
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		result = super.calculateThermodynamics(seq, seq2, pos1, pos2, result);
		Thermodynamics NNintervening = this.collector.getNNvalue(seq.substring(pos1, pos1 + 1) + seq.substring(pos2, pos2 + 1), seq2.substring(pos1, pos1 + 1) + seq2.substring(pos2, pos2 + 1));

		result.setEnthalpy(result.getEnthalpy() + NNintervening.getEnthalpy());
		result.setEntropy(result.getEntropy() + NNintervening.getEntropy());
		
		return result;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		
		if (this.collector.getNNvalue(seq1.substring(pos1, pos1 + 1) + seq1.substring(pos2, pos2 + 1), seq2.substring(pos1, pos1 + 1) + seq2.substring(pos2, pos2 + 1)) == null){
			return true;
		}
		
		return super.isMissingParameters(seq1, seq2, pos1, pos2);
	}
}
