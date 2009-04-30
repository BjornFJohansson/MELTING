package melting.cricksNNMethods;

import java.util.HashMap;

import melting.Helper;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class GlobalInitiationNNMethod extends CricksNNMethod {

	public GlobalInitiationNNMethod(String fileName) {
		super(fileName);
	}
	
	public ThermoResult calculateInitiationHybridation(HashMap<String, String> options, ThermoResult result){
		Thermodynamics parameter = new Thermodynamics(0,0);
		String seq1 = "";
		String complementarySeq = "";
		
		result = super.calculateInitiationHybridation(options, result);
		
		if (isOneGCBasePair(seq1,complementarySeq)){
			parameter = this.collector.getInitiation("one_GC_pair");
			
			result.setEnthalpy(result.getEnthalpy() + parameter.getEnthalpy());
			result.setEntropy(result.getEntropy() + parameter.getEntropy());
		}
		
		else {
			parameter = this.collector.getInitiation("all_AT_pair");
			
			result.setEnthalpy(result.getEnthalpy() + parameter.getEnthalpy());
			result.setEntropy(result.getEntropy() + parameter.getEntropy());
		}
		
		result.setEnthalpy(parameter.getEnthalpy());
		result.setEntropy(parameter.getEntropy());
		
		if (options.containsKey(OptionManagement.selfComplementarity)){
			parameter = this.collector.getSymetry();
			
			result.setEnthalpy(result.getEnthalpy() + parameter.getEnthalpy());
			result.setEntropy(result.getEntropy() + parameter.getEntropy());
		}
		
		return result;
	}
	
	private boolean isOneGCBasePair(String seq1, String seq2){
		int duplexLength = Math.min(seq1.length(), seq2.length());
		
		for (int i = 0; i < duplexLength; i++){
			if ((seq1.charAt(i) == 'G' || seq1.charAt(i) == 'C') && Helper.isComplementaryBasePair(seq1.charAt(i), seq2.charAt(i))){
				return true;
			}
		}
		return false;
	}

}
