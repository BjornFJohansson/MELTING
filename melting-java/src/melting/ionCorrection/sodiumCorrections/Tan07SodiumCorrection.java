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

/* Zhi-Jie Tan and Shi-Jie Chen," RNA helix stability in Mixed Na+/Mg2+ solutions", 2007, 
	 * Biophysical Journal, 92, 3615-3632.
	 * */

package melting.ionCorrection.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.correctionMethods.EntropyCorrection;

public class Tan07SodiumCorrection extends EntropyCorrection {
	
	private static String entropyCorrection = "delta S(Na) = delta S(Na = 1M) - 3.22 x (duplexLength - 1) x g"; 
	private static String aFormula = "a1 = -0.075 x ln(Na) + 0.012 x ln(Mg)^2";
	private static String bFormula = "b1 = 0.018 x ln(Mg)^2";
	private static String gFormula = "g1 = a1 + b1 / duplexLength";
	
	@Override
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		double NaEq = Helper.computesNaEquivalent(environment);
		if (NaEq == 0){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium concentration must be a positive numeric value.");
			isApplicable = false;
		}
		
		else if (NaEq < 0.003 && NaEq > 1){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Zhi-Jie Tan et al. (2007)" +
					"is reliable for sodium concentrations between 0.003M and 1M.");
		}
		
		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Zhi-Jie Tan et al. (2007) is originally established for " +
			"RNA duplexes.");
		}
		return isApplicable;
	}
	
	@Override
	protected double correctEntropy(Environment environment){
		
		double entropy = -3.22 * ((double)environment.getSequences().getDuplexLength() - 1.0) * calculateFreeEnergyPerBaseStack(environment);
		
		return entropy;
	}
	
	@Override
	public ThermoResult correctMeltingResults(Environment environment) {
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The sodium correction from Zhi-Jie Tan et al. (2007) : ");
		OptionManagement.meltingLogger.log(Level.FINE,entropyCorrection);

		double NaEq = Helper.computesNaEquivalent(environment);
		environment.setNa(NaEq);
		
		return super.correctMeltingResults(environment);
	}
	
	public static double calculateFreeEnergyPerBaseStack(Environment environment){
		
		OptionManagement.meltingLogger.log(Level.FINE, "where : ");
		OptionManagement.meltingLogger.log(Level.FINE, gFormula);
		OptionManagement.meltingLogger.log(Level.FINE, aFormula);
		OptionManagement.meltingLogger.log(Level.FINE, bFormula);
		
		double Na = environment.getNa();
		double square = Math.log(Na) * Math.log(Na);
		double a = -0.075 * Math.log(Na) + 0.012 * square;
		double b = 0.018 * square;
		
		double g = a + b / (double)environment.getSequences().getDuplexLength();
		
		return g;
	}
}
