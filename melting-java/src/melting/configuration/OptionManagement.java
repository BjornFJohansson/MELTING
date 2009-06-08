package melting.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import melting.Environment;
import melting.NucleotidSequences;
import melting.exceptions.NoExistingOutputFileException;
import melting.exceptions.OptionSyntaxError;

public class OptionManagement {
	
	public static final String sequence = "-S";
	public static final String complementarySequence = "-C";
	public static final String solutioncomposition = "-E";
	public static final String nucleotides = "-P";
	public static final String completMethod = "-mode";
	public static final String hybridization = "-H";
	public static final String approximativeMode = "-AMode";
	public static final String ionCorrection = "-ionCorr";
	public static final String DMSOCorrection = "-DMSOCorr";
	public static final String formamideCorrection = "-formCorr";
	public static final String NNMethod = "-NN";
	public static final String singleMismatchMethod = "-sinMM";
	public static final String wobbleBaseMethod = "-woddle";
	public static final String tandemMismatchMethod = "-tanMM";
	public static final String internalLoopMethod = "-intLoop";
	public static final String singleDanglingEndMethod = "-sinDE";
	public static final String doubleDanglingEndMethod = "-douDE";
	public static final String longDanglingEndMethod = "-lonDE";
	public static final String hairpinLoopMethod = "-hairpin";
	public static final String singleBulgeLoopMethod = "-sinBulge";
	public static final String longBulgeLoopMethod = "-lonBulge";
	public static final String CNGMethod = "-CNG";
	public static final String inosineMethod = "-ino";
	public static final String hydroxyadenineMethod = "-HydroxyA";
	public static final String azobenzeneMethod = "-azoB";
	public static final String lockedAcidMethod = "-locked";
	public static final String deoxyadenineMethod = "-deoxyA";
	public static final String NaEquivalentMethod = "-Naeq";
	public static final String meltingHelp = "-h";
	public static final String legalInformation = "-L";
	public static final String dataPathway = "-data";
	public static final String verboseMode = "-v";
	public static final String threshold = "-T";
	public static final String NN_Path = "-NNPath";
	public static final String outPutFile = "-out";
	public static final String versionNumber = "-V";
	public static final String selfComplementarity = "-self";
	public static final String factor = "-F";
	public static final Logger meltingLogger = Logger.getLogger("melting");
	
	private static final String version = "5";
	public static String dataPathwayValue = "src/melting/Data";
	private static int thresholdValue = 60;
	private static int factorValue = 4;
	
	
	private HashMap<String, String> DNADefaultOptions = new HashMap<String, String>();
	private HashMap<String, String> RNADefaultOptions = new HashMap<String, String>();
	private HashMap<String, String> hybridDefaultOptions = new HashMap<String, String>();
	private HashMap<String, String> mRNADefaultOptions = new HashMap<String, String>();

	public OptionManagement(){
		
		setDNADefaultOptions();
		setRNADefaultOptions();
		setHybridDefaultOptions();
		setMRNADefaultOptions();
	}
	
	public HashMap<String, String> getDNADefaultOptions() {
		return DNADefaultOptions;
	}
	
	private void setDNADefaultOptions() {
		this.DNADefaultOptions.put(NNMethod, "Santalucia_2004");
		this.DNADefaultOptions.put(singleMismatchMethod, "Allawi_Santalucia_Peyret_1997_1998_1999");
		this.DNADefaultOptions.put(tandemMismatchMethod, "Allawi_Santalucia_Peyret_1997_1998_1999");
		this.DNADefaultOptions.put(internalLoopMethod, "Santalucia_2004");
		this.DNADefaultOptions.put(singleBulgeLoopMethod, "Tanaka_2004");
		this.DNADefaultOptions.put(longBulgeLoopMethod, "Santalucia_2004");
		this.DNADefaultOptions.put(hairpinLoopMethod, "Santalucia_2004");
		this.DNADefaultOptions.put(approximativeMode, "Wetmurdna_1991");
		this.DNADefaultOptions.put(DMSOCorrection, "Ahsen_2001");
		this.DNADefaultOptions.put(inosineMethod, "Santalucia_2005");
		this.DNADefaultOptions.put(hydroxyadenineMethod, "Sugimoto_2001");
		this.DNADefaultOptions.put(azobenzeneMethod, "Asanuma_2005");
		this.DNADefaultOptions.put(lockedAcidMethod, "McTigue_2004");
		this.DNADefaultOptions.put(deoxyadenineMethod, "Sugimoto_2005");
		this.DNADefaultOptions.put(NaEquivalentMethod, "Ahsen_2007");
	}
	
