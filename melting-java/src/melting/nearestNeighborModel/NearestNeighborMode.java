package melting.nearestNeighborModel;

import java.util.HashMap;

import melting.Environment;
import melting.Helper;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public class NearestNeighborMode implements CompletCalculMethod{

	private Environment environment;
	private PartialCalculMethod azobenzeneMethod;
	private PartialCalculMethod CNGRepeatsMethod;
	private PartialCalculMethod deoxyadenosineMethod;
	private PartialCalculMethod doubleDangingEndMethod;
	private PartialCalculMethod hydroxyadenosineMethod;
	private PartialCalculMethod inosineMethod;
	private PartialCalculMethod internalLoopMethod;
	private PartialCalculMethod lockedAcidMethod;
	private PartialCalculMethod longBulgeLoopMethod;
	private PartialCalculMethod longDangingEndMethod;
	private PartialCalculMethod cricksMethod;
	private PartialCalculMethod singleBulgeLoopMethod;
	private PartialCalculMethod singleDangingEndMethod;
	private PartialCalculMethod singleMismatchMethod;
	private PartialCalculMethod tandemMismatchMethod;
	private PartialCalculMethod woddleMethod;
	
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
		
		if(sequences.isCNGMotif(pos1, pos1 + 5)){
			int position = pos1;
			while (sequences.isCNGMotif(position + 3, position + 5)){
				position += 3;
			}
			int [] positions = {pos1, position};
			return positions;
		}
		else {
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
				else if (position == sequences.getDuplexLength() - 1){
					int [] positions = {pos1 - 1, position};
					return positions;
				}
				else if (pos1 == 0){
					int [] positions = {pos1, position + 1};
					return positions;
				}
				else{
					int [] positions = {pos1 - 1, position + 1};
					return positions;
				}
			}
		}
		
	}
	
	private PartialCalculMethod getAppropriatePartialCalculMethod(int [] positions){
		if (environment.getSequences().isPerfectMatchSequence(positions[0], positions[1])){
			return this.cricksMethod;
		}
		else if (environment.getSequences().isCNGMotif(positions[0], positions[1])){
			return this.CNGRepeatsMethod;
		}
		else if (environment.getSequences().isDanglingEnd(positions[0], positions[1])){
			if (positions[1] - positions[0] + 1 == 2){
				return this.singleDangingEndMethod;
			}
			else if (positions[1] - positions[0] + 1 == 3){
				return this.doubleDangingEndMethod;
			}
			else if (positions[1] - positions[0] + 1 > 3){
				return this.longDangingEndMethod;
			}
		}
		else if (environment.getSequences().isMismatch(positions[0], positions[1])){
			if (positions[0] == 0 || positions[1] == environment.getSequences().getDuplexLength() - 1){
				System.out.println("ERROR : No method for terminal mismatches have been implemented yet.");
			}
			if (positions[1] - positions[0] + 1 == 3){
				return this.singleMismatchMethod;
			}
			else if (positions[1] - positions[0] + 1 == 4){
				return this.tandemMismatchMethod;
			}
			else if (positions[1] - positions[0] + 1 > 4){
				return this.internalLoopMethod;
			}
		}
		else if (environment.getSequences().isBulgeLoop(positions[0], positions[1])){
			if (positions[1] - positions[0] + 1 == 3){
				return this.singleBulgeLoopMethod;
			}
			else if (positions[1] - positions[0] + 1 >= 4){
				return this.longBulgeLoopMethod;
			}
		}
		return null;
	}
	
	private void initializeNecessaryMethods(NucleotidSequences sequences){
		int pos1 = 0;
		RegisterCalculMethod register = new RegisterCalculMethod();
		
		while (pos1 <= sequences.getDuplexLength() - 1){
			int pos2 = getPositionsMotif(sequences, pos1)[1];
			if (sequences.isPerfectMatchSequence(pos1, pos2)){
				if (this.cricksMethod == null){
					this.cricksMethod = register.getPartialCalculMethod(OptionManagement.NNMethod, environment.getOptions().get(OptionManagement.NNMethod));
					this.cricksMethod.initializeFileName(environment.getOptions().get(OptionManagement.NNMethod));
					this.cricksMethod.loadData(environment.getOptions());
				}
				
				if (this.cricksMethod.isApplicable(environment, pos1, pos2) == false){
					System.err.println("ERROR : we cannot compute the melting temperature for " + sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2));
				}
			}
			pos1 = pos2 + 1;
		}
	}
	
	private void analyzeSequence(NucleotidSequences sequences){
		
	}
}
