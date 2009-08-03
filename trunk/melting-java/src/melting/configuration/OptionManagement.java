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

package melting.configuration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;


import melting.Environment;
import melting.Helper;
import melting.MeltingFormatter;
import melting.exceptions.FileException;
import melting.exceptions.NoExistingOutputFileException;
import melting.exceptions.OptionSyntaxError;
import melting.sequences.BasePair;
import melting.sequences.NucleotidSequences;

/**
 * This class contains all the methods necessary to manage the options entered by the user.
 * It registers also all the possible options names, Melting version number, pathway of the
 * data files, default options, meltingLogger Logger, etc...
 */
public class OptionManagement {
	
	// Instance variables
	
	/**
	 * Option name for the melting help documentation.
	 */
	public static final String meltingHelp = "-h";
	
	/**
	 * Option name for the melting legal information.
	 */
	public static final String legalInformation = "-L";
	
	/**
	 * Option name for the default melting data files pathway.
	 */
	public static final String dataPathway = "-p";
	
	/**
	 * Option name for the current melting version number.
	 */
	public static final String versionNumber = "-V";
	
	/**
	 * Option name for the choice of computation method (approximative "-A" or nearest neighbor "-NN").
	 */
	public static final String globalMethod = "-mode";
	
	/**
	 * Option name for the choice of approximative computation method.
	 */
	public static final String approximativeMode = "-am";
	
	/**
	 * Option name for the choice of nearest neighbor computation method.
	 */
	public static final String NNMethod = "-nn";
	
	/**
	 * Option name for the choice of single mismatch computation method.
	 */
	public static final String singleMismatchMethod = "-sinMM";
	
	/**
	 * Option name for the choice of GU base pairs computation method.
	 */
	public static final String wobbleBaseMethod = "-GU";
	
	/**
	 * Option name for the choice of tandem mismatches computation method.
	 */
	public static final String tandemMismatchMethod = "-tan";
	
	/**
	 * Option name for the choice of internal loop computation method.
	 */
	public static final String internalLoopMethod = "-intLP";
	
	/**
	 * Option name for the choice of single dangling end computation method.
	 */
	public static final String singleDanglingEndMethod = "-sinDE";
	
	/**
	 * Option name for the choice of double dangling end computation method.
	 */
	public static final String doubleDanglingEndMethod = "-secDE";
	
	/**
	 * Option name for the choice of long dangling end computation method.
	 */
	public static final String longDanglingEndMethod = "-lonDE";
	
	/**
	 * Option name for the choice of single bulge loop computation method.
	 */
	public static final String singleBulgeLoopMethod = "-sinBU";
	
	/**
	 * Option name for the choice of long bulge loop computation method.
	 */
	public static final String longBulgeLoopMethod = "-lonBU";
	
	/**
	 * Option name for the choice of CNG repeats computation method.
	 */
	public static final String CNGMethod = "-CNG";
	
	/**
	 * Option name for the choice of inosine computation method.
	 */
	public static final String inosineMethod = "-ino";
	
	/**
	 * Option name for the choice of hydroxyadenine computation method.
	 */
	public static final String hydroxyadenineMethod = "-ha";
	
	/**
	 * Option name for the choice of azobenzene computation method.
	 */
	public static final String azobenzeneMethod = "-azo";
	
	/**
	 * Option name for the choice of locked nucleic acid computation method.
	 */
	public static final String lockedAcidMethod = "-lck";
	
	/**
	 * Option name for the sequence (5'3'). Mandatory
	 */
	public static final String sequence = "-S";
	
	/**
	 * Option name for the complementary sequence (3'5'). Not always mandatory.
	 */
	public static final String complementarySequence = "-C";
	
	/**
	 * Option name for the different ion and agent concentrations (mol/L). Mandatory
	 */
	public static final String solutioncomposition = "-E";
	
	/**
	 * Option name for the oligomer concentration (mol/L). Mandatory
	 */
	public static final String nucleotides = "-P";
	
	/**
	 * Option name for the type of hybridization. Mandatory
	 */
	public static final String hybridization = "-H";
	
	/**
	 * Option name for the choice of ion correction method.
	 */
	public static final String ionCorrection = "-ion";
	
