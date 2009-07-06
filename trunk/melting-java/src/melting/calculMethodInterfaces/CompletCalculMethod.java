package melting.calculMethodInterfaces;

import java.util.HashMap;

import melting.ThermoResult;

public interface CompletCalculMethod {

	public ThermoResult calculateThermodynamics();
	public boolean isApplicable();
	public void setUpVariable(HashMap<String, String> options);
}