	public HashMap<String, String> getRNADefaultOptions() {
		return RNADefaultOptions;
	}
	
	private void setRNADefaultOptions() {
		this.RNADefaultOptions.put(NNMethod, "Xia_1998");
		this.RNADefaultOptions.put(singleMismatchMethod, "Znosko_2008");
		this.RNADefaultOptions.put(wobbleBaseMethod, "Turner_1999");
		this.RNADefaultOptions.put(tandemMismatchMethod, "Turner_1999_2006");
		this.RNADefaultOptions.put(internalLoopMethod, "Turner_1999_2006");
		this.RNADefaultOptions.put(singleBulgeLoopMethod, "Serra_2007");
		this.RNADefaultOptions.put(longBulgeLoopMethod, "Turner_1999_2006");
		this.RNADefaultOptions.put(hairpinLoopMethod, "Serra_1997_1998_2000_2006");
		this.RNADefaultOptions.put(CNGMethod, "Broda_2005");
		this.RNADefaultOptions.put(approximativeMode, "Wetmurrna_1991");
		this.RNADefaultOptions.put(inosineMethod, "Znosko_2007");
	}
	
	public HashMap<String, String> getHybridDefaultOptions() {
		return hybridDefaultOptions;
	}
	
	private void setHybridDefaultOptions() {
		this.hybridDefaultOptions.put(NNMethod, "Sugimoto_1995");
		this.hybridDefaultOptions.put(approximativeMode, "Wetmurdnarna_1991");
	}
	
	public HashMap<String, String> getMRNADefaultOptions() {
		return mRNADefaultOptions;
	}
	
	private void setMRNADefaultOptions() {
		this.mRNADefaultOptions.put(NNMethod, "Turner_2006");
	}
	
	private boolean isAValue(String optionValue){
		if (optionValue.charAt(0) != '-'){
			return true;
		}
		return false;
	}
	
	private void setOptionValues(String [] args){

		boolean isNecessaryToSetOptionsValues = true;
		
		if (args.length == 1){
			isNecessaryToSetOptionsValues = false;
		}
		if (isNecessaryToSetOptionsValues){

		for (int i = 0;i < args.length ; i++){
			String option = args[i];
			
			if (i == args.length - 1){
				isNecessaryToSetOptionsValues = false;
				break;
			}
			
				String value = args[i+1];

				if (isAValue(option) == false){
					if (option.equals(NN_Path)){
						if (isAValue(value)){
							dataPathwayValue = value;
						}
						else {
							throw new OptionSyntaxError("I don't understand the option " + option + value + ".");
						}
					}	
				}
				else if (option.equals(threshold)){
					if (isAValue(value)){
						if (Integer.getInteger(value) != null && Integer.getInteger(value) >= 0) {
							thresholdValue = Integer.getInteger(value);
						}
						else {
							throw new OptionSyntaxError("The threshold must be a strictly positive numeric value.");
						}
					}
					else {
						throw new OptionSyntaxError("I don't understand the option " + option + value + ".");
					}
				}
				else if (option.equals(factor)){
					if (isAValue(value)){
						if (Integer.getInteger(value) != null && (Integer.getInteger(value) == 1 || Integer.getInteger(value) == 4)) {
							factorValue = Integer.getInteger(value);
						}
						else {
							throw new OptionSyntaxError("The correction factor must be 1 or 4.");
						}
					}
					else {
						throw new OptionSyntaxError("I don't understand the option " + option + value + ".");				}
				}
			}
		}
	}
	
	public boolean isMeltingInformationOption(String [] args){
		
		for (int i = 0;i < args.length; i++){
			String option = args[i];
			if (isAValue(option) == false){
				if (option.equals(meltingHelp)){
					return true;
				}
				else if (option.equals(legalInformation)){
					return true;
				}
				else if (option.equals(dataPathway)){
					return true;
				}
				else if (option.equals(versionNumber)){
					return true;
				}
			}
		}
		return false;
	}
	
	private void readMeltingHelp(){
		System.out.println("help");
	}
	
