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

/* Peyret N., 2000, "Prediction of nucleic acid hybridization : parameters and algorithms."
	 * Ph.D Thesis, Section .5.4.2, 128, Wayne State University, Detroit, MI.
	 * */

package melting.ionCorrection.sodiumEquivalence;

import java.util.logging.Level;

import melting.configuration.OptionManagement;

public class Peyret00_NaEquivalent extends SodiumEquivalent{
	
	private static double parameter = 3.3;
	private static String NaCorrection = "NaEquivalent = Na + K + Tris / 2 + 3.3 x sqrt(Mg - dNTP);";
	
	public double computeSodiumEquivalent(double Na, double Mg, double K,
			double Tris, double dNTP) {
		
		double NaEq = super.getSodiumEquivalent(Na, Mg, K, Tris, dNTP, parameter);

		OptionManagement.meltingLogger.log(Level.FINE, "from Peyret. (2000) : " + NaCorrection);

		return NaEq;
	}

}
