package melting;

import java.util.ArrayList;

public class BasePair {

	private String topAcid;
	private String bottomAcid;
	private int position;
	private static ArrayList<String> ExistingNucleicAcids = new ArrayList<String>();
	
	public BasePair (int pos){
		this.position = pos;
	}
	
	public static void initializeNucleicAcidList(){
		ExistingNucleicAcids.add("A");
		ExistingNucleicAcids.add("T");
		ExistingNucleicAcids.add("U");
		ExistingNucleicAcids.add("G");
		ExistingNucleicAcids.add("C");
		ExistingNucleicAcids.add("I");
		ExistingNucleicAcids.add("-");
		ExistingNucleicAcids.add("A*");
		ExistingNucleicAcids.add("AL");
		ExistingNucleicAcids.add("TL");
		ExistingNucleicAcids.add("GL");
		ExistingNucleicAcids.add("CL");
		ExistingNucleicAcids.add("UL");
		ExistingNucleicAcids.add("X_C");
		ExistingNucleicAcids.add("X_T");
	}
	
	public static ArrayList<String> getExistingNucleicAcids() {
		return ExistingNucleicAcids;
	}

	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public String getTopAcid() {
		return topAcid;
	}
	
	public void setTopAcid(String topAcid) {
		this.topAcid = topAcid;
	}
	
	public String getBottomAcid() {
		return bottomAcid;
	}
	
	public void setBottomAcid(String bottomAcid) {
		this.bottomAcid = bottomAcid;
	}
	
	public boolean isComplementaryBasePair(){
		if (topAcid.length() != 1 || bottomAcid.length() != 1){
			return false;
		}
		else {
			char acid1 = topAcid.charAt(0);
			char acid2 = bottomAcid.charAt(0);
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
	}

	public boolean isWatsonCrickTopBase(){
	if (topAcid.length() != 1){
		return false;
	}
	else{
		char base = topAcid.charAt(0);
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
}
	
	public boolean isWatsonCrickBottomBase(){
		if (bottomAcid.length() != 1){
			return false;
		}
		else{
			char base = bottomAcid.charAt(0);
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
	}
	
	public boolean isWobbleBasePair(){
		if (topAcid.length() != 1 || bottomAcid.length() != 1){
			return false;
		}
		else {
			char base1 = topAcid.charAt(0);
			char base2 = bottomAcid.charAt(0);
			
			if ((base1 == 'G' && base2 == 'U') || (base1 == 'U' && base2 == 'G')){
				return true;
			}
			else if ((base1 == 'I' && (base2 == 'U' || base2 == 'A' || base2 == 'C')) || (base2 == 'I' && (base1 == 'U' || base1 == 'A' || base1 == 'C'))){
				return true;
			}
			return false;
		}
	}
	
	public boolean isTopBasePyrimidine(){
		if (topAcid.length() != 1){
			return false;
		}
		else {
			char base = topAcid.charAt(0);
			switch (base) {
			case 'A':
				return false;
			case 'T':
				return true;
			case 'C':
				return true;
			case 'U':
				return true;
			case 'G':
				return false;
			default:
				return false;
			}
		}
		
	}
	
	public boolean isBottomBasePyrimidine(){
		if (bottomAcid.length() != 1){
			return false;
		}
		else {
			char base = bottomAcid.charAt(0);
			switch (base) {
			case 'A':
				return false;
			case 'T':
				return true;
			case 'C':
				return true;
			case 'U':
				return true;
			case 'G':
				return false;
			default:
				return false;
			}
		}
	}
	
	public boolean isBasePairEqualTo(String base1, String base2){
		
		if ((topAcid.equals(base1) && bottomAcid.equals(base2)) || (topAcid.equals(base2) && bottomAcid.equals(base1))){
			return true;
		}
		return false;
	}
	
	public boolean isBasePairStrictlyEqualTo(String base1, String base2){
		
		if (topAcid.equals(base1) && bottomAcid.equals(base2)){
			return true;
		}
		return false;
	}
	
	public int getLengthAcid(){
		if (topAcid == null && bottomAcid == null){
			return 0;
		}
		else if (topAcid == null){
			return bottomAcid.length();
		}
		else if (bottomAcid == null){
			return topAcid.length();
		}
		else{
			return topAcid.length();
		}
	}
	
	public boolean isUnpaired(){
		if (topAcid.equals("-") || bottomAcid.equals("-")){
			return true;
		}
		return false;
	}
}
