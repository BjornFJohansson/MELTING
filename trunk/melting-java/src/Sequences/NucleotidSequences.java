/* This program is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the 
 * License, or (at your option) any later version
                                
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
 * Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, 
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA                                                                  

 *       Marine Dumousseau and Nicolas Lenovere                                                   
 *       EMBL-EBI, neurobiology computational group,                          
*       Cambridge, UK. e-mail: lenov@ebi.ac.uk, marine@ebi.ac.uk        */

package Sequences;

import java.util.ArrayList;
import java.util.HashMap;


import melting.configuration.OptionManagement;
import melting.exceptions.SequenceException;

/** This class represents a pair of nucleic acid sequences. It encodes and decodes them, recognizes patterns 
 * and provides general purpose sequence manipulation methods. The instance variables are :
 * ArrayList duplex : an ArrayList containing the sorted BasePair objects.
 * String sequence : the sequence (5'3') represented by a String.
 * String complementary : the complementary sequence (3'5') represented by a String.
 * NucleotidSequences rnaEquivalent : the NucleotidSequences object which represents this NucleotidSequences object with RNA sequences
 * NucleotidSequences dnaEquivalent : the NucleotidSequences object which represents this NucleotidSequences object with DNA sequences
 * HashMap modifiedAcidNames : a Map which makes a link between the encoded specific nucleic acid in the String and the matching SpecificAcidNames enum value.
 */
public class NucleotidSequences {

	/**
	 * ArrayList duplex : an ArrayList containing the sorted BasePair objects.
	 */
	private ArrayList<BasePair> duplex;
	
	/**
	 * String sequence : the sequence (5'3') represented by a String.
	 */
	private String sequence;
	
	/**
	 * String complementary : the complementary sequence (3'5') represented by a String.
	 */
	private String complementary;
	
	/**
	 * NucleotidSequences rnaEquivalent : the NucleotidSequences object which represents this NucleotidSequences object with RNA sequences
	 * This object is initialized when a model for RNA sequences is selected by the user and the hybridisation type is dnadna
	 */
	private NucleotidSequences rnaEquivalent;
	
	/**
	 * NucleotidSequences dnaEquivalent : the NucleotidSequences object which represents this NucleotidSequences object with DNA sequences
     * This object is initialized when a model for DNA sequences is selected by the user and the hybridisation type is rnarna
	 */
	private NucleotidSequences dnaEquivalent;
	
	/**
	 * HashMap modifiedAcidNames : a Map which makes a link between the encoded specific nucleic acid in the String and the matching SpecificAcidNames enum value.
	 */
	private static HashMap<String, SpecificAcidNames> modifiedAcidNames = new HashMap<String, SpecificAcidNames>();
	
	/**
	 * Creates a NucleotidSequences object from two String.
	 * @param String sequence : the sequence (5'3')
	 * @param String complementary : the complementary sequence (3'5')
	 */
	public NucleotidSequences(String sequence, String complementary){
		this(sequence, complementary, true);
	}
	
	/**
	 * Creates a NucleotidSequence object from two String. The sequences will be encoded if the boolean
	 * encode is true.
	 * @param String sequence : the sequence (5'3')
	 * @param String complementary : the complementary sequence (3'5')
	 * @param boolean encode : If the sequences are already encoded, it is false otherwise
	 * the sequences must be encoded and the boolean encode is true;
	 */
	private NucleotidSequences(String sequence, String complementary, boolean encode) {
		String [] sequences;
		if (encode) {
			sequences = encodeSequences(sequence, complementary);
			sequences = correctSequences(sequences[0], sequences[1]);
		}
		else {
			sequences = new String[]{sequence, complementary};
		}
		
		if (sequences[0].length() != sequences[1].length()){
			throw new SequenceException("The sequences have two different length. Replace the gaps by the character '-'.");
		}
		this.sequence = sequences[0];
		this.complementary = sequences[1];
		this.duplex = getDuplexFrom(sequences[0], sequences[1]);
	}
	
