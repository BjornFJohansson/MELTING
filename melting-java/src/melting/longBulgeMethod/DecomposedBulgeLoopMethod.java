package melting.longBulgeMethod;

import java.util.HashMap;

import melting.DataCollect;
import melting.Helper;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public class DecomposedBulgeLoopMethod implements PartialCalculMethod{

	protected DataCollect collector;
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		Thermodynamics bulge = this.collector.getBulgeLoopvalue("1");
		
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
		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		if (this.collector.getBulgeLoopvalue(bulgeSize) == null){
			return true;
		}
		
		return false;
	}
	
	public void loadCrickNNData(HashMap<String, String> optionSet,int pos1, int pos2, ThermoResult result){
		RegisterCalculMethod getData = new RegisterCalculMethod();
		PartialCalculMethod crickNNMethod = getData.getWatsonCrickMethod(optionSet, result, pos1, pos2);
		
		this.collector.getDatas().putAll(crickNNMethod.getCollector().getDatas());
	}

}
