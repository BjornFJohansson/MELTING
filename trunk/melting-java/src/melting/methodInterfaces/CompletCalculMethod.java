package melting.methodInterfaces;

import java.util.HashMap;

import melting.ThermoResult;
import melting.configuration.RegisterMethods;

public interface CompletCalculMethod {

	public ThermoResult calculateThermodynamics();
	public boolean isApplicable();
	public void setUpVariable(HashMap<String, String> options);
	public RegisterMethods getRegister();
}
