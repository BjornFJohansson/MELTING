
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

package melting.patternModels.cricksPair;

import java.util.logging.Level;


import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.sequences.NucleotidSequences;

/**
 * This class represents the nearest neighbor model san04. It extends CricksNNMethod.
 * 
 * Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440
 */
public class Santalucia04 extends CricksNNMethod {
	
	// Instance variable
	
	/**
	 * String defaultFileName : default name for the xml file containing the thermodynamic parameters for each Crick's pair
	 */
	public static String defaultFileName = "Santalucia2004nn.xml";
	
	// PatternComputationMethod interface implementation

	@Override
	public boolean isApplicable(Environment environment, int pos1, int pos2) {

		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The model of Santalucia (2004)" +
			"is established for DNA sequences.");
		}
		return super.isApplicable(environment, pos1, pos2);
	}
	
	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model is from Santalucia et al (2004).");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		return super.computeThermodynamics(newSequences, pos1, pos2, result);
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		return super.isMissingParameters(newSequences, pos1, pos2);
	}
	
	@Override
	public void initialiseFileName(String methodName){
		super.initialiseFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	// Inherited method

	@Override
	public ThermoResult computesHybridizationInitiation(Environment environment){

		NucleotidSequences newSequences = environment.getSequences().getEquivalentSequences("dna");
		
		int [] truncatedPositions = newSequences.removeTerminalUnpairedNucleotides();

		super.computesHybridizationInitiation(environment);

		double numberTerminalAT = newSequences.calculateNumberOfTerminal("A", "T", truncatedPositions[0], truncatedPositions[1]);
		double enthalpy = 0.0;
		double entropy = 0.0;
		
		if (numberTerminalAT != 0){
			Thermodynamics terminalAT = this.collector.getTerminal("per_A/T");
			
			OptionManagement.meltingLogger.log(Level.FINE, numberTerminalAT + " x penalty per terminal AT : enthalpy = " + terminalAT.getEnthalpy() + "  entropy = " + terminalAT.getEntropy());
			
			enthalpy += numberTerminalAT * terminalAT.getEnthalpy();
			entropy += numberTerminalAT * terminalAT.getEntropy();
		}
		
		environment.addResult(enthalpy, entropy);
		
		return environment.getResult();
		
	}

}