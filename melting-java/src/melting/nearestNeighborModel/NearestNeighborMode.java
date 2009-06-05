package melting.nearestNeighborModel;

import java.util.HashMap;
import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ModifiedAcidNucleic;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.calculMethodInterfaces.CorrectionMethod;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;
import melting.cricksNNMethods.CricksNNMethod;
import melting.exceptions.MethodNotApplicableException;
import melting.exceptions.NoExistingMethodException;
import melting.exceptions.SequenceException;

public class NearestNeighborMode implements CompletCalculMethod{

	private Environment environment;
	private RegisterCalculMethod register = new RegisterCalculMethod();
	private PartialCalculMethod azobenzeneMethod;
	private PartialCalculMethod CNGRepeatsMethod;
	private PartialCalculMethod deoxyadenineMethod;
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
	private PartialCalculMethod wobbleMethod;
	
	private HashMap<PartialCalculMethod, String> matchingOptionNames = new HashMap<PartialCalculMethod, String>();
	
	public NearestNeighborMode(){
		initializeMatchingOptionNames();
	}
	
	private void initializeMatchingOptionNames(){
		
		matchingOptionNames.put(lockedAcidMethod, OptionManagement.lockedAcidMethod);
		matchingOptionNames.put(azobenzeneMethod, OptionManagement.azobenzeneMethod);
		matchingOptionNames.put(CNGRepeatsMethod, OptionManagement.CNGMethod);
		matchingOptionNames.put(deoxyadenineMethod, OptionManagement.deoxyadenineMethod);
		matchingOptionNames.put(doubleDangingEndMethod, OptionManagement.doubleDanglingEndMethod);
		matchingOptionNames.put(hydroxyadenosineMethod, OptionManagement.hydroxyadenineMethod);
		matchingOptionNames.put(inosineMethod, OptionManagement.inosineMethod);
		matchingOptionNames.put(internalLoopMethod, OptionManagement.internalLoopMethod);
		matchingOptionNames.put(longBulgeLoopMethod, OptionManagement.longBulgeLoopMethod);
		matchingOptionNames.put(longDangingEndMethod, OptionManagement.longDanglingEndMethod);
		matchingOptionNames.put(cricksMethod, OptionManagement.NNMethod);
		matchingOptionNames.put(singleBulgeLoopMethod, OptionManagement.singleBulgeLoopMethod);
		matchingOptionNames.put(singleDangingEndMethod, OptionManagement.singleDanglingEndMethod);
		matchingOptionNames.put(singleMismatchMethod, OptionManagement.singleMismatchMethod);
		matchingOptionNames.put(tandemMismatchMethod, OptionManagement.tandemMismatchMethod);
		matchingOptionNames.put(wobbleMethod, OptionManagement.wobbleBaseMethod);
	}
	
	public ThermoResult CalculateThermodynamics() {
		OptionManagement.meltingLogger.log(Level.FINE, "Nearest-Neighbor method :");

		analyzeSequence();
	
		CricksNNMethod initiationMethod = (CricksNNMethod)this.cricksMethod;
		ThermoResult resultinitiation = initiationMethod.calculateInitiationHybridation(this.environment);
		this.environment.setResult(resultinitiation);
		
		int pos1 = 0; 
		
		while (pos1 <= this.environment.getSequences().getDuplexLength() - 1){
			int [] positions = getPositionsMotif(pos1);
			int pos2 = positions[1];
			
			PartialCalculMethod currentCalculMethod = getAppropriatePartialCalculMethod(positions);

			if (currentCalculMethod == null){
				throw new NoExistingMethodException("There is no implemented method to compute the enthalpy and entropy of this segment " + environment.getSequences().getSequence(pos1, pos2) + "/" + environment.getSequences().getComplementary(pos1, pos2));
			}
			ThermoResult newResult = currentCalculMethod.calculateThermodynamics(this.environment.getSequences(), pos1, pos2, this.environment.getResult());
			
			this.environment.setResult(newResult);
		}
		
		double Tm = calculateMeltingTemperature(this.environment);
		this.environment.setResult(Tm);
		
		CorrectionMethod saltCorrection = register.getIonCorrectionMethod(this.environment);
		
		if (saltCorrection == null){
			throw new NoExistingMethodException("There is no implemented ion correction method.");
		}
		this.environment.setResult(saltCorrection.correctMeltingResult(this.environment));
		
		if (environment.getResult().getSaltIndependentEntropy() > 0){
			double TmInverse = 1 / this.environment.getResult().getTm() + this.environment.getResult().getSaltIndependentEntropy() / this.environment.getResult().getEnthalpy();
			this.environment.setResult(1 / TmInverse);
		}
				
		return this.environment.getResult();
	}

