/* Peyret N., 2000, "Prediction of nucleic acid hybridization : parameters and algorithms."
	 * Ph.D Thesis, Section .5.4.2, 128, Wayne State University, Detroit, MI.
	 * */

package melting.sodiumEquivalence;

import java.util.logging.Level;

import melting.configuration.OptionManagement;

public class Peyret00_NaEquivalent extends SodiumEquivalent{
	
	private static double parameter = 3.3;
	private static String NaCorrection = "NaEquivalent = Na + K + Tris / 2 + 3.3 x sqrt(Mg - dNTP);";
	
	public double getSodiumEquivalent(double Na, double Mg, double K,
			double Tris, double dNTP) {
		
		double NaEq = super.getSodiumEquivalent(Na, Mg, K, Tris, dNTP, parameter);

		OptionManagement.meltingLogger.log(Level.FINE, "from Peyret. (2000) : " + NaCorrection);

		return NaEq;
	}

}
