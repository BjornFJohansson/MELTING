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

/*McTigue et al.(2004). Biochemistry 43 : 5388-5405 */

package melting.patternModels.specificAcids;

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

public class McTigue04LockedAcid extends PatternComputation{
		
	public static String defaultFileName = "McTigue2004lockedmn.xml";
	
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
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The locked acid nuceic model is from McTigue et al. (2004) (delta delta H and delta delta S): ");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		result = calculateThermodynamicsNoModifiedAcid(newSequences, pos1, pos2, result);
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics lockedAcidValue;
		
		for (int i = pos1; i < pos2; i++){
			lockedAcidValue = this.collector.getLockedAcidValue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i));

			OptionManagement.meltingLogger.log(Level.FINE, newSequences.getSequenceNNPair(i) + "/" + newSequences.getComplementaryNNPair(i) + " : incremented enthalpy = " + lockedAcidValue.getEnthalpy() + "  incremented entropy = " + lockedAcidValue.getEntropy());

			enthalpy += lockedAcidValue.getEnthalpy();
			entropy += lockedAcidValue.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);

		int [] positions = correctPositions(pos1, pos2, environment.getSequences().getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		if (environment.getHybridization().equals("dnadna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for locked acid nucleiques of" +
					"McTigue et al. (2004) are established for DNA sequences.");
		}
				
		if ((pos1 == 0 || pos2 == environment.getSequences().getDuplexLength() - 1) && environment.getSequences().calculateNumberOfTerminal("L", "-", pos1, pos2) > 0){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamics parameters for locked acid nucleiques of " +
					"McTigue (2004) are not established for terminal locked acid nucleiques.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		if (this.collector.getNNvalue(newSequences.getSequenceNNPairUnlocked(pos1), newSequences.getComplementaryNNPairUnlocked(pos1)) == null || this.collector.getNNvalue(newSequences.getSequenceNNPairUnlocked(pos1 + 1), newSequences.getComplementaryNNPairUnlocked(pos1 + 1)) == null){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + newSequences.getSequenceNNPairUnlocked(pos1) + "/" + newSequences.getComplementaryNNPairUnlocked(pos1) + " or " + newSequences.getSequenceNNPairUnlocked(pos1 + 1) + "/" + newSequences.getComplementaryNNPairUnlocked(pos1 + 1) +
			" are missing. Check the locked nucleic acid parameters.");
			return true;
		}
		
		for (int i = pos1; i < pos2; i++){
			if (this.collector.getLockedAcidValue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)) == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + sequences.getSequenceNNPair(i) + "/" + sequences.getComplementaryNNPair(i) +
				"are missing. Check the locked nucleic acid parameters.");
				return true;
			}
		}
		
		return super.isMissingParameters(newSequences, pos1, pos2);
	}

	private ThermoResult calculateThermodynamicsNoModifiedAcid(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result){

		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		Thermodynamics firstNNValue = collector.getNNvalue(newSequences.getSequenceNNPairUnlocked(pos1), newSequences.getComplementaryNNPairUnlocked(pos1));
		Thermodynamics secondNNValue = collector.getNNvalue(newSequences.getSequenceNNPairUnlocked(pos1 + 1), newSequences.getComplementaryNNPairUnlocked(pos1 + 1));
		double enthalpy = result.getEnthalpy() + firstNNValue.getEnthalpy() + secondNNValue.getEnthalpy();
		double entropy = result.getEntropy() + firstNNValue.getEntropy()  + secondNNValue.getEntropy();
		
		OptionManagement.meltingLogger.log(Level.FINE, newSequences.getSequenceNNPairUnlocked(pos1) + "/" + newSequences.getComplementaryNNPairUnlocked(pos1) + " : enthalpy = " + firstNNValue.getEnthalpy() + "  entropy = " + firstNNValue.getEntropy());
		OptionManagement.meltingLogger.log(Level.FINE, newSequences.getSequenceNNPairUnlocked(pos1+1) + "/" + newSequences.getComplementaryNNPairUnlocked(pos1+1) + " : enthalpy = " + secondNNValue.getEnthalpy() + "  entropy = " + secondNNValue.getEntropy());

		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);
		
		String crickName = options.get(OptionManagement.NNMethod);
		RegisterMethods register = new RegisterMethods();
		PatternComputationMethod NNMethod = register.getPatternComputationMethod(OptionManagement.NNMethod, crickName);
		NNMethod.initialiseFileName(crickName);

		String NNfile = NNMethod.getDataFileName(crickName);
		
		
		loadFile(NNfile, this.collector);
	}
	
	private int[] correctPositions(int pos1, int pos2, int duplexLength){
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
