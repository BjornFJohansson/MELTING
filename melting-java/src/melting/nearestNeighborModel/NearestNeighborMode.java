package melting.nearestNeighborModel;

import java.util.HashMap;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.configuration.OptionManagement;

public class NearestNeighborMode implements CompletCalculMethod{

	private Environment environment;
	
	public ThermoResult CalculateThermodynamics() {
		return null;
	}

	public boolean isApplicable() {
		boolean isApplicable = true;
		
		if (Integer.getInteger(this.environment.getOptions().get(OptionManagement.threshold)) >= this.environment.getSequences().getDuplexLength()){
			System.out.println("WARNING : the Nearest Neighbor model is accurate for " +
			"shorter sequences. (length superior to 6 and inferior to" +
			 this.environment.getOptions().get(OptionManagement.threshold) +")");
			
			if (this.environment.getOptions().get(OptionManagement.completMethod).equals("default") == false){
				isApplicable = false;
			}
		}
		
		if (this.environment.getOptions().containsKey(OptionManagement.selfComplementarity) && Integer.getInteger(this.environment.getOptions().get(OptionManagement.factor)) != 1){
			System.out.println("ERROR : When the oligonucleotides are self-complementary, the correction factor F must be equal to 1.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public void setUpVariable(HashMap<String, String> options) {
		this.environment = new Environment(options);
		
		}
	
	private void analyzeSequence(){
		
		for (int i = 0; i < duplexLength; i++){
			
		}
	}
}