	/**
	 * Creates a new NucleotidSequences object from a reference nucleotiDSequences object. The sequences
	 * are converted in the hybridization type "hybridization type". the rnaEquivalent or dnaEquivalent object
	 * of the reference NucleotidSequences object are initialized.
	 * @param String hybridizationType : hybridization type of the duplex.
	 * @param NucleotidSequences sequences : the reference nucleotidSequences object. 
	 */
	protected NucleotidSequences(String hybridizationType, NucleotidSequences sequences){
		this.sequence = convertSequence(sequences.sequence, hybridizationType);
		this.complementary = convertSequence(sequences.complementary, hybridizationType);
		this.duplex = sequences.duplex;
		
		if (hybridizationType.equals("dna")) {
			this.dnaEquivalent = this;
		}
		else if (hybridizationType.equals("rna")) {
			this.rnaEquivalent = this;
		}
	}
	
	/**
	 * This method is called when the same NucleotidSequences is required but with the sequences converted in the new
	 * hybridization type "hybridizationType"
	 * @param String hybridizationType : the new hybridization type of the sequences
	 * @return the initialized object rnaEquivalent if the hybridization type is "rna" or the initialized object dnaEquivalent if the hybridization type is "dna"
	 */
	public NucleotidSequences getEquivalentSequences(String hybridizationType) {
		if (hybridizationType.equals("dna")) {
			if (dnaEquivalent == null){
				dnaEquivalent = new NucleotidSequences(hybridizationType, this);
			}
			return dnaEquivalent;
		}
		else if (hybridizationType.equals("rna")) {
			if (rnaEquivalent == null){
				rnaEquivalent = new NucleotidSequences(hybridizationType, this);
			}
			return rnaEquivalent;
		}
		throw new SequenceException("It is impossible to convert this sequences in a sequence of type " + hybridizationType + ".");
	}
	
	/**
	 * This method is called to get the ArrayList duplex of the NucleotidSequences object.
	 * @param String sequence : sequence (5'3')
	 * @param String complementary : complementary sequence (3'5')
	 * @return the ArrayList duplex of the NucleotiSequences object.
	 */
	public static ArrayList<BasePair> getDuplexFrom(String sequence, String complementary){
		ArrayList<BasePair> duplex = new ArrayList<BasePair>();
		int pos = 0;
		while (pos < sequence.length()){
			BasePair pair = getBasePair(sequence, complementary, pos);
			duplex.add(pair);
			pos += pair.getLengthAcid();
		}
		return duplex;
	}
	
