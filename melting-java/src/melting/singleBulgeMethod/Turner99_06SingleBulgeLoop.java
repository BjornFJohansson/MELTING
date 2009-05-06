package melting.singleBulgeMethod;

import melting.ThermoResult;
import melting.Thermodynamics;
import melting.longBulgeMethod.Turner99_06LongBulgeLoop;

public class Turner99_06SingleBulgeLoop extends Turner99_06LongBulgeLoop{
	
	/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924. 
	REF: Douglas M Turner et al (1999). J.Mol.Biol.  288: 911_940.*/ 
	
	public Turner99_06SingleBulgeLoop(){
		super();
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		result = super.calculateThermodynamics(seq, seq2, pos1, pos2, result);
		
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
