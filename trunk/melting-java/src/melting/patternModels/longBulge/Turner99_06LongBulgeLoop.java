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

/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924. 
	REF: Douglas M Turner et al (1999). J.Mol.Biol.  288: 911_940.*/ 

package melting.patternModels.longBulge;

import java.util.logging.Level;


import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.patternModels.PatternComputation;
import melting.sequences.NucleotidSequences;

public class Turner99_06LongBulgeLoop extends PatternComputation{
	
	public static String defaultFileName = "Turner1999_2006longbulge.xml";
	
	protected static String formulaEnthalpy = "delat H = H(bulge of n initiation) + number AU closing x H(AU closing) + number GU closing x H(GU closing)";
	
	@Override
	public void initialiseFileName(String methodName){
		super.initialiseFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	protected boolean isClosingPenaltyNecessary(){
		return true;
	}
	
	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];

		NucleotidSequences bulgeLoop = sequences.getEquivalentSequences("rna");
				
		OptionManagement.meltingLogger.log(Level.FINE, "\n The long bulge loop model is from Turner et al. (1999, 2006) : ");
		OptionManagement.meltingLogger.log(Level.FINE, formulaEnthalpy + " (entropy formula is similar)");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics initiationBulge = this.collector.getInitiationBulgevalue(bulgeSize);
		if (initiationBulge == null){
			initiationBulge = this.collector.getInitiationBulgevalue(">6");
			
			OptionManagement.meltingLogger.log(Level.FINE, "bulge loop of " + bulgeSize + " :  enthalpy = " + initiationBulge.getEnthalpy() + "  entropy = " + initiationBulge.getEntropy() + " / 310.15 x (8.7 - 1085.5 x ln( bulgeSize / 6)");

			enthalpy += initiationBulge.getEnthalpy();
			entropy += initiationBulge.getEntropy() / 310.15 * (8.7 - 1085.5 * Math.log( Double.parseDouble(bulgeSize) / 6.0));
		}
		else{
			OptionManagement.meltingLogger.log(Level.FINE, "bulge loop of " + bulgeSize + " :  enthalpy = " + initiationBulge.getEnthalpy() + "  entropy = " + initiationBulge.getEntropy());

			enthalpy += initiationBulge.getEnthalpy();
			entropy += initiationBulge.getEntropy();
		}
		
		if (isClosingPenaltyNecessary()){
			double numberAU = bulgeLoop.calculateNumberOfTerminal("A", "U", pos1, pos2);
			double numberGU = bulgeLoop.calculateNumberOfTerminal("G", "U", pos1, pos2);
			if (numberAU > 0){
				Thermodynamics closingAU = this.collector.getClosureValue("A", "U");
				
				OptionManagement.meltingLogger.log(Level.FINE, numberAU + " x AU closing : enthalpy = " + closingAU.getEnthalpy() + "  entropy = " + closingAU.getEntropy());

				enthalpy += numberAU * closingAU.getEnthalpy();
				entropy += numberAU * closingAU.getEntropy();
			}
			
			if (numberGU > 0){
				Thermodynamics closingGU = this.collector.getClosureValue("G", "U");
				
				OptionManagement.meltingLogger.log(Level.FINE, numberGU + " x GU closing : enthalpy = " + closingGU.getEnthalpy() + "  entropy = " + closingGU.getEntropy());
				
				enthalpy += numberGU * closingGU.getEnthalpy();
				entropy += numberGU * closingGU.getEntropy();
			}
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {

		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The long bulge loop parameters of " +
					"Turner (1999-2006) are originally established " +
					"for RNA sequences.");
		}
		
		return super.isApplicable(environment, pos1, pos2);
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];

		NucleotidSequences bulgeLoop = sequences.getEquivalentSequences("rna");
		
		double numberAU = bulgeLoop.calculateNumberOfTerminal("A", "U", pos1, pos2);
		double numberGU = bulgeLoop.calculateNumberOfTerminal("G", "U", pos1, pos2);
		boolean isMissingParameters = super.isMissingParameters(sequences, pos1, pos2);
		
		if (numberAU > 0){
			if (this.collector.getClosureValue("A", "U") == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for AU closing base pair are missing. Check the long bulge parameters.");
				return true;
			}
		}
		
		if (numberGU > 0){
			if (this.collector.getClosureValue("G", "U") == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for GU closing base pair are missing. Check the long bulge parameters.");
				return true;
			}
		}
		
		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		if (this.collector.getInitiationBulgevalue(bulgeSize) == null){
			if (this.collector.getInitiationBulgevalue("6") == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for a bulge loop of " + bulgeSize + " are missing. Check the long bulge parameters.");

				return true;
			}
		}
		return isMissingParameters;
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
