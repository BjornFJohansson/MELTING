package melting.methodInterfaces;


import melting.Environment;
import melting.ThermoResult;

public interface CorrectionMethod {

	public boolean isApplicable(Environment environment);
	
	public ThermoResult correctMeltingResult(Environment environment);
}
