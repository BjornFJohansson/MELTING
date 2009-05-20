package melting.ionCorrections;

import melting.Environment;
import melting.ThermoResult;
import melting.calculMethodInterfaces.IonCorrectionMethod;

public abstract class SodiumCorrections implements IonCorrectionMethod{

	public abstract ThermoResult correctMeltingResult(Environment environment);
		
	public boolean isApplicable(Environment environment) {

		if (environment.getNa() < 0){
			System.out.println("ERROR : The sodium concentration must be " +
					"positive .");
			return false;
		}
		return true;
	}

}
