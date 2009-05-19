package melting;

import java.util.ArrayList;
import java.util.HashMap;

public class NucleotidSequences {

	private String sequence;
	private String complementary;
	private static ArrayList<String> modifiedNucleotides = new ArrayList<String>();
	
	private static HashMap<String, ModifiedAcidNucleic> modifiedAcidNames = new HashMap<String, ModifiedAcidNucleic>();
	
	public NucleotidSequences(String sequence, String complementary){
		initializeModifiedAcidArrayList();
		initializeModifiedAcidHashmap();
		
		this.sequence = sequence;
		this.complementary = complementary;
		encodeSequences();
		
	}
	
	private void initializeModifiedAcidArrayList(){
		modifiedNucleotides.add("_A");
		modifiedNucleotides.add("_T");
		modifiedNucleotides.add("_C");
		modifiedNucleotides.add("_X");
		modifiedNucleotides.add("X");
		modifiedNucleotides.add("A*");
		modifiedNucleotides.add("L");
		modifiedNucleotides.add("I");
	}
	
	private void initializeModifiedAcidHashmap(){
		modifiedAcidNames.put("I", ModifiedAcidNucleic.inosine);
		modifiedAcidNames.put("L", ModifiedAcidNucleic.lockedAcidNucleic);
		modifiedAcidNames.put("A*", ModifiedAcidNucleic.hydroxyadenine);
		modifiedAcidNames.put("X", ModifiedAcidNucleic.azobenzene);
		modifiedAcidNames.put("_A", ModifiedAcidNucleic.L_deoxyadenine);
		modifiedAcidNames.put("_X", ModifiedAcidNucleic.L_deoxyadenine);
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
						System.err.println("ERROR : There is no implemented method for computing the" +
								"melting temperature of a sequence containing " + modifiedAcid + ".");
						return false;
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
	
	public boolean isTerminal5AT(){
		
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
			seq.deleteCharAt(indexStart);
			comp.deleteCharAt(indexStart);
		}
		
		if (indexStart == getDuplexLength() - 1){
			System.out.println("error : the sequences can be hybridezed.");
			return null;
		}
		
		int indexEnd = 0;
		while (Helper.isComplementaryBasePair(this.sequence.charAt(indexEnd), this.complementary.charAt(indexEnd)) == false && indexEnd >= 0){
			seq.deleteCharAt(indexEnd);
			comp.deleteCharAt(indexEnd);
		}
		
		if (indexEnd == 0){
			System.out.println("error : the sequences can be hybridezed.");
			return null;
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

	public static String getLoopFistMismatch(String loop){
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
				comp.insert(i," ");
			}
			else if (this.complementary.charAt(i) == '_' && (this.complementary.charAt(i + 1) == 'A' || this.complementary.charAt(i + 1) == 'X')){
				seq.insert(i," ");
			}
			else if (this.sequence.charAt(i - 1) == '_' && (this.sequence.charAt(i) == 'T' || this.sequence.charAt(i) == 'C')){
				comp.insert(i," ");
			}
			else if (this.complementary.charAt(i - 1) == '_' && (this.complementary.charAt(i) == 'T' || this.complementary.charAt(i) == 'C')){
				seq.insert(i," ");
			}
			else if (this.sequence.charAt(i) == 'X' && this.sequence.charAt(i - 1) != '_'){
				comp.insert(i," ");
			}
			else if (this.complementary.charAt(i) == 'X' && this.complementary.charAt(i - 1) != '_'){
				seq.insert(i," ");
			}
			else if (this.sequence.charAt(i) == 'L'){
				comp.insert(i," ");
			}
			else if (this.complementary.charAt(i) == 'L'){
				seq.insert(i," ");
			}
			else if (this.sequence.charAt(i) == '*'){
				comp.insert(i," ");
			}
			else if (this.complementary.charAt(i) == '*'){
				seq.insert(i," ");
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
	
	public String getComplementaryTo(String sequence, int pos1, int pos2){
		if (sequence.equals(getSequence(pos1, pos2))){
			return getComplementary(pos1, pos2);
		}
		else if (sequence.equals(getComplementary(pos1, pos2))){
			return getSequence(pos1, pos2);
		}
		else{
			return null;
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
		int index = pos1;
		
		if (pos2 > getDuplexLength() - 1 || pos2 - pos1 + 1 < 3 || pos1 < 0){
			return false;
		}
		
		while (index <= pos2 - 3){
			if (this.sequence.charAt(index + 1) != this.complementary.charAt(index + 1)){
				return false;
			}
			else {
				if(isBasePair('G', 'C', index + 2) && isBasePair('C', 'G', index)){
					index += 3;
				}
				return false;
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
		
		for (int i = pos1; i <= pos2; i++){
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
	
	public boolean isMismatch(int pos1, int pos2){
		
		if (pos2 > getDuplexLength() - 1 || pos1 < 0){
			return false;
		}
		int numbergapSequence = 0;
		int numbergapComplementary = 0;
		
		if (Helper.isComplementaryBasePair(this.sequence.charAt(pos1), this.complementary.charAt(pos1)) || Helper.isComplementaryBasePair(this.sequence.charAt(pos2), this.complementary.charAt(pos2))){
			return true;
		}
		
		if (Helper.isWatsonCrickBase(this.sequence.charAt(pos1)) == false || Helper.isWatsonCrickBase(this.complementary.charAt(pos1)) == false){
			if ((this.sequence.charAt(pos1) != '-' && this.complementary.charAt(pos1) != '-') && isBasePairEqualsTo('G', 'U', pos1) == false){
				return true;
			}
		}
		
		if (Helper.isWatsonCrickBase(this.sequence.charAt(pos2)) == false || Helper.isWatsonCrickBase(this.complementary.charAt(pos2)) == false){
			if ((this.sequence.charAt(pos2) != '-' && this.complementary.charAt(pos2) != '-') && isBasePairEqualsTo('G', 'U', pos2) == false){
				return true;
			}
		}
		
		for (int i = pos1 + 1; i <= pos2 - 1 ; i++){
			if (Helper.isComplementaryBasePair(this.sequence.charAt(i), this.complementary.charAt(i))){
				return false;
			}
			else if (Helper.isWatsonCrickBase(this.sequence.charAt(i)) == false){
				if (this.sequence.charAt(i) != '-'){
					return false;
				}
				numbergapSequence ++;
			}
			else if (Helper.isWatsonCrickBase(this.complementary.charAt(i)) == false){
				if (this.complementary.charAt(i) != '-'){
					return false;
				}
				numbergapComplementary ++;
			}
		}
		
		if (numbergapSequence == pos2 - pos1 || numbergapComplementary == pos2 - pos1){
			return false;
		}
		return true;
	}
	
	public boolean isBulgeLoop(int pos1, int pos2){
		
		if (pos2 > getDuplexLength() - 2 || pos1 < 0){
			return false;
		}
		
		if ((getSequence(pos1, pos2).contains("-") && getComplementary(pos1, pos2).contains("-")) || (getSequence(pos1, pos2).contains("-") == false && getComplementary(pos1, pos2).contains("-") == false)){
			return false;
		}
		
		String sequenceWithGap = getSequenceContainig("-", pos1, pos2);
		
		int numbergap = 0;
		
		for (int i = pos1; i <= pos2 ; i++){
			if (sequenceWithGap.charAt(i) == '-'){
				numbergap ++;
			}
		}
		
		if (numbergap != pos2 - pos1){
			return false;
		}
		return true;
	}
	
	private static String getModifiedAcid(String sequence, int pos){
		
		StringBuffer modifiedAcid = new StringBuffer();
		int index = pos;
		
		while (Helper.isWatsonCrickBase(sequence.charAt(index)) == false && index <= sequence.length() - 1){
			modifiedAcid.append(sequence.charAt(index));
			index ++;
		}
		return modifiedAcid.toString();
	}
	
	public boolean isListedModifiedAcid(int pos1, int pos2){
		if (pos2 > getDuplexLength() - 2 || pos1 < 0){
			return false;
		}
		
		String modifiedAcid1 = null;
		String modifiedAcid2 = null;
		
		for (int i = pos1; i <= pos2; i++){
			if (Helper.isWatsonCrickBase(this.sequence.charAt(i)) == false && this.sequence.charAt(i) != '-'){
				modifiedAcid1 = getModifiedAcid(this.sequence, i);
				break;
			}
		}
		
		for (int i = pos1; i <= pos2; i++){
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
		
		for (int i = 0; i <= complementary.length(); i++){
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
}
