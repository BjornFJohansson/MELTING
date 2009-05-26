package melting.calculMethodInterfaces;

import java.util.HashMap;

import melting.ThermoResult;

public interface CompletCalculMethod {

	public ThermoResult CalculateThermodynamics();
	public boolean isApplicable();
	public void setUpVariable(HashMap<String, String> options);
	public ThermoResult correctThermodynamics();
	public String getEquationMeltingTemperature();
}
