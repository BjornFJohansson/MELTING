package melting;

public class Helper {

	//public static int CalculateGCpercent(String seq1, String seq2){
		
	//}
	
	public static boolean isComplementaryBasePair(char acid1, char acid2){
		switch(acid1){
		
		case 'A': 
			if (acid2 == 'T'){
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
			
		default:
			return false;
		}
	}
}
