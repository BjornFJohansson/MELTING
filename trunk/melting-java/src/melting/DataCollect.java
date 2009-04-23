package melting;

import java.util.HashMap;

public class DataCollect {
	
	protected HashMap<String, Thermodynamics> datas = new HashMap<String, Thermodynamics>();
	
	public DataCollect(HashMap<String, Thermodynamics> datas){
		this.datas = datas;
	}
	
	public Thermodynamics getTerminal(String type){
		Thermodynamics s = datas.get("terminal" + type);
		return s;
	}
	
	public Thermodynamics getNNvalue(String sequence){
		Thermodynamics s = datas.get("neighbor"+sequence);
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
	
	public Thermodynamics getModifiedvalue(String sequence){
		Thermodynamics s = datas.get("modified"+sequence);
		return s;
	}
	
	public Thermodynamics getModifiedvalue(String sequence, String sens){
		Thermodynamics s = datas.get("modified"+sequence+"sens"+sens);
		return s;
	}
	
	public Thermodynamics getModifiedvalue(String sequence, String sens, String type){
		Thermodynamics s = datas.get("modified"+type+sequence+"sens"+sens);
		return s;
	}
	
	public Thermodynamics getDanglingValue(String sequence, String sens){
		Thermodynamics s = datas.get("dangling"+sequence+"sens"+sens);
		return s;
	}
	
	public Thermodynamics getmismatchvalue(String sequence){
		Thermodynamics s = datas.get("mismatch"+sequence);
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
	
	public Thermodynamics getMismatchParameterValue(String base){
		Thermodynamics s = datas.get("parameters"+base);
		return s;
	}
	
	public Thermodynamics getClosureValue(String base){
		Thermodynamics s = datas.get("closure"+"per_"+base);
		return s;
	}
	
	public Thermodynamics getmismatchValue(String sequence, String closing){
		Thermodynamics s = datas.get("mismatch" + sequence + "close" + closing);
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
	
	public Thermodynamics getFirstMismatch(String sequence, String loop){
		Thermodynamics s = datas.get("mismatch"+"first_non_canonical_pair"+"loop"+loop+sequence);
		return s;
	}
	
	public Thermodynamics getHairpinLoopvalue(String size){
		Thermodynamics s = datas.get("hairpin"+size);
		return s;
	}
	
	public Thermodynamics getBonus(String sequence, String type){
		Thermodynamics s = datas.get("bonus"+type+sequence);
		return s;
	}
	
	public Thermodynamics getPenalty(String type, String parameter){
		Thermodynamics s = datas.get("penalty"+type+"parameter"+parameter);
		return s;
	}
	
	public Thermodynamics getTerminalMismatchvalue(String sequence){
		Thermodynamics s = datas.get("hairpin"+"terminal_mismatch"+sequence);
		return s;
	}
	
	public Thermodynamics getCNGvalue(String repeats, String sequence){
		Thermodynamics s = datas.get("CNG"+"repeats"+repeats+sequence);
		return s;
	}
	
	public Thermodynamics getSingleBulgeLoopvalue(String sequence){
		Thermodynamics s = datas.get("bulge"+sequence);
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
