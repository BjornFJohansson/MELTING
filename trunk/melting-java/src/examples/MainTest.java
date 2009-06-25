package examples;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
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
	
	public static void displayResults(Properties properties, ArrayList<String> methodNames, String hybridization, String solution, String nucleotides, String option){
		NumberFormat format = NumberFormat.getInstance(); 
		format.setMaximumFractionDigits(2);
		
		Iterator<Map.Entry<Object, Object>> entry = properties.entrySet().iterator();
		while (entry.hasNext()){
			Map.Entry<Object, Object> pairs = entry.next();
			System.out.print("\n" + pairs.getKey() + "\t" + pairs.getValue());

			for (int i=0; i < methodNames.size(); i++){
				String [] argsOption = {"-H", hybridization, "-E", solution, "-P", nucleotides, "-S", pairs.getKey().toString(), option, methodNames.get(i)}; 
				double Tm = MainTest.getMeltingTest(argsOption);
				System.out.print("\t" + format.format(Tm));
			}
		}
	}
	
	public static void displayResultsWithComplementarySequence(Properties properties, ArrayList<String> methodNames, String hybridization, String solution, String nucleotides, String option){
		NumberFormat format = NumberFormat.getInstance(); 
		format.setMaximumFractionDigits(2);
		
		Iterator<Map.Entry<Object, Object>> entry = properties.entrySet().iterator();
		while (entry.hasNext()){
			Map.Entry<Object, Object> pairs = entry.next();
			String [] sequences = pairs.getKey().toString().split("/");
			System.out.print("\n" + pairs.getKey() + "\t" + pairs.getValue());

			for (int i=0; i < methodNames.size(); i++){
				String [] argsOption = {"-H", hybridization, "-E", solution, "-P", nucleotides, "-S", sequences[0], "-C", sequences[1], option, methodNames.get(i)}; 
				double Tm = MainTest.getMeltingTest(argsOption);
				System.out.print("\t" + format.format(Tm));
			}
		}
	}
	
	public static void displayResultsSelf(Properties properties, ArrayList<String> methodNames, String hybridization, String solution, String nucleotides, String option){
		NumberFormat format = NumberFormat.getInstance(); 
		format.setMaximumFractionDigits(2);
		
		Iterator<Map.Entry<Object, Object>> entry = properties.entrySet().iterator();
		while (entry.hasNext()){
			Map.Entry<Object, Object> pairs = entry.next();
			System.out.print("\n" + pairs.getKey() + "\t" + pairs.getValue());

			for (int i=0; i < methodNames.size(); i++){
				String [] argsOption = {"-H", hybridization, "-E", solution, "-P", nucleotides, "-S", pairs.getKey().toString(), option, methodNames.get(i), "-self"}; 
				double Tm = MainTest.getMeltingTest(argsOption);
				System.out.print("\t" + format.format(Tm));
			}
		}
	}
}
