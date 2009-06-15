package melting;


import java.text.NumberFormat;
import java.util.logging.Level;

import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;
import melting.nearestNeighborModel.NearestNeighborMode;

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
				NumberFormat format = NumberFormat.getInstance(); 
				format.setMaximumFractionDigits(2);
				
				Environment environment = optionManager.createEnvironment(args);

				RegisterCalculMethod register = new RegisterCalculMethod();
				CompletCalculMethod calculMethod = register.getCompletCalculMethod(environment.getOptions());
				ThermoResult results = calculMethod.calculateThermodynamics();
				environment.setResult(results);

				results = calculMethod.getRegister().computeOtherMeltingCorrections(environment);
				double enthalpy = results.getEnthalpy();
				double entropy = results.getEntropy();
				
				environment.setResult(results);
					
				OptionManagement.meltingLogger.log(Level.INFO, "\n The MELTING results are : ");
				if (calculMethod instanceof NearestNeighborMode){
					OptionManagement.meltingLogger.log(Level.INFO, "Enthalpy : " + format.format(enthalpy) + " cal/mol ( " + format.format(results.getEnergyValueInJ(enthalpy)) + " J /mol)");
					OptionManagement.meltingLogger.log(Level.INFO, "Entropy : " + format.format(entropy) + " cal/mol ( " + format.format(results.getEnergyValueInJ(entropy)) + " J /mol)");
				}
				OptionManagement.meltingLogger.log(Level.INFO, "Melting temperature : " + format.format(results.getTm()) + " degres C.");
			
			} catch (Exception e) {
				OptionManagement.meltingLogger.log(Level.SEVERE, e.getMessage());
			}
		}
	}
}
