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

package melting.patternModels.singleBulge;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.sequences.NucleotidSequences;

/**
 * This class represents the single bulge loop model tan04. It extends the GlobalSingleBulgeLoopMethod class.
 * 
 * Tanaka Fumiaki et al (2004). Biochemistry 43 : 7143-7150
 */
public class Tanaka04SingleBulgeLoop extends GlobalSingleBulgeLoopMethod{
	
	// Instance variables
	
	/**
	 * String defaultFileName : default name for the xml file containing the thermodynamic parameters for single bulge loop
	 */
	public static String defaultFileName = "Tanaka2004bulge.xml";
	
	// PatternComputationMethod interface implementation

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {

		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The single bulge loop parameters of " +
					"Tanaka (2004) are originally established " +
					"for DNA sequences.");
		}
	
		return super.isApplicable(environment, pos1, pos2);
	}

	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		int [] positions = super.correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for single bulge loop is from Tanaka et al. (2004) : ");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		return super.computeThermodynamics(newSequences, pos1, pos2, result);
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
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
}