package melting;

import java.util.HashMap;

public class DataCollect {
	
	private HashMap<String, Thermodynamics> datas = new HashMap<String, Thermodynamics>();
	
	public HashMap<String, Thermodynamics> getDatas() {
		return datas;
	}

	public void setDatas(HashMap<String, Thermodynamics> datas) {
		this.datas = datas;
	}

	public DataCollect(HashMap<String, Thermodynamics> datas){
		this.datas = datas;
	}
	
	private String getSymetricSequence(String seq1, String seq2){
		String newSeq1 = Helper.getInversedSequence(seq1);
		String newSeq2 = Helper.getInversedSequence(seq2);
		return newSeq1+"/"+newSeq2;
	}
	
	public Thermodynamics getTerminal(String type){
		Thermodynamics s = datas.get("terminal" + type);
		return s;
	}
	
	public Thermodynamics getNNvalue(String seq1, String seq2){
		Thermodynamics s = datas.get("neighbor"+seq1+"/"+seq2);
		if (s == null){
			s = datas.get("neighbor"+getSymetricSequence(seq1, seq2));
		}
		return s;
	}
	
	public Thermodynamics getInitiation(){
		Thermodynamics s = datas.get("initiation");
		return s;
	}
	
	public Thermodynamics getInitiation(String type){
		Thermodynamics s = datas.get("initiation" + type);
		return s;
	}
	
	public Thermodynamics getSymetry(){
		Thermodynamics s = datas.get("symetry");
		return s;
	}
	
	public Thermodynamics getModifiedvalue(String seq1, String seq2){
		Thermodynamics s = datas.get("modified"+seq1+"/"+seq2);
		if (s == null){
			s = datas.get("modified"+getSymetricSequence(seq1, seq2));
		}
		return s;
	}
	
	public Thermodynamics getModifiedvalue(String seq1, String seq2, String sens){
	
		Thermodynamics s = datas.get("modified"+seq1+"/"+seq2+"sens"+sens);;
		if (s == null){
			s = datas.get("modified"+getSymetricSequence(seq1, seq2)+"sens"+sens);
		}
		return s;
	}
	
	public Thermodynamics getAzobenzeneValue(String seq1, String seq2){
		
		String sequence = seq1;
		String complementary = seq2;
		String typeBase = "";
		
		if (seq1.contains("X")){
			complementary = complementary.replaceAll("-","");
			
		}
		else {
			sequence = sequence.replace("-","");
		}
		
		if (sequence.contains("_T")){
			sequence = sequence.replace("_T", "");
			typeBase = "trans";
		}
		else if (sequence.contains("_C")){
			sequence = sequence.replace("_C", "");
			typeBase = "cys";
		}
		else if (complementary.contains("_T")){
			complementary = complementary.replace("_T", "");
			typeBase = "trans";
		}
		else if (complementary.contains("_C")){
			complementary = complementary.replace("_C", "");
			typeBase = "cys";
		}
		
		Thermodynamics s = datas.get("modified"+ "type" + typeBase + sequence+"/"+complementary);
		if (s == null){
			s = datas.get("modified"+"type" + typeBase + getSymetricSequence(sequence, complementary));
		}
		return s;
	}
	
	public Thermodynamics getLockedAcidValue(String seq1, String seq2){
		String sequence = seq1;
		String complementary = seq2;
		
		if (sequence.contains("L")){
			complementary.replace("-", "");
		}
		
		if (complementary.contains("L")){
			sequence.replace("-", "");
		}
		
		Thermodynamics s = getModifiedvalue(sequence, complementary);
		return s;
	}
	
	public Thermodynamics getDeoxyadenosineValue(String seq1, String seq2){
		String sequence = seq1;
		String complementary = seq2;
		
		if ((sequence.contains("-A") || sequence.contains("-X")) && (complementary.contains("-A") == false && complementary.contains("-X") == false)){
			complementary.replace("-", "");
		}
		if ((complementary.contains("-A") || complementary.contains("-X")) && (sequence.contains("-A") == false && sequence.contains("-X") == false)){
			sequence.replace("-", "");
		}
		
		Thermodynamics s = getModifiedvalue(sequence, complementary);
		return s;
	}
	
	public Thermodynamics getHydroxyadenosineValue(String seq1, String seq2){
		String sequence = seq1;
		String complementary = seq2;
		String sens = getSens(seq1, seq2);
		
		if (sequence.contains("A*")){
			complementary = complementary.replaceAll("-", "");
		}
		else {
			sequence = sequence.replaceAll("-", "");
		}
		
		if (sens == null){
			Thermodynamics s = getModifiedvalue(sequence, complementary);
			return s;
		}
		else {
			Thermodynamics s = getModifiedvalue(sequence, complementary, sens);
			return s;
		}
	}
	
