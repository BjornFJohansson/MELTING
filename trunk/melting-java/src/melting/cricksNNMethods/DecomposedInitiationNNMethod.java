package melting.cricksNNMethods;

import java.util.HashMap;

import melting.ThermoResult;
import melting.Thermodynamics;

public abstract class DecomposedInitiationNNMethod extends CricksNNMethod {

	public DecomposedInitiationNNMethod(String fileName) {
		super(fileName);
	}
	
	public ThermoResult calculateInitiationHybridation(HashMap<String, String> options, ThermoResult result){
		Thermodynamics parameter1 = new Thermodynamics(0,0);
		Thermodynamics parameter2 = new Thermodynamics(0,0);
		String seq1 = "";
		String complementarySeq = "";
		int duplexLength = Math.min(seq1.length(), complementarySeq.length());
		
		result = super.calculateInitiationHybridation(options, result);
		
		parameter1 = this.collector.getInitiation("per_"+ seq1.charAt(0) + "/" + complementarySeq.charAt(0));
		parameter2 = this.collector.getInitiation("per_"+ seq1.charAt(duplexLength - 1) + "/" + complementarySeq.charAt(duplexLength - 1));
		
		if (parameter1 == null) {
			parameter1 = this.collector.getInitiation("per_"+ complementarySeq.charAt(0) + "/" + seq1.charAt(0));
		}
		
		if (parameter2 == null) {
			parameter2 = this.collector.getInitiation("per_"+ complementarySeq.charAt(duplexLength - 1) + "/" + seq1.charAt(duplexLength - 1));
		}
		result.setEnthalpy(result.getEnthalpy() + parameter1.getEnthalpy() + parameter2.getEnthalpy());
		result.setEntropy(result.getEntropy() + parameter1.getEntropy() + parameter2.getEntropy());
		
		return result;
	}

}
