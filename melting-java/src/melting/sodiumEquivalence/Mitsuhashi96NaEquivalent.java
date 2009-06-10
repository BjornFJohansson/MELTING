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
	
		double NaEq = super.getSodiumEquivalent(Na, Mg, K, Tris, dNTP, parameter);
		
		OptionManagement.meltingLogger.log(Level.FINE, "from Mitsuhashi et al. (1996) : " + NaCorrection);

		return NaEq;
	}
}
