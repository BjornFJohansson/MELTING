package melting;


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
	default:
		return false;
	}
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
}