	/**
	 * This method is called to get the nucleic acid at the position "pos" in the sequence "sequence"
	 * @param String sequence : represents one of the sequence or a substring of the sequence
	 * @param int pos : The nucleic acid position in the sequence
	 * @return String nucleic acid at the position "pos" in the String sequence.
	 */
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
		possibleNucleicAcids.clear();
		return acid;
	}
	
	/**
	 * encodes the complementary String of the object NucleotidSequences
	 * @param String sequence : the sequence (5'3')
	 * @param StringBuffer comp : the complementary sequence (3'5') to encode
	 * @return the encoded complementary String
	 */
	private String encodeComplementary(String sequence, StringBuffer comp){
		int pos = 0;
		while (pos < sequence.length()){
			String acid = getNucleicAcid(sequence, pos);
			if (acid != null){
				if (acid.equals("X_T") || acid.equals("X_C")){
					comp.insert(pos," ");
					comp.insert(pos + 1, " ");
					comp.insert(pos + 2, " ");
				}
				else if (acid.length() > 1){
					if (acid.charAt(1) == 'L'){
						comp.insert(pos + 1," ");
					}
					else if(acid.equals("A*")){
						comp.insert(pos + 1," ");
					}
				}
				pos += acid.length();
			}
			else {
				pos ++;
			}
		}	
		
		return comp.toString();
	}
	
	/**
	 * encodes the sequence String of the object NucleotidSequences
	 * @param String complementary : the complementary sequence (3'5')
	 * @param StringBuffer seq : the sequence (5'3') to encode
	 * @return the encoded sequence String
	 */
	private String encodeSequence(String complementary, StringBuffer seq){
		int pos = 0;
		
		while (pos < complementary.length()){
			String acid = getNucleicAcid(complementary, pos);
			if (acid != null){
				if (acid.equals("X_T") || acid.equals("X_C")){
					seq.insert(pos," ");
					seq.insert(pos + 1, " ");
					seq.insert(pos + 2, " ");
				}
				else if (acid.length() > 1){
					if (acid.charAt(1) == 'L'){
						seq.insert(pos + 1," ");
					}
					else if (acid.equals("A*")){
						seq.insert(pos + 1," ");
					}
				}
				pos += acid.length();
			}
			else{
				pos ++;
			}
		}	
		
		return seq.toString();
	}
	
	/**
	 * encodes the String sequence and complementary of the NucleotidSequences object.
	 * @param String sequence : the sequence (5'3')
	 * @param String complementary : the complementary sequence (3'5')
	 * @return a String [] object which contains the encoded String sequence and complementary of the NucleotidSequences object
	 */
	public String [] encodeSequences(String sequence, String complementary){
		StringBuffer seq = new StringBuffer();
		StringBuffer comp = new StringBuffer();

		seq.append(sequence);
		comp.append(complementary);

		String [] sequences = {encodeSequence(complementary, seq), encodeComplementary(sequence, comp)};
		return sequences;
	}
	
	/**
	 * removes the unnecessary "-" in the String sequence and complementary of the NucleotidSequences object
	 * @param String sequence : the sequence (5'3')
	 * @param String complementary : the complementary sequence (3'5')
	 * @return a String [] object which contains the String sequence and complementary without unnecessary gaps ("-")
	 */
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
	
	/**
	 * decodes the String "sequence".
	 * @param String sequence
	 * @return the decoded String "sequence".
	 */
	private static String decodeSequence(String sequence){
		
		String newSequence = sequence.replaceAll("X_[TC]", "X").replaceAll(" ", "");

		return newSequence;
	}
	
	/**
	 * decodes the String sequence and complementary of the NucleotidSequences object.
	 * @param String sequence : sequence (5'3')
	 * @param String complementary : complementary sequence (3'5')
	 * @return a String [] object containing the decoded String sequence and complementary of the NucleotidSequences object.
	 */
	public static String [] decodeSequences(String sequence, String complementary){
		StringBuffer seq = new StringBuffer();
		StringBuffer comp = new StringBuffer();

		seq.append(sequence);
		comp.append(complementary);

		String [] sequences = {decodeSequence(sequence), decodeSequence(complementary)};
		return sequences;
	}
	
	/**
	 * This method is called to get the BasePair object from the String sequence and complementary at the position pos in the duplex 
	 * @param String sequence : sequence (5'3')
	 * @param String complementary : complementary sequence (3'5')
	 * @param int pos : position in the sequence where is the base pair.
	 * @return BasePair object which represents the nucleic acid base pair at the position pos in the duplex
	 */
	private static BasePair getBasePair(String sequence, String complementary, int pos){
		BasePair acid = new BasePair(pos);
		
		String acid1 = getNucleicAcid(sequence, pos);
		String acid2 = getNucleicAcid(complementary, pos);
		if (acid1 == null && acid2 == null){
			throw new SequenceException("Some nucleic acids are unknown in the sequences. Check the options.");
		}
		else if (acid1 == null){
			acid1 = sequence.substring(pos, pos + acid2.length());
		}
		else if (acid2 == null){
			acid2 = complementary.substring(pos, pos + acid1.length());
		}
		acid.setTopAcid(acid1);
		acid.setBottomAcid(acid2);
		
		return acid;
	}
	
	/**
	 * initializes the HasMap modifiedAcidNames of the NucleotiSequences class.
	 */
	public static void initializeModifiedAcidHashmap(){
		modifiedAcidNames.put("I", SpecificAcidNames.inosine);
		modifiedAcidNames.put("AL", SpecificAcidNames.lockedNucleicAcid);
		modifiedAcidNames.put("TL", SpecificAcidNames.lockedNucleicAcid);
		modifiedAcidNames.put("CL", SpecificAcidNames.lockedNucleicAcid);
		modifiedAcidNames.put("GL", SpecificAcidNames.lockedNucleicAcid);
		modifiedAcidNames.put("A*", SpecificAcidNames.hydroxyadenine);
		modifiedAcidNames.put("X_C", SpecificAcidNames.azobenzene);
		modifiedAcidNames.put("X_T", SpecificAcidNames.azobenzene);
	}
	
	/**
	 * This method is called when we want to get the complementary String of the NucleotidSequences object with the
	 * type of hybridization "hybridization".
	 * @param String sequence
	 * @param String hybridization : type of hybridization
	 * @return String which represents the complementary sequence (3'5') with the specified nature.(rna, dna)
	 */
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
			}
		}
		return complementary.toString();
	}
	
	/**
	 * Check if the String sequence is composed of nucleic acids known by MELTING.
	 * @param String sequence
	 * @return false if one of the nucleic acids is not known by MELTING. (not registered in the ArrayList existingNucleicAcids of the
	 * BasePair class).
	 */
	public static boolean checkSequence(String sequence){
		
		int position = 0;
		
		if (sequence.length() == 0){
			throw new SequenceException("The sequence must be entered with the option " + OptionManagement.sequence);
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
	
	/**
	 * This method is called to get the length of the ArrayList duplex of the NucleotidSequences object.
	 * @return int length which represents the length of the nucleic acid duplex.
	 */
	public int getDuplexLength(){
		return duplex.size();
	}
	
	/**
	 * This method is called
	 * @return
	 */
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
		
		pos1 = this.duplex.get(pos1).getPosition();
		pos2 = this.duplex.get(pos2).getPosition() + this.duplex.get(pos2).getLengthAcid();
		return sequence.substring(pos1, pos2);
	}
	
	public String getComplementary(int pos1, int pos2){
		if (pos1 < 0 || pos2 > getDuplexLength() - 1){
			throw new SequenceException("The length of the duplex is inferior to " + pos2 + 1 + "and superior to 0.");
		}
		pos1 = this.duplex.get(pos1).getPosition();
		pos2 = this.duplex.get(pos2).getPosition() + this.duplex.get(pos2).getLengthAcid();

		return complementary.substring(pos1, pos2);
	}
	
	public String getSequenceNNPairUnlocked(int pos){
		return getSequence(pos, pos+1).replace("L", "").replace(" ", "");
	}
	
	public String getComplementaryNNPairUnlocked(int pos){
		return getComplementary(pos, pos + 1).replace("L", "").replace(" ", "");
	}
	
	public String getSequenceNNPair(int pos){
		return getSequence(pos, pos+1);
	}
	
	public String getComplementaryNNPair(int pos){
		return getComplementary(pos, pos+1);
	}
	
	public String[] getNNPairWithoutHydroxyA(int pos) {
		String[] pair1 = removeHydroxyA(getDuplex().get(pos).getTopAcid(), getDuplex().get(pos).getBottomAcid());
		String[] pair2 = removeHydroxyA(getDuplex().get(pos+1).getTopAcid(), getDuplex().get(pos+1).getBottomAcid());
		return new String[] { pair1[0]+pair2[0], pair1[1]+pair2[1] };
	}
	
	private static String[] removeHydroxyA(String acid1, String acid2) {
		if (acid1.equals("A*")) {
			return new String[]{"A", "T"};
		}
		if (acid2.equals("A*")) {
			return new String[]{"T", "A"};
		}
		return new String[]{acid1, acid2};
	}
	
	private static double calculateNumberOfTerminal(String base1, String base2, ArrayList<BasePair> duplex, int pos1, int pos2){
		double numberOfTerminal = 0.0;
		BasePair firstTerminalBasePair = duplex.get(pos1);
		BasePair lastTerminalBasePair = duplex.get(pos2);

		if (firstTerminalBasePair.isBasePairEqualTo(base1, base2)){
			numberOfTerminal++;
		}
		
		if (lastTerminalBasePair.isBasePairEqualTo(base1, base2)){
			numberOfTerminal++;
		}
		return numberOfTerminal;	
	}
	
	public double calculateNumberOfTerminal(String base1, String base2, int pos1, int pos2){
		return calculateNumberOfTerminal(base1, base2, this.duplex, pos1, pos2);
	}
	
	public double getNumberTerminal5TA(int pos1, int pos2){
		double number5TA = 0;
		BasePair firstTerminalBasePair = duplex.get(pos1);
		BasePair lastTerminalBasePair = duplex.get(pos2);
		
		if (firstTerminalBasePair.isBasePairStrictlyEqualTo("T", "A")){
			number5TA ++;
		}
		if (lastTerminalBasePair.isBasePairStrictlyEqualTo("A", "T")){
			number5TA ++;
		}
		return number5TA;
	}
	
	public int [] removeTerminalUnpairedNucleotides(){
		int indexStart = 0;
		while (indexStart <= getDuplexLength() - 1){
			BasePair pair = duplex.get(indexStart);
			if (pair.isUnpaired()){
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
			BasePair pair = duplex.get(getDuplexLength() - indexEnd - 1);
			if (pair.isUnpaired()){
				indexEnd --;
			}
			else {
				break;
			}
		}
		if (indexEnd == 0){
			throw new SequenceException("The sequences can be hybridized. Check the sequences.");
		}
		int [] positions = {indexStart, indexEnd};
		return positions;
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

	public ArrayList<BasePair> getDuplex() {
		return duplex;
	}

	public static HashMap<String, SpecificAcidNames> getModifiedAcidNames() {
		return modifiedAcidNames;
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

	public String [] getLoopFistMismatch(int pos1){
		String mismatch1 = convertToPyr_Pur(duplex.get(pos1).getTopAcid()) + duplex.get(pos1 + 1).getTopAcid();
		String mismatch2 = convertToPyr_Pur(duplex.get(pos1).getBottomAcid()) + duplex.get(pos1 + 1).getBottomAcid();
		String [] firstMismatch = {mismatch1, mismatch2};
		return firstMismatch;
	}
	
	public String [] getSingleBulgeNeighbors(int pos1){
		String NNPair1 = duplex.get(pos1).getTopAcid() + duplex.get(pos1 + 2).getTopAcid();
		String NNPair2 = duplex.get(pos1).getBottomAcid() + duplex.get(pos1 + 2).getBottomAcid();
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
		BasePair pair = new BasePair(0);

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
	
	public static NucleotidSequences buildSymetricSequences(String seq1, String seq2){
		StringBuffer symetricSequence = new StringBuffer(seq1.length());
		
		symetricSequence.append(seq1.substring(0, 2));
		symetricSequence.append(seq2.substring(1, 2));
		symetricSequence.append(seq2.substring(0,1));
	
		StringBuffer symetricComplementary = new StringBuffer(seq1.length());
		
		symetricComplementary.append(seq2.substring(0, 2));
		symetricComplementary.append(seq1.substring(1, 2));
		symetricComplementary.append(seq1.substring(0, 1));
		
		return new NucleotidSequences(symetricSequence.toString(), symetricComplementary.toString());
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
	
	public ArrayList<BasePair> removeHydroxyadenine(int pos1, int pos2){
		ArrayList<BasePair> newDuplex = new ArrayList<BasePair>();
		
		for (int i = pos1; i <= pos2; i++){
			newDuplex.add(duplex.get(i));
			if (newDuplex.get(i).getTopAcid().equals("A*")){
				newDuplex.get(i).setTopAcid("A");
				newDuplex.get(i).setBottomAcid(duplex.get(i).getBottomAcid().replace(" ", ""));
			}
			else if (newDuplex.get(i).getBottomAcid().equals("A*")){
				newDuplex.get(i).setBottomAcid("A");
				newDuplex.get(i).setTopAcid(duplex.get(i).getTopAcid().replace(" ", ""));
			}
		}
		return newDuplex;
	}
		
		public ArrayList<BasePair> removeLockedAcid(int pos1, int pos2){
			ArrayList<BasePair> newDuplex = new ArrayList<BasePair>();
			
			for (int i = pos1; i <= pos2; i++){
				newDuplex.add(duplex.get(i));
				if (newDuplex.get(i).getTopAcid().contains("L")){
					newDuplex.get(i).setTopAcid(newDuplex.get(i).getTopAcid().substring(0, 1));
					newDuplex.get(i).setBottomAcid(duplex.get(i).getBottomAcid().replace(" ", ""));
				}
				else if (newDuplex.get(i).getBottomAcid().contains("L")){
					newDuplex.get(i).setBottomAcid(newDuplex.get(i).getBottomAcid().substring(0, 1));
					newDuplex.get(i).setTopAcid(duplex.get(i).getTopAcid().replace(" ", ""));
				}
			}

		return newDuplex;
	}
	
	public boolean isCNGPattern(int pos1, int pos2){
		if (pos2 - pos1 + 1 != getDuplexLength() || pos2 - pos1 + 1 < 8){
			return false;
		}
		
		if (duplex.get(0).getTopAcid().equals("G") == false || duplex.get(duplex.size() - 1).equals("C") == false){
			return false;
		}

		int index = 1;
		String CNG = "C" + duplex.get(2).getTopAcid() + "G";
		while (index <= getDuplexLength() - 4){

			if (getSequence(index, index + 2).equals(CNG) == false){
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
			if (duplex.get(i).isComplementaryBasePair() == false){
				return false;
			}
		}
		return true;
	}
	
	public boolean isDanglingEnd(int pos1, int pos2){
		
		if ((getSequence(pos1, pos2).contains("-") && getComplementary(pos1, pos2).contains("-")) || (getSequence(pos1, pos2).contains("-") == false && getComplementary(pos1, pos2).contains("-") == false)){
			return false;
		}
		
		if (pos1 != 0 && pos2 != getDuplexLength() - 1){
			return false;
		}

		int numberGapSequence = 0;
		int numberGapComplementary = 0;
		for (int i = pos1; i <= pos2; i++){
			BasePair pair = duplex.get(i);
			if (pair.getTopAcid().equals("-")){
				numberGapSequence ++;
				if (pair.isWatsonCrickBottomBase() == false){
					return false;
				}
			}
			else if (pair.getBottomAcid().equals("-")){
				numberGapComplementary ++;
				if (pair.isFrequentNucleicAcidTopBase() == false){
					return false;
				}
			}
		}
		
		if (numberGapSequence < pos2 - pos1 + 1 && numberGapComplementary < pos2 - pos1 + 1){
			return false;
		}
		return true;
	}
	
	public boolean isGUSequences(int pos1, int pos2){
		if (pos2 > getDuplexLength() - 1 || pos1 < 0){
			return false;
		}
		
		for (int i = pos1; i <= pos2; i++){
			BasePair pair = duplex.get(i);
			if (pair.isBasePairEqualTo("G", "U") == false){
				return false;
			}
		}
		return true;
	}
	
	public boolean isMismatchPair(int pos){
		BasePair pair = duplex.get(pos);
		if (pair.isComplementaryBasePair() == false && pair.isWatsonCrickBottomBase() && pair.isFrequentNucleicAcidTopBase()){
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
			BasePair pair = duplex.get(i);
			if (isMismatchPair(i) == false){
				if (pair.isUnpaired() == false || (pair.isUnpaired() && (pair.isWatsonCrickBottomBase() == false && pair.isFrequentNucleicAcidTopBase() == false))){
					return false;
				}
				else if (pair.isUnpaired() && pair.getTopAcid().equals("-")){
					numbergapSequence++;
				}
				else if (pair.isUnpaired() && pair.getBottomAcid().equals("-")){
					numbergapComplementary++;
				}
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
		
		int numberGapSequence = 0;
		int numberGapComplementary = 0;
		for (int i = pos1; i <= pos2; i++){
			BasePair pair = duplex.get(i);
			if (pair.getTopAcid().equals("-")){
				numberGapSequence ++;
				if (pair.isWatsonCrickBottomBase() == false){
					return false;
				}
			}
			else if (pair.getBottomAcid().equals("-")){
				numberGapComplementary ++;
				if (pair.isFrequentNucleicAcidTopBase() == false){
					return false;
				}
			}
		}
		
		if (numberGapSequence < pos2 - pos1 + 1 && numberGapComplementary < pos2 - pos1 + 1){
			return false;
		}
		
		return true;
	}
	
	public boolean isListedModifiedAcid(int pos){
		if (pos > getDuplexLength() - 1 || pos < 0){
			return false;
		}
		BasePair pair = duplex.get(pos);
		if (BasePair.getExistingNucleicAcids().contains(pair.getTopAcid()) == false && BasePair.getExistingNucleicAcids().contains(pair.getBottomAcid()) == false){
			return false;
		}
		return true;
	}
	
	public SpecificAcidNames getModifiedAcidName(BasePair pair){
		SpecificAcidNames name;	

		if (pair != null && modifiedAcidNames.containsKey(pair.getTopAcid())){
			name = modifiedAcidNames.get(pair.getTopAcid());
			return name;
		}
		
		if (pair != null && modifiedAcidNames.containsKey(pair.getBottomAcid())){
			name = modifiedAcidNames.get(pair.getBottomAcid());
			return name;
		}
		return null;
	}
	
	public boolean isAPyrimidineInThePosition(int pos){
		BasePair pair = duplex.get(pos);
		if (pair.isTopBasePyrimidine() || pair.isBottomBasePyrimidine()){
			return true;
		}
		return false;
	}
	
	public boolean isTandemMismatchGGPenaltyNecessary(int pos){
		BasePair pair = duplex.get(pos);
		BasePair secondpair = null;
		if (pos + 1 > getDuplexLength() - 1){
			return false;
		}
		else{
			secondpair = duplex.get(pos + 1);
		}
		if ((pair.isBasePairStrictlyEqualTo("G", "G") && secondpair.isBasePairStrictlyEqualTo("A", "A")) || (secondpair.isBasePairStrictlyEqualTo("G", "G") && pair.isBasePairStrictlyEqualTo("A", "A"))){
			return true;	
		}
		else if ((pair.isBasePairStrictlyEqualTo("G", "G") && isAPyrimidineInThePosition(pos + 1)) || (secondpair.isBasePairStrictlyEqualTo("G", "G") && isAPyrimidineInThePosition(pos))){
			return true;
		}
		return false;
	}
	
	public boolean isTandemMismatchDeltaPPenaltyNecessary(int pos){
		BasePair pair = duplex.get(pos);
		BasePair secondpair = null;
		if (pos + 1 > getDuplexLength() - 1){
			return false;
		}
		else{
			secondpair = duplex.get(pos + 1);
		}

		if (pair.isBasePairEqualTo("A", "G") || secondpair.isBasePairEqualTo("A", "G")){
			if (pair.isBasePairEqualTo("C", "U") || secondpair.isBasePairEqualTo("C", "U")){
				return true;
			}
			else if (pair.isBasePairEqualTo("C", "C") || secondpair.isBasePairEqualTo("C", "C")){
				return true;
			}
		}
		else if ((pair.isBasePairEqualTo("U", "U") && secondpair.isBasePairEqualTo("A", "A")) || (secondpair.isBasePairEqualTo("U", "U") && pair.isBasePairEqualTo("A", "A"))){
			return true;
		}
		return false;
	}

	public boolean isGapInSequence(int pos1, int pos2){
		if (getSequence(pos1, pos2).contains("-") || getComplementary(pos1, pos2).contains("-")){
			return false;
		}
		return true;
	}
}
