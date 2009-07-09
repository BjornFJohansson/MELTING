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

public abstract class GlobalInitiationNNMethod extends CricksNNMethod {
	
	@Override
	public ThermoResult calculateInitiationHybridation(Environment environment){
		
		super.calculateInitiationHybridation(environment);

		double enthalpy = 0.0;
		double entropy = 0.0;

		if (environment.getSequences().isOneGCBasePair()){
			Thermodynamics initiationOneGC = this.collector.getInitiation("one_GC_Pair");

			OptionManagement.meltingLogger.log(Level.FINE, "\n The initiation if there is at least one GC base pair : enthalpy = " + initiationOneGC.getEnthalpy() + "  entropy = " + initiationOneGC.getEntropy());

			enthalpy += initiationOneGC.getEnthalpy();
			entropy += initiationOneGC.getEntropy();
		}
		
		else {
			Thermodynamics initiationAllAT = this.collector.getInitiation("all_AT_pairs");
			
			OptionManagement.meltingLogger.log(Level.FINE, "\n The initiation if there is only AT base pairs : enthalpy = " + initiationAllAT.getEnthalpy() + "  entropy = " + initiationAllAT.getEntropy());
			enthalpy += initiationAllAT.getEnthalpy();
			entropy += initiationAllAT.getEntropy();
		}
		
		environment.addResult(enthalpy, entropy);
		
		return environment.getResult();
	}

}
