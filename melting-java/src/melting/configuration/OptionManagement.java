package melting.configuration;

import java.util.HashMap;

import melting.CompletCalculMethod;
import melting.Helper;

public class OptionManagement {
	
	public static final String sequence = "-seq";
	public static final String complementarySequence = "-seq2";
	public static final String Na = "-Na";
	public static final String Mg = "-Mg";
	public static final String K = "-K";
	public static final String Tris = "-Tris";
	public static final String dNTP = "-dNTP";
	public static final String nucleotides = "-probe";
	public static final String completMethod = "-method";
	public static final String hybridization = "-Hybrid";
	public static final String approximativeMode = "-AMode";
	public static final String correctionIon = "-corr";
	public static final String NNMethod = "-NN";
	public static final String singleMismatchMethod = "-singleMM";
	public static final String tandemMismatchMethod = "-tandemMM";
	public static final String internalLoopMethod = "-internalLoop";
	public static final String singleDanglingEndMethod = "-singleDE";
	public static final String doubleDanglingEndMethod = "-doubleDE";
	public static final String longDanglingEndMethod = "-longDE";
	public static final String hairpinLoopMethod = "-hairpin";
	public static final String singleBulgeLoopMethod = "-singleBulge";
	public static final String longBulgeLoopMethod = "-longBulge";
	public static final String CNGMethod = "-CNG";
	public static final String inosineMethod = "-inosine";
	public static final String hydroxyadenineMethod = "-HydroxyA";
	public static final String azobenzeneMethod = "-azobenzene";
	public static final String lockedAcidMethod = "-locked";
	public static final String deoxyadenosineMethod = "-deoxyA";
	public static final String oldCommandLine = "-old";
	public static final String quitMelting = "-q";
	public static final String meltingHelp = "-help";
	public static final String legalInformation = "-L";
	public static final String dataPathway = "-p";
	public static final String verboseMode = "-verbose";
	public static final String threshold = "-thres";
	public static final String NN_Path = "-NNPath";
	public static final String outPutFile = "-output";
	public static final String inPutFile = "-input";
	public static final String versionNumber = "-Version";
	public static final String NaEquivalentMethod = "-Naeq";
	
	private static final String version = "1";
	private static final int totalOptionsRequired = 3;
	private static int thresholdValue = 60;
	private static String dataPathwayValue = "../../../Data";
	
	private HashMap<String, String> DNADefaultOptions = new HashMap<String, String>();
	private HashMap<String, String> RNADefaultOptions = new HashMap<String, String>();
	private HashMap<String, String> hybridDefaultOptions = new HashMap<String, String>();
	private HashMap<String, String> mRNADefaultOptions = new HashMap<String, String>();

	public OptionManagement(){
		this.DNADefaultOptions.put("NNMethod", "Santalucia_2004");
		this.DNADefaultOptions.put("singleMismatchMethod", "Allawi_Santalucia_Peyret_1997_1998_1999");
		this.DNADefaultOptions.put("tandemMismatchMethod", "Allawi_Santalucia_Peyret_1997_1998_1999");
		this.DNADefaultOptions.put("internalLoopMethod", "Santalucia_2004");
		this.DNADefaultOptions.put("singleBulgeMethod", "Tanaka_2004");
		this.DNADefaultOptions.put("longBulgeMethod", "Santalucia_2004");
		this.DNADefaultOptions.put("hairpinMethod", "Santalucia_2004");
		this.DNADefaultOptions.put("approximativeMode", "Wetmur_1981");
		this.DNADefaultOptions.put("correctionIon", "Owczarzy_2008");
		this.DNADefaultOptions.put("inosineMethod", "Santalucia_2005");
		this.DNADefaultOptions.put("hydroxyadenineMethod", "Sugimoto_2001");
		this.DNADefaultOptions.put("azobenzeneMethod", "Asanuma_2005");
		this.DNADefaultOptions.put("lockedAcidMethod", "McTigue_2004");
		this.DNADefaultOptions.put("deoxyadenosineMethod", "Sugimoto_2005");
		
		this.RNADefaultOptions.put("NNMethod", "Xia_1998");
		this.RNADefaultOptions.put("singleMismatchMethod", "Znosko_2008");
		this.RNADefaultOptions.put("woddleBaseMethod", "Turner_1999");
		this.RNADefaultOptions.put("tandemMismatchMethod", "Turner_1999_2006");
		this.RNADefaultOptions.put("internalLoopMethod", "Turner_1999_2006");
		this.RNADefaultOptions.put("singleBulgeMethod", "Serra_2007");
		this.RNADefaultOptions.put("longBulgeMethod", "Turner_1999_2006");
		this.RNADefaultOptions.put("hairpinMethod", "Serra_1997_1998_2000_2006");
		this.RNADefaultOptions.put("CNGMethod", "Broda_2005");
		this.DNADefaultOptions.put("approximativeMode", "Wetmur_1981");
		this.DNADefaultOptions.put("correctionIon", "Tan_2007");
		this.DNADefaultOptions.put("inosineMethod", "Znosko_2007");
		this.DNADefaultOptions.put("NaEquivalentMethod", "Ahsen_2007");
		
		this.hybridDefaultOptions.put("NNMethod", "Sugimoto_1995");
		this.hybridDefaultOptions.put("ApproximativeMode", "Wetmur_1981");
		this.hybridDefaultOptions.put("correctionIon", "Wetmur_1981");
		
		this.mRNADefaultOptions.put("NNArticle", "Turner_2006");
	}
	
