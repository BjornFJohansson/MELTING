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

package melting.ionCorrection.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.methodInterfaces.CorrectionMethod;

/**
 * This class represents the sodium correction model marschdot. It implements the CorrectionMethod interface.
 * 
 * Blake, R. D., and Delcourt, S. G. (1998) Thermal stability of DNA,
 * Nucleic Acids Res. 26, 3323-3332 and corrigendum.
 * 
 *  Marmur, J., and Doty, P. (1962) Determination of the base
 *  composition of deoxyribonucleic acid from its thermal denaturation
 *  temperature, J. Mol. Biol. 5, 109-118.
 */
public class MarmurSchildkrautDoty98_62SodiumCorrection implements CorrectionMethod{
	
	// Instance variables
	
	/**
	 * String temperatureCorrection : formula for the temperature correction.
	 */
	private static String temperatureCorrection = "Tm(Na) = Tm(Na = 1M) + (8.75 - 2.83 x Fgc) x ln(Na)";

	// CorrectionMethod interface implementation

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = true;
		double NaEq = Helper.computesNaEquivalent(environment);
		if (NaEq == 0){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium concentration must be a positive numeric value.");
			isApplicable = false;
		}
		
		else if (NaEq < 0.069 || NaEq > 1.02){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium correction of Marmur Schildkraut and Doty (1962, 1998)" +
					" is originally established for sodium concentrations between 0.069 and 1.02M.");
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium correction of Marmur Schildkraut and Doty (1962, 1998) is originally established for " +
			"DNA duplexes.");
		}
		
		return isApplicable;
	}
	
	public ThermoResult correctMeltingResults(Environment environment) {
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The sodium correction is from Marmur, Schildkraut and Doty. (1962, 1998) : ");
		OptionManagement.meltingLogger.log(Level.FINE,temperatureCorrection);

		double NaEq = Helper.computesNaEquivalent(environment);
		double Fgc = environment.getSequences().computesPercentGC() / 100.0;
		
		double Tm = environment.getResult().getTm() + (8.75 - 2.83 * Fgc) * Math.log(NaEq);
		environment.setResult(Tm);
		
		return environment.getResult();
	}

}
