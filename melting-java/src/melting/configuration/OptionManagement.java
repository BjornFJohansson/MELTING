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

public class OptionManagement {
	
	public static final String meltingHelp = "-h";
	public static final String legalInformation = "-L";
	public static final String dataPathway = "-p";
	public static final String versionNumber = "-V";
	public static final String globalMethod = "-mode";
	public static final String approximativeMode = "-am";
	public static final String NNMethod = "-nn";
	public static final String singleMismatchMethod = "-sinMM";
	public static final String wobbleBaseMethod = "-GU";
	public static final String tandemMismatchMethod = "-tan";
	public static final String internalLoopMethod = "-intLP";
	public static final String singleDanglingEndMethod = "-sinDE";
	public static final String doubleDanglingEndMethod = "-secDE";
	public static final String longDanglingEndMethod = "-lonDE";
	public static final String singleBulgeLoopMethod = "-sinBU";
	public static final String longBulgeLoopMethod = "-lonBU";
	public static final String CNGMethod = "-CNG";
	public static final String inosineMethod = "-ino";
	public static final String hydroxyadenineMethod = "-ha";
	public static final String azobenzeneMethod = "-azo";
	public static final String lockedAcidMethod = "-lck";
	public static final String sequence = "-S";
	public static final String complementarySequence = "-C";
	public static final String solutioncomposition = "-E";
	public static final String nucleotides = "-P";
	public static final String hybridization = "-H";
	public static final String ionCorrection = "-ion";
	public static final String NaEquivalentMethod = "-naeq";
	public static final String DMSOCorrection = "-DMSO";
	public static final String formamideCorrection = "-for";
	public static final String verboseMode = "-v";
	public static final String threshold = "-T";
	public static final String NN_Path = "-NNPath";
	public static final String outPutFile = "-O";
	public static final String selfComplementarity = "-self";
	public static final String factor = "-F";
	public static final Logger meltingLogger = Logger.getLogger("melting");
	
	private static final ArrayList<String> registerPartialMethods = new ArrayList<String>();
	private static final ArrayList<String> registerMeltingVariables = new ArrayList<String>();
	private static final ArrayList<String> registerEnvironmentOptions = new ArrayList<String>();
	
	private static final String version = "5";
	public static String dataPathwayValue = "src/Data";
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
		initializeMeltingVariables();
		initializeRegisterPartialMethods();
		initializeRegisterEnvironmentOptions();
	}
	
	private void initializeRegisterPartialMethods(){
		registerPartialMethods.add(NNMethod);
		registerPartialMethods.add(singleMismatchMethod);
		registerPartialMethods.add(wobbleBaseMethod);
		registerPartialMethods.add(tandemMismatchMethod);
		registerPartialMethods.add(internalLoopMethod);
		registerPartialMethods.add(singleDanglingEndMethod);
		registerPartialMethods.add(doubleDanglingEndMethod);
		registerPartialMethods.add(longDanglingEndMethod);
		registerPartialMethods.add(singleBulgeLoopMethod);
		registerPartialMethods.add(longBulgeLoopMethod);
		registerPartialMethods.add(CNGMethod);
		registerPartialMethods.add(inosineMethod);
		registerPartialMethods.add(hydroxyadenineMethod);
		registerPartialMethods.add(azobenzeneMethod);
		registerPartialMethods.add(lockedAcidMethod);
	}
	
	private void initializeRegisterEnvironmentOptions(){
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
	
	private void initializeMeltingVariables(){
		registerMeltingVariables.add(NN_Path);
		registerMeltingVariables.add(threshold);
		registerMeltingVariables.add(factor);
	}
	
	public HashMap<String, String> getDNADefaultOptions() {
		return DNADefaultOptions;
	}
	
	private void setDNADefaultOptions() {
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
	
	public HashMap<String, String> getRNADefaultOptions() {
		return RNADefaultOptions;
	}
	
	private void setRNADefaultOptions() {
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
	
	public HashMap<String, String> getHybridDefaultOptions() {
		return hybridDefaultOptions;
	}
	
	private void setHybridDefaultOptions() {
		this.hybridDefaultOptions.put(NNMethod, "sug95");
		this.hybridDefaultOptions.put(approximativeMode, "Wetdnarna91");
		this.hybridDefaultOptions.put(NaEquivalentMethod, "ahs01");
		this.hybridDefaultOptions.put(DMSOCorrection, "ahs01");
		this.hybridDefaultOptions.put(formamideCorrection, "bla96");
		
	}
	
	public HashMap<String, String> getMRNADefaultOptions() {
		return mRNADefaultOptions;
	}
	
	private void setMRNADefaultOptions() {
		this.mRNADefaultOptions.put(NNMethod, "tur06");
		this.mRNADefaultOptions.put(NaEquivalentMethod, "ahs01");
		this.mRNADefaultOptions.put(DMSOCorrection, "ahs01");
		this.mRNADefaultOptions.put(formamideCorrection, "bla96");

	}
	
	private boolean isAValue(String optionValue){
		if (optionValue.length() == 0){
			return false;
		}
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
					if (isAValue(value)){
						if (Integer.getInteger(value) != null && (Integer.parseInt(value) == 1 || Integer.parseInt(value) == 4)) {
							factorValue = Integer.parseInt(value);
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
		
		initializeLogger();
		
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
			throw new OptionSyntaxError("The nucleotide concentration must be a numeric value.", e);
		}
		
		String value = optionSet.get(sequence).toUpperCase();
		BasePair.initializeNucleicAcidList();
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
		optionSet.put(globalMethod, "def");
		optionSet.put(selfComplementarity, "false");
			
		return optionSet;
	}
	
	private void initializeLogger(){
		StreamHandler handler = new StreamHandler(System.out, new MeltingFormatter());
		meltingLogger.setUseParentHandlers(false);
		meltingLogger.addHandler(handler);
	}
	
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
	
	public HashMap<String, String> collectOptions(String [] args){

		if (args.length < 2){
			throw new OptionSyntaxError("There is a syntax error int the options. Check the manual for further informations about melting options.");
		}

		initializeLogger();
		
		setOptionValues(args);
			
		HashMap<String, String> optionSet = initializeDefaultOptions(args);

		for (int i = 0;i <= args.length - 1; i++){
			String option = args[i];
			String value = "";
			if (i + 1 <= args.length - 1){
				value = args[i+1];
			}
			boolean isAnValue = isAValue(option);
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
						if (isAValue(value)){
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
							if (isAValue(value)){
								optionSet.put(option, value);
							}
						}
						else if (isAValue(value) || option.equals(OptionManagement.sequence) || option.equals(OptionManagement.complementarySequence)){

							optionSet.put(option, value);
						}
						else{
							throw new OptionSyntaxError("I don't understand the option " + option + value + ".");
						}
					}
					else if (registerMeltingVariables.contains(option) == false && registerPartialMethods.contains(option)){
						if (option.equals(NNMethod)){
							optionSet.put(globalMethod, "NN");
							if (isAValue(value)){
								optionSet.put(option, value);
							}
						}
						else if (isAValue(value)){
							if (Helper.useOtherDataFile(value) && Helper.getOptionMethodName(value).length() == 0){
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
