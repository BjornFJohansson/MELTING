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


import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.patternModels.PatternComputation;
import melting.sequences.NucleotidSequences;

public abstract class GlobalSingleBulgeLoopMethod extends PatternComputation{
	
	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		Thermodynamics singleBulge = this.collector.getSingleBulgeLoopvalue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		double enthalpy = result.getEnthalpy() + singleBulge.getEnthalpy();
		double entropy = result.getEntropy() + singleBulge.getEntropy();
		
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " : enthalpy = " + singleBulge.getEnthalpy() + "  entropy = " + singleBulge.getEntropy());
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);

		return result;
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		if (this.collector.getSingleBulgeLoopvalue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)) == null){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " are missing. Check the single bulge loop parameters.");

			return true;
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}

	protected int[] correctPositions(int pos1, int pos2, int duplexLength){
		if (pos1 > 0){
			pos1 --;
		}
		if (pos2 < duplexLength - 1){
			pos2 ++;
		}
		int [] positions = {pos1, pos2};
		return positions;
	}
}
