package melting.sodiumEquivalence;

public class Mitsuhashi96NaEquivalent extends SodiumEquivalent {

	/* Mitsuhashi M., 1996, "Technical report: Part 1. Basic requirements for designing optimal 
	 * oligonucleotide probe sequences.", J. Clin. Lab. Anal, 10, 277-284.
	 * */
	
	private static double parameter = 4;

	public double getSodiumEquivalent(double Na, double Mg, double K, double Tris,
			double dNTP) {
	
		return super.getSodiumEquivalent(Na, Mg, K, Tris, dNTP, parameter);
	}
}
