package melting.sodiumEquivalence;

public class Peyret00_NaEquivalent extends SodiumEquivalent{
	
	/* Peyret N., 2000, "Prediction of nucleic acid hybridization : parameters and algorithms."
	 * Ph.D Thesis, Section .5.4.2, 128, Wayne State University, Detroit, MI.
	 * */
	
	private static double parameter = 3.3;

	public double getSodiumEquivalent(double Na, double Mg, double K,
			double Tris, double dNTP) {
		
		return super.getSodiumEquivalent(Na, Mg, K, Tris, dNTP, parameter);
	}

}
