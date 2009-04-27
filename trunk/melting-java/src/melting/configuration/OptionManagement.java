package melting.configuration;

import java.util.HashMap;

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
	public static final String correctionNa = "-corrNa";
	public static final String correctionMg = "-corrMg";
	public static final String correctionMixte = "-corrMixte";
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
	
	private static final String version = "1";
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
		this.DNADefaultOptions.put("correctionNa", "Santalucia_1998");
		this.DNADefaultOptions.put("correctionMg", "Owczarzy_2008");
		this.DNADefaultOptions.put("correctionMixte", "Owczarzy_2008");
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
		this.DNADefaultOptions.put("correctionNa", "Tan_2007");
		this.DNADefaultOptions.put("correctionMg", "Tan_2007");
		this.DNADefaultOptions.put("correctionMg", "Tan_2007");
		this.DNADefaultOptions.put("inosineMethod", "Znosko_2007");
		
		this.hybridDefaultOptions.put("NNMethod", "Sugimoto_1995");
		this.hybridDefaultOptions.put("ApproximativeMode", "Wetmur_1981");
		this.hybridDefaultOptions.put("correctionNa", "Wetmur_1981");
		
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
	
	private String getHybridization(String [] args){
		for (int i = 0;i < args.length; i++){
			if (args[i].equals(hybridization)){
				if (isAValue(hybridization)) {
					return args[i+1];
				}
			}
		}
		return null;
	}
	
	private HashMap<String, String> initializeOptions(String hybridization){
		hybridization = hybridization.toLowerCase();
		HashMap<String, String> optionSet = new HashMap<String, String> ();
		
		if (hybridization == null){
			System.out.println("the hybridization type is required");
			System.exit(1);
		}
		else {
			optionSet.put("hybridization", hybridization);
			if (hybridization.equals("dnadna")) {
				optionSet.putAll(DNADefaultOptions);
			}
			else if (hybridization.equals("rnarna")) {
				optionSet.putAll(RNADefaultOptions);
			}
			else if (hybridization.equals("rnadna") || hybridization.equals("dnarna")) {
				optionSet.putAll(hybridDefaultOptions);
			}
			else if (hybridization.equals("mrnarna") || hybridization.equals("rnamrna")) {
				optionSet.putAll(mRNADefaultOptions);
			}
		}
		return optionSet;
	}
	
	public HashMap<String, String> collectOptions(String [] args){
		HashMap<String, String> optionSet = new HashMap<String, String>();
		
		initializeOptions(getHybridization(args));
		
		for (int i = 0;i < args.length; i++){
			String option = args[i];
			if (isAValue(option) == false){
				if (option.equals(versionNumber)){
					System.out.println("The current version is the java " + version);
					System.exit(0);
					break;
				}
				else if (option.equals(dataPathway)){
					System.out.println("The set of calorimetric parameters are in " + NN_Path);
					System.exit(0);
					break;
				}
				else if (option.equals(legalInformation)){
					System.exit(0);
					break;
				}
				else if (option.equals(meltingHelp)){
					System.exit(0);
					break;
				}
				
				else if (option.equals(verboseMode)){
				}
				else if (option.equals(threshold)){
					if (isAValue(args[i+1]) && Integer.getInteger(args[i+1]) != null){
						this.thresholdValue = Integer.getInteger(args[i+1]);
					}
					else {
						System.err.println("I don't understand the option " + option + args[i+1]);
						System.exit(1);
						break;
					}
				}
				else if (option.equals(NN_Path)){
					this.dataPathwayValue = NN_Path;
				}
				else if (option.equals(outPutFile)){
				}
				else if (option.equals(inPutFile)){
				}
				
				else if (option.equals(sequence)){
					if (isAValue(args[i+1])){
						optionSet.put("sequence", args[i+1]);
					}
					else {
						System.err.println("I don't understand the option " + option + args[i+1]);
						System.exit(1);
						break;
					}
				}
				else if (option.equals(complementarySequence)){
					if (isAValue(args[i+1])){
						optionSet.put("complementarySequence", args[i+1]);
					}
					else {
						System.err.println("I don't understand the option " + option + args[i+1]);
						System.exit(1);
						break;
					}
				}
				else if (option.equals(Na)){
					if (isAValue(args[i+1])){
						optionSet.put("Na", args[i+1]);
					}
					else {
						System.err.println("I don't understand the option " + option + args[i+1]);
						System.exit(1);
						break;
					}
				}
					else if (option.equals(Mg)){
						if (isAValue(args[i+1])){
							optionSet.put("Mg", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
				}
					else if (option.equals(K)){
						if (isAValue(args[i+1])){
							optionSet.put("K", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(Tris)){
						if (isAValue(args[i+1])){
							optionSet.put("Tris", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(dNTP)){
						if (isAValue(args[i+1])){
							optionSet.put("dNTP", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(nucleotides)){
						if (isAValue(args[i+1])){
							optionSet.put("nucleotides", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(completMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("completMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(approximativeMode)){
						if (isAValue(args[i+1])){
							optionSet.put("approximativeMode", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(correctionNa)){
						if (isAValue(args[i+1])){
							optionSet.put("correctionNa", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(correctionMg)){
						if (isAValue(args[i+1])){
							optionSet.put("correctionMg", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(correctionMixte)){
						if (isAValue(args[i+1])){
							optionSet.put("correctionMixte", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(NNMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("NNMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(singleMismatchMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("singleMismatchMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(tandemMismatchMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("tandemMismatchMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(internalLoopMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("internalLoopMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(hairpinLoopMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("hairpinLoopMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(singleBulgeLoopMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("singleBulgeLoopMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(longBulgeLoopMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("longBulgeLoopMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(CNGMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("CNGMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(longBulgeLoopMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("longBulgeLoopMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(inosineMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("inosineMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(hydroxyadenineMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("hydroxyadenineMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(azobenzeneMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("azobenzeneMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(lockedAcidMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("lockedAcidMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
					else if (option.equals(deoxyadenosineMethod)){
						if (isAValue(args[i+1])){
							optionSet.put("deoxyadenosineMethod", args[i+1]);
						}
						else {
							System.err.println("I don't understand the option " + option + args[i+1]);
							System.exit(1);
							break;
						}
					}
			}
		}
		return optionSet;
	}
	
}