	/**
	 * Option name for the choice of sodium equivalence method.
	 */
	public static final String NaEquivalentMethod = "-naeq";
	
	/**
	 * Option name for the choice of DMSO correction computation method.
	 */
	public static final String DMSOCorrection = "-DMSO";
	
	/**
	 * Option name for the choice of formamide correction computation method.
	 */
	public static final String formamideCorrection = "-for";
	
	/**
	 * Option name to activate the verbose mode.
	 */
	public static final String verboseMode = "-v";
	
	/**
	 * Option name for the threshold value.
	 */
	public static final String threshold = "-T";
	
	/**
	 * Option name for the data file pathway.
	 */
	public static final String NN_Path = "-NNPath";
	
	/**
	 * Option name to print the melting results in an output file.
	 */
	public static final String outPutFile = "-O";
	
	/**
	 * Option name to precise the self complementarity of the sequences.
	 */
	public static final String selfComplementarity = "-self";
	
	/**
	 * Option name for the oligomer concentration correction factor.
	 */
	public static final String factor = "-F";
	
	/**
	 * Logger meltingLogger : Logger object to print the Melting warning, severe or/and info messages
	 */
	public static final Logger meltingLogger = Logger.getLogger("melting");
	
	/**
	 * ArrayList registerPatternModels : contains the option names to choose a pattern model.
	 */
	private static final ArrayList<String> registerPatternModels = new ArrayList<String>();
	
	/**
	 * ArrayList registerMeltingVariables : contains the option names to choose the private static instance variables values of the OptionManagement
	 * object. (data file pathways, threshold value, etc...).
	 */
	private static final ArrayList<String> registerMeltingVariables = new ArrayList<String>();
	
	/**
	 * ArrayList registerEnvironmentOptions : contains the option names to choose the Melting environment (type of hybridization, sequences, concentrations, etc...).
	 */
	private static final ArrayList<String> registerEnvironmentOptions = new ArrayList<String>();
	
	/**
	 * String version : current Melting version number.
	 */
	private static final String version = "5";
	
	/**
	 * String dataPathwayValue : default Melting data file pathway. (files containing
	 * the thermodynamic parameters.)
	 */
	public static String dataPathwayValue = System.getProperty("NN_PATH", "Data");
	
	/**
	 * int thresholdValue : the threshold value. It is the maximum oligonucleotides length for which we
	 * will use the nearest neighbor computation. 
	 */
	private static int thresholdValue = 60;
	
	/**
	 * int factorValue : default oligomer concentration factor value
	 */
	private static int factorValue = 4;
	
	/**
	 * HashMap<String, String> DNADefaultOptions : contains the default options if the type of hybridization is "dnadna"
	 */
	private HashMap<String, String> DNADefaultOptions = new HashMap<String, String>();
	
	/**
	 * HashMap<String, String> RNADefaultOptions : contains the default options if the type of hybridization is "rnarna"
	 */
	private HashMap<String, String> RNADefaultOptions = new HashMap<String, String>();
	
	/**
	 * HashMap<String, String> hybridDefaultOptions : contains the default options if the type of hybridization is "dnarna" or "rnadna"
	 */
	private HashMap<String, String> hybridDefaultOptions = new HashMap<String, String>();
	
	/**
	 * HashMap<String, String> mRNADefaultOptions : contains the default options if the type of hybridization is "mrnarna" or "rnamrna"
	 */
	private HashMap<String, String> mRNADefaultOptions = new HashMap<String, String>();

	// OptionManagement constructor
	
	/**
	 * creates an OptionManagement object. initialises the default options, variables, etc... 
	 */
	public OptionManagement(){
		
		initialisesDNADefaultOptions();
		initialiseRNADefaultOptions();
		initialiseHybridDefaultOptions();
		initialiseMRNADefaultOptions();
		initialiseMeltingVariables();
		initialiseRegisterPatternModels();
		initialiseRegisterEnvironmentOptions();
	}
	
	// private methods
	
