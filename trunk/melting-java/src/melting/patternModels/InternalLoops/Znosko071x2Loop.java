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


/*REF: Brent M Znosko et al (2007). Biochemistry 46: 14715-14724. */

package melting.patternModels.InternalLoops;

import java.util.logging.Level;


import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.patternModels.PatternComputation;
import melting.sequences.NucleotidSequences;

public class Znosko071x2Loop extends PatternComputation {
	
	public static String defaultFileName = "Znosko20071x2loop.xml";
	private static String formulaEnthalpy = "delat H = H(first mismath) + H(initiation 1x2 loop) + number AU closing x H(closing AU) + number GU closing x H(closing GU)";
	
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

		NucleotidSequences internalLoop = sequences.getEquivalentSequences("rna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The 1 x 2 internal loop model is from Znosco et al. (2007) : ");
		OptionManagement.meltingLogger.log(Level.FINE,formulaEnthalpy + " (entropy formula is similar)");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		String [] mismatch = internalLoop.getLoopFistMismatch(pos1);
		double numberAU = internalLoop.calculateNumberOfTerminal("A", "U", pos1, pos2);
		double numberGU = internalLoop.calculateNumberOfTerminal("G", "U", pos1, pos2);
		
		Thermodynamics initiationLoop = this.collector.getInitiationLoopValue();
		
		OptionManagement.meltingLogger.log(Level.FINE, "1x2 Internal loop :  enthalpy = " + initiationLoop.getEnthalpy() + "  entropy = " + initiationLoop.getEntropy());

		double enthalpy = result.getEnthalpy() + initiationLoop.getEnthalpy();
		double entropy = result.getEntropy() + initiationLoop.getEntropy();
		
		Thermodynamics firstMismatch; 
		if (sequences.getDuplex().get(pos1 + 1).isBasePairEqualTo("G", "A")){
			if (this.collector.getFirstMismatch("A", "G_not_RA/YG", "1x2") == null){
				firstMismatch = new Thermodynamics(0,0);
			}
			else {
				firstMismatch = this.collector.getFirstMismatch("A", "G_not_RA/YG", "1x2");
			}
			
			OptionManagement.meltingLogger.log(Level.FINE, "First mismatch A/G, not RA/YG : enthalpy = " + firstMismatch.getEnthalpy() + "  entropy = " + firstMismatch.getEntropy());
		}
		else {
			if (this.collector.getFirstMismatch(mismatch[0], mismatch[1], "1x2") == null){
				firstMismatch = new Thermodynamics(0,0);
			}
			else {
				firstMismatch = this.collector.getFirstMismatch(mismatch[0], mismatch[1], "1x2");
			}
			
			OptionManagement.meltingLogger.log(Level.FINE, "First mismatch " + mismatch[0] + "/" + mismatch[1] + " : enthalpy = " + firstMismatch.getEnthalpy() + "  entropy = " + firstMismatch.getEntropy());
		}
		enthalpy += firstMismatch.getEnthalpy();
		entropy += firstMismatch.getEntropy();
		
		if (numberAU > 0){
			Thermodynamics closureAU = this.collector.getClosureValue("A", "U");
			
			OptionManagement.meltingLogger.log(Level.FINE, numberAU + " x AU closure : enthalpy = " + closureAU.getEnthalpy() + "  entropy = " + closureAU.getEntropy());

			enthalpy += numberAU * closureAU.getEnthalpy();
			entropy += numberAU * closureAU.getEntropy();
			
		}
		
		if (numberGU > 0){
			Thermodynamics closureGU = this.collector.getClosureValue("G", "U");
			
			OptionManagement.meltingLogger.log(Level.FINE, numberGU + " x GU closure : enthalpy = " + closureGU.getEnthalpy() + "  entropy = " + closureGU.getEntropy());

			enthalpy += numberGU * closureGU.getEnthalpy();
			entropy += numberGU * closureGU.getEntropy();
			
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		int [] positions = correctPositions(pos1, pos2, environment.getSequences().getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		String loopType = environment.getSequences().getInternalLoopType(pos1,pos2);

		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, " The internal 1x2 loop parameters of " +
					"Znosco et al. (2007) are originally established " +
					"for RNA sequences.");
			
		}
		
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		if (loopType.equals("1x2") == false && loopType.equals("2x1") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, " The thermodynamic parameters of Znosco et al. (2007) are" +
					"established only for 1x2 internal loop.");
			
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

		NucleotidSequences internalLoop = sequences.getEquivalentSequences("rna");

		boolean isMissingParameters = super.isMissingParameters(sequences, pos1, pos2);
		if (this.collector.getInitiationLoopValue() == null){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for loop initiation are missing. Check the internal loop parameters.");

			return true;
		}
		
		if (internalLoop.calculateNumberOfTerminal("A", "U", pos1, pos2) > 0){
			if (this.collector.getClosureValue("A", "U") == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for AU closing are missing. Check the internal loop parameters.");

				return true;
			}
		}
		
		if (internalLoop.calculateNumberOfTerminal("G", "U", pos1, pos2) > 0){
			if (this.collector.getClosureValue("G", "U") == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for GU closing are missing. Check the internal loop parameters.");

				return true;
			}
		}
		return isMissingParameters;
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
