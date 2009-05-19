package melting.calculMethodInterfaces;


import melting.Environment;
import melting.ThermoResult;

public interface IonCorrectionMethod {

	public boolean isApplicable(Environment environment);
	
	public ThermoResult correctMeltingResult(Environment environment);
}
