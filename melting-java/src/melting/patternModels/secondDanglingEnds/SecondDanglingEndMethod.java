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

package melting.patternModels.secondDanglingEnds;

import java.util.HashMap;
import java.util.logging.Level;


import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterMethods;
import melting.methodInterfaces.PatternComputationMethod;
import melting.patternModels.PatternComputation;
import melting.sequences.NucleotidSequences;

public abstract class SecondDanglingEndMethod extends PatternComputation {

	
	@Override
	public abstract ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result);
	
	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);

		int [] positions = correctPositions(pos1, pos2, environment.getSequences().getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for second dangling end of Serra et al." +
					"(2005 and 2006) is established for RNA sequences.");
		}
				
		if (NucleotidSequences.getDanglingSens(environment.getSequences().getSequence(pos1, pos2), environment.getSequences().getComplementary(pos1, pos2)).equals("5")){
			isApplicable = false;
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for second dangling end of Serra et al." +
					"(2005 and 2006) is only established for 3' second dangling end.");
		}
		
		return isApplicable;
	}
	
	public ThermoResult calculateThermodynamicsWithoutSecondDanglingEnd(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		String gapSequence = sequences.getSequenceContainig("-", pos1, pos2);
		
		Thermodynamics danglingValue;
		if (gapSequence.charAt(0) == '-'){
			danglingValue = this.collector.getDanglingValue(sequences.getSequence(pos1 + 1, pos2),sequences.getComplementary(pos1 + 1, pos2));

		}
		else{
			danglingValue = this.collector.getDanglingValue(sequences.getSequence(pos1, pos2 - 1),sequences.getComplementary(pos1, pos2 - 1));
		}
		double enthalpy = result.getEnthalpy() + danglingValue.getEnthalpy();
		double entropy = result.getEntropy() + danglingValue.getEntropy();
		
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2 - 1) + "/" + sequences.getComplementary(pos1, pos2 - 1) + " : enthalpy = " + danglingValue.getEnthalpy() + "  entropy = " + danglingValue.getEntropy());

		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		String gapSequence = sequences.getSequenceContainig("-", pos1, pos2);
		
		if (gapSequence.charAt(0) == '-'){
			if (this.collector.getDanglingValue(sequences.getSequence(pos1 + 1, pos2),sequences.getComplementary(pos1 + 1, pos2)) == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + sequences.getSequence(pos1 + 1, pos2) + "/" + sequences.getComplementary(pos1 + 1, pos2) + " are missing. Check the single dangling ends parameters.");
				return true;
			}
		}
		else{
			if (this.collector.getDanglingValue(sequences.getSequence(pos1, pos2 - 1),sequences.getComplementary(pos1, pos2 - 1)) == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + sequences.getSequence(pos1 + 1, pos2) + "/" + sequences.getComplementary(pos1 + 1, pos2) + " are missing. Check the single dangling ends parameters.");
				return true;
			}
		}
		
		return super.isMissingParameters(sequences, pos1, pos2);
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);
		
		String singleDanglingName = options.get(OptionManagement.singleDanglingEndMethod);
		RegisterMethods register = new RegisterMethods();
		PatternComputationMethod singleDangling = register.getPatternComputationMethod(OptionManagement.singleDanglingEndMethod, singleDanglingName);
		singleDangling.initialiseFileName(singleDanglingName);

		String fileDoubleDangling = singleDangling.getDataFileName(singleDanglingName);
		
		
		loadFile(fileDoubleDangling, this.collector);
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
