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
	
	public static String getComplementarySequence(String sequence, String hybridization){
		StringBuffer complementary = new StringBuffer(sequence.length());
		for (int i = 0; i < sequence.length(); i++){
			switch(sequence.charAt(i)){
			case 'A':
				if (hybridization.equals("dnadna") || hybridization.equals("rnadna")){
					complementary.append('T');
				}
				else if (hybridization.equals("rnarna") || hybridization.equals("mrnarna") || hybridization.equals("dnarna")){
					complementary.append('U');
				}
				break;
			case 'T':
				complementary.append('A');
				break;
			case 'C':
				complementary.append('G');
				break;
			case 'G':
				complementary.append('C');
				break;
			case 'U':
				complementary.append('A');
				break;
			}
		}
		return complementary.toString();
	}
	
	public static String getInversedSequence(String sequence){
		StringBuffer newSequence = new StringBuffer(sequence.length());
		for (int i = 0; i < sequence.length(); i++){
			newSequence.append(sequence.charAt(sequence.length() - i)); 
		}
		return newSequence.toString();
	}
	
	public static String convertToPyr_Pur(String sequence){
		StringBuffer newSeq = new StringBuffer(sequence.length());
		
		for (int i = 0; i < sequence.length(); i++){
			switch (sequence.charAt(i)) {
			case 'A':
				newSeq.append('R');
				break;
			case 'G':
				newSeq.append('R');
				break;
			case 'U':
				newSeq.append('Y');
				break;
			case 'C':
				newSeq.append('Y');
				break;

			default:
				System.err.println("There are non watson crick bases in the sequence.");
				break;
			}
		}
		return newSeq.toString();
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
}
