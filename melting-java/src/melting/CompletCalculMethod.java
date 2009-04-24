package melting;

import java.util.HashMap;

public interface CompletCalculMethod {

	public ThermoResult CalculateThermodynamics(HashMap<String, String> options);
	public boolean isApplicable(HashMap<String, String> options);
}
