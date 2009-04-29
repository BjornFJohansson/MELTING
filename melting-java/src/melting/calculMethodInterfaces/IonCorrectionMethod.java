package melting.calculMethodInterfaces;

import java.util.HashMap;

import melting.ThermoResult;

public interface IonCorrectionMethod {

	public boolean isApplicable(HashMap<String, String> options);
	
	public ThermoResult correctMeltingResult(String seq, String seq2, ThermoResult result);
}
