package melting;

import melting.calculMethodInterfaces.SodiumEquivalentMethod;
import melting.configuration.RegisterCalculMethod;
import melting.exceptions.NoExistingMethodException;


public class Helper {
	
	public static boolean useOtherDataFile(String methodName){
		if (methodName.contains(":")){
			return true;
		}
		return false;
	}
	
	public static String getOptionFileName(String methodName){
		return methodName.split(":")[1];
	}
	
	public static String getOptionMethodName(String methodName){
		return methodName.split(":")[0];
	}
	
	public static double calculateNaEquivalent(Environment environment){
		double NaEq = environment.getNa() + environment.getK() + environment.getTris() / 2;
		
		if (environment.getMg() > 0){
			RegisterCalculMethod setNaEqMethod = new RegisterCalculMethod();
			SodiumEquivalentMethod method = setNaEqMethod.getNaEqMethod(environment.getOptions());
			if (method == null){
				throw new NoExistingMethodException("There is no implemented method to compute the Na equivalent concentration.");
			}
			
			NaEq = method.getSodiumEquivalent(environment.getNa(), environment.getMg(), environment.getK(), environment.getTris(),environment.getDNTP());
		}
		
		return NaEq;
	}
}
