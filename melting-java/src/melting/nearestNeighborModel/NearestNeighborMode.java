package melting.nearestNeighborModel;

import java.util.ArrayList;
import java.util.HashMap;

import melting.Environment;
import melting.Helper;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;

public class NearestNeighborMode implements CompletCalculMethod{

	private Environment environment;
	private ArrayList<PartialCalculMethod>
	
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
	
	private int [] getPositionsMotif(NucleotidSequences sequences, int pos1){
		if (Helper.isComplementaryBasePair(sequences.getSequence().charAt(pos1), sequences.getComplementary().charAt(pos1))){
			int position = pos1;
			while (Helper.isComplementaryBasePair(sequences.getSequence().charAt(position + 1), sequences.getComplementary().charAt(position + 1))){
				position ++;
			}

			int [] positions = {pos1, position};
			return positions;
		}
		
		else {
			int position = pos1;
			while (Helper.isComplementaryBasePair(sequences.getSequence().charAt(position + 1), sequences.getComplementary().charAt(position + 1)) == false){
				position ++;
			}
			if (pos1 == 0 && position == sequences.getDuplexLength()){
				int [] positions = {pos1, position};
				return positions;
			}
			if (position == sequences.getDuplexLength() - 1){
				int [] positions = {pos1 - 1, position};
				return positions;
			}
			if (pos1 == 0){
				int [] positions = {pos1, position + 1};
				return positions;
			}
			int [] positions = {pos1 - 1, position + 1};
			return positions;
		}
	}
	
	private PartialCalculMethod getMethod(NucleotidSequences sequences, int [] positions){
		
		return null;
	}
	
	private ArrayList<PartialCalculMethod> initializeNecessaryMethods
	
	private void analyzeSequence(NucleotidSequences sequences){
		
	}
}