	public RegisterCalculMethod getRegister() {
		return register;
	}

	public boolean isApplicable() {
		boolean isApplicable = true;
		
		if (Integer.getInteger(this.environment.getOptions().get(OptionManagement.threshold)) >= this.environment.getSequences().getDuplexLength()){
			OptionManagement.meltingLogger.log(Level.WARNING, "the Nearest Neighbor model is accurate for " +
			"shorter sequences. (length superior to 6 and inferior to" +
			 this.environment.getOptions().get(OptionManagement.threshold) +")");
			
			if (this.environment.getOptions().get(OptionManagement.completMethod).equals("default")){
				isApplicable = false;
			}
		}
		
		if (this.environment.getOptions().containsKey(OptionManagement.selfComplementarity) && Integer.getInteger(this.environment.getOptions().get(OptionManagement.factor)) != 1){
			OptionManagement.meltingLogger.log(Level.WARNING, "When the oligonucleotides are self-complementary, the correction factor F must be equal to 1.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public void setUpVariable(HashMap<String, String> options) {
		this.environment = new Environment(options);
		
		}
	
	private int [] getPositionsMotif(int pos1){
		int position = pos1;

		if(environment.getSequences().isCNGMotif(pos1, pos1 + 5)){
			while (environment.getSequences().isCNGMotif(position + 3, position + 5)){
				position += 3;
			}
			int [] positions = {pos1, position};
			return positions;
		}
		else {
			if (Helper.isComplementaryBasePair(environment.getSequences().getSequence().charAt(pos1), environment.getSequences().getComplementary().charAt(pos1))){
				
				while (Helper.isComplementaryBasePair(environment.getSequences().getSequence().charAt(position + 1), environment.getSequences().getComplementary().charAt(position + 1))){
					position ++;
				}

				int [] positions = {pos1, position};
				return positions;
			}
			
			else {
				if (environment.getSequences().isBasePairEqualsTo('G', 'U', pos1)){
					while (environment.getSequences().isBasePairEqualsTo('G', 'U', position + 1)){
						position ++;
					}
					if (Helper.isComplementaryBasePair(environment.getSequences().getSequence().charAt(position + 1), environment.getSequences().getComplementary().charAt(position + 1)) == false){
						if (pos1 == 0){
							int [] positions = {pos1, position};
							return positions;
						}
						else{
							int [] positions = {pos1 - 1, position};
							return positions;
						}
					}
					else{
						int [] positions = {pos1 - 1, pos1 + 1};
						return positions;
					}
				}
				else {
					while (Helper.isComplementaryBasePair(environment.getSequences().getSequence().charAt(position + 1), environment.getSequences().getComplementary().charAt(position + 1)) == false && position + 1 <= environment.getSequences().getDuplexLength() - 1){
						position ++;
					}
					if (environment.getSequences().isBasePairEqualsTo('G', 'U', position)){
						position --;
					}
					if (pos1 == 0 && position == environment.getSequences().getDuplexLength()){
						int [] positions = {pos1, position};
						return positions;
					}
					else if (position == environment.getSequences().getDuplexLength() - 1){
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
	}
	
	private PartialCalculMethod getAppropriatePartialCalculMethod(int [] positions){
		if (positions[0] == 0 || positions[1] == environment.getSequences().getDuplexLength() - 1){
			if (environment.getSequences().isDanglingEnd(positions[0], positions[1])){
				if (positions[1] - positions[0] + 1 == 2){
					return this.singleDangingEndMethod;
				}
				else if (positions[1] - positions[0] + 1 == 3){
					return this.doubleDangingEndMethod;
				}
				else if (positions[1] - positions[0] + 1 > 3){
					return this.longDangingEndMethod;
				}
				else {
					throw new SequenceException("we don't recognize the motif " + environment.getSequences().getSequence(positions[0], positions[1]) + "/" + environment.getSequences().getComplementary(positions[0], positions[1]));
				}
			}
			
			if ((environment.getSequences().isMismatch(positions[0], positions[1]))){
				throw new NoExistingMethodException("No method for terminal mismatches have been implemented yet.");
			}
		}
		
		if (environment.getSequences().isPerfectMatchSequence(positions[0], positions[1])){
			return this.cricksMethod;
		}
		else if (environment.getSequences().isCNGMotif(positions[0], positions[1])){
			return this.CNGRepeatsMethod;
		}
		else if (environment.getSequences().isGUSequences(positions[0], positions[1])){
			return this.wobbleMethod;
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
		else if (environment.getSequences().isListedModifiedAcid(positions[0], positions[1])){
			ModifiedAcidNucleic acidName = environment.getSequences().getModifiedAcidName(environment.getSequences().getSequence(positions[0], positions[1]), environment.getSequences().getComplementary(positions[0], positions[1]));
			if (environment.getSequences().getModifiedAcidName(environment.getSequences().getSequence(positions[0], positions[1]), environment.getSequences().getComplementary(positions[0], positions[1])) != null){
				switch (acidName) {
				case inosine:
					return this.inosineMethod;
				case azobenzene:
					return this.azobenzeneMethod;
				case hydroxyadenine:
					return this.hydroxyadenosineMethod;
				case L_deoxyadenine:
					return this.deoxyadenineMethod;
				case lockedAcidNucleic:
					return this.lockedAcidMethod;
				default:
					System.err.println("ERROR : There is a unknown modified acid nucleic in the sequences.");
					break;
				}
			}
		}
		return null;
	}
	
	private void initializeNecessaryMethods(){
		int pos1 = 0;
		
		while (pos1 <= environment.getSequences().getDuplexLength() - 1){
			int [] positions = getPositionsMotif(pos1);
			int pos2 = positions[1];
			PartialCalculMethod necessaryMethod = getAppropriatePartialCalculMethod(positions);
			
			if (necessaryMethod == null){
				String optionName = this.matchingOptionNames.get(necessaryMethod);
				String methodName = environment.getOptions().get(optionName);
				
				necessaryMethod = register.getPartialCalculMethod(optionName, methodName);
				
				if (necessaryMethod != null){
					necessaryMethod.initializeFileName(methodName);
					necessaryMethod.loadData(environment.getOptions());
				
				}
				else {
					throw new NoExistingMethodException("one or more method(s) is(are) missing to compute the melting" +
							"temperature at the positions " + pos1 + "-" + pos2);
				}
			}
			
			pos1 = pos2;
		}
	}
	
	private boolean checkIfMethodsAreApplicable(){
		int pos1 = 0;
		boolean isApplicableMethod = true;

		while (pos1 <= environment.getSequences().getDuplexLength() - 1){
			int [] positions = getPositionsMotif(pos1);
			int pos2 = positions[1];
			PartialCalculMethod necessaryMethod = getAppropriatePartialCalculMethod(positions);
			
			if (necessaryMethod == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "We cannot comput the melting temperature, there is an option we don't understand.");
				isApplicableMethod = false;
			}
			
			if (necessaryMethod.isApplicable(this.environment, pos1, pos2) == false){
				OptionManagement.meltingLogger.log(Level.WARNING, " We cannot comput the melting temperature, a method is not applicable with the chosen options.");
				isApplicableMethod = false;
			}
			pos1 = pos2;
		}
		return isApplicableMethod;
	}
	
	private void analyzeSequence(){
		initializeNecessaryMethods();
		
		if (checkIfMethodsAreApplicable() == false){
			throw new MethodNotApplicableException("we cannot compute the melting because one method is not applicable. Check the sequences.");
		}
	}
	
	public static double calculateMeltingTemperature(Environment environment){
		double Tm = environment.getResult().getEnthalpy() / (environment.getResult().getEntropy() + 1.99 * Math.log( environment.getNucleotides() / environment.getFactor() )) - 273.15;
		
		return Tm;
	}

	public ThermoResult correctThermodynamics() {
		
		if (this.environment.getDMSO() > 0){
			CorrectionMethod DMSOCorrection = register.getCorrectionMethod(OptionManagement.DMSOCorrection, this.environment.getOptions().get(OptionManagement.DMSOCorrection));
			
			if (DMSOCorrection == null){
				throw new NoExistingMethodException("There is no implemented DMSO correction.");
			}
			else if (DMSOCorrection.isApplicable(this.environment)){
				this.environment.setResult(DMSOCorrection.correctMeltingResult(this.environment));
			}
			else {
				throw new MethodNotApplicableException("The DMSO correction is not applicable with this environment (option " + OptionManagement.DMSOCorrection + ").");
			}
		}
		if (this.environment.getFormamide() > 0){
			CorrectionMethod formamideCorrection = register.getCorrectionMethod(OptionManagement.formamideCorrection, this.environment.getOptions().get(OptionManagement.formamideCorrection));
			
			if (formamideCorrection == null){
				throw new NoExistingMethodException("There is no implemented formamide correction.");
			}
			else if (formamideCorrection.isApplicable(this.environment)){
				this.environment.setResult(formamideCorrection.correctMeltingResult(this.environment));
			}
			else {
				throw new MethodNotApplicableException("The formamide correction is not applicable with this environment (option " + OptionManagement.formamideCorrection + ").");
			}
		}
		
		return this.environment.getResult();
	}
}
