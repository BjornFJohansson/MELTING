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

/*Sugimoto et al.(2001). Nucleic acids research 29 : 3289-3296*/

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

public class Sugimoto01Hydroxyadenine extends PatternComputation{
	
	public static String defaultFileName = "Sugimoto2001hydroxyAmn.xml";
	
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
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The hydroxyadenine model is from Sugimoto et al. (2001) (delta delta H and delta delta S): ");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		result = calculateThermodynamicsNoModifiedAcid(newSequences, pos1, pos2, result);
		Thermodynamics hydroxyAdenineValue = this.collector.getHydroxyadenosineValue(newSequences.getSequence(pos1,pos2), newSequences.getComplementary(pos1,pos2));
		double enthalpy = result.getEnthalpy() + hydroxyAdenineValue.getEnthalpy();
		double entropy = result.getEntropy() + hydroxyAdenineValue.getEntropy();
		
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " : incremented enthalpy = " + hydroxyAdenineValue.getEnthalpy() + "  incremented entropy = " + hydroxyAdenineValue.getEntropy());

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
		
		NucleotidSequences modified = environment.getSequences().getEquivalentSequences("dna");
		
		if (environment.getHybridization().equals("dnadna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for 2-hydroxyadenine base of" +
					"Sugimoto (2001) are established for DNA sequences.");
					}
				
		if (pos1 != 0 && pos2 != modified.getDuplexLength() - 1){
			String seq = modified.getSequenceContainig("A*", pos1, pos2);
			String comp = modified.getComplementaryTo(seq.toString(), pos1, pos2);
						
			if (seq.equals("TA*A")== false && seq.equals("GA*C") == false){
				isApplicable = false;
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for 2-hydroxyadenine terminal base of" +
				"Sugimoto (2001) are established for TA*A/AT or GA*C/CG sequences.");
			}
			else if ((seq.equals("TA*A")== true && comp.matches("A[AUTGC] T") == false) || (seq.equals("GA*C") == true && comp.matches("C[ATCGU] G") == false)){
				isApplicable = false;
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for 2-hydroxyadenine terminal base of" +
				"Sugimoto (2001) are established for TA*A/AT or GA*C/CG sequences.");
			}
			
			else {
				if ((modified.getDuplex().get(pos1).getTopAcid().equals("T") == false && modified.getDuplex().get(pos1).getTopAcid().equals("G") == false) || (modified.getDuplex().get(pos2).getTopAcid().equals("A") == false && modified.getDuplex().get(pos2).getTopAcid().equals("C") == false)){
					isApplicable = false;
					OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for 2-hydroxyadenine base of" +
					"Sugimoto (2001) are established for TA*A/ANT or GA*C/CNG sequences.");
				}
				else {
					if (modified.getDuplex().get(pos1).isComplementaryBasePair() == false || modified.getDuplex().get(pos1).isComplementaryBasePair() == false){
						isApplicable = false;
						OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for 2-hydroxyadenine base of" +
						"Sugimoto (2001) are established for TA*A/ANT or GA*C/CNG sequences.");
					}
				}
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
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		if (pos1 != 0 && pos2 != sequences.getDuplexLength() - 1){
			for (int i = pos1; i < pos2; i++){
				String[] pair = newSequences.getNNPairWithoutHydroxyA(i);
				if (this.collector.getNNvalue(pair[0], pair[1]) == null){
					OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + pair[0] + "/" + pair[1] + " are missing. Check the hydroxyadenine parameters.");
					return true;
				}
			}
		}
		else {
			return true;
		}
		
		if (this.collector.getHydroxyadenosineValue(newSequences.getSequence(pos1, pos2), newSequences.getComplementary(pos1,pos2)) == null){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + newSequences.getSequence(pos1,pos2) + "/" + newSequences.getComplementary(pos1,pos2) + " are missing. Check the hydroxyadenine parameters.");

			return true;
		}
		return super.isMissingParameters(newSequences, pos1, pos2);	
	}
	
	private ThermoResult calculateThermodynamicsNoModifiedAcid(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result){
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics NNValue;

		for (int i = pos1; i < pos2; i++){
			String[] pair = sequences.getNNPairWithoutHydroxyA(i);
			NNValue = this.collector.getNNvalue(pair[0], pair[1]);
			enthalpy += NNValue.getEnthalpy();
			entropy += NNValue.getEntropy();
			
			OptionManagement.meltingLogger.log(Level.FINE, pair[0] + "/" + pair[1] + " : enthalpy = " + NNValue.getEnthalpy() + "  entropy = " + NNValue.getEntropy());
		}
	
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
		if (pos1 > 1){
			pos1 --;
		}
		if (pos2 < duplexLength - 1){
			pos2 ++;
		}
		int [] positions = {pos1, pos2};
		return positions;
	}

}
