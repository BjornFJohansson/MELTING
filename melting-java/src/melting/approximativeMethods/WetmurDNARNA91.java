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

package melting.approximativeMethods;

import java.util.logging.Level;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

/**
 * This class represents the model wetdnarna91. It extends Wetmur91.
 */
public class WetmurDNARNA91 extends Wetmur91 {
	
	// Instance variables
	
	/**
	 * Temperature formula
	 */
	private static String temperatureEquation = "Tm = 67 + 16.6 * log10(Na / (1.0 + 0.7 * Na)) + 0.8 * percentGC - 500 / duplexLength - percentMismatching";
	
	// public methods

	@Override
	public ThermoResult computesThermodynamics() {
		double percentGC = this.environment.getSequences().computesPercentGC();
		double percentMismatching = this.environment.getSequences().computesPercentMismatching();
		double duplexLength = (double)this.environment.getSequences().getDuplexLength();
		double Tm = super.computesThermodynamics().getTm();
		
		Tm = 67.0 + 16.6 * Math.log10(this.environment.getNa() / (1.0 + 0.7 *this.environment.getNa())) + 0.8 * percentGC - 500.0 / duplexLength - percentMismatching;

		this.environment.setResult(Tm);
		
		OptionManagement.meltingLogger.log(Level.FINE, temperatureEquation);
		
		return this.environment.getResult();
	}
	
	@Override
	public boolean isApplicable() {
		
		if (this.environment.getHybridization().equals("dnarna") == false && this.environment.getHybridization().equals("rnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "\n The wetmur equation used here was originally established for hybrid DNA/RNA duplexes.");
		}
		
		return super.isApplicable();
	}
}
