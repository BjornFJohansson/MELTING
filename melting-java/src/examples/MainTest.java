/* This program is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the 
 * License, or (at your option) any later version
                                
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
 * Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, 
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA                                                                  

 *       Marine Dumousseau and Nicolas Lenovere                                                   
 *       EMBL-EBI, neurobiology computational group,                          
 *       Cambridge, UK. e-mail: lenov@ebi.ac.uk, marine@ebi.ac.uk        */

package examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterMethods;
import melting.methodInterfaces.MeltingComputationMethod;

public class MainTest {

	public static Properties loadSequencesTest(String fileName){
		
		Properties properties = new Properties();
		FileInputStream stream ;
		try {
			stream = new FileInputStream(fileName);
			properties.load(stream);
			stream.close();
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

			RegisterMethods register = new RegisterMethods();
			MeltingComputationMethod calculMethod = register.getMeltingComputationMethod(environment.getOptions());
			ThermoResult results = calculMethod.computesThermodynamics();
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
	
	public static void displayResultsSodium(Properties properties, ArrayList<String> methodNames, String hybridization, String nucleotides, String option){
		NumberFormat format = NumberFormat.getInstance(); 
		format.setMaximumFractionDigits(2);
		Iterator<Map.Entry<Object, Object>> entry = properties.entrySet().iterator();
		while (entry.hasNext()){
			Map.Entry<Object, Object> pairs = entry.next();

			String [] coupleValues = pairs.getKey().toString().split("/");
			System.out.print("\n" + coupleValues[0] + "\t" + pairs.getValue());

			for (int i=0; i < methodNames.size(); i++){
				String [] argsOption = {"-H", hybridization, "-E", "Na=" + coupleValues[1], "-P", nucleotides, "-S", coupleValues[0], option, methodNames.get(i)}; 
				double Tm = MainTest.getMeltingTest(argsOption);
				System.out.print("\t" + format.format(Tm));
			}
		}
	}
	
	public static void displayResultsLongDanglingEnd(Properties properties, ArrayList<String> methodNames, String hybridization, String solution, String nucleotides, String option){
		NumberFormat format = NumberFormat.getInstance(); 
		format.setMaximumFractionDigits(2);
		
		Iterator<Map.Entry<Object, Object>> entry = properties.entrySet().iterator();
		while (entry.hasNext()){
			Map.Entry<Object, Object> pairs = entry.next();
			System.out.print("\n" + pairs.getKey() + "\t" + pairs.getValue());

			for (int i=0; i < methodNames.size(); i++){
				String [] argsOption = {"-H", hybridization, "-E", solution, "-P", nucleotides, "-S", pairs.getKey().toString(), option, methodNames.get(i), "-secDE", methodNames.get(i), "-sinDE", methodNames.get(i) }; 
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
	
	public static void main(String[] args) {
		System.out.println(MainTest.getCMeltingResult("-SAGCGTAATT -Hdnadna -N1 -P0.004"));

	}

	public static double getCMeltingResult(String args) {
		String path = "/home/compneur/Desktop/meltin_c/MELTING_SOURCE/";
		File execDir = new File(path + "BIN");

		try {
			ProcessBuilder pb = new ProcessBuilder((path
					+ "BIN/melting4_2h-linuxi386 " + args).split(" "));
			pb.directory(execDir).environment()
					.put("NN_PATH", path + "NNFILES");
			final Process meltProcess = pb.redirectErrorStream(true).start();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					meltProcess.getInputStream()));

			String line = null;
			while ((line = br.readLine()) != null) {
				int ind = line.indexOf("Melting temperature");
				if (ind != -1) {
					ind += "Melting temperature: ".length();
					int endInd = line.indexOf("C", ind) - 4;
					String resultValue = line.substring(ind, endInd);
					return Double.parseDouble(resultValue);
				}
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		throw new RuntimeException("No value found");
	}

	public static void displayResultsMeltingC(Properties properties, ArrayList<String> methodNames, String hybridization, String ion, String nucleotides){
		NumberFormat format = NumberFormat.getInstance(); 
		format.setMaximumFractionDigits(2);
		
		Iterator<Map.Entry<Object, Object>> entry = properties.entrySet().iterator();
		while (entry.hasNext()){
			Map.Entry<Object, Object> pairs = entry.next();
			System.out.print("\n" + pairs.getKey() + "\t" + pairs.getValue());

			for (int i=0; i < methodNames.size(); i++){
				String argsOption = "-H"+hybridization + " " + "-N"+ion + " " + "-P"+nucleotides + " " + "-S"+pairs.getKey().toString() + " " + "-A"+methodNames.get(i); 
				double Tm = MainTest.getCMeltingResult(argsOption);
				System.out.print("\t" + format.format(Tm));
			}
		}
	}
	
	public static void displayResultsMeltingCSelfComplementary(Properties properties, ArrayList<String> methodNames, String hybridization, String ion, String nucleotides){
		NumberFormat format = NumberFormat.getInstance(); 
		format.setMaximumFractionDigits(2);
		
		Iterator<Map.Entry<Object, Object>> entry = properties.entrySet().iterator();
		while (entry.hasNext()){
			Map.Entry<Object, Object> pairs = entry.next();
			System.out.print("\n" + pairs.getKey() + "\t" + pairs.getValue());

			for (int i=0; i < methodNames.size(); i++){
				String argsOption = "-H"+hybridization + " " + "-N"+ion + " " + "-P"+nucleotides + " " + "-S"+pairs.getKey().toString() + " " + "-A"+methodNames.get(i) + " " + "-F1"; 
				double Tm = MainTest.getCMeltingResult(argsOption);
				System.out.print("\t" + format.format(Tm));
			}
		}
	}
}