	private void readLegalInformation(){
		StringBuffer legalInformation = new StringBuffer();
		legalInformation.append("   Melting 5 is copyright (C) 2009 by Nicolas Le Novère and Marine Dumousseau\n\n");
		legalInformation.append("   This  program  is  free  software; you can redistribute it\n");
		legalInformation.append("   and/or modify it under the terms of the GNU General Public\n");
		legalInformation.append("   License  as  published  by  the  Free Software Foundation;\n");
		legalInformation.append("   either version 2 of the License, or (at your  option)  any\n");
		legalInformation.append("   later version.\n");
		legalInformation.append("   This  program  is  distributed in the hope that it will be\n");
		legalInformation.append("   useful, but WITHOUT ANY WARRANTY; without even the implied\n");
		legalInformation.append("   warranty  of  MERCHANTABILITY  or FITNESS FOR A PARTICULAR\n");
		legalInformation.append("  PURPOSE.  See the GNU  General  Public  License  for  more\n");
		legalInformation.append("  details.\n\n");
		legalInformation.append("  You  should have received a copy of the GNU General Public\n");
		legalInformation.append("  License along with this program; if not, write to the Free\n");
		legalInformation.append("  Software  Foundation,  Inc.,  59  Temple Place, Suite 330,\n");
		legalInformation.append("  Boston, MA  02111-1307 USA\n\n");
		legalInformation.append("  Nicolas Le Novère and Marine Dumousseau, Computational Biology, EMBL-EBI\n");
		legalInformation.append("  Hinxton CB10 1SD United-Kingdom\n");
		legalInformation.append("  lenov@ebi.ac.uk\n");
		
		meltingLogger.log(Level.INFO, legalInformation.toString());
	}
	
	public void readOptions(String [] args){

		setOptionValues(args);
		
		meltingLogger.setLevel(Level.INFO);
		
		for (int i = 0;i < args.length; i++){
			String option = args[i];
				
			if (isAValue(option) == false){
				if (option.equals(meltingHelp)){
					readMeltingHelp();
				}
				else if (option.equals(legalInformation)){
					readLegalInformation();
				}
				else if (option.equals(dataPathway)){
					meltingLogger.log(Level.INFO, "The current data files are in "+ dataPathwayValue + ".");
				}
				else if (option.equals(versionNumber)){
					meltingLogger.log(Level.INFO, "This MELTING program is the java version "+ version + ".");
				}
			}
		}
	}
	
	private boolean hasRequiredOptions(HashMap<String, String> optionSet){
		boolean needComplementaryInput = false;

		if (optionSet.containsKey(hybridization) == false || optionSet.containsKey(nucleotides) == false || optionSet.containsKey(sequence) == false){
			return false;
		}

		try {
			double val = Double.parseDouble(optionSet.get(nucleotides));

			if (val <= 0){
				throw new OptionSyntaxError("The nucleotide concentration must be strictly positive.");
			}
			
		} catch (NumberFormatException e) {
			throw new OptionSyntaxError("The nucleotide concentration must be a numeric value.");
		}
				
		String value = optionSet.get(sequence).toUpperCase();
		if (NucleotidSequences.checkSequence(value)){

			if (value.contains("I") || value.contains("A*") || (value.contains("X") && value.contains("_X") == false)){
				needComplementaryInput = true;
			}
		}
		else {
			throw new OptionSyntaxError("The sequence contains some characters we can't understand.");
		}

		if (checkConcentrations(optionSet.get(solutioncomposition)) == false) {

			throw new OptionSyntaxError("There is one syntax mistake in the concentrations. Check the option" + solutioncomposition + ".");
		}
		
		if (needComplementaryInput && optionSet.containsKey(complementarySequence) == false){
			return false;
		}
		
		if (optionSet.containsKey(complementarySequence) == false && needComplementaryInput == false){
			String seq2 = NucleotidSequences.getComplementarySequence(optionSet.get(sequence), optionSet.get(hybridization));
			optionSet.put(complementarySequence, seq2);
		}
		
		return true;
	}
	
	private boolean checkConcentrations(String solutionComposition){
		String [] solution = solutionComposition.split(":"); 
				
		if (solution == null){
			throw new OptionSyntaxError("There is a syntax error in the value of the option " + solutioncomposition + ".");
		}

		for (int i = 0; i < solution.length; i++){
			String [] couple = solution[i].split("=");
			if (couple == null){
				throw new OptionSyntaxError("There is a syntax error in the value of the option " + solutioncomposition + ".");
			}

			String concentration = solution[i].split("=")[1];
			double val = Double.parseDouble(concentration);
			try {
				if (val < 0){
					throw new OptionSyntaxError("All the concentrations must be positive.");
				}
			} catch (NumberFormatException e) {
				throw new OptionSyntaxError("All the concentrations must be a numeric value.");
			}
		}		
		return true;
	}
	
