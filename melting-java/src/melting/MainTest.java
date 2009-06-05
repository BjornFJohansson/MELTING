package melting;


import java.util.logging.Level;

import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public class MainTest {

	public static void main(String[] args) {
	
		OptionManagement optionManager = new OptionManagement();
		
		if (optionManager.isMeltingInformationOption(args)){
			try {
				optionManager.readOptions(args);
			} catch (Exception e) {
				OptionManagement.meltingLogger.log(Level.SEVERE, e.getMessage());
			}
		}
		else {
			try {
				Environment environment = optionManager.createEnvironment(args);
				
				RegisterCalculMethod register = new RegisterCalculMethod();
				CompletCalculMethod calculMethod = register.getCompletCalculMethod(environment.getOptions());
			
				ThermoResult results = calculMethod.CalculateThermodynamics();
					
				results = calculMethod.getRegister().computeOtherMeltingCorrections(environment);
				double enthalpy = results.getEnthalpy();
				double entropy = results.getEntropy();
				
				environment.setResult(results);
										
				OptionManagement.meltingLogger.log(Level.INFO, "The MELTING results are : ");
				OptionManagement.meltingLogger.log(Level.INFO, "Enthalpy : " + enthalpy + "cal/mol ( " + results.getEnergyValueInJ(enthalpy) + "J/mol)");
				OptionManagement.meltingLogger.log(Level.INFO, "Entropy : " + entropy + "cal/mol ( " + results.getEnergyValueInJ(entropy) + "J/mol)");
				OptionManagement.meltingLogger.log(Level.INFO, "Melting temperature : " + results.getTm() + "degres C.");
			
			} catch (Exception e) {
				OptionManagement.meltingLogger.log(Level.SEVERE, e.getMessage());
			}
		}
	}
}