	/**
	 * Initialises the registerPatternModels HashMap of the OptionManagement object.
	 */
	private void initialiseRegisterPatternModels(){
		registerPatternModels.add(NNMethod);
		registerPatternModels.add(singleMismatchMethod);
		registerPatternModels.add(wobbleBaseMethod);
		registerPatternModels.add(tandemMismatchMethod);
		registerPatternModels.add(internalLoopMethod);
		registerPatternModels.add(singleDanglingEndMethod);
		registerPatternModels.add(doubleDanglingEndMethod);
		registerPatternModels.add(longDanglingEndMethod);
		registerPatternModels.add(singleBulgeLoopMethod);
		registerPatternModels.add(longBulgeLoopMethod);
		registerPatternModels.add(CNGMethod);
		registerPatternModels.add(inosineMethod);
		registerPatternModels.add(hydroxyadenineMethod);
		registerPatternModels.add(azobenzeneMethod);
		registerPatternModels.add(lockedAcidMethod);
	}
	
	/**
	 * Initialises the registerEnvironmentOptions HashMap of the OptionManagement object.
	 */
	private void initialiseRegisterEnvironmentOptions(){
		registerEnvironmentOptions.add(solutioncomposition);
		registerEnvironmentOptions.add(globalMethod);
		registerEnvironmentOptions.add(approximativeMode);
		registerEnvironmentOptions.add(NaEquivalentMethod);
		registerEnvironmentOptions.add(sequence);
		registerEnvironmentOptions.add(complementarySequence);
		registerEnvironmentOptions.add(nucleotides);
		registerEnvironmentOptions.add(hybridization);
		registerEnvironmentOptions.add(ionCorrection);
		registerEnvironmentOptions.add(DMSOCorrection);
		registerEnvironmentOptions.add(formamideCorrection);

	}
	
	/**
	 * Initialises the registerMeltingVariables HashMap of the OptionManagement object.
	 */
	private void initialiseMeltingVariables(){
		registerMeltingVariables.add(NN_Path);
		registerMeltingVariables.add(threshold);
		registerMeltingVariables.add(factor);
	}
	
	/**
	 * initialises the DNADefaultOptions HashMap of the OptionManagement object.
	 */
	private void initialisesDNADefaultOptions() {
		this.DNADefaultOptions.put(NNMethod, "san04");
		this.DNADefaultOptions.put(singleMismatchMethod, "allsanpey");
		this.DNADefaultOptions.put(tandemMismatchMethod, "allsanpey");
		this.DNADefaultOptions.put(internalLoopMethod, "san04");
		this.DNADefaultOptions.put(singleDanglingEndMethod, "bom00");
		this.DNADefaultOptions.put(doubleDanglingEndMethod, "sugdna02");
		this.DNADefaultOptions.put(singleBulgeLoopMethod, "tan04");
		this.DNADefaultOptions.put(longDanglingEndMethod, "sugdna02");
		this.DNADefaultOptions.put(longBulgeLoopMethod, "san04");
		this.DNADefaultOptions.put(approximativeMode, "wetdna91");
		this.DNADefaultOptions.put(DMSOCorrection, "ahs01");
		this.DNADefaultOptions.put(formamideCorrection, "bla96");
		this.DNADefaultOptions.put(inosineMethod, "san05");
		this.DNADefaultOptions.put(hydroxyadenineMethod, "sug01");
		this.DNADefaultOptions.put(azobenzeneMethod, "asa05");
		this.DNADefaultOptions.put(lockedAcidMethod, "mct04");
		this.DNADefaultOptions.put(NaEquivalentMethod, "ahs01");

	}
	
	/**
	 * initialises the RNADefaultOptions HashMap of the OptionManagement object.
	 */
	private void initialiseRNADefaultOptions() {
		this.RNADefaultOptions.put(NNMethod, "xia98");
		this.RNADefaultOptions.put(singleMismatchMethod, "zno07");
		this.RNADefaultOptions.put(wobbleBaseMethod, "tur99");
		this.RNADefaultOptions.put(tandemMismatchMethod, "tur06");
		this.RNADefaultOptions.put(internalLoopMethod, "tur06");
		this.RNADefaultOptions.put(singleBulgeLoopMethod, "tur06");
		this.RNADefaultOptions.put(longBulgeLoopMethod, "tur06");
		this.RNADefaultOptions.put(CNGMethod, "bro05");
		this.RNADefaultOptions.put(approximativeMode, "wetrna91");
		this.RNADefaultOptions.put(inosineMethod, "zno07");
		this.RNADefaultOptions.put(NaEquivalentMethod, "ahs01");
		this.RNADefaultOptions.put(DMSOCorrection, "ahs01");
		this.RNADefaultOptions.put(formamideCorrection, "bla96");
		this.RNADefaultOptions.put(singleDanglingEndMethod, "ser08");
		this.RNADefaultOptions.put(doubleDanglingEndMethod, "ser06");
		this.RNADefaultOptions.put(longDanglingEndMethod, "sugrna02");

	}
	
