package melting;

import java.util.ArrayList;
import java.util.HashMap;

import melting.exceptions.NucleicAcidNotKnownException;
import melting.exceptions.SequenceException;

public class NucleotidSequences {

	private String sequence;
	private String complementary;
	private static ArrayList<String> modifiedNucleotides = new ArrayList<String>();
	
	private static HashMap<String, ModifiedAcidNucleic> modifiedAcidNames = new HashMap<String, ModifiedAcidNucleic>();
	
	public NucleotidSequences(String sequence, String complementary){

		this.sequence = sequence;
		this.complementary = complementary;		
	}
	
	public void initializeModifiedAcidArrayList(){
		modifiedNucleotides.add("_A");
		modifiedNucleotides.add("_X");
		modifiedNucleotides.add("X_T");
		modifiedNucleotides.add("X_C");
		modifiedNucleotides.add("A*");
		modifiedNucleotides.add("L");
		modifiedNucleotides.add("I");
	}
	
	public void initializeModifiedAcidHashmap(){
		modifiedAcidNames.put("I", ModifiedAcidNucleic.inosine);
		modifiedAcidNames.put("L", ModifiedAcidNucleic.lockedAcidNucleic);
		modifiedAcidNames.put("A*", ModifiedAcidNucleic.hydroxyadenine);
		modifiedAcidNames.put("X_C", ModifiedAcidNucleic.azobenzene);
		modifiedAcidNames.put("X_T", ModifiedAcidNucleic.azobenzene);
		modifiedAcidNames.put("_A", ModifiedAcidNucleic.L_deoxyadenine);
		modifiedAcidNames.put("_X", ModifiedAcidNucleic.L_deoxyadenine);
	}
	
	public void setSequence(String sequence){
		this.sequence = sequence;
	}
	
