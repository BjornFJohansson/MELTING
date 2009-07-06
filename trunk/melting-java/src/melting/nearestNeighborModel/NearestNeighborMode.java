package melting.nearestNeighborModel;

import java.util.HashMap;
import java.util.logging.Level;

import melting.Environment;
import melting.ModifiedAcidNucleic;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterMethods;
import melting.cricksPair.CricksNNMethod;
import melting.exceptions.MethodNotApplicableException;
import melting.exceptions.NoExistingMethodException;
import melting.exceptions.SequenceException;
import melting.methodInterfaces.CompletCalculMethod;
import melting.methodInterfaces.CorrectionMethod;
import melting.methodInterfaces.PartialCalculMethod;

public class NearestNeighborMode implements CompletCalculMethod{

	private Environment environment;
	private RegisterMethods register = new RegisterMethods();
	private PartialCalculMethod azobenzeneMethod;
	private PartialCalculMethod CNGRepeatsMethod;
	private PartialCalculMethod doubleDanglingEndMethod;
	private PartialCalculMethod hydroxyadenosineMethod;
	private PartialCalculMethod inosineMethod;
	private PartialCalculMethod internalLoopMethod;
	private PartialCalculMethod lockedAcidMethod;
	private PartialCalculMethod longBulgeLoopMethod;
	private PartialCalculMethod longDanglingEndMethod;
	private PartialCalculMethod cricksMethod;
	private PartialCalculMethod singleBulgeLoopMethod;
	private PartialCalculMethod singleDanglingEndMethod;
	private PartialCalculMethod singleMismatchMethod;
	private PartialCalculMethod tandemMismatchMethod;
	private PartialCalculMethod wobbleMethod;
	
	public ThermoResult calculateThermodynamics() {
		OptionManagement.meltingLogger.log(Level.FINE, "\n Nearest-Neighbor method :");

		analyzeSequence();
		int pos1 = 0; 
		int pos2 = 0;

		while (pos2 < this.environment.getSequences().getDuplexLength() - 1){
			int [] positions = getPositionsMotif(pos1);

			pos1 = positions[0];
			pos2 = positions[1];
			PartialCalculMethod currentCalculMethod = getAppropriatePartialCalculMethod(positions);
			if (currentCalculMethod == null){
				throw new NoExistingMethodException("There is no implemented method to compute the enthalpy and entropy of this segment " + environment.getSequences().getSequence(pos1, pos2) + "/" + environment.getSequences().getComplementary(pos1, pos2));
			}
			ThermoResult newResult = currentCalculMethod.calculateThermodynamics(this.environment.getSequences(), pos1, pos2, this.environment.getResult());
			this.environment.setResult(newResult);
			
				pos1 = pos2 + 1;
		}
		double Tm = 0.0;
		boolean isASaltCorrectionNecessary = true;
		
		if (this.CNGRepeatsMethod == null){
			if (this.cricksMethod == null){
				this.cricksMethod = initializeMethod(OptionManagement.NNMethod, this.environment.getOptions().get(OptionManagement.NNMethod));
			}
			CricksNNMethod initiationMethod = (CricksNNMethod)this.cricksMethod;
			ThermoResult resultinitiation = initiationMethod.calculateInitiationHybridation(this.environment);

			this.environment.setResult(resultinitiation);
			Tm = calculateMeltingTemperature(this.environment);
		}
		else {
			int CNGRepeats = (this.environment.getSequences().getDuplexLength() - 2) / 3;
			if (CNGRepeats > 4){
				Tm = calculateHairpinTemperature(this.environment);
				
				isASaltCorrectionNecessary = false;
			}
			else{
				Tm = calculateMeltingTemperature(this.environment);

			}
		}
		this.environment.setResult(Tm);
		
		if (isASaltCorrectionNecessary){
			CorrectionMethod saltCorrection = register.getIonCorrectionMethod(this.environment);
			
			if (saltCorrection == null){
				throw new NoExistingMethodException("There is no implemented ion correction method.");
			}
			this.environment.setResult(saltCorrection.correctMeltingResult(this.environment));
			
			if (environment.getResult().getSaltIndependentEntropy() > 0){
				double TmInverse = 1 / this.environment.getResult().getTm() + this.environment.getResult().getSaltIndependentEntropy() / this.environment.getResult().getEnthalpy();
				this.environment.setResult(1 / TmInverse);
			}
		}
		return this.environment.getResult();
	}

