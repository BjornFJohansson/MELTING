package melting;

import java.util.ArrayList;
import java.util.HashMap;

import melting.exceptions.SequenceException;

public class NucleotidSequencesTest {

	private ArrayList<BasePair> duplex = new ArrayList<BasePair>();
	
	private static HashMap<String, ModifiedAcidNucleic> modifiedAcidNames = new HashMap<String, ModifiedAcidNucleic>();
	
	public NucleotidSequencesTest(String sequence, String complementary){
		BasePair.initializeNucleicAcidList();
		initializeModifiedAcidHashmap();
		String [] sequences = encodeSequences(sequence, complementary);
		sequences = correctSequences(sequences[0], sequences[1]);
		
		int pos = 0;
		while (pos < sequences[0].length()){
			BasePair pair = getBasePair(sequences[0], sequences[1], pos);
			duplex.add(pair);
			pos += pair.getLengthAcid();
		}
	}
	
	private static String getNucleicAcid(String sequence, int pos){
		ArrayList<String> possibleNucleicAcids = new ArrayList<String>();
		String acid = null;
		String seq = sequence.substring(pos);
		
		for (int i = 0; i < BasePair.getExistingNucleicAcids().size(); i++){
			if (seq.startsWith(BasePair.getExistingNucleicAcids().get(i))){
				possibleNucleicAcids.add(BasePair.getExistingNucleicAcids().get(i));
			}
		}
		int lengthAcid = 0;
		
		if (possibleNucleicAcids.size() == 0 && seq.charAt(0) != ' '){
			throw new SequenceException("Some nucleic acids are unknown in the sequences. Check the options and the sequence: " + sequence);
		}
		else {
			for (int i = 0; i < possibleNucleicAcids.size() ; i++){
				if (possibleNucleicAcids.get(i).length() > lengthAcid){
					lengthAcid = possibleNucleicAcids.get(i).length();
					acid = possibleNucleicAcids.get(i);
				}
			}
		}
		return acid;
	}
	
	private String encodeComplementary(String sequence, StringBuffer comp){
		int pos = 0;
		
		while (pos < sequence.length()){
			String acid = getNucleicAcid(sequence, pos);
			if (acid.equals("X_T") || acid.equals("X_C")){
				comp.insert(pos," ");
				comp.insert(pos + 1, " ");
				comp.insert(pos + 2, " ");
			}
			else if ((acid.equals("_A") && getNucleicAcid(comp.toString(), pos).equals("_X") == false) || (acid.equals("_X") && getNucleicAcid(comp.toString(), pos).equals("_A") == false)){
				comp.insert(pos," ");
			}
			else if (acid.charAt(1) == 'L'){
				comp.insert(pos," ");
			}
			else if(acid.equals("A*")){
				comp.insert(pos," ");
			}
			pos += acid.length();
		}	
		
		return comp.toString();
	}
	
	private String encodeSequence(String complementary, StringBuffer seq){
		int pos = 0;
		
		while (pos < complementary.length()){
			String acid = getNucleicAcid(complementary, pos);
			if (acid.equals("X_T") || acid.equals("X_C")){
				seq.insert(pos," ");
				seq.insert(pos + 1, " ");
				seq.insert(pos + 2, " ");
			}
			else if ((acid.equals("_A") && getNucleicAcid(seq.toString(), pos).equals("_X") == false) || (acid.equals("_X") && getNucleicAcid(seq.toString(), pos).equals("_A") == false)){
				seq.insert(pos," ");
			}
			else if (acid.charAt(1) == 'L'){
				seq.insert(pos," ");
			}
			else if(acid.equals("A*")){
				seq.insert(pos," ");
			}
			pos += acid.length();
		}	
		
		return seq.toString();
	}
	
	public String [] encodeSequences(String sequence, String complementary){
		StringBuffer seq = new StringBuffer();
		StringBuffer comp = new StringBuffer();

		seq.append(sequence);
		comp.append(complementary);

		String [] sequences = {encodeSequence(complementary, seq), encodeComplementary(sequence, comp)};
		return sequences;
	}
	
