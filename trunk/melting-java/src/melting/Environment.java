package melting;

import java.util.HashMap;

import melting.configuration.OptionManagement;
import melting.exceptions.OptionSyntaxError;

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

		if (options == null){
			throw new OptionSyntaxError("Some required options are missing. Read the manual for further informations or see the option " + OptionManagement.meltingHelp);
		}
				
		initializeConcentrations();
		
		if (isRequiredConcentrations() == false){
			throw new OptionSyntaxError("You must enter at lest one of these concentrations : Na, Mg, K or Tris.");
		}
		
		this.nucleotides = Double.parseDouble(options.get(OptionManagement.nucleotides));
		this.Hybridization = options.get(OptionManagement.hybridization).toLowerCase();

		if (this.Hybridization.equals("rnamrna")){
			this.Hybridization = "mrnarna";
		}
		
		this.factor = Integer.parseInt(options.get(OptionManagement.factor));

		if (options.containsKey(OptionManagement.selfComplementarity)){
			this.IsSelfComplementarity = true;
		}

		sortSquences(this.Hybridization, options.get(OptionManagement.sequence).toUpperCase(), options.get(OptionManagement.complementarySequence).toUpperCase());
		
		this.result = new ThermoResult(0,0,0);
		
	}
	
	public void sortSquences(String hybridization, String firstSequence, String secondSequence){
		
		if (hybridization.equals("rnadna")){
			this.sequences = new NucleotidSequences(secondSequence, secondSequence);
		}
		else {
			this.sequences = new NucleotidSequences(firstSequence, secondSequence);
		}
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
	
	public HashMap<String, Double> getConcentrations(){
		return this.concentrations;
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
		double Na = 0;
		double Mg = 0;
		double K = 0;
		double Tris = 0;
		
		if (concentrations.containsKey("Na")){
			Na = concentrations.get("Na");
		}
		if (concentrations.containsKey("Mg")){
			Mg = concentrations.get("Mg");
		}
		if (concentrations.containsKey("K")){
			K = concentrations.get("K");
		}
		if (concentrations.containsKey("Tris")){
			Tris = concentrations.get("Tris");
		}
		if (Na > 0 || K > 0 || Mg > 0 || Tris > 0){
			return true;
		}
		return false;
	}
	
	public static Environment modifieSequences(Environment environment, String sequence, String complementary){
		environment.getSequences().setSequence(sequence);
		environment.getSequences().setComplementary(complementary);
		
		return environment;
	}
	
}