	public RegisterMethods getRegister() {
		return register;
	}

	public boolean isApplicable() {
		boolean isApplicable = true;
		if (Integer.parseInt(this.environment.getOptions().get(OptionManagement.threshold)) <= this.environment.getSequences().getDuplexLength()){
			OptionManagement.meltingLogger.log(Level.WARNING, "the Nearest Neighbor model is accurate for " +
			"shorter sequences. (length superior to 6 and inferior to" +
			 this.environment.getOptions().get(OptionManagement.threshold) +")");
			
			if (this.environment.getOptions().get(OptionManagement.globalMethod).equals("def")){
				isApplicable = false;
			}
		}
		if (this.environment.getOptions().get(OptionManagement.selfComplementarity).equals("true") && Integer.parseInt(this.environment.getOptions().get(OptionManagement.factor)) != 1){
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
		if (pos1 == 0){
			if(environment.getSequences().isCNGPattern(0, this.environment.getSequences().getSequence().length() - 1) && this.environment.isSelfComplementarity()){			
				int [] positions = {0, this.environment.getSequences().getDuplexLength() - 1};
				return positions;
			}
		}
			
		if (environment.getSequences().getDuplex().get(pos1).isComplementaryBasePair()){
			while (position < environment.getSequences().getDuplexLength() - 1){
				int testPosition = position + 1;
				if (environment.getSequences().getDuplex().get(testPosition).isComplementaryBasePair()){
					position ++;
				}
				else{
					break;
					}
				}
			int [] positions = {pos1, position};
			return positions;
		}
			
		else {
			if (environment.getSequences().getDuplex().get(pos1).isBasePairEqualTo("G", "U")){
				while (position < environment.getSequences().getDuplexLength() - 1){
					int testPosition = position + 1;
					if (environment.getSequences().getDuplex().get(testPosition).isBasePairEqualTo("G", "U")){
						position ++;
					}
					else{
						break;
					}
				}
				
				int [] positions = {pos1, position};
				return positions;
			}
			else {
				while (position < environment.getSequences().getDuplexLength() - 1){
					int testPosition = position + 1;
					if (environment.getSequences().getDuplex().get(testPosition).isBasePairEqualTo("G", "U") == false && environment.getSequences().getDuplex().get(testPosition).isComplementaryBasePair() == false){
						position ++;
						
					}
					else{
						break;
					}
				}
					int [] positions = {pos1, position};
					return positions;
				}
			}
	}
	
	private PartialCalculMethod getAppropriatePartialCalculMethod(int [] positions){
		if (positions[0] == 0 || positions[1] == environment.getSequences().getDuplexLength() - 1){
			if (environment.getSequences().isCNGPattern(positions[0], positions[1]) && this.environment.isSelfComplementarity()){
				if (this.CNGRepeatsMethod == null){
					initializeCNGRepeatsMethod();
				}
				return this.CNGRepeatsMethod;
			}
			else if (environment.getSequences().isDanglingEnd(positions[0], positions[1])){
				if (positions[1] - positions[0] + 1 == 1){
					if (this.singleDanglingEndMethod == null){
						initializeSingleDanglingEndMethod();
					}
					return this.singleDanglingEndMethod;
				}
				else if (positions[1] - positions[0] + 1 == 2){
					if (this.doubleDanglingEndMethod == null){
						initializeDoubleDanglingEndMethod();
					}
					return this.doubleDanglingEndMethod;
				}
				else if (positions[1] - positions[0] + 1 > 2){
					if (this.longDanglingEndMethod == null){
						initializeLongDanglingEndMethod();
					}
					return this.longDanglingEndMethod;
				}
				else {
					throw new SequenceException("we don't recognize the motif " + environment.getSequences().getSequence(positions[0], positions[1]) + "/" + environment.getSequences().getComplementary(positions[0], positions[1]));
				}
			}
		else if (environment.getSequences().isMismatchPair(positions[0]) || environment.getSequences().isMismatchPair(positions[1])){
			throw new NoExistingMethodException("No method for terminal mismatches have been implemented yet.");
		}
	}
	if (environment.getSequences().isPerfectMatchSequence(positions[0], positions[1])){
			if (this.cricksMethod == null){
				initializeCrickMethod();
			}
			return this.cricksMethod;
		}
		
		else if (environment.getSequences().isGUSequences(positions[0], positions[1])){
			if (this.wobbleMethod == null){
				initializeWobbleMethod();
			}
			return this.wobbleMethod;
		}
		else if (environment.getSequences().isMismatch(positions[0], positions[1])){
			if (positions[1] - positions[0] + 1 == 1){
				if (this.singleMismatchMethod == null){
					initializeSingleMismatchMethod();
				}
				return this.singleMismatchMethod;
			}
			else if (positions[1] - positions[0] + 1 == 2 && this.environment.getSequences().isGapInSequence(positions[0], positions[1])){
				if (this.tandemMismatchMethod == null){
					initializeTandemMismatchMethod();
				}
				return this.tandemMismatchMethod;
			}
			else if (positions[1] - positions[0] + 1 >= 2){
				if (this.internalLoopMethod == null){
					initializeInternalLoopMethod();
				}
				return this.internalLoopMethod;
			}
		}
		else if (environment.getSequences().isBulgeLoop(positions[0], positions[1])){
			if (positions[1] - positions[0] + 1 == 1){
				if (this.singleBulgeLoopMethod == null){
					initializeSingleBulgeLoopMethod();
				}
				return this.singleBulgeLoopMethod;
			}
			else if (positions[1] - positions[0] + 1 >= 2){
				if (this.longBulgeLoopMethod == null){
					initializeLongBulgeLoopMethod();
				}
				return this.longBulgeLoopMethod;
			}
		}
		else if (environment.getSequences().isListedModifiedAcid(positions[0])){
			ModifiedAcidNucleic acidName = environment.getSequences().getModifiedAcidName(environment.getSequences().getDuplex().get(positions[0]));
			if (acidName != null){

				switch (acidName) {
				case inosine:
					if (this.inosineMethod == null){
						initializeInosineMethod();
					}
					return this.inosineMethod;
				case azobenzene:
					if (this.azobenzeneMethod == null){
						initializeAzobenzeneMethod();
					}
					return this.azobenzeneMethod;
				case hydroxyadenine:
					if (this.hydroxyadenosineMethod == null){
						initializeHydroxyadenosineMethod();
					}
					return this.hydroxyadenosineMethod;
				case lockedAcidNucleic:
					if (this.lockedAcidMethod == null){
						initializeLockedAcidMethod();
					}
					return this.lockedAcidMethod;
				default:
					throw new SequenceException("There is a unknown modified acid nucleic in the sequences.");
				}
			}
		}
		return null;
	}
	
	private void initializeCrickMethod(){
		String optionName = OptionManagement.NNMethod;
		String methodName = this.environment.getOptions().get(optionName);
		this.cricksMethod = initializeMethod(optionName, methodName);
	}
	
	private void initializeAzobenzeneMethod(){
		String optionName = OptionManagement.azobenzeneMethod;
		String methodName = this.environment.getOptions().get(optionName);
		this.azobenzeneMethod = initializeMethod(optionName, methodName);
	}
	
	private void initializeCNGRepeatsMethod(){
		String optionName = OptionManagement.CNGMethod;
		String methodName = this.environment.getOptions().get(optionName);
		this.CNGRepeatsMethod = initializeMethod(optionName, methodName);
	}
	
	private void initializeDoubleDanglingEndMethod(){
		String optionName = OptionManagement.doubleDanglingEndMethod;
		String methodName = this.environment.getOptions().get(optionName);
		this.doubleDanglingEndMethod = initializeMethod(optionName, methodName);
	}
	
	private void initializeHydroxyadenosineMethod(){
		String optionName = OptionManagement.hydroxyadenineMethod;
		String methodName = this.environment.getOptions().get(optionName);
		this.hydroxyadenosineMethod = initializeMethod(optionName, methodName);
	}
	
	private void initializeInosineMethod(){
		String optionName = OptionManagement.inosineMethod;
		String methodName = this.environment.getOptions().get(optionName);
		this.inosineMethod = initializeMethod(optionName, methodName);
	}
	
	private void initializeInternalLoopMethod(){
		String optionName = OptionManagement.internalLoopMethod;
		String methodName = this.environment.getOptions().get(optionName);

		this.internalLoopMethod = initializeMethod(optionName, methodName);

	}
	
	private void initializeLockedAcidMethod(){
		String optionName = OptionManagement.lockedAcidMethod;
		String methodName = this.environment.getOptions().get(optionName);
		this.lockedAcidMethod = initializeMethod(optionName, methodName);
	}
	
	private void initializeLongBulgeLoopMethod(){
		String optionName = OptionManagement.longBulgeLoopMethod;
		String methodName = this.environment.getOptions().get(optionName);
		this.longBulgeLoopMethod = initializeMethod(optionName, methodName);
	}
	
	private void initializeLongDanglingEndMethod(){
		String optionName = OptionManagement.longDanglingEndMethod;
		String methodName = this.environment.getOptions().get(optionName);
		this.longDanglingEndMethod = initializeMethod(optionName, methodName);
	}
	
	private void initializeSingleBulgeLoopMethod(){
		String optionName = OptionManagement.singleBulgeLoopMethod;
		String methodName = this.environment.getOptions().get(optionName);
		this.singleBulgeLoopMethod = initializeMethod(optionName, methodName);
	}
	
	private void initializeSingleDanglingEndMethod(){
		String optionName = OptionManagement.singleDanglingEndMethod;
		String methodName = this.environment.getOptions().get(optionName);

		this.singleDanglingEndMethod = initializeMethod(optionName, methodName);
	}
	
	private void initializeSingleMismatchMethod(){
		String optionName = OptionManagement.singleMismatchMethod;
		String methodName = this.environment.getOptions().get(optionName);
		this.singleMismatchMethod = initializeMethod(optionName, methodName);
	}
	
	private void initializeTandemMismatchMethod(){
		String optionName = OptionManagement.tandemMismatchMethod;
		String methodName = this.environment.getOptions().get(optionName);
		this.tandemMismatchMethod = initializeMethod(optionName, methodName);
	}
	private void initializeWobbleMethod(){
		String optionName = OptionManagement.wobbleBaseMethod;
		String methodName = this.environment.getOptions().get(optionName);
		this.wobbleMethod = initializeMethod(optionName, methodName);
	}
	private PartialCalculMethod initializeMethod(String optionName, String methodName){

		PartialCalculMethod necessaryMethod = register.getPartialCalculMethod(optionName, methodName);
		if (necessaryMethod != null){
			necessaryMethod.initializeFileName(methodName);
			necessaryMethod.loadData(environment.getOptions());

		}
		else {
			throw new NoExistingMethodException("one or more method(s) is(are) missing to compute the melting" +
					"temperature.");
		}
		return necessaryMethod;
	}
	
	private boolean checkIfMethodsAreApplicable(){
		int pos1 = 0;
		int pos2 = 0;
		boolean isApplicableMethod = true;
		while (pos2 + 1 <= environment.getSequences().getDuplexLength() - 1){

			int [] positions = getPositionsMotif(pos1);
			pos1 = positions[0];
			pos2 = positions[1];
			PartialCalculMethod necessaryMethod = getAppropriatePartialCalculMethod(positions);
			if (necessaryMethod == null){
				throw new NoExistingMethodException("We don't have a method to compute the energy for the positions from " + pos1 + " to " + pos2 );
			}
			if (necessaryMethod.isApplicable(this.environment, pos1, pos2) == false){
				OptionManagement.meltingLogger.log(Level.WARNING, " We cannot comput the melting temperature, a method is not applicable with the chosen options.");
				isApplicableMethod = false;
			}
				pos1 = pos2 + 1;
		}
		return isApplicableMethod;
	}
	
	private void analyzeSequence(){
		if (checkIfMethodsAreApplicable() == false){
			throw new MethodNotApplicableException("we cannot compute the melting because one method is not applicable. Check the sequences.");
		}
	}
	public static double calculateHairpinTemperature(Environment environment){
		double Tm = environment.getResult().getEnthalpy() / environment.getResult().getEntropy() - 273.15;
		OptionManagement.meltingLogger.log(Level.FINE, "\n Melting temperature : Tm = delta H / delta - 273.15");
		
		return Tm;
	}
	
	public static double calculateMeltingTemperature(Environment environment){
		double Tm = environment.getResult().getEnthalpy() / (environment.getResult().getEntropy() + 1.99 * Math.log( environment.getNucleotides() / environment.getFactor() )) - 273.15;
		OptionManagement.meltingLogger.log(Level.FINE, "\n Melting temperature : Tm = delta H / (delta S + 1.99 x ln([nucleotides] / F)) - 273.15");
		return Tm;
	}

}
