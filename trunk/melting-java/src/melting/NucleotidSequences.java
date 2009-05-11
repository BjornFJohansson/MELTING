package melting;

public class NucleotidSequences {

	private String sequence;
	private String complementary;
	
	public NucleotidSequences(String sequence, String complementary){
		this.sequence = sequence;
		this.complementary = complementary;
		encodeSequences();
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
	
	public boolean isBasePairEqualsTo(char base1, char base2, int pos){
		
		if ((this.sequence.charAt(pos) == base1 && this.complementary.charAt(pos) == base2) || (this.sequence.charAt(pos) == base2 && complementary.charAt(pos) == base1)){
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
	
	public static String getSens(String seq1, String seq2){
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
	
	public String convertToPyr_Pur(String sequence){
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
	
	public String getLoopType(int pos1, int pos2){
		if (isAsymetricLoop(pos1, pos2)){
			int internalLoopSize1 = 0;
			int internalLoopSize2 = 0;
			
			for (int i = 1; i < getSequence(pos1, pos2).length() - 1; i++ ){
				if (this.sequence.charAt(i) != '-'){
					internalLoopSize1 ++;
				}
				
				if (this.complementary.charAt(i) != '-'){
					internalLoopSize2 ++;
				}
			}
			if (Math.min(internalLoopSize1, internalLoopSize2) == 1 && Math.max(internalLoopSize2, internalLoopSize1) > 2){
				return Integer.toString(internalLoopSize1) + "x" + "n_n>2";
			}
			else if ((internalLoopSize1 == 2 && internalLoopSize2 != 3) || (internalLoopSize2 == 2 && internalLoopSize1 != 3)){
				return "others_non_2x2";
			}
			else {
				return Integer.toString(internalLoopSize1) + "x" + Integer.toString(internalLoopSize2);
			}
		}
		else{
			String internalLoopSize = Integer.toString(getDuplexLength() - 2);
			return internalLoopSize + "x" + internalLoopSize;
		}
	}

	public String getLoopFistMismatch(String loop){
		String mismatch = convertToPyr_Pur(loop.substring(1,2)) + loop.substring(3, 4);
		
		return mismatch;
	}
	
	public String getSingleBulgeNeighbors(String bulge){
		String NNPair = bulge.substring(0, 1) + bulge.substring(2, 3);
		return NNPair;
	}
	
	public boolean isSymetric(int pos1, int pos2){
		for (int i = 0; i < getSequence(pos1, pos2).length(); i++){
			if (getSequence(pos1, pos2).charAt(i) == getComplementary(pos1, pos2).charAt(getSequence(pos1, pos2).length() - i - 1)){
				return true;
			}
		}
		return false;
	}
	
	public String buildSymetricSequence(String seq1, String seq2){
		StringBuffer symetricSequence = new StringBuffer(seq1.length());
		
		symetricSequence.append(seq1.substring(0, 2));
		symetricSequence.append(seq2.substring(1, 2));
		symetricSequence.append(seq2.substring(0,1));
	
		return symetricSequence.toString();
	}
	
	public String buildSymetricComplementary(String seq1, String seq2){
		StringBuffer symetricComplementary = new StringBuffer(seq1.length());
		
		symetricComplementary.append(seq2.substring(0, 2));
		symetricComplementary.append(seq1.substring(1, 2));
		symetricComplementary.append(seq1.substring(0, 1));
	
		return symetricComplementary.toString();
	}
	
	public static String getInversedSequence(String sequence){
		StringBuffer newSequence = new StringBuffer(sequence.length());
		for (int i = 0; i < sequence.length(); i++){
			newSequence.append(sequence.charAt(sequence.length() - i)); 
		}
		return newSequence.toString();
	}
	
	private void encodeSequences(){
		StringBuffer seq = new StringBuffer(Math.max(this.sequence.length(), this.complementary.length()));
		StringBuffer comp = new StringBuffer(Math.max(this.sequence.length(), this.complementary.length()));
		
		seq.append(this.sequence);
		comp.append(this.complementary);
		
		for (int i = 0; i < Math.max(this.sequence.length(), this.complementary.length());i++){
			
			if (this.sequence.charAt(i) == '_' && (this.sequence.charAt(i + 1) == 'A' || this.sequence.charAt(i + 1) == 'X')){
				comp.insert(i,"-");
			}
			else if (this.complementary.charAt(i) == '_' && (this.complementary.charAt(i + 1) == 'A' || this.complementary.charAt(i + 1) == 'X')){
				seq.insert(i,"-");
			}
			else if (this.sequence.charAt(i - 1) == '_' && (this.sequence.charAt(i) == 'T' || this.sequence.charAt(i) == 'C')){
				comp.insert(i,"-");
			}
			else if (this.complementary.charAt(i - 1) == '_' && (this.complementary.charAt(i) == 'T' || this.complementary.charAt(i) == 'C')){
				seq.insert(i,"-");
			}
			else if (this.sequence.charAt(i) == 'X' && this.sequence.charAt(i - 1) != '_'){
				comp.insert(i,"-");
			}
			else if (this.complementary.charAt(i) == 'X' && this.complementary.charAt(i - 1) != '_'){
				seq.insert(i,"-");
			}
			else if (this.sequence.charAt(i) == 'L'){
				comp.insert(i,"-");
			}
			else if (this.complementary.charAt(i) == 'L'){
				seq.insert(i,"-");
			}
			else if (this.sequence.charAt(i) == '*'){
				comp.insert(i,"-");
			}
			else if (this.complementary.charAt(i) == '*'){
				seq.insert(i,"-");
			}
		}
		
		this.sequence = seq.toString();
		this.complementary = comp.toString();
	}
	
	public static NucleotidSequences decodeSequences(String sequence, String complementary){
		StringBuffer seq = new StringBuffer(Math.max(sequence.length(), complementary.length()));
		StringBuffer comp = new StringBuffer(Math.max(sequence.length(), complementary.length()));
		
		seq.append(sequence);
		comp.append(complementary);
		
		for (int i = 0; i < Math.max(sequence.length(), complementary.length());i++){
			
			if (sequence.charAt(i) == '_' && (sequence.charAt(i + 1) == 'A' || sequence.charAt(i + 1) == 'X')){
				comp.deleteCharAt(i);
			}
			else if (complementary.charAt(i) == '_' && (complementary.charAt(i + 1) == 'A' || complementary.charAt(i + 1) == 'X')){
				seq.deleteCharAt(i);
			}
			else if (sequence.charAt(i - 1) == '_' && (sequence.charAt(i) == 'T' || sequence.charAt(i) == 'C')){
				comp.deleteCharAt(i);
			}
			else if (complementary.charAt(i - 1) == '_' && (complementary.charAt(i) == 'T' || complementary.charAt(i) == 'C')){
				seq.deleteCharAt(i);
			}
			else if (sequence.charAt(i) == 'X' && sequence.charAt(i - 1) != '_'){
				comp.deleteCharAt(i);
			}
			else if (complementary.charAt(i) == 'X' && complementary.charAt(i - 1) != '_'){
				seq.deleteCharAt(i);
			}
			else if (sequence.charAt(i) == 'L'){
				comp.deleteCharAt(i);
			}
			else if (complementary.charAt(i) == 'L'){
				seq.deleteCharAt(i);
			}
			else if (sequence.charAt(i) == '*'){
				comp.deleteCharAt(i);
			}
			else if (complementary.charAt(i) == '*'){
				seq.deleteCharAt(i);
			}
		}
		
		NucleotidSequences DecodedSequences = new NucleotidSequences(seq.toString(), comp.toString());
		
		return DecodedSequences;
	}
}
