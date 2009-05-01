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
		String seq = optionSet.get(OptionManagement.sequence);
		String seq2 = optionSet.get(OptionManagement.complementarySequence);
		int duplexLength = Math.min(seq.length(), seq2.length());
		boolean isApplicable = true;
		
		if (Integer.getInteger(optionSet.get(OptionManagement.threshold)) >= duplexLength){
			System.out.println("WARNING : the Nearest Neighbor model is accurate for " +
			"shorter sequences. (length superior to 6 and inferior to" +
			 optionSet.get(OptionManagement.threshold) +")");
			
			if (this.optionSet.get(OptionManagement.completMethod).equals("default") == false){
				isApplicable = false;
			}
		}
		
		if (optionSet.containsKey(OptionManagement.selfComplementarity) && Integer.getInteger(optionSet.get(OptionManagement.factor)) != 1){
			System.out.println("ERROR : When the oligonucleotides are self-complementary, the correction factor F must be equal to 1.");
			isApplicable = false;
		}
		
		return isApplicable;
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
