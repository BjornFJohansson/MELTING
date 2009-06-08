package melting;


import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

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
		
		/*File santalucia = new File("../Data/AllawiSantalucia1997nn.xml");
		HashMap<String, Thermodynamics> data = new HashMap<String, Thermodynamics>();
		FileReader reader = new FileReader();
		
		try {
			data = reader.readFile(santalucia, data);
		} catch (ParserConfigurationException e) {
			System.err.println("coucou");

		} catch (SAXException e) {
		}*/
	}
}
