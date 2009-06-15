package melting;

import java.util.HashMap;

public class DataCollect {
	
	private HashMap<String, Thermodynamics> data = new HashMap<String, Thermodynamics>();
	
	public HashMap<String, Thermodynamics> getData() {
		return data;
	}

	public void setData(HashMap<String, Thermodynamics> parameters) {
		this.data.putAll(parameters);
	}
	
	public void setDatas(HashMap<String, Thermodynamics> datas) {
		this.data = datas;
	}
	
	private String getSymetricSequencePairs(String seq1, String seq2){
		
		String newSeq1 = NucleotidSequences.getInversedSequence(seq1);
		String newSeq2 = NucleotidSequences.getInversedSequence(seq2);
		return newSeq2+"/"+ newSeq1;
	}
	
	public Thermodynamics getTerminal(String type){
		Thermodynamics s = data.get("terminal" + type);
		return s;
	}
	
	public Thermodynamics getNNvalue(String seq1, String seq2){
		Thermodynamics s = data.get("neighbor"+seq1+"/"+seq2);
		if (s == null){
			s = data.get("neighbor"+getSymetricSequencePairs(seq1, seq2));
		}
		return s;
	}
	
	
	public Thermodynamics getInitiation(){
		Thermodynamics s = data.get("initiation");
		return s;
	}
	
	public Thermodynamics getInitiation(String type){
		Thermodynamics s = data.get("initiation" + type);
		return s;
	}
	
	public Thermodynamics getsymmetry(){
		Thermodynamics s = data.get("symmetry");
		return s;
	}
	
	public Thermodynamics getModifiedvalue(String seq1, String seq2){
		Thermodynamics s = data.get("modified"+seq1+"/"+seq2);
		if (s == null){
			s = data.get("modified"+getSymetricSequencePairs(seq1, seq2));
		}
		return s;
	}
	
	public Thermodynamics getModifiedvalue(String seq1, String seq2, String sens){
	
		Thermodynamics s = data.get("modified"+seq1+"/"+seq2+"sens"+sens);;
		if (s == null){
			s = data.get("modified"+getSymetricSequencePairs(seq1, seq2)+"sens"+sens);
		}
		return s;
	}
	
	public Thermodynamics getAzobenzeneValue(String seq1, String seq2){
		
		String typeBase = "";
		NucleotidSequences sequences = NucleotidSequences.decodeSequences(seq1, seq2);
	
		if (seq1.contains("_T") || seq2.contains("_T")){
			typeBase = "trans";
		}
		else if (seq1.contains("_C") || seq2.contains("_C")){
			typeBase = "cys";
		}
		
		Thermodynamics s = data.get("modified"+ "type" + typeBase + sequences.getSequence()+"/"+sequences.getComplementary());
		if (s == null){
			s = data.get("modified"+"type" + typeBase + getSymetricSequencePairs(sequences.getSequence(), sequences.getComplementary()));
		}
		return s;
	}
	
	public Thermodynamics getLockedAcidValue(String seq1, String seq2){
		NucleotidSequences sequences = NucleotidSequences.decodeSequences(seq1, seq2);
		
		Thermodynamics s = getModifiedvalue(sequences.getSequence(), sequences.getComplementary());
		return s;
	}
	
	public Thermodynamics getDeoxyadenosineValue(String seq1, String seq2){
		String sequence = seq1;
		String complementary = seq2;
		
		if ((sequence.contains("-A") || sequence.contains("-X")) && (complementary.contains("-A") == false && complementary.contains("-X") == false)){
			complementary = complementary.replace("-", "");
		}
		if ((complementary.contains("-A") || complementary.contains("-X")) && (sequence.contains("-A") == false && sequence.contains("-X") == false)){
			sequence = sequence.replace("-", "");
		}
		
		Thermodynamics s = getModifiedvalue(sequence, complementary);
		return s;
	}
	
	public Thermodynamics getHydroxyadenosineValue(String seq1, String seq2){
		NucleotidSequences sequences = NucleotidSequences.decodeSequences(seq1, seq2);
		String sens = NucleotidSequences.getSens(seq1, seq2);
		
		if (sens == null){
			Thermodynamics s = getModifiedvalue(sequences.getSequence(), sequences.getComplementary());
			return s;
		}
		else {
			Thermodynamics s = getModifiedvalue(sequences.getSequence(), sequences.getComplementary(), sens);
			return s;
		}
	}
	
