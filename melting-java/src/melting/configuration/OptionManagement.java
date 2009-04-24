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
	public static final String threshold = "-thres";
	public static final String hybridization = "-Hybrid";
	public static final String approximativeMode = "-AMode";
	public static final String NNMode = "-NNmode";
	public static final String VersionNumber = "-Version";
	public static final String Version = "1";
	public static final String correctionNa = "-corrNa";
	public static final String correctionMg = "-corrMg";
	public static final String correctionMixte = "-corrMixte";
	public static final String NNMethod = "-NN";
	public static final String SingleMismatchMethod = "-singleMM";
	public static final String TandemMismatchMethod = "-tandemMM";
	public static final String InternalLoopMethod = "-internalLoop";
	public static final String singleDanglingEndMethod = "-singleDE";
	public static final String doubleDanglingEndMethod = "-doubleDE";
	public static final String longDanglingEndMethod = "-longDE";
	public static final String hairpinLoopMethod = "-hairpin";
	public static final String singleBulgeLoopMethod = "-singleBulge";
	public static final String longBulgeLoopMethod = "-longBulge";
	public static final String CNGMethod = "-CNG";
	public static final String modifiedNucleotidesMethod = "-MN method";
	
	private HashMap<String, String> DNAdefaultOptions = new HashMap<String, String>();
	private HashMap<String, String> RNAdefaultOptions = new HashMap<String, String>();
	private HashMap<String, String> hybriddefaultOptions = new HashMap<String, String>();
	private HashMap<String, String> mRNAdefaultOptions = new HashMap<String, String>();

	public OptionManagement(){
		this.DNAdefaultOptions.put("NNMethod", "Santalucia_2004");
		this.DNAdefaultOptions.put("SingleMismatchArticle", "Allawi_Santalucia_Peyret_1997_1998_1999");
		this.DNAdefaultOptions.put("TandemMismatchArticle", "Allawi_Santalucia_Peyret_1997_1998_1999");
		this.DNAdefaultOptions.put("InternalLoopArticle", "Santalucia_2004");
		this.DNAdefaultOptions.put("SingleBulgeArticle", "Tanaka_2004");
		this.DNAdefaultOptions.put("LongBulgeArticle", "Santalucia_2004");
		this.DNAdefaultOptions.put("HairpinArticle", "Santalucia_2004");
		this.DNAdefaultOptions.put("ApproximativeMethod", "Wetmur_1981");
		this.DNAdefaultOptions.put("SodiumCorrection", "Santalucia_1998");
		
		this.RNAdefaultOptions.put("NNMethod", "Xia_1998");
		this.RNAdefaultOptions.put("SingleMismatchArticle", "Znosko_2008");
		this.RNAdefaultOptions.put("WoddleBaseArticle", "Turner_1999");
		this.RNAdefaultOptions.put("TandemMismatchArticle", "Turner_1999_2006");
		this.RNAdefaultOptions.put("InternalLoopArticle", "Turner_1999_2006");
		this.RNAdefaultOptions.put("SingleBulgeArticle", "Serra_2007");
		this.RNAdefaultOptions.put("LongBulgeArticle", "Turner_1999_2006");
		this.RNAdefaultOptions.put("HairpinArticle", "Serra_1997_1998_2000_2006");
		this.DNAdefaultOptions.put("ApproximativeMethod", "Wetmur_1981");
		this.DNAdefaultOptions.put("SodiumCorrection", "Santalucia_1998");
		
		this.hybriddefaultOptions.put("NNMethod", "Sugimoto_1995");
		this.DNAdefaultOptions.put("ApproximativeMethod", "Wetmur_1981");
		this.DNAdefaultOptions.put("SodiumCorrection", "Santalucia_1998");
		
		this.mRNAdefaultOptions.put("NNArticle", "Turner_2006");
	}
	
	public HashMap<String, String> getDNAdefaultOptions() {
		return DNAdefaultOptions;
	}
	
	public HashMap<String, String> getRNAdefaultOptions() {
		return RNAdefaultOptions;
	}
	
	public HashMap<String, String> getHybriddefaultOptions() {
		return hybriddefaultOptions;
	}
	
	public HashMap<String, String> getMRNAdefaultOptions() {
		return mRNAdefaultOptions;
	}
	
	
}
