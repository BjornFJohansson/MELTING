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

/*Turner et al (2006)
 * Nucleic acids research 34: 3609-3614
 */

package melting.patternModels.cricksPair;

import java.util.logging.Level;


import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.exceptions.MethodNotApplicableException;
import melting.sequences.NucleotidSequences;

public class Turner06 extends CricksNNMethod {
	
	public static String defaultFileName = "Turner2006nn.xml";
	
	@Override
	public void initialiseFileName(String methodName){
		super.initialiseFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics NNValue;
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model is from Turner et al. (2006)");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);
 
		for (int i = pos1; i <= pos2 - 1; i++){
			
			NNValue = this.collector.getNNvalue("m" + sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i));
			
			OptionManagement.meltingLogger.log(Level.FINE, "m"+ sequences.getSequenceNNPair(i) + "/" + sequences.getComplementaryNNPair(i) + " : enthalpy = " + NNValue.getEnthalpy() + "  entropy = " + NNValue.getEntropy());
			
			enthalpy += NNValue.getEnthalpy();
			entropy += NNValue.getEntropy();
		}
		
		entropy = getEntropy1MNa(entropy, pos1, pos2);
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	@Override
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		if (environment.getHybridization().equals("mrnarna") == false && environment.getHybridization().equals("rnamrna") == false){
			isApplicable = false;
			OptionManagement.meltingLogger.log(Level.WARNING, "The model of Turner et al. (2006)" +
			"is established for 2-0-methyl RNA/RNA sequences.");
		}
		
		if (environment.isSelfComplementarity()){
			throw new MethodNotApplicableException ( "The thermodynamic parameters of Turner et al. (2006)" +
					"are established for hybrid mRNA/RNA sequences.");
		}
		return isApplicable;
	}
	
	@Override
	public ThermoResult calculateInitiationHybridation(Environment environment){
		int [] truncatedPositions =  environment.getSequences().removeTerminalUnpairedNucleotides();
		
		super.calculateInitiationHybridation(environment);
		
		double numberTerminalAU = environment.getSequences().calculateNumberOfTerminal("A", "U", truncatedPositions[0], truncatedPositions[1]);
		double enthalpy = 0.0;
		double entropy = 0.0;
		
		if (numberTerminalAU != 0) {
			Thermodynamics terminalAU = this.collector.getTerminal("per_A/U");
			
			OptionManagement.meltingLogger.log(Level.FINE, numberTerminalAU + " x penalty per terminal AU : enthalpy = " + terminalAU.getEnthalpy() + "  entropy = " + terminalAU.getEntropy());
			
			enthalpy += numberTerminalAU * terminalAU.getEnthalpy();
			entropy += numberTerminalAU * terminalAU.getEntropy();
		}
		
		environment.addResult(enthalpy, entropy);
		
		return environment.getResult();
	}
	
	private double getEntropy1MNa(double entropy0_1MNa, int pos1, int pos2){
		//sodium correction of Santalucia 1998_2004.
		double entropy1MNa = entropy0_1MNa - 0.368 * (Math.abs(pos2 - pos1)) * Math.log(0.1);
		
		return entropy1MNa;
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {

		boolean isMissing = false;
		for (int i = pos1; i <= pos2 - 1; i++){
			if (this.collector.getNNvalue("m" + sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)) == null){
				isMissing = true;
			}
		}
		return isMissing;
	}

}