	public String [] correctSequences( String sequence, String complementary){
		StringBuffer correctedSequence = new StringBuffer(sequence.length());
		StringBuffer correctedComplementary = new StringBuffer(complementary.length());
		
		correctedSequence.append(sequence);
		correctedComplementary.append(complementary);
		
		for (int i = 0; i < correctedSequence.toString().length(); i++){
			if (correctedSequence.toString().charAt(i) == '-' && correctedComplementary.toString().charAt(i) == '-'){
				correctedSequence.deleteCharAt(i);
				correctedComplementary.deleteCharAt(i);
			}
		}
		String [] sequences = {correctedSequence.toString(), correctedComplementary.toString()};
		return sequences;
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
	
	private static BasePair getBasePair(String sequence, String complementary, int pos){
		BasePair acid = new BasePair();
		
		String acid1 = getNucleicAcid(sequence, pos);
		String acid2 = getNucleicAcid(complementary, pos);
		if (acid1 == null && acid2 == null){
			throw new SequenceException("Some nucleic acids are unknown in the sequences. Check the options.");
		}
		else if (acid1 == null){
			acid1 = sequence.substring(pos, acid2.length());
		}
		else if (acid2 == null){
			acid2 = sequence.substring(pos, acid1.length());
		}
		acid.setTopAcid(acid1);
		acid.setBottomAcid(acid2);
		
		return acid;
	}
	
	public void initializeModifiedAcidHashmap(){
		modifiedAcidNames.put("I", ModifiedAcidNucleic.inosine);
		modifiedAcidNames.put("AL", ModifiedAcidNucleic.lockedAcidNucleic);
		modifiedAcidNames.put("TL", ModifiedAcidNucleic.lockedAcidNucleic);
		modifiedAcidNames.put("CL", ModifiedAcidNucleic.lockedAcidNucleic);
		modifiedAcidNames.put("GL", ModifiedAcidNucleic.lockedAcidNucleic);
		modifiedAcidNames.put("A*", ModifiedAcidNucleic.hydroxyadenine);
		modifiedAcidNames.put("X_C", ModifiedAcidNucleic.azobenzene);
		modifiedAcidNames.put("X_T", ModifiedAcidNucleic.azobenzene);
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
				else if (hybridization.equals("rnarna") || hybridization.equals("mrnarna") || hybridization.equals("rnamrna") || hybridization.equals("dnarna")){
					complementary.append('U');
				}
				break;
			case 'T':
				if (i != 0){
					if (sequence.charAt(i - 1) != '_'){
						complementary.append('A');
					}
				}
				else{
					complementary.append('A');
				}
				break;
			case 'C':
				if (i != 0){
					if (sequence.charAt(i - 1) != '_'){
						complementary.append('G');
					}
				}
				else{
					complementary.append('G');
				}
				break;
			case 'G':
				complementary.append('C');
				break;
			case 'U':
				complementary.append('A');
				break;
			case '-':
				complementary.append('-');
				break;
			case 'X':
				if (i != 0){
					if (sequence.charAt(i - 1) == '_'){
						complementary.append('A');
					}
				}
				break;
			}
		}
		return complementary.toString();
	}
	
	public static boolean checkSequence(String sequence){
		
		int position = 0;
		
		if (sequence.length() == 0){
			throw new SequenceException("The sequence must be entered with the option -S.");
		}
		
		while (position < sequence.length()){
			String acid = getNucleicAcid(sequence, position);
			if (acid == null){
				return false;
			}
			
			position += acid.length();
		}
		return true;
	}
	
	public int getDuplexLength(){
		return duplex.size();
	}
	
	public String getSequence() {
		return getSequence(0, getDuplexLength() - 1);
	}

	public String getComplementary() {
		return getComplementary(0, getDuplexLength() - 1);
	}
	
	public String getSequence(int pos1, int pos2){
		if (pos1 < 0 || pos2 > getDuplexLength() - 1){
			throw new SequenceException("The length of the duplex is inferior to " + pos2 + 1 + "and superior to 0.");
		}
		StringBuffer sequence = new StringBuffer();
		for (int i = pos1; i <= pos2; i++){
			BasePair basePair = duplex.get(i);
			sequence.append(basePair.getTopAcid());
		}
		return sequence.toString();
	}
	
	public String getComplementary(int pos1, int pos2){
		if (pos1 < 0 || pos2 > getDuplexLength() - 1){
			throw new SequenceException("The length of the duplex is inferior to " + pos2 + 1 + "and superior to 0.");
		}
		StringBuffer complementary = new StringBuffer();
		for (int i = pos1; i <= pos2; i++){
			BasePair basePair = duplex.get(i);
			complementary.append(basePair.getBottomAcid());
		}
		return complementary.toString();
	}
	
	public String getSequenceNNPair(int pos){
		return getSequence(pos, pos+1);
	}
	
	public String getComplementaryNNPair(int pos){
		return getComplementary(pos, pos + 1);
	}
	
	public double calculateNumberOfTerminal(String base1, String base2){
		double numberOfTerminal = 0.0;
		BasePair firstTerminalBasePair = duplex.get(0);
		BasePair lastTerminalBasePair = duplex.get(getDuplexLength() - 1);

		if (firstTerminalBasePair.isBasePairEqualTo(base1, base2)){
			numberOfTerminal++;
		}
		
		if (lastTerminalBasePair.isBasePairEqualTo(base1, base2)){
			numberOfTerminal++;
		}
		return numberOfTerminal;
	}
	
	public double getNumberTerminal5TA(){
		double number5TA = 0;
		BasePair firstTerminalBasePair = duplex.get(0);
		BasePair lastTerminalBasePair = duplex.get(getDuplexLength() - 1);
		
		if (firstTerminalBasePair.isBasePairStrictlyEqualTo("T", "A")){
			number5TA ++;
		}
		if (lastTerminalBasePair.isBasePairStrictlyEqualTo("A", "T")){
			number5TA ++;
		}
		return number5TA;
	}
	
	public ArrayList<BasePair> removeTerminalUnpairedNucleotides(){
		ArrayList<BasePair> newDuplex = new ArrayList<BasePair>();
		newDuplex = duplex;
		int indexStart = 0;
		while (indexStart <= getDuplexLength() - 1){
			BasePair pair = duplex.get(0);
			if (pair.isUnpaired()){
				newDuplex.remove(0);
				indexStart ++;
			}
			else {
				break;
			}
		}

		if (indexStart == getDuplexLength() - 1){
			throw new SequenceException("The sequences can be hybridized. Check the sequences.");
		}
		
		int indexEnd = getDuplexLength() - 1;
		while (indexStart <= getDuplexLength() - 1){
			BasePair pair = duplex.get(getDuplexLength() - 1);
			if (pair.isUnpaired()){
				newDuplex.remove(getDuplexLength() - 1);
				indexEnd --;
			}
			else {
				break;
			}
		}
		if (indexEnd == 0){
			throw new SequenceException("The sequences can be hybridized. Check the sequences.");
		}
		
		return newDuplex;
	}
	
	public boolean isBasePair(String base1, String base2, int pos){
		BasePair pair = duplex.get(pos);
		if (pair.isBasePairEqualTo(base1, base2)){
			return true;
		}
		return false;
	}
	
	public static String getSens(String seq1, String seq2){
		if (seq1.length() == 0){
			return "3";
		}
		else if (seq2.length() == 0){
			return "5";
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
	
	public String getSequenceSens(String sequence, int pos1, int pos2){
		if (sequence.equals(getSequence(pos1, pos2))){
			return "5'3'";
		}
		else if (sequence.equals(getComplementary(pos1, pos2))){
			return "3'5'";
		}
		else {
			throw new SequenceException("We don't recognize the sequence " + getSequence(pos1, pos2));
		}
	}
	
	public double calculatePercentGC(){
		double numberGC = 0.0;
		
		for (int i = 0; i < getDuplexLength();i++){
			BasePair pair = duplex.get(i);
			if (pair.isBasePairEqualTo("G", "C")){
				numberGC++;
			}
		}
		
		return numberGC / (double)getDuplexLength() * 100.0;
	}
	
	public double getPercentMismatching(){
		double numberMismatching = 0.0;
	
		for (int i = 0; i < getDuplexLength();i++){
			BasePair pair = duplex.get(i);
			if (pair.isComplementaryBasePair() == false){
				numberMismatching++;
			}
		}
		return numberMismatching / (double)getDuplexLength() * 100.0;
	}
	
	public boolean isOneGCBasePair(){
		for (int i = 0; i < getDuplexLength();i++){
			BasePair pair = duplex.get(i);
			if (pair.isBasePairEqualTo("G", "C")){
				return true;
			}
		}
		return false;
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

	public int calculateInternalLoopLength (int pos1, int pos2){
		int loop = 2 * (pos2 - pos1 - 1);
		
		for (int i = pos1 + 1; i <= pos2 - 1; i++){
			BasePair pair = duplex.get(i);
			if (pair.isUnpaired()){
				loop--;
			}
			if (pair.isUnpaired()){
				loop--;
			}
		}
		return loop;
	}

	public boolean isAsymetricLoop(int pos1, int pos2){
		
		for (int i= pos1 + 1; i <= pos2 - 1; i++){
			BasePair pair = duplex.get(i);
			if (pair.isUnpaired()){
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
			for (int i = pos1 + 1; i <= pos2 - 1; i++ ){
				BasePair pair = duplex.get(i);
				if (pair.getTopAcid().equals("-") == false){
					internalLoopSize1 ++;
				}
				
				if (pair.getBottomAcid().equals("-") == false){
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

	public static String [] getLoopFistMismatch(ArrayList<BasePair> loop){
		String mismatch1 = convertToPyr_Pur(loop.get(0).getTopAcid()) + loop.get(1).getTopAcid();
		String mismatch2 = convertToPyr_Pur(loop.get(0).getBottomAcid()) + loop.get(1).getBottomAcid();
		String [] firstMismatch = {mismatch1, mismatch2};
		return firstMismatch;
	}
	
	public static String [] getSingleBulgeNeighbors(ArrayList<BasePair> bulge){
		String NNPair1 = bulge.get(0).getTopAcid() + bulge.get(2).getTopAcid();
		String NNPair2 = bulge.get(0).getBottomAcid() + bulge.get(2).getBottomAcid();
		String [] NNPair = {NNPair1, NNPair2};
		return NNPair;
	}
	
	public static String removeDanglingEnds(String sequence){
		StringBuffer newSequence = new StringBuffer();
		newSequence.append(sequence);
		int startIndex = 0;
		
		while (startIndex <= sequence.length() - 1){
			if (sequence.charAt(startIndex) == '-'){
				newSequence.deleteCharAt(0);
				newSequence.deleteCharAt(newSequence.toString().length() - 1);
				startIndex ++;
			}
			else{
				break;
			}
		}
		
		int endIndex = sequence.length() - 1;
		while (endIndex >=0){
			if (sequence.charAt(endIndex) == '-'){
				newSequence.deleteCharAt(newSequence.toString().length() - 1);
				newSequence.deleteCharAt(0);
				endIndex --;
			}
			else{
				break;
			}
		}
		
		if (newSequence.toString().length() == 0){
			throw new SequenceException("No hybridization is possible with this sequence "+ sequence +". Check the sequences");
		}
		return newSequence.toString();
	}
	
	public static boolean isSelfComplementarySequence(String sequence){
		String seq = removeDanglingEnds(sequence);
		BasePair pair = new BasePair();

		for (int i = 0; i < seq.length() - 1; i++){
			pair.setTopAcid(seq.substring(i, i + 1));
			pair.setBottomAcid(seq.substring(seq.length() - i - 1, seq.length() - i));
			
			if (pair.isComplementaryBasePair() == false){
				return false;
			}
		}
		return true;
	}
	
	public boolean isSymetric(int pos1, int pos2){
		for (int i = pos1; i < pos2; i++){
			if (duplex.get(i).getTopAcid() != duplex.get(pos2 - pos1 - i).getBottomAcid()){
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
	
	/*public static String getInversedSequence(String sequence){
		StringBuffer newSequence = new StringBuffer(sequence.length());

		for (int i = 0; i < sequence.length(); i++){

			newSequence.append(sequence.charAt(sequence.length() - i - 1)); 
		}
		return newSequence.toString();
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
		
		seq.append(getSequence(pos1, pos2));
		comp.append(getComplementary(pos1, pos2));
		
		if (seq.toString().contains("_")){
			int index = seq.toString().indexOf("_");
			seq.deleteCharAt(index);
			comp.deleteCharAt(index);
						
		}
		
		else if (comp.toString().contains("_")){
			int index = comp.toString().indexOf("_");
			seq.deleteCharAt(index);
			comp.deleteCharAt(index);
		}

		if (seq.toString().contains("X")){
			int Xpos = seq.toString().indexOf("X");
			seq.deleteCharAt(Xpos);
			seq.insert(Xpos, 'T');
		}
		
		else if (comp.toString().contains("X")){
			int Xpos = comp.toString().indexOf("X");
			comp.deleteCharAt(Xpos);
			comp.insert(Xpos, 'T');
		}
		
		NucleotidSequences noModifiedSequence = new NucleotidSequences(seq.toString(), comp.toString());

		return noModifiedSequence;
	}
	
	public static boolean isCNGSequence(String sequence){
		if (sequence.charAt(0) != 'G' || sequence.charAt(sequence.length() - 1) != 'C'){
			return false;
		}
		if (sequence.length() < 8){
			return false;
		}
		int index = 1;
		String CNG = "C" + sequence.substring(2, 3) + "G";
		while (index <= sequence.length() - 4){
			if (sequence.substring(index, index + 3).equals(CNG) == false){
				return false;
			}
			else{
				index += 3;
			}
		}
		return true;
	}
	
	public boolean isCNGPattern(int pos1, int pos2){
		if (pos2 - pos1 + 1 != getDuplexLength() || pos2 - pos1 + 1 < 8){
			return false;
		}
		
		if (sequence.charAt(0) != 'G' || sequence.charAt(sequence.length() - 1) != 'C'){
			return false;
		}

		int index = 1;
		String CNG = "C" + sequence.substring(2, 3) + "G";
		while (index <= sequence.length() - 4){

			if (sequence.substring(index, index + 3).equals(CNG) == false){
				return false;
			}
			else{
				index += 3;
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

		for (int i = 0; i <= sequenceWithoutDanglingEnd.length() - 1; i++){
			if (Helper.isWatsonCrickBase(sequenceWithDanglingEnd.charAt(i)) == false){
				return false;
			}
			if (sequenceWithoutDanglingEnd.charAt(i) != '-'){
				return false;
			}
		}
		return true;
	}
	
	public boolean isGUSequences(int pos1, int pos2){
		if (pos2 > getDuplexLength() - 1 || pos1 < 0){
			return false;
		}
		
		for (int i = pos1; i <= pos2; i++){
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
		for (int i = pos1; i <= pos2; i++){
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
		if (numbergapSequence == pos2 - pos1 + 1 || numbergapComplementary == pos2 - pos1 + 1){
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
		
		for (int i = 0; i <= pos2 - pos1 ; i++){
			if (sequenceWithGap.charAt(i) != '-'){
				return false;
			}
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
		
		while (index <= sequence.length() - 1){
			if (Helper.isWatsonCrickBase(sequence.charAt(index)) == false){
				modifiedAcid.append(sequence.charAt(index));
				if (modifiedAcid.toString().equals("X_")){
					index ++;
					modifiedAcid.append(sequence.charAt(index));
				}
				index ++;
			}
			else {
				break;
			}
		}
		if (modifiedAcid.toString().equals("_")){
			modifiedAcid.append(sequence.charAt(index));
		}
		if (index - 2 > 0){
			if (modifiedAcid.toString().equals("*") || modifiedAcid.toString().equals("L")){
				index -= 2;
				modifiedAcid.deleteCharAt(0);
				modifiedAcid.append(sequence.substring(index, index + 2));
			}
		}

		if (modifiedAcid.toString().length() > 1 && modifiedAcid.toString().contains("I")){
			int numberI = 0;
			for (int i = 0; i < modifiedAcid.toString().length(); i++){
				if( modifiedAcid.toString().charAt(i) == 'I'){
					numberI ++;
				}
			}
			
			if (numberI > 1 && numberI == modifiedAcid.toString().length()){
				return "I";
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
			if (Helper.isWatsonCrickBase(this.sequence.charAt(i)) == false && this.sequence.charAt(i) != '-' && this.sequence.charAt(i) != ' '){
				modifiedAcid1 = getModifiedAcid(this.sequence, i);

				break;
			}
			if (Helper.isWatsonCrickBase(this.complementary.charAt(i)) == false && this.complementary.charAt(i) != '-' && this.complementary.charAt(i) != ' '){
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
		String modifiedAcid1 = sequence;
		String modifiedAcid2 = complementary;
		ModifiedAcidNucleic name;
		
		if (getModifiedAcid(sequence, 0).equals("I") || getModifiedAcid(complementary, 0).equals("I")){
			modifiedAcid1 = "I";
			modifiedAcid2 = null;
		}

		if (modifiedAcid1 != null && modifiedAcid1.contains(" ") == false){
			name = modifiedAcidNames.get(modifiedAcid1);

			if (name != null){
				return name;
			}
		}
		
		if (modifiedAcid2 != null && modifiedAcid1.contains(" ") == false){
			System.out.println(modifiedAcid2);

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

	public boolean isGapInSequence(int pos1, int pos2){
		if (getSequence(pos1, pos2).contains("-") || getComplementary(pos1, pos2).contains("-")){
			return false;
		}
		return true;
	}*/

}
