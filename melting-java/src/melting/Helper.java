package melting;


public class Helper {

	public static int CalculatePercentGC(String seq1, String seq2){
		int numberGC = 0;
		for (int i = 0; i < Math.min(seq1.length(), seq2.length());i++){
			if (seq1.charAt(i) == 'G' || seq1.charAt(i) == 'C'){
				if (isComplementaryBasePair(seq1.charAt(i), seq2.charAt(i))){
					numberGC++;
				}
			}
		}
		return numberGC / Math.min(seq1.length(), seq2.length()) * 100;
	}
	
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
	
	public static String getComplementarySequence(String sequence){
		String complementary = "";
		for (int i = 0; i < sequence.length(); i++){
			switch(sequence.charAt(i)){
			case 'A':
				complementary += 'T';
				break;
			case 'T':
				complementary += 'A';
				break;
			case 'C':
				complementary += 'G';
				break;
			case 'G':
				complementary += 'C';
				break;
			}
		}
		return complementary;
	}
}