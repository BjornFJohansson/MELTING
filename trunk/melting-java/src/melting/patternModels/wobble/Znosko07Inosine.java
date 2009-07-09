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

package melting.patternModels.wobble;

/*Brent M Znosko et al. (2005). Biochemistry 46 : 4625-4634 */

import java.util.logging.Level;


import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.sequences.NucleotidSequences;

public class Znosko07Inosine extends InosineNNMethod {
	
	public static String defaultFileName = "Znosko2007inomn.xml";
	
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
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences inosine = sequences.getEquivalentSequences("rna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for inosine is from Znosco et al. (2007) : ");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		result = super.computeThermodynamics(inosine, pos1, pos2, result);
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		double numberIU = inosine.calculateNumberOfTerminal("I", "U", pos1, pos2);
		
		if ((pos1 == 0 || pos2 == sequences.getDuplexLength() - 1) && numberIU > 0) {
			Thermodynamics terminaIU = this.collector.getTerminal("per_I/U");
			OptionManagement.meltingLogger.log(Level.FINE, numberIU + " x terminal IU : enthalpy = " + terminaIU.getEnthalpy() + "  entropy = " + terminaIU.getEntropy());

			enthalpy += numberIU * terminaIU.getEnthalpy();
			entropy += numberIU * terminaIU.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}
	
	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		NucleotidSequences inosine = environment.getSequences().getEquivalentSequences("rna");
		
		if (environment.getHybridization().equals("rnarna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for inosine base of" +
					"Znosco (2007) are established for RNA sequences.");
		
		}
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);

		for (int i = 0; i < inosine.getDuplexLength() - 1; i++){
			if ((inosine.getSequence().charAt(i) == 'I' || inosine.getComplementary().charAt(i) == 'I') && inosine.getDuplex().get(i).isBasePairEqualTo("I", "U") == false){
				isApplicable = false;
				OptionManagement.meltingLogger.log(Level.WARNING, " The thermodynamic parameters of Znosco" +
						"(2007) are only established for IU base pairs.");
				break;
			}
		}
		return isApplicable;
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences inosine = sequences.getEquivalentSequences("rna");
		
		double numberIU = inosine.calculateNumberOfTerminal("I", "U", pos1, pos2);
		
		if ((pos1 == 0 || pos2 == inosine.getDuplexLength() - 1) && numberIU > 0){
			if (this.collector.getTerminal("per_I/U") == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameter for terminal IU base pair is missing.");
				return true;
			}
		}
		return false;
	}
	
}
