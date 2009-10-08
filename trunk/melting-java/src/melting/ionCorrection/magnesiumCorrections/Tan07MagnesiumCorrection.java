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

package melting.ionCorrection.magnesiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.configuration.OptionManagement;
import melting.correctionMethods.EntropyCorrection;

/**
 * This class represents the magnesium correction model tanmg07. It extends EntropyCorrection.
 * 
 * Zhi-Jie Tan and Shi-Jie Chen," RNA helix stability in Mixed Na+/Mg2+ solutions", 2007, 
 * Biophysical Journal, 92, 3615-3632.
 */
public class Tan07MagnesiumCorrection extends EntropyCorrection {
	
	// Instance variables

	/**
	 * String entropyCorrection : formula for the entropy correction
	 */
	
	protected static String entropyCorrection = "delta S(Mg) = delta S(Na = 1M) - 3.22 x (duplexLength - 1) x g"; 
	
	protected static String aFormula = "a2 = -0.6 / duplexLength + 0.025 x ln(Mg) + 0.0068 x ln(Mg)^2";
	
	protected static String bFormula = "b2 = ln(Mg) + 0.38 x ln(Mg)^2";
	
	/**
	 * String gFormula : function associated with the electrostatic folding free energy per base stack.
	 */
	protected static String gFormula = "g2 = a2 + b2 / (duplexLength^2)";
	
	// CorrectionMethod interface implementation

	@Override
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		if (environment.getMg() == 0){
			OptionManagement.meltingLogger.log(Level.WARNING, "\n The magnesium concentration must be a positive numeric value.");
			isApplicable = false;
		}
		
		else if (environment.getMg() < 0.1 && environment.getMg() > 0.3){
			OptionManagement.meltingLogger.log(Level.WARNING, "\n The magnesium correction of Zhi-Jie Tan et al. (2007)" +
					"is reliable for magnseium concentrations between 0.1M and 0.3M.");
		}
		
		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "\n The magnesium correction of Zhi-Jie Tan et al. (2007) is originally established for " +
			"RNA duplexes.");
		}
		return isApplicable;
	}
	
	// Inherited method

	@Override
	protected double correctEntropy(Environment environment){

		OptionManagement.meltingLogger.log(Level.FINE, "\n The magnesium correction from Zhi-Jie Tan et al. (2007) : ");
		OptionManagement.meltingLogger.log(Level.FINE,entropyCorrection);

		double entropy = -3.22 * ((double)environment.getSequences().getDuplexLength() - 1) * computeFreeEnergyPerBaseStack(environment);
		
		return entropy;
	}
	
	// public static method

	/**
	 * represents the function associated with the electrostatic folding free energy per base stack.
	 * @param environment
	 * @return double g2 which represents the result of the function associated with the electrostatic folding free energy per base stack.
	 */
	public static double computeFreeEnergyPerBaseStack(Environment environment){
		OptionManagement.meltingLogger.log(Level.FINE, "where : ");
		OptionManagement.meltingLogger.log(Level.FINE, gFormula);
		OptionManagement.meltingLogger.log(Level.FINE, aFormula);
		OptionManagement.meltingLogger.log(Level.FINE, bFormula);
		
		double Mg = environment.getMg() - environment.getDNTP();
		double duplexLength = (double)environment.getSequences().getDuplexLength();
		
		double square = Math.log(Mg) * Math.log(Mg);
		double a = -0.6 / duplexLength + 0.025 * Math.log(Mg) + 0.0068 * square;
		double b = Math.log(Mg) + 0.38 * square;
		double g = a + b / (duplexLength * duplexLength);
		
		return g;
	}
}
