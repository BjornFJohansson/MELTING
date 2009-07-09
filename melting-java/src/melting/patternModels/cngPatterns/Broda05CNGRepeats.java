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


/*REF: Broda et al (2005). Biochemistry 44: 10873-10882.*/

package melting.patternModels.cngPatterns;

import java.util.logging.Level;


import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.patternModels.PatternComputation;
import melting.sequences.NucleotidSequences;

public class Broda05CNGRepeats extends PatternComputation {	
	
	public static String defaultFileName = "Broda2005CNG.xml";

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
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n CNG motifs model : from Broda et al. (2005). \n");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		int repeats = (pos2 - pos1 - 1) / 3;
		Thermodynamics CNGValue = this.collector.getCNGvalue(Integer.toString(repeats), sequences.getSequence(pos1 + 1, pos1 + 3,"rna"));
		double enthalpy = result.getEnthalpy() + CNGValue.getEnthalpy();
		double entropy = result.getEntropy() + CNGValue.getEntropy();			
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		OptionManagement.meltingLogger.log(Level.FINE, "motif (" + sequences.getSequence(pos1 + 1, pos1 + 3) + ")" + repeats + " : " + "enthalpy = " + CNGValue.getEnthalpy() + "  entropy = " + CNGValue.getEntropy());
		
		return result;
	}
	
	
	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The nearest neighbor model for CNG repeats of Broda et al." +
					"(2005) is only established for RNA sequences.");
		}
		
		if (environment.getSequences().getSequence().charAt(pos1) != 'G' && environment.getSequences().getSequence().charAt(pos2) != 'C'){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for CNG repeats of Broda et al." +
			"(2005) are only established for CNG RNA sequences. The sequence must begin with a G/C base pair and end with a C/Gbase pair.");
		}
		
		if (environment.isSelfComplementarity() == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for CNG repeats of Broda et al." +
			"(2005) are only established for self complementary RNA sequences.");
			
			isApplicable = false;
		}

		return isApplicable;
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int repeats = (sequences.getDuplexLength() - 2) / 3;
		if (this.collector.getCNGvalue(Integer.toString(repeats), sequences.getSequence(pos1 + 1, pos1 + 3, "rna")) == null){
			return true;			
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}

}
