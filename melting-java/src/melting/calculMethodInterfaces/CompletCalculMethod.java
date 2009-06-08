package melting.calculMethodInterfaces;

import java.util.HashMap;

import melting.ThermoResult;
import melting.configuration.RegisterCalculMethod;

public interface CompletCalculMethod {

	public ThermoResult CalculateThermodynamics();
	public boolean isApplicable();
	public void setUpVariable(HashMap<String, String> options);
	public RegisterCalculMethod getRegister();
}
