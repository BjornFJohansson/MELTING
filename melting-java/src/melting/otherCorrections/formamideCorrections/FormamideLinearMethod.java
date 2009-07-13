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

package melting.otherCorrections.formamideCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.methodInterfaces.CorrectionMethod;

/**
 * This class represents the linear formamide correction model lincorr. It implements the CorrectionMethod interface.
 * 
 * McConaughy, B.L., Laird, C.D. and McCarthy, B.I., 1969, Biochemistry
 * 8, 3289-3295.
 * 
 * Record, M.T., Jr, 1967, Biopolymers, 5, 975-992.
 * 
 * Casey J., and Davidson N., 1977, Nucleic acids research, 4, 1539-1532.
 * 
 * Hutton Jr, 1977, Nucleic acids research, 4, 3537-3555.
 */
public class FormamideLinearMethod implements CorrectionMethod{
	
	// Instance variable
	
	/**
	 * String temperatureCorrection : formula for the temperature correction
	 */
	private static String temperatureCorrection = "Tm (x % formamide) = Tm(0 % formamide) - 0.65 * x % formamide";
	
	// CorrectionMethod interface implementation

	public boolean isApplicable(Environment environment) {
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The implemented formamide correction methods are established for DNA duplexes..");

			return false;
		}
		return true;
		
	}

	public ThermoResult correctMeltingResults(Environment environment) {
		double Tm = environment.getResult().getTm() - 0.65 * environment.getFormamide();
		environment.setResult(Tm);
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The linear formamide correction : ");
		OptionManagement.meltingLogger.log(Level.FINE,temperatureCorrection);
		
		return environment.getResult();
	}

}
