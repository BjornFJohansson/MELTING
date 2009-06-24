package melting.approximativeMethods;

import java.util.HashMap;
import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.calculMethodInterfaces.SodiumEquivalentMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;
import melting.exceptions.NoExistingMethodException;

public class ApproximativeMode implements CompletCalculMethod{
	
	protected Environment environment;
	protected RegisterCalculMethod register = new RegisterCalculMethod();
	
	public ThermoResult calculateThermodynamics() {
		OptionManagement.meltingLogger.log(Level.FINE, "\n Approximative method : ");
		
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
		
		if (Integer.parseInt(environment.getOptions().get(OptionManagement.threshold)) <= environment.getSequences().getDuplexLength()){
			
			if (environment.getOptions().get(OptionManagement.globalMethod).equals("def")){
				isApplicable = false;
			}
			OptionManagement.meltingLogger.log(Level.WARNING, "the approximative equations " +
			"were originally established for long DNA duplexes. (length superior to " +
			 environment.getOptions().get(OptionManagement.threshold) +").");
		}
		return isApplicable;
	}
	
	protected boolean isNaEqPossible(){
		return true;
	}

	public void setUpVariable(HashMap<String, String> options) {
		this.environment = new Environment(options);

		if (isNaEqPossible()){
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
	}
}
