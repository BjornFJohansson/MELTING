package melting;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public class MainTest {

	public static Properties loadSequencesTest(String fileName){
		
		Properties properties = new Properties();
		FileInputStream stream ;
		try {
			stream = new FileInputStream(fileName);
			properties.load(stream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return properties;
	}
	
	public static double getMeltingTest(String [] args){
		OptionManagement optionManager = new OptionManagement();
		
		try {
			
			Environment environment = optionManager.createEnvironment(args);

			RegisterCalculMethod register = new RegisterCalculMethod();
			CompletCalculMethod calculMethod = register.getCompletCalculMethod(environment.getOptions());
			ThermoResult results = calculMethod.calculateThermodynamics();
			environment.setResult(results);

			results = calculMethod.getRegister().computeOtherMeltingCorrections(environment);
		
			environment.setResult(results);
					
			return environment.getResult().getTm();
		} catch (Exception e) {
			OptionManagement.meltingLogger.log(Level.SEVERE, e.getMessage());
		}
		return 0.0;
	}
}