	/**
	 * initialises the hybridDefaultOptions HashMap of the OptionManagement object.
	 */
	private void initialiseHybridDefaultOptions() {
		this.hybridDefaultOptions.put(NNMethod, "sug95");
		this.hybridDefaultOptions.put(approximativeMode, "Wetdnarna91");
		this.hybridDefaultOptions.put(NaEquivalentMethod, "ahs01");
		this.hybridDefaultOptions.put(DMSOCorrection, "ahs01");
		this.hybridDefaultOptions.put(formamideCorrection, "bla96");
		
	}
	
	/**
	 * initialises the mRNADefaultOptions HashMap of the OptionManagement object.
	 */
	private void initialiseMRNADefaultOptions() {
		this.mRNADefaultOptions.put(NNMethod, "tur06");
		this.mRNADefaultOptions.put(NaEquivalentMethod, "ahs01");
		this.mRNADefaultOptions.put(DMSOCorrection, "ahs01");
		this.mRNADefaultOptions.put(formamideCorrection, "bla96");

	}
	
	/**
	 * to check if the argument 'option' entered by the user is an option value.
	 * @param String option
	 * @return true if the argument 'option' entered by the user is an option value.
	 * Ex : "-nn" => isAnOptionValue("-nn") = false
	 * Ex : "san04" => isAnOptionValue("san04") = true
	 */ 
	private boolean isAnOptionValue(String option){
		if (option.length() == 0){
			return false;
		}
		if (option.charAt(0) != '-'){
			return true;
		}
		return false;
	}
	
	/**
	 * changes the default Threshold value, correction factor value and/or data file pathway value if the user wants to.
	 * @param String [] args : contains the options entered by the user
	 * If there is an error in the options syntax, a OptonSyntaxError is thrown.
	 */
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

