package melting.sodiumEquivalence;

public class Ahsen01_NaEquivalent extends SodiumEquivalent{
	
	/* Nicolas Von Ahsen, Carl T Wittwer and Ekkehard Schutz, "Oligonucleotide
	 * melting temperatures under PCR conditions : deoxynucleotide Triphosphate
	 * and Dimethyl sulfoxide concentrations with comparison to alternative empirical 
	 * formulas", 2001, Clinical Chemistry, 47, 1956-1961.
	 * */
	
	private static double parameter = 3.79;

	public double getSodiumEquivalent(double Na, double Mg, double K, double Tris,
			double dNTP) {
	
		return super.getSodiumEquivalent(Na, Mg, K, Tris, dNTP, parameter);
	}

}
