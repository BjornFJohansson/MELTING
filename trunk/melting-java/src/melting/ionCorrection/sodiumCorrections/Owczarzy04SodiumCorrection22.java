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

/*Richard Owczarzy, Yong You, Bernardo G. Moreira, Jeffrey A.Manthey, Lingyan Huang, Mark A. Behlke and Joseph 
	 * A.Walder, "Effects of sodium ions on DNA duplex oligomers: Improved predictions of melting temperatures",
	 * Biochemistry, 2004, 43, 3537-3554.*/

package melting.ionCorrection.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.methodInterfaces.CorrectionMethod;

public class Owczarzy04SodiumCorrection22 implements CorrectionMethod {
	
	private static String temperatureCorrection = "1 / Tm(Na) = 1 / Tm(Na = 1M) + (4.29 * Fgc - 3.95) x 1 / 100000 x ln(Na) + 9.40 x 1 / 1000000 x ln(Na)^2";

	public ThermoResult correctMeltingResults(Environment environment) {
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The sodium correction (22) is from Owczarzy et al. (2004) : "); 
		OptionManagement.meltingLogger.log(Level.FINE,temperatureCorrection);
		
		double NaEq = Helper.computesNaEquivalent(environment);
		double Fgc = environment.getSequences().computesPercentGC() / 100.0;
		double square = Math.log(NaEq) * Math.log(NaEq);
		double TmInverse = 1.0 / (environment.getResult().getTm() + 273.15) + (4.29 * Fgc - 3.95) * 1.0 / 100000.0 * Math.log(NaEq) + 9.40 * 1.0 / 1000000.0 * square;
		environment.setResult((1.0 / TmInverse) - 273.15);
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = true;
		double NaEq = Helper.computesNaEquivalent(environment);
		
		if (NaEq == 0){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium concentration must be strictly positive.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium correction of Owczarzy et al. (2004) 22 is originally established for " +
			"DNA duplexes.");

		}
		
		return isApplicable;
	}
}
