package melting;

import java.util.HashMap;

import melting.configuration.OptionManagement;

public class Environment {

	private HashMap<String, Double> concentrations = new HashMap<String, Double>();
	private double nucleotides;
	private int factor;
	private boolean IsSelfComplementarity = false;
	private String Hybridization;
	private NucleotidSequences sequences;
	private ThermoResult result;
	private HashMap<String, String> options = new HashMap<String, String>();
	
	public Environment(HashMap<String, String> options){
		this.options = options;
		initializeConcentrations();
		if (isRequiredConcentrations() == false){
			System.err.println("ERROR : you must enter at lest one of these concentrations : Na, Mg, K or Tris.");
		}
		this.nucleotides = Double.parseDouble(options.get(OptionManagement.nucleotides));
		this.Hybridization = options.get(OptionManagement.hybridization);
		this.factor = Integer.getInteger(options.get(OptionManagement.factor));
		
		if (options.containsKey(OptionManagement.selfComplementarity)){
			this.IsSelfComplementarity = true;
		}
		
		this.sequences = new NucleotidSequences(options.get(OptionManagement.sequence), options.get(OptionManagement.complementarySequence));
		this.result = new ThermoResult(0,0,0);
	}

	public int getFactor() {
		return factor;
	}

	public ThermoResult getResult() {
		return result;
	}

	public NucleotidSequences getSequences() {
		return sequences;
	}

	public double getNucleotides() {
		return nucleotides;
	}
	
	public boolean isSelfComplementarity() {
		return IsSelfComplementarity;
	}
	
	public String getHybridization() {
		return Hybridization;
	}
	
	public HashMap<String, String> getOptions() {
		return options;
	}
	
	public double getNa() {
		if (concentrations.containsKey("Na")){
			return concentrations.get("Na");
		}
		return 0;
	}
	
	public double getMg() {
		if (concentrations.containsKey("Mg")){
			return concentrations.get("Mg");
		}
		return 0;
	}
	
	public double getTris() {
		if (concentrations.containsKey("Tris")){
			return concentrations.get("Tris");
		}
		return 0;
	}
	
	public double getK() {
		if (concentrations.containsKey("K")){
			return concentrations.get("K");
		}
		return 0;
	}
	
	public double getDMSO() {
		if (concentrations.containsKey("DMSO")){
			return concentrations.get("DMSO");
		}
		return 0;
	}
	
	public double getFormamide() {
		if (concentrations.containsKey("formamide")){
			return concentrations.get("formamide");
		}
		return 0;
	}
	
	public void setNa(double na) {
		concentrations.put("Na", na);
	}
	
	public void setResult(double enthalpy, double entropy){
		this.result.setEnthalpy(this.result.getEnthalpy() + enthalpy);
		this.result.setEntropy(this.result.getEntropy() + entropy);
	}
	
	public void setResult(double temperature){
		this.result.setTm(temperature);
	}
	
	public void setResult(ThermoResult result){
		this.result=result;
	}

	public double getDNTP() {
		if (concentrations.containsKey("dNTP")){
			return concentrations.get("dNTP");
		}
		return 0;
	}
	
	private void initializeConcentrations(){
		String [] solution = this.options.get(OptionManagement.solutioncomposition).split(":");
		
		for (int i = 0; i < solution.length; i++){
			String [] couple = solution[i].split("=");
			this.concentrations.put(couple[0], Double.parseDouble(couple[1]));
		}
	}
	
	private boolean isRequiredConcentrations(){
		if (concentrations.containsKey("Na") || concentrations.containsKey("K") || concentrations.containsKey("Mg") || concentrations.containsKey("Tris")){
			if (concentrations.get("Na") != 0 || concentrations.get("K") != 0 || concentrations.get("Mg") != 0 || concentrations.get("Tris") != 0){
				return true;
			}
		}
		return false;
	}
	
}
