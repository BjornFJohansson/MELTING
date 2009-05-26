package melting;


import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public class MainTest {

	public static void main(String[] args) {
	
		OptionManagement optionManager = new OptionManagement();
		Environment environment = optionManager.createEnvironment(args);
		
		RegisterCalculMethod register = new RegisterCalculMethod();
		CompletCalculMethod calculMethod = register.getCompletCalculMethod(environment.getOptions());
		
		if (calculMethod == null){
			System.err.println("ERROR : no method (approximative or nearest-neighbor) is applicable.");
		}
		else{
			ThermoResult results = calculMethod.CalculateThermodynamics();
			double enthalpy = results.getEnthalpy();
			double entropy = results.getEntropy();
			
			if (environment.getDMSO() > 0 || environment.getFormamide() > 0){
				results = calculMethod.correctThermodynamics();
			}
			
			environment.setResult(results);
			
			System.out.println("The MELTING results are :");
			System.out.println("Enthalpy : " + enthalpy + "cal/mol ( " + results.getEnergyValueInJ(enthalpy) + "J/mol)");
			System.out.println("Entropy : " + entropy + "cal/mol ( " + results.getEnergyValueInJ(entropy) + "J/mol)");
			System.out.println("Melting temperature : " + results.getTm() + "degres C.");

		}
	}

}