	private HashMap<String, String> initializeDefaultOptions(String [] args){

		HashMap<String, String> optionSet = new HashMap<String, String> ();
		String hybrid = "";
		
		for (int i = 0; i < args.length; i++){
			if (args[i].equals(OptionManagement.hybridization)){
				if (i != args.length - 1 && isAValue(args[i + 1])){
					hybrid = args[i + 1];
					break;
				}
			}
		}

		if (hybrid.equals("dnadna")) {
			optionSet.putAll(DNADefaultOptions);
		}
		else if (hybrid.equals("rnarna")) {
			optionSet.putAll(RNADefaultOptions);
		}
		else if (hybrid.equals("rnadna") || hybrid.equals("dnarna")) {
			optionSet.putAll(hybridDefaultOptions);
		}
		else if (hybrid.equals("mrnarna") || hybrid.equals("rnamrna")) {
			optionSet.putAll(mRNADefaultOptions);
		}
		else {
			throw new OptionSyntaxError("The hybridization type is required. It can be dnadna, rnarna, rnadna (dnarna) or mrnarna (rnamrna).");
		}
		
		optionSet.put(solutioncomposition, "Na=0:Mg=0:K=0:Tris=0:dNTP=0:DMSO=0:formamide=0");
		optionSet.put(NN_Path, dataPathwayValue);
		optionSet.put(threshold, Integer.toString(thresholdValue));
		optionSet.put(factor, Integer.toString(factorValue));
		optionSet.put(completMethod, "default");
			
		return optionSet;
	}
	
	public HashMap<String, String> collectOptions(String [] args){

		if (args.length < 2){
			throw new OptionSyntaxError("There is a syntax error int the options. Check the manual for further informations about melting options.");
		}

		setOptionValues(args);
		meltingLogger.setLevel(Level.INFO);

		HashMap<String, String> optionSet = new HashMap<String, String>();
			
		optionSet = initializeDefaultOptions(args);

		for (int i = 0;i < args.length - 1; i++){
			String option = args[i];
			String value = args[i+1];

				if (isAValue(option) == false){

					if (option.equals(OptionManagement.verboseMode)){
						meltingLogger.setLevel(Level.FINE);
						
						StringBuffer verboseValue = new StringBuffer();
						verboseValue.append("******************************************************************************\n");
						verboseValue.append(versionNumber + "\n");
						verboseValue.append("This program   computes for a nucleotide probe, the enthalpy, the entropy \n");
						verboseValue.append("and the melting temperature of the binding to its complementary template. \n");
						verboseValue.append("Four types of hybridisation are possible: DNA/DNA, DNA/RNA, RNA/RNA and 2-O-methyl RNA/RNA. \n");
						verboseValue.append("Copyright (C) Nicolas Le Novère and Marine Dumousseau 2009 \n \n");
						verboseValue.append("******************************************************************************\n");
						
						meltingLogger.log(Level.FINE, verboseValue.toString());
					}
					else if (option.equals(OptionManagement.outPutFile)){
						if (isAValue(value)){
							try {
								meltingLogger.addHandler(new FileHandler(value));
								
								meltingLogger.setUseParentHandlers(false);
								
							} catch (SecurityException e) {
								throw new NoExistingOutputFileException("We cannot output the results in a file. Check the option " + outPutFile);
							} catch (IOException e) {
								throw new NoExistingOutputFileException("We cannot output the results in a file. Check the option " + outPutFile);
							}
						}
						else{
							throw new OptionSyntaxError("I don't understand the option " + option + value + ". We need a file name after the option " + outPutFile);
						}
					}
					else {

						if (isAValue(value)){
							optionSet.put(option, value);

						}
						else{
							throw new OptionSyntaxError("I don't understand the option " + option + value + ".");
						}
					}
				}
			}
		if (hasRequiredOptions(optionSet) == false){

			throw new OptionSyntaxError("To compute, MELTING need at less the hybridization type " +
					"(option " + hybridization + "), the nucleic acid concentration (option " 
					+ nucleotides + ") and the sequence (option " + sequence + "). If there " +
					"are inosine bases in the sequence, a complementary sequence is required (option"
					+ complementarySequence + ").");
		}
			
		return optionSet;
	}
	
	public Environment createEnvironment(String [] args){

		HashMap<String, String> optionDictionnary = collectOptions(args);
			
		return new Environment(optionDictionnary);
	}

}