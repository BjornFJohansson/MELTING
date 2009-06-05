package melting.approximativeMethods;

import java.util.HashMap;
import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.calculMethodInterfaces.CorrectionMethod;
import melting.calculMethodInterfaces.SodiumEquivalentMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;
import melting.exceptions.MethodNotApplicableException;
import melting.exceptions.NoExistingMethodException;

public class ApproximativeMode implements CompletCalculMethod{
	
	protected Environment environment;
	protected RegisterCalculMethod register = new RegisterCalculMethod();
	
	public ThermoResult CalculateThermodynamics() {
		OptionManagement.meltingLogger.log(Level.FINE, "Approximative method : ");
		
		return environment.getResult();
	}
	
	public RegisterCalculMethod getRegister() {
		return register;
	}

	public boolean isApplicable() {
		boolean isApplicable = true;
		
		if (environment.getSequences().getPercentMismatching() != 0){
			OptionManagement.meltingLogger.log(Level.WARNING, "The approximative mode formulas" +
					"cannot properly account for the presence of mismatches" +
					" and unpaired nucleotides.");
		}
		
		if (Integer.getInteger(environment.getOptions().get(OptionManagement.threshold)) <= environment.getSequences().getDuplexLength()){
			
			if (environment.getOptions().get(OptionManagement.completMethod).equals("default")){
				isApplicable = false;
			}
			OptionManagement.meltingLogger.log(Level.WARNING, "the approximative equations " +
			"were originally established for long DNA duplexes. (length superior to " +
			 environment.getOptions().get(OptionManagement.threshold) +").");
		}
		return isApplicable;
	}

	public void setUpVariable(HashMap<String, String> options) {
		this.environment = new Environment(options);
		
		if (environment.getMg() > 0 || environment.getK() > 0 || environment.getTris() > 0){
			
			SodiumEquivalentMethod method = this.register.getNaEqMethod(options);
			if (method != null){
				environment.setNa(method.getSodiumEquivalent(environment.getNa(), environment.getMg(), environment.getK(), environment.getTris(), environment.getDNTP()));
			}
			else{
				throw new NoExistingMethodException("There are other ions than Na+ in the solution and no ion correction method is avalaible for this type of hybridization.");
			}
		}
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
