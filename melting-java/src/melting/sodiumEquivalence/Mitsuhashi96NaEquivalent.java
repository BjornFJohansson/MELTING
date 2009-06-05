package melting.sodiumEquivalence;

import java.util.logging.Level;

import melting.configuration.OptionManagement;

public class Mitsuhashi96NaEquivalent extends SodiumEquivalent {

	/* Mitsuhashi M., 1996, "Technical report: Part 1. Basic requirements for designing optimal 
	 * oligonucleotide probe sequences.", J. Clin. Lab. Anal, 10, 277-284.
	 * */
	
	private static double parameter = 4;
	private static String NaCorrection = "NaEquivalent = Na + K + Tris / 2 + 4 x sqrt(Mg - dNTP);";
	
	public double getSodiumEquivalent(double Na, double Mg, double K, double Tris,
			double dNTP) {
	
		OptionManagement.meltingLogger.log(Level.FINE, "If we use a sodium correction and there is another cations than Na+, we can use a sodium equivalence : from Mitsuhashi et al. (1996) : " + NaCorrection);

		return super.getSodiumEquivalent(Na, Mg, K, Tris, dNTP, parameter);
	}
}
