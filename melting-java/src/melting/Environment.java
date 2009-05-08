package melting;

import java.util.HashMap;

import melting.configuration.OptionManagement;

public class Environment {

	private double Na;
	private double Mg;
	private double Tris;
	private double K;
	private double dNTP;
	private double nucleotides;
	private boolean IsSelfComplementarity = false;
	private String Hybridization;
	private NucleotidSequences sequences;
	private ThermoResult result;
	private HashMap<String, String> options = new HashMap<String, String>();
	
	public Environment(HashMap<String, String> options){
		this.options = options;
		
		this.Na = Double.parseDouble(options.get(OptionManagement.Na));
		this.Mg = Double.parseDouble(options.get(OptionManagement.Mg));
		this.Tris = Double.parseDouble(options.get(OptionManagement.Tris));
		this.K = Double.parseDouble(options.get(OptionManagement.K));
		this.dNTP = Double.parseDouble(options.get(OptionManagement.dNTP));
		this.nucleotides = Double.parseDouble(options.get(OptionManagement.nucleotides));
		this.Hybridization = options.get(OptionManagement.hybridization);
		
		if (options.containsKey(OptionManagement.selfComplementarity)){
			this.IsSelfComplementarity = true;
		}
		
		this.sequences = new NucleotidSequences(options.get(OptionManagement.sequence), options.get(OptionManagement.complementarySequence));
		this.result = new ThermoResult(0,0,0);
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
	
}
