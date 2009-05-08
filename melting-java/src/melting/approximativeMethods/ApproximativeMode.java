package melting.approximativeMethods;

import java.util.HashMap;

import melting.Environment;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.calculMethodInterfaces.SodiumEquivalentMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public class ApproximativeMode implements CompletCalculMethod{
	
	protected Environment environment;
	
	public ThermoResult CalculateThermodynamics() {
		return environment.getResult();
	}

	public boolean isApplicable() {
		boolean isApplicable = true;
		
		if (environment.getSequences().getPercentMismatching() != 0){
			System.out.println("WARNING : The approximative mode formulas" +
					"cannot properly account for the presence of mismatches" +
					" and unpaired nucleotides.");
			isApplicable = false;
		}
		
		if (Integer.getInteger(environment.getOptions().get(OptionManagement.threshold)) <= environment.getSequences().getDuplexLength()){
			
			if (environment.getOptions().get(OptionManagement.completMethod).equals("default") == false){
				isApplicable = false;
			}
			System.out.println("WARNING : the approximative equations " +
			"were originally established for long DNA duplexes. (length superior to " +
			 environment.getOptions().get(OptionManagement.threshold) +")");
		}
		return isApplicable;
	}

	public void setUpVariable(HashMap<String, String> options) {
		this.environment = new Environment(options);
		
		if (environment.getMg() > 0 || environment.getK() > 0 || environment.getTris() > 0){
			RegisterCalculMethod setNaEqMethod = new RegisterCalculMethod();
			SodiumEquivalentMethod method = setNaEqMethod.getNaEqMethod(options);
			
			environment.setNa(method.getSodiumEquivalent(environment.getNa(), environment.getMg(), environment.getK(), environment.getTris(), environment.getDNTP()));
		}
	}

}
