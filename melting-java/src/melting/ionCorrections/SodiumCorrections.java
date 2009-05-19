package melting.ionCorrections;

import melting.Environment;
import melting.ThermoResult;
import melting.calculMethodInterfaces.IonCorrectionMethod;
import melting.calculMethodInterfaces.SodiumEquivalentMethod;
import melting.configuration.RegisterCalculMethod;

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
	
	protected boolean isNaeqMethodNecessary(Environment environment){
		if (environment.getMg() > 0 || environment.getK() > 0 || environment.getTris() > 0){
			return true;
		}
		return false;
	}
	
	protected double calculateNaEqEquivalent(Environment environment){
		double NaEq = environment.getNa();
		
		if (isNaeqMethodNecessary(environment)){
			RegisterCalculMethod setNaEqMethod = new RegisterCalculMethod();
			SodiumEquivalentMethod method = setNaEqMethod.getNaEqMethod(environment.getOptions());
			NaEq = method.getSodiumEquivalent(environment.getNa(), environment.getMg(), environment.getK(), environment.getTris(),environment.getDNTP());
		}
		
		return NaEq;
	}

}
