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

package melting.patternModels.singleMismatch;

import java.util.logging.Level;


import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.sequences.NucleotidSequences;

/**
 * This class represents the single mismatch model zno07. It extends the ZnoskoMethod class.
 * 
 * Brent M Znosko et al (2007). Biochemistry 46: 13425-13436.
 */
public class Znosko07mm extends ZnoskoMethod {
	
	// Instance variables
	
	/**
	 * String defaultFileName : default name for the xml file containing the thermodynamic parameters for single mismatch
	 */
	public static String defaultFileName = "Znosko2007mm.xml";
	
	// PatternComputationMethod interface implementation

	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		int [] positions = super.correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];

		NucleotidSequences newSequences = sequences.getEquivalentSequences("rna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The model for single mismatches is from Znosco et al. (2007) : ");
		OptionManagement.meltingLogger.log(Level.FINE,formulaEnthalpy + " (entropy formula is similar)");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		return super.computeThermodynamics(newSequences, pos1, pos2, result);
	}
	
	@Override
	public void initialiseFileName(String methodName){
		super.initialiseFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}


}
