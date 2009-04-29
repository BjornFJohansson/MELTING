package melting.calculMethodInterfaces;

import java.util.HashMap;

import melting.ThermoResult;

public interface PartialCalculMethod {
	
	public boolean isApplicable(HashMap<String, String> options);
	
	public ThermoResult calculateThermodynamics(String seq, String seq2, int pos1, int pos2, ThermoResult result);
	

}
