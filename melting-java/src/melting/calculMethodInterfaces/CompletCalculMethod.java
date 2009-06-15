package melting.calculMethodInterfaces;

import java.util.HashMap;

import melting.ThermoResult;
import melting.configuration.RegisterCalculMethod;

public interface CompletCalculMethod {

	public ThermoResult calculateThermodynamics();
	public boolean isApplicable();
	public void setUpVariable(HashMap<String, String> options);
	public RegisterCalculMethod getRegister();
}
