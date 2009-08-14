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

import java.util.logging.Level;

import melting.configuration.OptionManagement;

/**
 * This class represents the model for a sodium equivalence ahs01. It extends SodiumEquivalent.
 * 
 * Nicolas Von Ahsen, Carl T Wittwer and Ekkehard Schutz, "Oligonucleotide
 * melting temperatures under PCR conditions : deoxynucleotide Triphosphate
 * and Dimethyl sulfoxide concentrations with comparison to alternative empirical 
 * formulas", 2001, Clinical Chemistry, 47, 1956-1961.
 */
public class Ahsen01_NaEquivalent extends SodiumEquivalent{
	
	// Instance variables
	
	private static double parameter = 3.79;
	
	/**
	 * String NaCorrection : formula to compute a sodium equivalence.
	 */
	private static String NaCorrection = "NaEquivalent = Na + K + Tris / 2 + 3.79 x sqrt(Mg - dNTP)";

	// SodiumEquivalentMethod interface implementation
	
	public double computeSodiumEquivalent(double Na, double Mg, double K, double Tris,
			double dNTP) {
		double NaEq = super.getSodiumEquivalent(Na, Mg, K, Tris, dNTP, parameter);
		
		OptionManagement.meltingLogger.log(Level.FINE, "from Ahsen et al. (2001) : " + NaCorrection);

		return NaEq;
	}

}