	public HashMap<String, String> getDNADefaultOptions() {
		return DNADefaultOptions;
	}
	
	public HashMap<String, String> getRNADefaultOptions() {
		return RNADefaultOptions;
	}
	
	public HashMap<String, String> getHybridDefaultOptions() {
		return hybridDefaultOptions;
	}
	
	public HashMap<String, String> getMRNADefaultOptions() {
		return mRNADefaultOptions;
	}
	
	private boolean isAValue(String optionValue){
		if (optionValue.charAt(0) != '-'){
			return true;
		}
		return false;
	}
	
	private boolean hasRequiredOptions(String [] args, HashMap<String, String> optionSet){
		int numberRequiredOptions = 0;
		boolean hasComplementarySequence = false;
		boolean containsInosineBase = false;
		
		for (int i = 0;i < args.length; i++){
			String option = args[i];
			String value = args[i+1];
			if (option.equals(hybridization)){
				if (isAValue(value)) {
					numberRequiredOptions ++;
					value = value.toLowerCase();
					optionSet.put(option, value);
				}
			}
			else if (option.equals(nucleotides)){
				if (isAValue(value)) {
					double val = Double.parseDouble(value);
					try {
						if (val > 0){
							numberRequiredOptions ++;
						}
						else {
							System.err.println("The nucleotide concentration must be positive.");
						}
					} catch (NumberFormatException e) {
						System.err.println("The nucleotide concentration must be a numeric value.");
					}
				}
			}
			else if (option.equals(sequence)){
				if (isAValue(value)) {
					value = value.toUpperCase();
					if (value.contains("I") == false){
						numberRequiredOptions ++;
						value = value.toUpperCase();
					}
					else {
						containsInosineBase = true;
					}
				}
			}
			else if (option.equals(complementarySequence)){
				if (isAValue(value)) {
					hasComplementarySequence = true;
					value = value.toUpperCase();
				}
			}
		}
		
		if (containsInosineBase && hasComplementarySequence == true){
			numberRequiredOptions ++;
		}
		
		if (hasComplementarySequence == false){
			String seq2 = Helper.getComplementarySequence(optionSet.get(sequence), optionSet.get(hybridization));
			optionSet.put(complementarySequence, seq2);
		}
		
		if (numberRequiredOptions == totalOptionsRequired){
			return true;
		}
		return false;
	}
	
	private HashMap<String, String> initializeDefaultOptions(String [] args){
		HashMap<String, String> optionSet = new HashMap<String, String> ();
		
		if (hasRequiredOptions(args, optionSet) == false){
			System.err.println("To compute, MELTING need at less the hybridization type " +
					"(option " + hybridization + "), the nucleic acid concentration (option " 
					+ nucleotides + ") and the sequence (option " + sequence + "). If there " +
					"are inosine bases in the sequence, a complementary sequence is required (option"
					+ complementarySequence + ").");
		}
		else {
			String hybrid = optionSet.get("hybridization");
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
				optionSet.put(hybridization, "mrnarna");
				optionSet.putAll(mRNADefaultOptions);
			}
		}
		optionSet.put("Na", "0");
		optionSet.put("Mg", "0");
		optionSet.put("K", "0");
		optionSet.put("Tris", "0");
		optionSet.put("dNTP", "0");
		
		optionSet.put(completMethod, "default");
		
