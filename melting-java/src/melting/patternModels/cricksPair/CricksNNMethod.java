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
import melting.patternModels.PatternComputation;
import melting.sequences.NucleotidSequences;

public abstract class CricksNNMethod extends PatternComputation{
	
	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics NNValue;
		for (int i = pos1; i <= pos2 - 1; i++){
			NNValue = this.collector.getNNvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i));
			OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequenceNNPair(i) + "/" + sequences.getComplementaryNNPair(i) + " : enthalpy = " + NNValue.getEnthalpy() + "  entropy = " + NNValue.getEntropy());
			
			enthalpy += NNValue.getEnthalpy();
			entropy += NNValue.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
				
		return result;
	}
	
	public ThermoResult calculateInitiationHybridation(Environment environment){
		double enthalpy = 0.0;
		double entropy = 0.0;

		Thermodynamics initiation = this.collector.getInitiation();

		
		if (initiation != null) {
			OptionManagement.meltingLogger.log(Level.FINE, "\n Initiation : enthalpy = " + initiation.getEnthalpy() + "  entropy = " + initiation.getEntropy());
			
			enthalpy += initiation.getEnthalpy();
			entropy += initiation.getEntropy();
		}
		
		if (environment.isSelfComplementarity()){
			Thermodynamics symmetry = this.collector.getsymmetry();
			
			OptionManagement.meltingLogger.log(Level.FINE, "Self complementarity : enthalpy = " + symmetry.getEnthalpy() + "  entropy = " + symmetry.getEntropy());
			
			enthalpy += symmetry.getEnthalpy();
			entropy += symmetry.getEntropy();
		}
		
		environment.addResult(enthalpy, entropy);
		
		return environment.getResult();
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1, int pos2){

		for (int i = pos1; i <= pos2 - 1; i++){
			if (collector.getNNvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)) == null) {
				return true;
			}
		}
		return false;
	}
}
