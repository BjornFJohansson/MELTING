package melting;

import melting.calculMethodInterfaces.SodiumEquivalentMethod;
import melting.configuration.RegisterCalculMethod;
import melting.exceptions.NoExistingMethodException;


public class Helper {
	
	public static boolean isComplementaryBasePair(char acid1, char acid2){
		switch(acid1){
		
		case 'A': 
			if (acid2 == 'T' || acid2 == 'U'){
				return true;
			}
			return false;
			
		case 'T':
			if (acid2 == 'A'){
				return true;
			}
			return false;
			
		case 'G':
			if (acid2 == 'C'){
				return true;
			}
			return false;
		
		case 'C':
			if (acid2 == 'G'){
				return true;
			}
			return false;
			
		case 'U':
			if (acid2 == 'A'){
				return true;
			}
			return false;
			
		default:
			return false;
		}
	}

	public static boolean isWatsonCrickBase(char base){
	switch (base) {
	case 'A':
		return true;
	case 'T':
		return true;
	case 'C':
		return true;
	case 'G':
		return true;
	case 'U':
		return true;
	default:
		return false;
	}
}
	
	public static boolean isWobbleBasePair(char base1, char base2){
		if ((base1 == 'G' && base2 == 'U') || (base1 == 'U' && base2 == 'G')){
			return true;
		}
		else if ((base1 == 'I' && (base2 == 'U' || base2 == 'A' || base2 == 'C')) || (base2 == 'I' && (base1 == 'U' || base1 == 'A' || base1 == 'C'))){
			return true;
		}
		return false;
	}
	
	public static boolean isPyrimidine(char base){
		switch (base) {
			case 'A':
				return false;
			case 'T':
				return true;
			case 'C':
				return true;
			case 'G':
				return false;
			default:
				return false;
		}
	}
	
	public static boolean useOtherDataFile(String methodName){
		if (methodName.contains(":")){
			return true;
		}
		return false;
	}
	
	public static String getOptionFileName(String methodName){
		return methodName.split(":")[1];
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