		return optionSet;
	}
	
	public HashMap<String, String> collectOptions(String [] args){
		
		HashMap<String, String> optionSet = new HashMap<String, String>();
		optionSet = initializeDefaultOptions(args);
		
		for (int i = 0;i < args.length; i++){
			String option = args[i];
			String value = args[i+1];
			
			if (isAValue(option) == false){
				if (option.equals(versionNumber)){
					System.out.println("The current version is the java " + version);
					break;
				}
				else if (option.equals(dataPathway)){
					System.out.println("The set of calorimetric parameters are in " + NN_Path);
					break;
				}
				else if (option.equals(legalInformation)){
					break;
				}
				else if (option.equals(meltingHelp)){
					break;
				}
				else if (option.equals(threshold)){
					if (isAValue(value) && Integer.getInteger(args[i+1]) != null){
						thresholdValue = Integer.getInteger(args[i+1]);
					}
					else {
						System.err.println("I don't understand the option " + option + args[i+1]);
						break;
					}
				}
				else if (option.equals(NN_Path)){
					if (isAValue(value)){
						dataPathwayValue = NN_Path;
					}
					else {
						System.err.println("I don't understand the option " + option + args[i+1]);
						break;
					}
				}
				else if (option.equals(Na)){
					if (isAValue(value)){
						double val = Double.parseDouble(value);
						try {
							if (val >= 0){
								optionSet.put(Na, value);
							}
							else {
								System.err.println("The sodium concentration must be positive.");
							}
						} catch (NumberFormatException e) {
							System.err.println("The sodium concentration must be a numeric value.");
						}
					}
					else {
						System.err.println("I don't understand the option " + option + args[i+1]);
						break;
					}
				}
				else if (option.equals(Mg)){
					if (isAValue(value)){
						double val = Double.parseDouble(value);
						try {
							if (val >= 0){
								optionSet.put(Mg, value);
							}
							else {
								System.err.println("The magnesium concentration must be positive.");
							}
						} catch (NumberFormatException e) {
							System.err.println("The magnesium concentration must be a numeric value.");
						}
					}
					else {
						System.err.println("I don't understand the option " + option + args[i+1]);
						break;
					}
				}
				else if (option.equals(K)){
					if (isAValue(value)){
						double val = Double.parseDouble(value);
						try {
							if (val >= 0){
								optionSet.put(K, value);
							}
							else {
								System.err.println("The potassium concentration must be positive.");
							}
						} catch (NumberFormatException e) {
							System.err.println("The potassium concentration must be a numeric value.");
						}
					}
					else {
						System.err.println("I don't understand the option " + option + args[i+1]);
						break;
					}
				}
				else if (option.equals(Tris)){
					if (isAValue(value)){
						double val = Double.parseDouble(value);
						try {
							if (val >= 0){
								optionSet.put(Tris, value);
							}
							else {
								System.err.println("The Tris concentration must be positive.");
							}
						} catch (NumberFormatException e) {
							System.err.println("The Tris concentration must be a numeric value.");
						}
					}
					else {
						System.err.println("I don't understand the option " + option + args[i+1]);
						break;
					}
				}
				else if (option.equals(dNTP)){
					if (isAValue(value)){
						double val = Double.parseDouble(value);
						try {
							if (val >= 0){
								optionSet.put(dNTP, value);
							}
							else {
								System.err.println("The dNTP concentration must be positive.");
							}
						} catch (NumberFormatException e) {
							System.err.println("The dNTP concentration must be a numeric value.");
						}
					}
					else {
						System.err.println("I don't understand the option " + option + args[i+1]);
						break;
					}
				}
				
				else {
					if (option.equals(hybridization) == false) {
						if (isAValue(value)){
							optionSet.put(option, value);
						}
						else{
							System.err.println("One option is not correct");
						}
					}
				}
			}
			else {
				System.err.println("I don't understand the option " + option);
			}
		}
		return optionSet;
	}
	
	/*public CompletCalculMethod getCompletCalculMethod(HashMap<String, String> optionSet){
		
		String calculMethod = optionSet.get(completMethod);
		
		
		if (calculMethod.equals("default")){
			
			int thres = Integer.getInteger(optionSet.get(threshold));
			String seq = optionSet.get(sequence);
			String seq2 = optionSet.get(complementarySequence);
			int duplexLength = Math.min(seq.length(),seq2.length());
			
			if (duplexLength > thres){
			}
		}
	}*/
	
}
