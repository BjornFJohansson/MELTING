package melting;

import java.util.HashMap;

import melting.configuration.OptionManagement;

public class Environment {

	private HashMap<String, Double> concentrations = new HashMap<String, Double>();
	private double Na;
	private double Mg;
	private double Tris;
	private double K;
	private double dNTP;
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
		return Na;
	}
	
	public double getMg() {
		return Mg;
	}
	
	public double getTris() {
		return Tris;
	}
	
	public double getK() {
		return K;
	}
	
	public void setNa(double na) {
		Na = na;
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
		return dNTP;
	}
	
	private void initializeConcentrations(){
		String [] solution = this.options.get(OptionManagement.solutioncomposition).split(":");
		
		for (int i = 0; i < solution.length; i++){
			String [] couple = solution[i].split("=");
			this.concentrations.put(couple[0], Double.parseDouble(couple[1]));
		}
	}
	
}
