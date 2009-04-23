package melting.configuration;

import java.util.HashMap;

public class OptionManagement {

	private HashMap<String, String> options = new HashMap<String, String>();
	private HashMap<String, String> DNAdefaultOptions = new HashMap<String, String>();
	private HashMap<String, String> RNAdefaultOptions = new HashMap<String, String>();
	private HashMap<String, String> hybriddefaultOptions = new HashMap<String, String>();
	private HashMap<String, String> mRNAdefaultOptions = new HashMap<String, String>();

	public OptionManagement(){
		this.DNAdefaultOptions.put("NNArticle", "Santalucia_2004");
		this.DNAdefaultOptions.put("SingleMismatchArticle", "Allawi_Santalucia_Peyret_1997_1998_1999");
		this.DNAdefaultOptions.put("TandemMismatchArticle", "Allawi_Santalucia_Peyret_1997_1998_1999");
		this.DNAdefaultOptions.put("InternalLoopArticle", "Santalucia_2004");
		this.DNAdefaultOptions.put("SingleBulgeArticle", "Tanaka_2004");
		this.DNAdefaultOptions.put("LongBulgeArticle", "Santalucia_2004");
		this.DNAdefaultOptions.put("HairpinArticle", "Santalucia_2004");
		this.DNAdefaultOptions.put("ApproximativeMethod", "Wetmur_1981");
		this.DNAdefaultOptions.put("SodiumCorrection", "Santalucia_1998");
		
		this.RNAdefaultOptions.put("NNArticle", "Xia_1998");
		this.RNAdefaultOptions.put("SingleMismatchArticle", "Znosko_2008");
		this.RNAdefaultOptions.put("WoddleBaseArticle", "Turner_1999");
		this.RNAdefaultOptions.put("TandemMismatchArticle", "Turner_1999_2006");
		this.RNAdefaultOptions.put("InternalLoopArticle", "Turner_1999_2006");
		this.RNAdefaultOptions.put("SingleBulgeArticle", "Serra_2007");
		this.RNAdefaultOptions.put("LongBulgeArticle", "Turner_1999_2006");
		this.RNAdefaultOptions.put("HairpinArticle", "Serra_1997_1998_2000_2006");
		this.DNAdefaultOptions.put("ApproximativeMethod", "Wetmur_1981");
		this.DNAdefaultOptions.put("SodiumCorrection", "Santalucia_1998");
		
		this.hybriddefaultOptions.put("NNArticle", "Sugimoto_1995");
		this.DNAdefaultOptions.put("ApproximativeMethod", "Wetmur_1981");
		this.DNAdefaultOptions.put("SodiumCorrection", "Santalucia_1998");
		
		this.mRNAdefaultOptions.put("NNArticle", "Turner_2006");
	}
	
	public HashMap<String, String> getOptions() {
		return options;
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