	public void setComplementary(String sequence){
		this.complementary = sequence;
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
			case 'X':
				if (i != 0 && sequence.charAt(i - 1) == '_'){
					complementary.append('A');
				}
			}
		}
		return complementary.toString();
	}
	
	public static boolean checkSequence(String sequence){
		
		String modifiedAcid = null;
		int position = 0;
		
		for (int i = 0; i < sequence.length(); i++){
			if (Helper.isWatsonCrickBase(sequence.charAt(i)) == false && sequence.charAt(i) != '-' && modifiedAcid == null){
				modifiedAcid = getModifiedAcid(sequence, i);
				position = i;
			}
			else if (Helper.isWatsonCrickBase(sequence.charAt(i)) == false && sequence.charAt(i) != '-' && modifiedAcid != null){
				if (i > position + modifiedAcid.length()){
					position = 0;
					modifiedAcid = null;
				}
				else if (i == position + modifiedAcid.length()){
					if (modifiedNucleotides.contains(modifiedAcid) == false){
						throw new NucleicAcidNotKnownException("the acid nucleic " + modifiedAcid + "is not known.");
					}
				}
			}
		}
		return true;
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
		return Math.max(this.sequence.length(), this.complementary.length());
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
	
	public int calculateNumberOfTerminal(String base1, String base2){
		int numberOfTerminal = 0;
		
		if ((this.sequence.startsWith(base1) && this.complementary.startsWith(base2)) || (this.sequence.startsWith(base2) && complementary.startsWith(base1))){
			numberOfTerminal++;
		}
		
		if ((this.sequence.endsWith(base1) && this.complementary.endsWith(base2)) || (this.sequence.endsWith(base2) && complementary.endsWith(base1))){
			numberOfTerminal++;
		}
		return numberOfTerminal;
	}
	
	public boolean isTerminal5TA(){
		
		if (this.sequence.charAt(0) == 'T' && this.complementary.charAt(0) == 'A'){
			return true;
		}
		return false;
	}
	
	public NucleotidSequences removeTerminalUnpairedNucleotides(){
		StringBuffer seq = new StringBuffer();
		StringBuffer comp = new StringBuffer();
		
		seq.append(this.sequence);
		comp.append(this.complementary);
		
		int indexStart = 0;
		while (Helper.isComplementaryBasePair(this.sequence.charAt(indexStart), this.complementary.charAt(indexStart)) == false && indexStart <= getDuplexLength() - 1){
			seq.deleteCharAt(0);
			comp.deleteCharAt(0);
			indexStart ++;
		}

		if (indexStart == getDuplexLength() - 1){
			throw new SequenceException("The sequences can be hybridized. Check the sequences.");
		}
		
		int indexEnd = getDuplexLength() - 1;
		while (Helper.isComplementaryBasePair(this.sequence.charAt(indexEnd), this.complementary.charAt(indexEnd)) == false && indexEnd >= 0){
			seq.deleteCharAt(getDuplexLength() - 1);
			comp.deleteCharAt(getDuplexLength() - 1);
			indexEnd --;
		}
		if (indexEnd == 0){
			throw new SequenceException("The sequences can be hybridized. Check the sequences.");
		}
		
		NucleotidSequences newSequences = new NucleotidSequences(seq.toString(), comp.toString());
		return newSequences;
	}
	
	public boolean isBasePairEqualsTo(char base1, char base2, int pos){
		
		if ((this.sequence.charAt(pos) == base1 && this.complementary.charAt(pos) == base2) || (this.sequence.charAt(pos) == base2 && complementary.charAt(pos) == base1)){
			return true;
		}
		return false;
	}
	
	public boolean isBasePair(char base1, char base2, int pos){
		
		if (this.sequence.charAt(pos) == base1 && this.complementary.charAt(pos) == base2){
			return true;
		}
		return false;
	}
	
	public static String getSens(String seq1, String seq2){
		if (seq1.length() == 0){
			return "5";
		}
		else if (seq2.length() == 0){
			return "3";
		}
		else if (seq2.charAt(0) == '-'){
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
		else if (seq1.contains("-") == false && seq2.contains("-") == false){
			return null;
		}
		throw new SequenceException("We cannot determine the sens of the dangling end. Check the sequence.");
	}
	
	public String getSequenceSens(String sequence){
		if (sequence.equals(this.sequence)){
			return "5'3'";
		}
		else if (sequence.equals(this.complementary)){
			return "3'5'";
		}
		else {
			throw new SequenceException("We don't recognize the sequence " + sequence);
		}
	}
	
	public double calculatePercentGC(){
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
	
	public double getPercentMismatching(){
		double numberMismatching = 0.0;
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
	
	private String convertSequence(String sequence, String hybridizationType){
		char acidToRemplace;
		char remplacingAcid;
		
		if (hybridizationType.equals("dna")){
			remplacingAcid = 'T';
			acidToRemplace = 'U';
		}
		else if (hybridizationType.equals("rna")) {
			remplacingAcid = 'U';
			acidToRemplace = 'T';
		}
		else {
			throw new SequenceException("It is impossible to convert this sequences in a sequence of type " + hybridizationType + ".");
		}
		
		String newSequence = sequence.replace(acidToRemplace, remplacingAcid);

		return newSequence;
	}
	
	public String getSequence(int pos1, int pos2, String hybridizationType){
		
		return convertSequence(getSequence(pos1, pos2), hybridizationType);
	}
	
	public String getComplementary(int pos1, int pos2, String hybridizationType){
		return convertSequence(getComplementary(pos1, pos2), hybridizationType);
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
	
	public static String convertToPyr_Pur(String sequence){
		StringBuffer newSeq = new StringBuffer(sequence.length());
		for (int i = 0; i <= sequence.length() - 1; i++){
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
			case 'T':
				newSeq.append('Y');
				break;
			case 'C':
				newSeq.append('Y');
				break;
			case '-':
				newSeq.append('-');
				break;
			default:
				throw new SequenceException("There are non watson crick bases in the sequence.");
			}
		}
		return newSeq.toString();
	}
	
	public String getLoopType(int pos1, int pos2){
		if (isAsymetricLoop(pos1, pos2)){
			int internalLoopSize1 = 0;
			int internalLoopSize2 = 0;
			for (int i = 1; i <= getSequence(pos1, pos2).length() - 2; i++ ){
				
				if (getSequence(pos1, pos2).charAt(i) != '-'){
					internalLoopSize1 ++;
				}
				
				if (getComplementary().charAt(i) != '-'){
					internalLoopSize2 ++;
				}
			}
			if (Math.min(internalLoopSize1, internalLoopSize2) == 1 && Math.max(internalLoopSize2, internalLoopSize1) > 2){
				return Integer.toString(internalLoopSize1) + "x" + "n_n>2";
			}
			else if ((internalLoopSize1 == 2 && internalLoopSize2 != 3 && internalLoopSize2 != 1) || (internalLoopSize2 == 2 && internalLoopSize1 != 3 && internalLoopSize1 != 1)){
				return "others_non_2x2";
			}
			else {
				return Integer.toString(internalLoopSize1) + "x" + Integer.toString(internalLoopSize2);
			}
		}
		else{
			String internalLoopSize = Integer.toString(pos2 - pos1 - 1);
			return internalLoopSize + "x" + internalLoopSize;
		}
	}

	public static String getLoopFistMismatch(String loop){
		String mismatch = convertToPyr_Pur(loop.substring(0,1)) + loop.substring(1, 2);
		
		return mismatch;
	}
	
	public static String getSingleBulgeNeighbors(String bulge){
		String NNPair = bulge.substring(0, 1) + bulge.substring(2, 3);
		return NNPair;
	}
	
	public boolean isSymetric(int pos1, int pos2){
		for (int i = 0; i < getDuplexLength() - 1; i++){
			if (getSequence(pos1, pos2).charAt(i) != getComplementary(pos1, pos2).charAt(getSequence(pos1, pos2).length() - i - 1)){
				return false;
			}
		}
		return true;
	}
	
	public static String buildSymetricSequence(String seq1, String seq2){
		StringBuffer symetricSequence = new StringBuffer(seq1.length());
		
		symetricSequence.append(seq1.substring(0, 2));
		symetricSequence.append(seq2.substring(1, 2));
		symetricSequence.append(seq2.substring(0,1));
	
		return symetricSequence.toString();
	}
	
	public static String buildSymetricComplementary(String seq1, String seq2){
		StringBuffer symetricComplementary = new StringBuffer(seq1.length());
		
		symetricComplementary.append(seq2.substring(0, 2));
		symetricComplementary.append(seq1.substring(1, 2));
		symetricComplementary.append(seq1.substring(0, 1));
	
		return symetricComplementary.toString();
	}
	
	public static String getInversedSequence(String sequence){
		StringBuffer newSequence = new StringBuffer(sequence.length());

		for (int i = 0; i < sequence.length(); i++){

			newSequence.append(sequence.charAt(sequence.length() - i - 1)); 
		}
		return newSequence.toString();
	}
	
	public void encodeSequence(){
		StringBuffer comp = new StringBuffer(getDuplexLength());
		comp.append(this.complementary);
		
		for (int i = 0; i < this.sequence.length();i++){
			
			if (i < this.sequence.length() - 2){
				if (this.sequence.substring(i, i+3).equals("X_T") || this.sequence.substring(i, i+2).equals("X_C")){
					comp.insert(i," ");
					comp.insert(i + 1, " ");
					comp.insert(i + 2, " ");
				}
			}
			if (i < this.sequence.length() - 1){
				if (this.sequence.substring(i, i+2).equals("_A") || this.sequence.substring(i, i+2).equals("_X")){
					comp.insert(i," ");
				}
			}
			
			if (this.sequence.charAt(i) == 'L'){
				comp.insert(i," ");
			}
			else if (this.sequence.charAt(i) == '*'){
				comp.insert(i," ");
			}
		}
		
		this.complementary = comp.toString();
	}
	
	public void encodeComplementary(){
		StringBuffer seq = new StringBuffer(getDuplexLength());
		seq.append(this.sequence);
		
		for (int i = 0; i < this.complementary.length();i++){
			if (i < this.complementary.length() - 2){
				if (this.complementary.substring(i, i+3).equals("X_T") || this.complementary.substring(i, i+2).equals("X_C")){
					seq.insert(i," ");
					seq.insert(i + 1, " ");
					seq.insert(i + 2, " ");
				}
			}
			if (i < this.complementary.length() - 1){
				if (this.complementary.substring(i, i+2).equals("_A") || this.complementary.substring(i, i+2).equals("_X")){
					seq.insert(i," ");
				}
			}
			
			if (this.complementary.charAt(i) == 'L'){
				seq.insert(i," ");
			}
			else if (this.complementary.charAt(i) == '*'){
				seq.insert(i," ");
			}
		}
		
		this.sequence = seq.toString();
	}
	
	public static NucleotidSequences decodeSequences(String sequence, String complementary){
		StringBuffer seq = new StringBuffer(sequence.length());
		StringBuffer comp = new StringBuffer(complementary.length());
		
		seq.append(sequence);
		comp.append(complementary);
		
		for (int i = 0 ; i < sequence.length() - 2; i++){
			if (sequence.substring(i, i + 3).equals("X_T") || sequence.substring(i, i + 3).equals("X_C")){
				seq.deleteCharAt(i + 1);
				seq.deleteCharAt(i + 1);

			}
			if (complementary.substring(i, i + 3).equals("X_T") || complementary.substring(i, i + 3).equals("X_C")){
				comp.deleteCharAt(i + 1);
				comp.deleteCharAt(i + 1);	
			}
		}

		String newSequence = seq.toString().replace(" ", "");
		String newComplementary = comp.toString().replace(" ", "");
		
		NucleotidSequences DecodedSequences = new NucleotidSequences(newSequence, newComplementary);
		
		return DecodedSequences;
	}
	
	public String getComplementaryTo(String sequence, int pos1, int pos2){
		
		if (sequence.equals(getSequence(pos1, pos2))){
			return getComplementary(pos1, pos2);
		}
		else if (sequence.equals(getComplementary(pos1, pos2))){
			return getSequence(pos1, pos2);
		}
		else{
			throw new SequenceException("There is no complementary sequence.");
		}
	}
	
	public String getSequenceContainig(String motif, int pos1, int pos2){
		if (getSequence(pos1, pos2).contains(motif)){
			return getSequence(pos1, pos2);
		}
		else if (getComplementary(pos1, pos2).contains(motif)){
			return getComplementary(pos1, pos2);
		}
		else{
			return null;
		}
	}
	
	public NucleotidSequences removeHydroxyadenine(int pos1, int pos2){
		NucleotidSequences modifiedSequence = new NucleotidSequences(getSequence(pos1, pos2), getComplementary(pos1, pos2));
		StringBuffer seq = new StringBuffer(modifiedSequence.getDuplexLength());
		StringBuffer comp = new StringBuffer(modifiedSequence.getDuplexLength());
		
		seq.append(getSequenceContainig("A*", pos1, pos2));
		comp.append(getComplementaryTo(seq.toString(), pos1, pos2));
		
		int index = seq.toString().indexOf("*");
		seq.deleteCharAt(index);
		comp.deleteCharAt(index);
		if (index > 0){
			comp.deleteCharAt(index - 1);
			comp.insert(index - 1, 'T');
		}
		
		NucleotidSequences noModifiedSequence = new NucleotidSequences(seq.toString(), comp.toString());

		return noModifiedSequence;
	}
	
	public NucleotidSequences removeDeoxyadenine(int pos1, int pos2){
		
		NucleotidSequences modifiedSequence = new NucleotidSequences(getSequence(pos1, pos2), getComplementary(pos1, pos2));
		StringBuffer seq = new StringBuffer(modifiedSequence.getDuplexLength());
		StringBuffer comp = new StringBuffer(modifiedSequence.getDuplexLength());
		
		seq.append(getSequenceContainig("_", pos1, pos2));
		comp.append(getComplementaryTo(seq.toString(), pos1, pos2));
		
		int index = seq.toString().indexOf("_");
		
		seq.deleteCharAt(index);
		comp.deleteCharAt(index);
			
		if (seq.toString().contains("X")){
			int Xpos = seq.toString().indexOf("X");
				comp.deleteCharAt(Xpos);
				comp.insert(Xpos, 'T');
			}

		NucleotidSequences noModifiedSequence = new NucleotidSequences(seq.toString(), comp.toString());

		return noModifiedSequence;
	}
	
	public boolean isCNGMotif(int pos1, int pos2){
		if (pos2 != getDuplexLength() - 1 || pos1 != 0 || getDuplexLength() - 2 < 3){
			return false;
		}
		
		int index = pos1 + 1;
		char mismatch = sequence.charAt(pos1 + 2);
		while (index <= pos2 - 3){
			if (this.sequence.charAt(index + 1) != this.complementary.charAt(index + 1)){
				return false;
			}
			else {
				if(isBasePair('G', 'C', index + 2) && isBasePair('C', 'G', index) && isBasePair(mismatch, mismatch, index + 1)){
					index += 3;
				}
				else{
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean isPerfectMatchSequence(int pos1, int pos2){
		if (pos2 > getDuplexLength() - 1 || pos1 < 0){
			return false;
		}
		
		for (int i = pos1; i <= pos2; i++){
			if (Helper.isComplementaryBasePair(this.sequence.charAt(i), this.complementary.charAt(i)) == false){
				return false;
			}
		}
		return true;
	}
	
	public int calculateNumberDanglingEnds(){
		int numberDe = 0;
		if ((sequence.charAt(0) == '-' && Helper.isWatsonCrickBase(complementary.charAt(0))) || (complementary.charAt(0) == '-' && Helper.isWatsonCrickBase(sequence.charAt(0)))){
			numberDe ++;
		}
		else if ((sequence.charAt(getDuplexLength() - 1) == '-' && Helper.isWatsonCrickBase(complementary.charAt(getDuplexLength() - 1))) || (complementary.charAt(getDuplexLength() - 1) == '-' && Helper.isWatsonCrickBase(sequence.charAt(getDuplexLength() - 1)))){
			numberDe ++;
		}
		return numberDe;
	}
	
	public boolean isDanglingEnd(int pos1, int pos2){
		
		if ((getSequence(pos1, pos2).contains("-") && getComplementary(pos1, pos2).contains("-")) || (getSequence(pos1, pos2).contains("-") == false && getComplementary(pos1, pos2).contains("-") == false)){
			return false;
		}
		
		if (pos1 != 0 && pos2 != getDuplexLength() - 1){
			return false;
		}

		String sequenceWithoutDanglingEnd = getSequenceContainig("-", pos1, pos2);
		String sequenceWithDanglingEnd = getComplementaryTo(sequenceWithoutDanglingEnd, pos1, pos2);
		for (int i = pos1 + 1; i < pos2; i++){
			if (sequenceWithoutDanglingEnd.charAt(i) != '-' || Helper.isWatsonCrickBase(sequenceWithDanglingEnd.charAt(i)) == false){
				return false;
			}
		}
		return true;
	}
	
	public boolean isGUSequences(int pos1, int pos2){
		if (pos2 > getDuplexLength() - 1 || pos1 < 0){
			return false;
		}
		
		if (Helper.isComplementaryBasePair(this.sequence.charAt(pos1), this.complementary.charAt(pos1)) == false && isBasePairEqualsTo('G', 'U', pos1) == false){
			return false;
		}
		
		if (Helper.isComplementaryBasePair(this.sequence.charAt(pos2), this.complementary.charAt(pos2)) == false && isBasePairEqualsTo('G', 'U', pos2) == false){
			return false;
		}
		
		for (int i = pos1 + 1; i <= pos2 - 1; i++){
			if (isBasePairEqualsTo('G', 'U', i) == false){
				return false;
			}
		}
		return true;
	}
	
	public boolean isMismatchPair(int pos){
		if (Helper.isComplementaryBasePair(this.sequence.charAt(pos), this.complementary.charAt(pos)) == false && Helper.isWatsonCrickBase(this.sequence.charAt(pos)) && Helper.isWatsonCrickBase(this.complementary.charAt(pos))){
			return true;
		}
		return false;
	}
	
	public boolean isMismatch(int pos1, int pos2){
		
		if (pos2 > getDuplexLength() - 1 || pos1 < 0){
			return false;
		}
		int numbergapSequence = 0;
		int numbergapComplementary = 0;
		
		if (isBasePair('G', 'U', pos1) == false && Helper.isComplementaryBasePair(this.sequence.charAt(pos1), this.complementary.charAt(pos1)) == false){
			return false;
		}
		else if (isBasePair('G', 'U', pos2) == false && Helper.isComplementaryBasePair(this.sequence.charAt(pos2), this.complementary.charAt(pos2)) == false){
				return false;
		}
		
		for (int i = pos1 + 1; i <= pos2 - 1 ; i++){
			if (Helper.isComplementaryBasePair(this.sequence.charAt(i), this.complementary.charAt(i))){
				return false;
			}
			else if (Helper.isWatsonCrickBase(this.sequence.charAt(i)) == false){
				if (this.sequence.charAt(i) != '-' || (this.sequence.charAt(i) == '-' && pos2 - pos1 + 1 == 3)){
					return false;
				}
				numbergapSequence ++;
			}
			else if (Helper.isWatsonCrickBase(this.complementary.charAt(i)) == false){
				if (this.complementary.charAt(i) != '-' || (this.sequence.charAt(i) == '-' && pos2 - pos1 + 1 == 3)){
					return false;
				}
				numbergapComplementary ++;
			}
		}
		
		if (numbergapSequence == pos2 - pos1 - 1 || numbergapComplementary == pos2 - pos1 - 1){
			return false;
		}
		return true;
	}
	
	public boolean isBulgeLoop(int pos1, int pos2){
		if (pos2 > getDuplexLength() - 1 || pos1 < 0){
			return false;
		}

		if ((getSequence(pos1, pos2).contains("-") && getComplementary(pos1, pos2).contains("-")) || (getSequence(pos1, pos2).contains("-") == false && getComplementary(pos1, pos2).contains("-") == false)){
			return false;
		}
		String sequenceWithGap = getSequenceContainig("-", pos1, pos2);

		int numbergap = 0;
		
		for (int i = 0; i <= pos2 - pos1 ; i++){
			if (sequenceWithGap.charAt(i) == '-'){
				numbergap ++;
			}
		}
		if (numbergap != pos2 - pos1 - 1){
			return false;
		}
		return true;
	}
	
	public boolean isModifiedAcidAfter(int pos){
		if (isBasePairEqualsTo('L', ' ', pos) || isBasePairEqualsTo('*', ' ', pos)){
			return true;
		}
		return false;
	}
	
	public boolean isModifiedAcidBefore(int pos){
		if (pos + 1 <= getDuplexLength() - 1){
			if (getSequence(pos, pos + 1).equals("_A") || getSequence(pos, pos + 1).equals("_X") || getComplementary(pos, pos + 1).equals("A_") || getComplementary(pos, pos + 1).equals("X_")){
				return true;
			}
		}
		return false;
	}
	
	private static String getModifiedAcid(String sequence, int pos){
		
		StringBuffer modifiedAcid = new StringBuffer();
		int index = pos;
		
		while (Helper.isWatsonCrickBase(sequence.charAt(index)) == false && index <= sequence.length() - 1){
			modifiedAcid.append(sequence.charAt(index));
			if (modifiedAcid.toString().equals("X_")){
				index ++;
				modifiedAcid.append(sequence.charAt(index));
			}
			index ++;
		}
		if (modifiedAcid.toString().equals("_")){
			modifiedAcid.append(sequence.charAt(index));
		}
		if (index - 2 > 0){
			if (modifiedAcid.toString().equals("*")){
				index -= 2;
				modifiedAcid.deleteCharAt(0);
				modifiedAcid.append(sequence.substring(index, index + 2));
			}
		}
		return modifiedAcid.toString();
	}
	
	public boolean isListedModifiedAcid(int pos1, int pos2){
		if (pos2 > getDuplexLength() - 1 || pos1 < 0){
			return false;
		}
		
		String modifiedAcid1 = null;
		String modifiedAcid2 = null;
		
		for (int i = pos1; i <= pos2; i++){
			if (Helper.isWatsonCrickBase(this.sequence.charAt(i)) == false && this.sequence.charAt(i) != '-'){
				modifiedAcid1 = getModifiedAcid(this.sequence, i);
				break;
			}
			if (Helper.isWatsonCrickBase(this.complementary.charAt(i)) == false && this.complementary.charAt(i) != '-'){
				modifiedAcid2 = getModifiedAcid(this.complementary, i);
				break;
			}
		}
		
		if (modifiedAcid1 != null && modifiedNucleotides.contains(modifiedAcid1) == false){
			return false;
		}
		
		if (modifiedAcid2 != null && modifiedNucleotides.contains(modifiedAcid2) == false){
			return false;
		}
		
		if (modifiedAcid1 == null && modifiedAcid2 == null){
			return false;
		}
		
		return true;
	}
	
	public ModifiedAcidNucleic getModifiedAcidName(String sequence, String complementary){
		String modifiedAcid1 = null;
		String modifiedAcid2 = null;
		ModifiedAcidNucleic name;
		
		for (int i = 0; i <= sequence.length() - 1; i++){
			if (Helper.isWatsonCrickBase(sequence.charAt(i)) == false && sequence.charAt(i) != '-'){
				modifiedAcid1 = getModifiedAcid(sequence, i);
				break;
			}
		}
		for (int i = 0; i <= complementary.length() - 1; i++){

			if (Helper.isWatsonCrickBase(complementary.charAt(i)) == false && complementary.charAt(i) != '-'){
				modifiedAcid2 = getModifiedAcid(complementary, i);
				break;
			}
		}

		if (modifiedAcid1 != null){
			name = modifiedAcidNames.get(modifiedAcid1);
			if (name != null){
				return name;
			}
		}
		
		if (modifiedAcid2 != null){
			name = modifiedAcidNames.get(modifiedAcid2);

			if (name != null){
				return name;
			}
		}
		return null;
	}
	
	public boolean isAPyrimidineInThePosition(int pos){
		if (Helper.isPyrimidine(this.sequence.charAt(pos)) || Helper.isPyrimidine(this.complementary.charAt(pos))){
			return true;
		}
		return false;
	}
	
	public boolean isTandemMismatchGGPenaltyNecessary(int pos){
		if ((isBasePair('G', 'G', pos) && isBasePair('A','A', pos + 1)) || (isBasePair('G', 'G', pos + 1) && isBasePair('A','A', pos))){
			return true;	
		}
		else if ((isBasePair('G', 'G', pos) && isAPyrimidineInThePosition(pos + 1)) || (isBasePair('G', 'G', pos + 1) && isAPyrimidineInThePosition(pos))){
			return true;
		}
		return false;
	}
	
	public boolean isTandemMismatchDeltaPPenaltyNecessary(int pos){
		if (isBasePairEqualsTo('A', 'G', pos) || isBasePairEqualsTo('A', 'G', pos + 1)){
			if (isBasePairEqualsTo('C', 'U', pos) || isBasePairEqualsTo('C', 'U', pos + 1)){
				return true;
			}
			else if (isBasePair('C', 'C', pos) || isBasePair('C', 'C', pos + 1)){
				return true;
			}
		}
		else if ((isBasePair('U', 'U', pos) && isBasePair('A', 'A', pos + 1)) || (isBasePair('U', 'U', pos + 1) && isBasePair('A', 'A', pos))){
			return true;
		}
		return false;
	}
	
	public boolean is2StabilizingMismatchesPenaltyNecessary(int pos){
		if (isBasePairEqualsTo('G', 'U', pos) || isBasePairEqualsTo('G', 'A', pos) || isBasePair('U', 'U', pos)){
			if (isBasePairEqualsTo('G', 'U', pos + 1) || isBasePairEqualsTo('G', 'A', pos + 1) || isBasePair('U', 'U', pos + 1)){
				return true;
			}
		}
		return false;
	}
	
	public boolean is1StabilizingMismatchPenaltyNecessary(int pos){
		if ((isBasePairEqualsTo('G', 'U', pos) || isBasePairEqualsTo('G', 'A', pos) || isBasePair('U', 'U', pos)) || (isBasePairEqualsTo('G', 'U', pos + 1) || isBasePairEqualsTo('G', 'A', pos + 1) || isBasePair('U', 'U', pos + 1))){
			if (isBasePairEqualsTo('C', 'A', pos ) == false && isBasePairEqualsTo('C', 'A', pos + 1) == false){
				return true;
			}
		}
		return false;
	}

	public boolean isSequenceGap(int pos1, int pos2){
		if (getSequence(pos1, pos2).contains("-") || getComplementary(pos1, pos2).contains("-")){
			return false;
		}
		return true;
	}

}
