/* This program is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the 
 * License, or (at your option) any later version
                                
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
 * Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, 
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA                                                                  

 *       Marine Dumousseau and Nicolas Le Novere                                                   
 *       EMBL-EBI, neurobiology computational group,                          
 *       Cambridge, UK. e-mail: melting-forum@googlegroups.com        */

package melting.ionCorrection.sodiumEquivalence;

import melting.configuration.OptionManagement;
import melting.methodInterfaces.NamedMethod;

/**
 * This class represents the model for a sodium equivalence ahs01. It extends SodiumEquivalent.
 * 
 * Nicolas Von Ahsen, Carl T Wittwer and Ekkehard Schutz, "Oligonucleotide
 * melting temperatures under PCR conditions : deoxynucleotide Triphosphate
 * and Dimethyl sulfoxide concentrations with comparison to alternative empirical 
 * formulas", 2001, Clinical Chemistry, 47, 1956-1961.
 */
public class Ahsen01_NaEquivalent extends SodiumEquivalent
  implements NamedMethod
{	
	// Instance variables
	
	private static double parameter = 3.79;
	
	/**
	 * String NaCorrection : formula to compute a sodium equivalence.
	 */
	private static String NaCorrection = "NaEquivalent = Na + K + Tris / 2 + 3.79 x sqrt(Mg - dNTP)";

  /**
   * Full name of the method.
   */
  private static String methodName = "Ahsen, Wittzer and Schutz (2001)";

	// SodiumEquivalentMethod interface implementation
	
	public double computeSodiumEquivalent(double Na, double Mg, double K, double Tris,
			double dNTP) {
		double NaEq = super.getSodiumEquivalent(Na, Mg, K, Tris, dNTP, parameter);
		
    OptionManagement.logMethodName(methodName);
    OptionManagement.logMessage(NaCorrection);

		return NaEq;
	}

  /**
   * Gets the full name of the method.
   * @return The full name of the method.
   */
  @Override
  public String getName()
  {
    return methodName;
  }
}
