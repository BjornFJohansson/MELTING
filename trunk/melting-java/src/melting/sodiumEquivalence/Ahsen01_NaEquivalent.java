/* Nicolas Von Ahsen, Carl T Wittwer and Ekkehard Schutz, "Oligonucleotide
	 * melting temperatures under PCR conditions : deoxynucleotide Triphosphate
	 * and Dimethyl sulfoxide concentrations with comparison to alternative empirical 
	 * formulas", 2001, Clinical Chemistry, 47, 1956-1961.
	 * */

package melting.sodiumEquivalence;

import java.util.logging.Level;

import melting.configuration.OptionManagement;

public class Ahsen01_NaEquivalent extends SodiumEquivalent{
	
	private static double parameter = 3.79;
	private static String NaCorrection = "NaEquivalent = Na + K + Tris / 2 + 3.79 x sqrt(Mg - dNTP);";

	public double getSodiumEquivalent(double Na, double Mg, double K, double Tris,
			double dNTP) {
		double NaEq = super.getSodiumEquivalent(Na, Mg, K, Tris, dNTP, parameter);
		
		OptionManagement.meltingLogger.log(Level.FINE, "from Ahsen et al. (2001) : " + NaCorrection);

		return NaEq;
	}

}
