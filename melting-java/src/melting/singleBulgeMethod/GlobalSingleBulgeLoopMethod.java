package melting.singleBulgeMethod;

import java.util.HashMap;

import melting.DataCollect;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;

public class GlobalSingleBulgeLoopMethod implements PartialCalculMethod{

	protected DataCollect collector;
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		Thermodynamics bulge = this.collector.getSingleBulgeLoopvalue(seq.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1));
		
		result.setEnthalpy(result.getEnthalpy() + bulge.getEnthalpy());
		result.setEntropy(result.getEntropy() + bulge.getEntropy());

		return result;
	}

	public DataCollect getCollector() {
		return this.collector;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (isMissingParameters(seq1, seq2, pos1, pos2)){
			System.out.println("WARNING : some thermodynamic parameters are missing.");
			
			return false;
		}
		return true;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		if (this.collector.getSingleBulgeLoopvalue(seq1.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1)) == null){
			return true;
		}
		return false;
	}

}