				if (isAnOptionValue(option) == false){
					if (option.equals(NN_Path)){
						if (isAnOptionValue(value)){
							dataPathwayValue = value;
						}
						else {
							throw new OptionSyntaxError("I don't understand the option " + option + value + ".");
						}
					}	
				}
				else if (option.equals(threshold)){
					if (isAnOptionValue(value)){
						if (Integer.getInteger(value) != null && Integer.parseInt(value) >= 0) {
							thresholdValue = Integer.parseInt(value);
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
					if (isAnOptionValue(value)){
						if (Integer.getInteger(value) != null && (Integer.parseInt(value) == 1 || Integer.parseInt(value) == 2 || Integer.parseInt(value) == 4)) {
							factorValue = Integer.parseInt(value);
						}
						else {
							throw new OptionSyntaxError("The correction factor must be 1, 2 or 4.");
						}
					}
					else {
						throw new OptionSyntaxError("I don't understand the option " + option + value + ".");				}
				}
			}
		}
	}
	
	/**
	 * displays Melting help documentation.
	 * If a FileNotFoundException or a IOException is catch, a FileException is thrown.
	 */
	private void readMeltingHelp(){
		try {
			FileReader help = new FileReader("src/melting/help.txt");
			BufferedReader buffer = new BufferedReader(help);
			
			String line = buffer.readLine();
			while (line != null){
				meltingLogger.log(Level.INFO, line);
				line = buffer.readLine();
			}

			buffer.close();
	
		} catch (FileNotFoundException e) {
			throw new FileException("The help file doesn't exist. Check if the melting help file is src/melting/help.txt", e);
		}
		catch (IOException e) {
			throw new FileException("The help file can't be read or can't be closed. Check src/melting/help.txt.", e);
		}
		
	}
	
	/**
	 * displays Melting legal information.
	 */
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
	
	/**
	 * to check if all mandatory options are present and valid. (hybridization type, ion concentrations, sequence (5'3'), oligomer concentration and sometimes the 
	 * complementary sequence (3'5'))
	 * @param HashMap<String, String> optionSet : contains the options (default options and options entered by the user)
	 * @return true if all mandatory options are present and valid.
	 * If one of the required options is not valid, an OptionSynthaxError is thrown.
	 */
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
			throw new OptionSyntaxError("The nucleotide concentration must be a numeric value.", e);
		}
		
		String value = optionSet.get(sequence).toUpperCase();
		BasePair.initialiseNucleicAcidList();
		if (NucleotidSequences.checkSequence(value)){
			if ((value.contains("I") && optionSet.get(selfComplementarity).equals("false")) || value.contains("A*")){
				needComplementaryInput = true;
			}
		}
		else {
			throw new OptionSyntaxError("The sequence contains some characters we can't understand.");
		}

		if (checkConcentrations(optionSet.get(solutioncomposition)) == false) {

			throw new OptionSyntaxError("There is one syntax mistake in the concentrations. Check the option" + solutioncomposition + ".");
		}
		if(optionSet.containsKey(complementarySequence)){
			if (NucleotidSequences.checkSequence(optionSet.get(OptionManagement.complementarySequence)) == false){
				throw new OptionSyntaxError("The complementary sequence contains some characters we can't understand.");
			}
		}
		
		else if (needComplementaryInput && optionSet.containsKey(complementarySequence) == false){
			return false;
		}
		else if (optionSet.containsKey(complementarySequence) == false && needComplementaryInput == false){
			if (NucleotidSequences.isSelfComplementarySequence(optionSet.get(OptionManagement.sequence).toUpperCase()) || optionSet.get(selfComplementarity).equals("true")){
				optionSet.put(selfComplementarity, "true");
				optionSet.put(factor, "1");
				
				String seq2 = NucleotidSequences.getInversedSequence(optionSet.get(sequence));
				optionSet.put(complementarySequence, seq2);
			}
			else {
				String seq2 = NucleotidSequences.getComplementarySequence(optionSet.get(sequence), optionSet.get(hybridization));
				optionSet.put(complementarySequence, seq2);
			}

		}
		return true;
	}
	
	/**
	 * to check if all the ion and agent concentrations entered by the user are valid. (positive numeric values)
	 * @param String solutionComposition : different ion and agent concentrations
	 * @return true if all the ion and agent concentrations entered by the are valid.
	 * If at least one of the concentrations is not valid, an OptionSynthaxException is thrown.
	 * If a NumberFormatException is catch, an OptionSynthaxError is thrown.
	 */
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
				throw new OptionSyntaxError("All the concentrations must be a numeric value.", e);
			}
		}		
		return true;
	}
	
	/**
	 * puts the default options in a new HashMap depending on the type of hybridization entered by the user.
	 * @param String [] args : contains the options entered by the user.
	 * @return HasMap containing the default options. The default options depend on the type of hybridization entered by the user.
	 * If the type of hybridization is missing, an OptionSyntaxError is thrown.
	 */
	private HashMap<String, String> initialiseDefaultOptions(String [] args){

		HashMap<String, String> optionSet = new HashMap<String, String> ();
		String hybrid = "";
		
		for (int i = 0; i < args.length; i++){
			if (args[i].equals(OptionManagement.hybridization)){
				if (i != args.length - 1 && isAnOptionValue(args[i + 1])){
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
		optionSet.put(globalMethod, "def");
		optionSet.put(selfComplementarity, "false");
			
		return optionSet;
	}
	
	/**
	 * initialises the meltingLogger Logger.
	 */
	private void initialiseLogger(){
		StreamHandler handler = new StreamHandler(System.out, new MeltingFormatter());
		meltingLogger.setUseParentHandlers(false);
		meltingLogger.addHandler(handler);
	}
	
	/**
	 * the verbose mode introduction. (Melting introduction)
	 * @return String containing the melting introduction for the verbose mode.
	 */
	private String getVerbose(){
		StringBuffer verboseValue = new StringBuffer();
		verboseValue.append("******************************************************************************\n");
		verboseValue.append("melting " + version + "\n");
		verboseValue.append("This program   computes for a nucleotide probe, the enthalpy, the entropy \n");
		verboseValue.append("and the melting temperature of the binding to its complementary template. \n");
		verboseValue.append("Four types of hybridisation are possible: DNA/DNA, DNA/RNA, RNA/RNA and 2-O-methyl RNA/RNA. \n");
		verboseValue.append("Copyright (C) Nicolas Le Novère and Marine Dumousseau 2009 \n \n");
		verboseValue.append("******************************************************************************\n");
		return verboseValue.toString();
	}
	
	/**
	 * collects the options entered by the user and stocks them in a HashMap.
	 * @param String [] args : contains the options entered by the user.
	 * @return HasMap containing the options entered by the user.
	 * If one of the options is invalid, an OptionsSyntaxError is thrown.
	 * If one of the Melting required options is missing, an OptionsSyntaxError is thrown. 
	 */
	private HashMap<String, String> collectOptions(String [] args){

		if (args.length < 2){
			throw new OptionSyntaxError("There is a syntax error int the options. Check the manual for further informations about melting options.");
		}

		initialiseLogger();
		
		setOptionValues(args);
			
		HashMap<String, String> optionSet = initialiseDefaultOptions(args);

		for (int i = 0;i <= args.length - 1; i++){
			String option = args[i];
			String value = "";
			if (i + 1 <= args.length - 1){
				value = args[i+1];
			}
			boolean isAnValue = isAnOptionValue(option);
			if (i > 0){
				if (args[i - 1].equals(OptionManagement.sequence) || args[i - 1].equals(OptionManagement.complementarySequence)){
					isAnValue = true;
				}
			}
			if (isAnValue == false){
					if (option.equals(OptionManagement.verboseMode)){
						meltingLogger.setLevel(Level.FINE);
						Handler[] handlers = meltingLogger.getHandlers();
						for ( int index = 0; index < handlers.length; index++ ) {
						    handlers[index].setLevel( Level.FINE );
						}

					}
					else if (option.equals(OptionManagement.outPutFile)){
						if (isAnOptionValue(value)){
							try {
								Handler[] handlers = meltingLogger.getHandlers();
								for ( int index = 0; index < handlers.length; index++ ) {
								    handlers[index].setLevel( Level.OFF );
								}
								FileHandler fileHandler = new FileHandler(value);
								fileHandler.setLevel(meltingLogger.getLevel());
								fileHandler.setFormatter(new MeltingFormatter());
								
								meltingLogger.addHandler(fileHandler);
								
							} catch (SecurityException e) {
								throw new NoExistingOutputFileException("We cannot output the results in a file. Check the option " + outPutFile, e);
							} catch (IOException e) {
								throw new NoExistingOutputFileException("We cannot output the results in a file. Check the option " + outPutFile, e);
							}
						}
						else{
							throw new OptionSyntaxError("I don't understand the option " + option + value + ". We need a file name after the option " + outPutFile);
						}
					}
					else if (option.equals(OptionManagement.selfComplementarity)){
						optionSet.put(option, "true");
						optionSet.put(factor, "1");
					}
					else if (registerEnvironmentOptions.contains(option)){
						if (option.equals(approximativeMode)){
							optionSet.put(globalMethod, "A");
							if (isAnOptionValue(value)){
								optionSet.put(option, value);
							}
						}
						else if (isAnOptionValue(value) || option.equals(OptionManagement.sequence) || option.equals(OptionManagement.complementarySequence)){

							optionSet.put(option, value);
						}
						else{
							throw new OptionSyntaxError("I don't understand the option " + option + value + ".");
						}
					}
					else if (registerMeltingVariables.contains(option) == false && registerPatternModels.contains(option)){
						if (option.equals(NNMethod)){
							optionSet.put(globalMethod, "NN");
							if (isAnOptionValue(value)){
								optionSet.put(option, value);
							}
						}
						else if (isAnOptionValue(value)){
							if (Helper.useOtherDataFile(value) && Helper.extractsOptionMethodName(value).length() == 0){
								String newValue = optionSet.get(option) + value;
								optionSet.put(option, newValue);
							}
							else {
								optionSet.put(option, value);
							}
						}
						else{
							throw new OptionSyntaxError("I don't understand the option " + option + value + ".");
						}
					}
					else{
						throw new OptionSyntaxError("I don't understand the option " + option + value + ".");
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
		
		meltingLogger.log(Level.FINE, getVerbose());

		return optionSet;
		
	}
	
	// public methods
	
	/**
	 * This method is called to get the DNADefaultOptions HashMap of the OptionManagement object.
	 * @return the DNADefaultOptions HashMap of the OptionManagement object.
	 */
	public HashMap<String, String> getDNADefaultOptions() {
		return DNADefaultOptions;
	}
	
	/**
	 * This method is called to get the RNADefaultOptions HashMap of the OptionManagement object.
	 * @return the RNADefaultOptions HashMap of the OptionManagement object.
	 */
	public HashMap<String, String> getRNADefaultOptions() {
		return RNADefaultOptions;
	}
	
	/**
	 * This method is called to get the hybridDefaultOptions HashMap of the OptionManagement object.
	 * @return the hybridDefaultOptions HashMap of the OptionManagement object.
	 */
	public HashMap<String, String> getHybridDefaultOptions() {
		return hybridDefaultOptions;
	}
	
	/**
	 * This method is called to get the mRNADefaultOptions HashMap of the OptionManagement object.
	 * @return the mRNADefaultOptions HashMap of the OptionManagement object.
	 */
	public HashMap<String, String> getMRNADefaultOptions() {
		return mRNADefaultOptions;
	}
	
	/**
	 * to check if the user just wants to know some information about melting. (help documentation, legal information, data file pathway or Melting version number)
	 * @param String [] args : contains the options entered by the user
	 * @return true if the user wants to know some information about melting.
	 */
	public boolean isMeltingInformationOption(String [] args){
		
		for (int i = 0;i < args.length; i++){
			String option = args[i];
			if (isAnOptionValue(option) == false){
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
	
	/**
	 * if at least one of the options entered by the user is an option to display some information about Melting,
	 * the requested information is displayed.
	 * @param String [] args : contains the option entered by the user
	 */
	public void readOptions(String [] args){

		setOptionValues(args);
		
		initialiseLogger();
		
		for (int i = 0;i < args.length; i++){
			String option = args[i];
				
			if (isAnOptionValue(option) == false){
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
	
	/**
	 * collects the options entered by the user and creates an Environment object from them.
	 * @param String [] args : contains the options entered by the user
	 * @return Environment object initialized with the default options and the options entered by the user.
	 */
	public Environment createEnvironment(String [] args){

		HashMap<String, String> optionDictionnary = collectOptions(args);
		Environment environment = new Environment(optionDictionnary);
		
		OptionManagement.meltingLogger.log(Level.FINE, "Environment : ");
		Iterator<Map.Entry<String, Double>> entry = environment.getConcentrations().entrySet().iterator();
		while (entry.hasNext()){
			Map.Entry<String, Double> couple = entry.next();
			String ion = couple.getKey();
			Double concentration = couple.getValue();
			OptionManagement.meltingLogger.log(Level.FINE, ion + " = " + concentration + " mol / L");
		}
		OptionManagement.meltingLogger.log(Level.FINE, "hybridization type : " + environment.getHybridization());
		OptionManagement.meltingLogger.log(Level.FINE, "probe concentration : " + environment.getNucleotides() + "mol/L");
		OptionManagement.meltingLogger.log(Level.FINE, "correction factor F : " + environment.getFactor());
		if (environment.isSelfComplementarity()){
			OptionManagement.meltingLogger.log(Level.FINE, "self complementarity ");
		}
		else{
			OptionManagement.meltingLogger.log(Level.FINE, "no self complementarity ");
		}
		OptionManagement.meltingLogger.log(Level.FINE, "sequence : " + environment.getSequences().getSequence());
		OptionManagement.meltingLogger.log(Level.FINE, "complementary sequence : " + environment.getSequences().getComplementary());
		
		return environment;
	}

}