	public Thermodynamics getDanglingValue(String seq1, String seq2){
		String sens = NucleotidSequences.getSens(seq1, seq2);
		seq1 = seq1.replaceAll("-", "");
		seq2 = seq2.replaceAll("-", "");
		Thermodynamics s = data.get("dangling"+seq1+"/"+seq2+"sens"+sens);

		if (s == null){
			s = data.get("dangling"+getSymetricSequencePairs(seq1, seq2)+"sens"+sens);
		}
		return s;
	}
	
	public Thermodynamics getMismatchvalue(String seq1, String seq2){
		Thermodynamics s = data.get("mismatch"+seq1+"/"+seq2);
		if (s == null){
			s = data.get("mismatch"+getSymetricSequencePairs(seq1, seq2));
		}
		return s;
	}
	
	public Thermodynamics getInternalLoopValue(String size){
		Thermodynamics s = data.get("mismatch"+"size"+size);
		return s;
	}
	
	public Thermodynamics getInitiationLoopValue(String size){
		Thermodynamics s = data.get("mismatch"+"initiation"+"size"+size);
		return s;
	}
	
	public Thermodynamics getInitiationLoopValue(){
		Thermodynamics s = data.get("mismatch"+"initiation");
		return s;
	}
	
	public Thermodynamics getMismatchParameterValue(String base1, String base2){
		Thermodynamics s = data.get("parameters"+base1+"/"+base2);
		if (s == null){
			s = data.get("parameters"+getSymetricSequencePairs(base1, base2));
		}
		return s;
	}
	
	public Thermodynamics getClosureValue(String base1, String base2){
		Thermodynamics s = data.get("closure"+"per_"+base1 + "/" + base2);
		return s;
	}
	
	public Thermodynamics getMismatchValue(String seq1, String seq2, String closing){
		Thermodynamics s = data.get("mismatch" + seq1+"/"+seq2 + "close" + closing);
		if (s == null){
			s = data.get("mismatch"+getSymetricSequencePairs(seq1, seq2) + "close" + closing);
		}
		return s;
	}
	
	public Thermodynamics getPenalty(String type){
		Thermodynamics s = data.get("penalty" + type);
		return s;
	}
	
	public Thermodynamics getAsymmetry(){
		Thermodynamics s = data.get("asymetry");
		return s;
	}
	
	public Thermodynamics getFirstMismatch(String seq1, String seq2, String loop){
		Thermodynamics s = data.get("mismatch"+"first_non_canonical_pair"+"loop"+loop+seq1+"/"+seq2);
		if (s == null){
			s = data.get("mismatch"+"first_non_canonical_pair"+"loop"+loop+getSymetricSequencePairs(seq1, seq2));
		}
		return s;
	}
	
	public Thermodynamics getHairpinLoopvalue(String size){
		Thermodynamics s = data.get("hairpin"+size);
		return s;
	}
	
	public Thermodynamics getBonus(String base1, String lastBase, String seqLoop, String type){
		Thermodynamics s = data.get("bonus"+type+base1+seqLoop+"/"+lastBase);
		if (s == null){
			s = data.get("bonus"+type+lastBase+NucleotidSequences.getInversedSequence(seqLoop)+"/"+base1);
		}
		return s;
	}
	
	public Thermodynamics getPenalty(String type, String parameter){
		Thermodynamics s = data.get("penalty"+type+"parameter"+parameter);
		return s;
	}
	
	public Thermodynamics getTerminalMismatchvalue(String seq1, String seq2){
		Thermodynamics s = data.get("hairpin"+"terminal_mismatch"+seq1+"/"+seq2);
		if (s == null){
			s = data.get("hairpin"+"terminal_mismatch"+getSymetricSequencePairs(seq1, seq2));
		}
		return s;
	}
	
	public Thermodynamics getCNGvalue(String repeats, String seq1, String seq2){
		Thermodynamics s = data.get("CNG"+"repeats"+repeats+seq1+"/"+seq2);
		return s;
	}
	
	public Thermodynamics getSingleBulgeLoopvalue(String seq1, String seq2){
		String sequence1 =seq1.replace("-", "");
		String sequence2 =seq2.replace("-", "");

		Thermodynamics s = data.get("bulge"+sequence1+"/"+sequence2);
		if (s == null){
			s = data.get("bulge"+getSymetricSequencePairs(sequence1, sequence2));
		}
		return s;
	}
	
	public Thermodynamics getBulgeLoopvalue(String size){
		Thermodynamics s = data.get("bulge"+"size"+size);
		return s;
	}
	
	public Thermodynamics getInitiationBulgevalue(String size){
		Thermodynamics s = data.get("bulge"+"initiation"+"size"+size);
		return s;
	}
}
