package melting;

import java.util.HashMap;

public interface CompletCalculMethod {

	public ThermoResult CalculateThermodynamics();
	public boolean isApplicable();
	public void setUpVariable(HashMap<String, String> options);
}
