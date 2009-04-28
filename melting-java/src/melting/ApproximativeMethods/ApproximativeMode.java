package melting.ApproximativeMethods;

import java.util.HashMap;

import melting.CompletCalculMethod;
import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class ApproximativeMode implements CompletCalculMethod{

	public ThermoResult CalculateThermodynamics(HashMap<String, String> options) {
		double Tm = 0;
		
		ThermoResult result = new ThermoResult();
		result.setTm(Tm);
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options) {
		return true;
	}

}
