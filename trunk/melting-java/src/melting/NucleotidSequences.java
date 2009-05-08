package melting;

public class NucleotidSequences {

	private String sequence;
	private String complementary;
	
	public NucleotidSequences(String sequence, String complementary){
		this.sequence = sequence;
		this.complementary = complementary;
	}
	
	public String getSequence() {
		return sequence;
	}

	public String getComplementary() {
		return complementary;
	}
	
	public String getSequenceNNPair(int pos){
		return this.sequence.substring(pos, pos+2);
	}
	
	public String getComplementaryNNPair(int pos){
		return this.complementary.substring(pos, pos + 2);
	}
	
	public int getDuplexLength(){
		return Math.min(this.sequence.length(), this.complementary.length());
	}
	
	public int calculateNumberOfTerminal(char base1, char base2){
		int numberOfTerminal = 0;
		
		if ((sequence.charAt(0) == base1 && complementary.charAt(0) == base2) || (sequence.charAt(0) == base2 && complementary.charAt(0) == base1)){
			numberOfTerminal++;
		}
		
		if ((sequence.charAt(getDuplexLength() - 1) == base1 && complementary.charAt(getDuplexLength() - 1) == base2) || (sequence.charAt(getDuplexLength() - 1) == base2 && complementary.charAt(getDuplexLength() - 1) == base1)){
			numberOfTerminal++;
		}
		return numberOfTerminal;
	}
	
	public boolean isTerminal5AT(){
		
		if (this.sequence.charAt(0) == 'T' && this.complementary.charAt(0) == 'A'){
			return true;
		}
		return false;
	}
	
	public boolean isDanglingEnds(){
		if ((sequence.charAt(0) == '-' && Helper.isWatsonCrickBase(complementary.charAt(0))) || (complementary.charAt(0) == '-' && Helper.isWatsonCrickBase(sequence.charAt(0)))){
			return true;
		}
		else if ((sequence.charAt(getDuplexLength() - 1) == '-' && Helper.isWatsonCrickBase(complementary.charAt(getDuplexLength() - 1))) || (complementary.charAt(getDuplexLength() - 1) == '-' && Helper.isWatsonCrickBase(sequence.charAt(getDuplexLength() - 1)))){
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getSens(String seq1, String seq2){
		if (seq2.charAt(0) == '-'){
			return "5";
		}
		else if (seq1.charAt(0) == '-'){
			return "3";
		}
		else if (seq2.charAt(seq2.length() - 1) == '-'){
			return "3";
		}
		else if (seq1.charAt(seq1.length() - 1) == '-'){
			return "5";
		}
		return null;
	}
	
	public int calculatePercentGC(){
		int numberGC = 0;
		for (int i = 0; i < getDuplexLength();i++){
			if (this.sequence.charAt(i) == 'G' || this.sequence.charAt(i) == 'C'){
				if (Helper.isComplementaryBasePair(this.sequence.charAt(i), this.complementary.charAt(i))){
					numberGC++;
				}
			}
		}
		return numberGC / getDuplexLength() * 100;
	}
	
	public int getPercentMismatching(){
		int numberMismatching = 0;
		for (int i = 0; i < getDuplexLength(); i++){
			if (Helper.isComplementaryBasePair(this.sequence.charAt(i), this.complementary.charAt(i)) == false){
				numberMismatching++;
			}
		}
		return numberMismatching / getDuplexLength() * 100;
	}
	
	public boolean isOneGCBasePair(){
		for (int i = 0; i < getDuplexLength(); i++){
			if ((this.sequence.charAt(i) == 'G' || this.sequence.charAt(i) == 'C') && Helper.isComplementaryBasePair(this.sequence.charAt(i), this.complementary.charAt(i))){
				return true;
			}
		}
		return false;
	}
	
	public String getSequence(int pos1, int pos2){
		return this.sequence.substring(pos1, pos2 + 1);
	}
	
	public String getComplementary(int pos1, int pos2){
		return this.complementary.substring(pos1, pos2 + 1);
	}
	
	public int calculateLoopLength (int pos1, int pos2){
		String seq1 = getSequence(pos1, pos2);
		String seq2 = getComplementary(pos1, pos2);
		int loop = seq1.length() - 2 + seq2.length() - 2;
		
		for (int i = 1; i < seq1.length() - 1; i++){
			if (seq1.charAt(i) == '-'){
				loop--;
			}
			if (seq2.charAt(i) == '-'){
				loop--;
			}
		}
		return loop;
	}

	public boolean isAsymetricLoop(int pos1, int pos2){
		String seq1 = getSequence(pos1, pos2);
		String seq2 = getComplementary(pos1, pos2);
		
		for (int i= 1; i < seq1.length() - 1; i++){
			if (seq1.charAt(i) == '-' || seq2.charAt(i) == '-'){
				return true;
			}
		}
		return false;
	}
}