	public Thermodynamics getDanglingValue(String seq1, String seq2){
		String sens = getSens(seq1, seq2);
		seq1.replaceAll("-", "");
		seq2.replaceAll("-", "");
		Thermodynamics s = datas.get("dangling"+seq1+"/"+seq2+"sens"+sens);
		if (s == null){
			s = datas.get("dangling"+getSymetricSequence(seq1, seq2)+"sens"+sens);
		}
		return s;
	}
	
	public Thermodynamics getMismatchvalue(String seq1, String seq2){
		Thermodynamics s = datas.get("mismatch"+seq1+"/"+seq2);
		if (s == null){
			s = datas.get("mismatch"+getSymetricSequence(seq1, seq2));
		}
		return s;
	}
	
	public Thermodynamics getInternalLoopValue(String size){
		Thermodynamics s = datas.get("mismatch"+"size"+size);
		return s;
	}
	
	public Thermodynamics getInitiationLoopValue(String size){
		Thermodynamics s = datas.get("mismatch"+"initiation"+"size"+size);
		return s;
	}
	
	public Thermodynamics getInitiationLoopValue(){
		Thermodynamics s = datas.get("mismatch"+"initiation");
		return s;
	}
	
	public Thermodynamics getMismatchParameterValue(String base1, String base2){
		Thermodynamics s = datas.get("parameters"+base1+"/"+base2);
		if (s == null){
			s = datas.get("parameters"+getSymetricSequence(base1, base2));
		}
		return s;
	}
	
	public Thermodynamics getClosureValue(String base1, String base2){
		Thermodynamics s = datas.get("closure"+"per_"+base1 + "/" + base2);
		return s;
	}
	
	public Thermodynamics getMismatchValue(String seq1, String seq2, String closing){
		Thermodynamics s = datas.get("mismatch" + seq1+"/"+seq2 + "close" + closing);
		if (s == null){
			s = datas.get("mismatch"+getSymetricSequence(seq1, seq2) + "close" + closing);
		}
		return s;
	}
	
	public Thermodynamics getPenalty(String type){
		Thermodynamics s = datas.get("penalty" + type);
		return s;
	}
	
	public Thermodynamics getAsymetry(){
		Thermodynamics s = datas.get("symetry");
		return s;
	}
	
	public Thermodynamics getFirstMismatch(String seq1, String seq2, String loop){
		Thermodynamics s = datas.get("mismatch"+"first_non_canonical_pair"+"loop"+loop+seq1+"/"+seq2);
		if (s == null){
			s = datas.get("mismatch"+"first_non_canonical_pair"+"loop"+loop+getSymetricSequence(seq1, seq2));
		}
		return s;
	}
	
	public Thermodynamics getHairpinLoopvalue(String size){
		Thermodynamics s = datas.get("hairpin"+size);
		return s;
	}
	
	public Thermodynamics getBonus(String base1, String lastBase, String seqLoop, String type){
		Thermodynamics s = datas.get("bonus"+type+base1+seqLoop+"/"+lastBase);
		if (s == null){
			s = datas.get("bonus"+type+lastBase+Helper.getInversedSequence(seqLoop)+"/"+base1);
		}
		return s;
	}
	
	public Thermodynamics getPenalty(String type, String parameter){
		Thermodynamics s = datas.get("penalty"+type+"parameter"+parameter);
		return s;
	}
	
	public Thermodynamics getTerminalMismatchvalue(String seq1, String seq2){
		Thermodynamics s = datas.get("hairpin"+"terminal_mismatch"+seq1+"/"+seq2);
		if (s == null){
			s = datas.get("hairpin"+"terminal_mismatch"+getSymetricSequence(seq1, seq2));
		}
		return s;
	}
	
	public Thermodynamics getCNGvalue(String repeats, String seq1, String seq2){
		Thermodynamics s = datas.get("CNG"+"repeats"+repeats+seq1+"/"+seq2);
		return s;
	}
	
	public Thermodynamics getSingleBulgeLoopvalue(String seq1, String seq2){
		Thermodynamics s = datas.get("bulge"+seq1+"/"+seq2.substring(0, 1)+seq2.substring(2,3));
		if (s == null){
			s = datas.get("bulge"+getSymetricSequence(seq1, seq2.substring(0, 1)+seq2.substring(2,3)));
		}
		return s;
	}
	
	public Thermodynamics getBulgeLoopvalue(String size){
		Thermodynamics s = datas.get("mismatch"+"size"+size);
		return s;
	}
	
	public Thermodynamics getInitiationBulgevalue(String size){
		Thermodynamics s = datas.get("mismatch"+"initiation"+"size"+size);
		return s;
	}
	
	private String getSens(String seq1, String seq2){
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

}
