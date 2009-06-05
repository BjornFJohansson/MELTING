package melting.sodiumEquivalence;

import java.util.logging.Level;

import melting.configuration.OptionManagement;

public class Peyret00_NaEquivalent extends SodiumEquivalent{
	
	/* Peyret N., 2000, "Prediction of nucleic acid hybridization : parameters and algorithms."
	 * Ph.D Thesis, Section .5.4.2, 128, Wayne State University, Detroit, MI.
	 * */
	
	private static double parameter = 3.3;
	private static String NaCorrection = "NaEquivalent = Na + K + Tris / 2 + 3.3 x sqrt(Mg - dNTP);";
	
	public double getSodiumEquivalent(double Na, double Mg, double K,
			double Tris, double dNTP) {
		
		OptionManagement.meltingLogger.log(Level.FINE, "If we use a sodium correction and there is another cations than Na+, we can use a sodium equivalence : from Peyret. (2000) : " + NaCorrection);

		return super.getSodiumEquivalent(Na, Mg, K, Tris, dNTP, parameter);
	}

}
