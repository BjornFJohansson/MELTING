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

package melting;

import java.text.NumberFormat;
import java.util.logging.Level;

import melting.configuration.OptionManagement;
import melting.configuration.RegisterMethods;
import melting.methodInterfaces.MeltingComputationMethod;
import melting.nearestNeighborModel.NearestNeighborMode;

/**
 * The Melting main class which contains the public static void main(String[] args) method.
 */
public class Main {

	// private static methods
	
	/**
	 * compute the entropy, enthalpy and the melting temperature and display the results. 
	 * @param args : contains the options entered by the user.
	 * @param OptionManagement optionManager : the OptionManegement which allows to manage
	 * the different options entered by the user.
	 */
	private static void runMelting(String [] args, OptionManagement optionManager){
		try {
			NumberFormat format = NumberFormat.getInstance(); 
			format.setMaximumFractionDigits(2);

			Environment environment = optionManager.createEnvironment(args);
			RegisterMethods register = new RegisterMethods();
			MeltingComputationMethod calculMethod = register.getMeltingComputationMethod(environment.getOptions());
			ThermoResult results = calculMethod.computesThermodynamics();
			environment.setResult(results);

			results = calculMethod.getRegister().computeOtherMeltingCorrections(environment);
				
			environment.setResult(results);
			displaysMeltingResults(environment.getResult(), calculMethod);
			
		} catch (Exception e) {
			OptionManagement.meltingLogger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	/**
	 * displays the results of Melting : the computed enthalpy and entropy (in cal/mol and J/mol), and the computed 
	 * melting temperature (in degrees).
	 * @param results : the ThermoResult containing the computed enthalpy, entropy and
	 * melting temperature
	 * @param MeltingComputationMethod calculMethod : the melting computation method (Approximative or nearest neighbor computation)
	 */
	private static void displaysMeltingResults(ThermoResult results, MeltingComputationMethod calculMethod){
		NumberFormat format = NumberFormat.getInstance(); 
		format.setMaximumFractionDigits(2);
		
		double enthalpy = results.getEnthalpy();
		double entropy = results.getEntropy();

		OptionManagement.meltingLogger.log(Level.INFO, "\n The MELTING results are : ");
		if (calculMethod instanceof NearestNeighborMode){
			OptionManagement.meltingLogger.log(Level.INFO, "Enthalpy : " + format.format(enthalpy) + " cal/mol ( " + format.format(results.getEnergyValueInJ(enthalpy)) + " J /mol)");
			OptionManagement.meltingLogger.log(Level.INFO, "Entropy : " + format.format(entropy) + " cal/mol ( " + format.format(results.getEnergyValueInJ(entropy)) + " J /mol)");
		}
		OptionManagement.meltingLogger.log(Level.INFO, "Melting temperature : " + format.format(results.getTm()) + " degres C.");
	}
	
	// public static main method
	
	/**
	 * @param args : contains the options entered by the user.
	 */
	public static void main(String[] args) {
	
		OptionManagement optionManager = new OptionManagement();
		
		if (args.length == 0){
			optionManager.initialiseLogger();
			optionManager.readMeltingHelp();
		}
		else if (optionManager.isMeltingInformationOption(args)){
			try {
				optionManager.readOptions(args);

			} catch (Exception e) {
				OptionManagement.meltingLogger.log(Level.SEVERE, e.getMessage());
			}
		}
		else {
			runMelting(args, optionManager);
		}
	}
}
