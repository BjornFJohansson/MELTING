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


/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */

package melting.patternModels.InternalLoops;

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

public class Santalucia04InternalLoop extends PatternComputation{
	
	public static String defaultFileName = "Santalucia2004longmm.xml";
	
	private static String formulaEnthalpy = "delat H = H(right terminal mismath) + H(left terminal mismatch) + H(asymmetric loop)";
	private static String formulaEntropy = "delat S = S(right terminal mismath) + S(left terminal mismatch) + S(asymmetric loop) + S(loop)";

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
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The internal loop model is from Santalucia (2004) : ");
		OptionManagement.meltingLogger.log(Level.FINE, formulaEnthalpy);
		OptionManagement.meltingLogger.log(Level.FINE, formulaEntropy);
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		Thermodynamics rightMismatch =  collector.getMismatchValue(newSequences.getSequenceNNPair(pos1), newSequences.getComplementaryNNPair(pos1));
		Thermodynamics leftMismatch =  collector.getMismatchValue(newSequences.getSequenceNNPair(pos2 - 2), newSequences.getComplementaryNNPair(pos2 - 2));

		OptionManagement.meltingLogger.log(Level.FINE, "Right terminal mismatch : " + newSequences.getSequenceNNPair(pos1) + "/" + newSequences.getComplementaryNNPair(pos1) + " : enthalpy = " + rightMismatch.getEnthalpy() + "  entropy = " + rightMismatch.getEntropy());
		OptionManagement.meltingLogger.log(Level.FINE, "Left terminal mismatch : " + newSequences.getSequenceNNPair(pos2 - 1) + "/" + newSequences.getComplementaryNNPair(pos2 - 1) + " : enthalpy = " + leftMismatch.getEnthalpy() + "  entropy = " + leftMismatch.getEntropy());

		double saltIndependentEntropy = result.getSaltIndependentEntropy();
		double enthalpy = result.getEnthalpy() + rightMismatch.getEnthalpy() + leftMismatch.getEnthalpy();
		double entropy = result.getEntropy()  + rightMismatch.getEntropy() + leftMismatch.getEntropy();
		
		int loopLength = newSequences.computesInternalLoopLength(pos1, pos2);
		Thermodynamics internalLoop = collector.getInternalLoopValue(Integer.toString(loopLength));
		if (internalLoop != null){
			OptionManagement.meltingLogger.log(Level.FINE, "Internal loop of" + loopLength + " :  enthalpy = " + internalLoop.getEnthalpy() + "  entropy = " + internalLoop.getEntropy());
			
			if (loopLength > 4){
				saltIndependentEntropy += internalLoop.getEntropy();
			}
			else {
				entropy += internalLoop.getEntropy();
			}
		}
		else {
			double value = collector.getInternalLoopValue("30").getEntropy() - 2.44 * 1.99 * Math.log(loopLength/30.0);
			
			OptionManagement.meltingLogger.log(Level.FINE, "Internal loop of" + loopLength + " :  enthalpy = 0" + "  entropy = " + value);

			if (loopLength > 4){				
				saltIndependentEntropy += value;
				
			}
			else {
				entropy += value;
			}		
		}
		
		if (newSequences.isAsymetricInternalLoop(pos1, pos2)){
			Thermodynamics asymmetry = collector.getAsymmetry();
			
			OptionManagement.meltingLogger.log(Level.FINE, "asymmetry : enthalpy = " + asymmetry.getEnthalpy() + "  entropy = " + asymmetry.getEntropy());
				
			enthalpy += asymmetry.getEnthalpy();

			if (loopLength > 4){
				saltIndependentEntropy += asymmetry.getEntropy();
			}
			else {
				entropy += asymmetry.getEntropy();
			}	
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		result.setSaltIndependentEntropy(saltIndependentEntropy);
		
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		int [] positions = correctPositions(pos1, pos2, environment.getSequences().getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, " the internal loop parameters of " +
					"Santalucia (2004) are originally established " +
					"for DNA sequences.");
		}
		
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);

		if (environment.getSequences().computesInternalLoopLength(pos1, pos2) == 2){
			OptionManagement.meltingLogger.log(Level.WARNING, "The internal loop parameter of Santalucia (2004) are not estblished for single mismatches.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) { 
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		boolean isMissingParameters = super.isMissingParameters(newSequences, pos1, pos2);
		
		if (this.collector.getInternalLoopValue(Integer.toString(newSequences.computesInternalLoopLength(pos1,pos2))) == null){
			if (this.collector.getInitiationLoopValue("30") == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for internal loop of 30 are missing. Check the internal loop parameters.");

				return true;
			}
		}
		if (newSequences.isAsymetricInternalLoop(pos1, pos2)){
			if (collector.getAsymmetry() == null) {
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for loop asymetry are missing. Check the internal loop parameters.");
				isMissingParameters = true;
			}
		}
		return isMissingParameters;
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);

		String singleMismatchName = options.get(OptionManagement.singleMismatchMethod);

		RegisterMethods register = new RegisterMethods();

		PatternComputationMethod singleMismatch = register.getPatternComputationMethod(OptionManagement.singleMismatchMethod, singleMismatchName);
		singleMismatch.initialiseFileName(singleMismatchName);
		String fileSingleMismatch = singleMismatch.getDataFileName(singleMismatchName);
		
		loadFile(fileSingleMismatch, this.collector);
	}
	
	private double calculateGibbs (String seq1, String seq2){
		double gibbs = Math.abs(seq1.length() - seq2.length()) * 0.3;
		return gibbs;
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
