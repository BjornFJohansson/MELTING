package melting.nearestNeighborModel;

import java.util.HashMap;

import melting.ThermoResult;
import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.configuration.OptionManagement;

public class NearestNeighborMode implements CompletCalculMethod{

	private double Na;
	private double Mg;
	private double K;
	private double Tris;
	private double dNTP;
	private double Tm;
	private String hybridization;
	private String seq;
	private String seq2;
	private int duplexLength;
	private HashMap<String, String> optionSet;
	
	public ThermoResult CalculateThermodynamics() {
		return null;
	}

	public boolean isApplicable() {
		return false;
	}

	public void setUpVariable(HashMap<String, String> options) {
			this.Na = Double.parseDouble(options.get(OptionManagement.Na));
			this.Mg = Double.parseDouble(options.get(OptionManagement.Mg));
			this.K = Double.parseDouble(options.get(OptionManagement.K));
			this.Tris = Double.parseDouble(options.get(OptionManagement.Tris));
			this.dNTP = Double.parseDouble(options.get(OptionManagement.dNTP));
			
			this.Tm = 0;
			this.seq = options.get(OptionManagement.sequence);
			this.seq2 = options.get(OptionManagement.complementarySequence);
			this.duplexLength = Math.min(seq.length(), seq2.length());
			this.hybridization = options.get(OptionManagement.hybridization);
			
			this.optionSet = options;
		}
	
	private void analyzeSequence(){
		
		for (int i = 0; i < duplexLength; i++){
			
		}
	}
}
