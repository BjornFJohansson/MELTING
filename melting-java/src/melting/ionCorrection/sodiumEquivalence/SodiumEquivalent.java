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

package melting.ionCorrection.sodiumEquivalence;

import java.util.HashMap;
import java.util.logging.Level;

import melting.configuration.OptionManagement;
import melting.methodInterfaces.SodiumEquivalentMethod;

public abstract class SodiumEquivalent implements SodiumEquivalentMethod{

	public double getSodiumEquivalent(double Na, double Mg, double K, double Tris,
			double dNTP, double b) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n Other cations than Na+ are present in the solution, we can use a sodium equivalence : "); 
		
		double NaEq = Na + K + Tris / 2 + b * Math.sqrt(Mg - dNTP);
		
		return NaEq;
	}

	public boolean isApplicable(HashMap<String, String> options) {
		String hybridization = options.get(OptionManagement.hybridization);
		
		if (hybridization.equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The current equations to have the sodium equivalent" +
					"concentration are established for DNA duplexes.");
		}
		return true;
	}
}
