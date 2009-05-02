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
	
	private String getInversedSequence(String sequence){
		String newSequence = "";
		for (int i = 0; i < sequence.length(); i++){
			newSequence += sequence.charAt(sequence.length() - i); 
		}
		return newSequence;
	}
	
	private String getSymetricSequence(String seq1, String seq2){
		String newSeq1 = getInversedSequence(seq1);
		String newSeq2 = getInversedSequence(seq2);
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
	
	public Thermodynamics getModifiedvalue(String seq1, String seq2, String sens, String type){
		Thermodynamics s = datas.get("modified"+type+seq1+"/"+seq2+"sens"+sens);
		if (s == null){
			s = datas.get("modified"+type+getSymetricSequence(seq1, seq2)+"sens"+sens);
		}
		return s;
	}
	
	public Thermodynamics getDanglingValue(String seq1, String seq2, String sens){
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
	
	public Thermodynamics asymetry(){
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
			s = datas.get("bonus"+type+lastBase+getInversedSequence(seqLoop)+"/"+base1);
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
		Thermodynamics s = datas.get("bulge"+seq1+"/"+seq2);
		if (s == null){
			s = datas.get("bulge"+getSymetricSequence(seq1, seq2));
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

}
