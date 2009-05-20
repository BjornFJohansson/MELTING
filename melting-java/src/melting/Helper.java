package melting;

import melting.calculMethodInterfaces.SodiumEquivalentMethod;
import melting.configuration.RegisterCalculMethod;


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
	
	public static boolean isNaeqMethodNecessary(Environment environment){
		if (environment.getMg() > 0 || environment.getK() > 0 || environment.getTris() > 0){
			return true;
		}
		return false;
	}
	
	public static double calculateNaEquivalent(Environment environment){
		double NaEq = 0;
		
		if (environment.getK() > 0 && environment.getNa() == 0 && environment.getMg() == 0 && environment.getTris() == 0){
			NaEq = environment.getK();
		}
		
		else if (NaEq == 0 && isNaeqMethodNecessary(environment)){
			RegisterCalculMethod setNaEqMethod = new RegisterCalculMethod();
			SodiumEquivalentMethod method = setNaEqMethod.getNaEqMethod(environment.getOptions());
			NaEq = method.getSodiumEquivalent(environment.getNa(), environment.getMg(), environment.getK(), environment.getTris(),environment.getDNTP());
		}
		else {
			NaEq = environment.getNa();
		}
		
		return NaEq;
	}
}
