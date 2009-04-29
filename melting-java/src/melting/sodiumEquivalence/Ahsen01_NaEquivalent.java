package melting.sodiumEquivalence;

import java.util.HashMap;

import melting.calculMethodInterfaces.SodiumEquivalentMethod;
import melting.configuration.OptionManagement;

public class Ahsen01_NaEquivalent implements SodiumEquivalentMethod{
	
	/* Nicolas Von Ahsen, Carl T Wittwer and Ekkehard Schutz, "Oligonucleotide
	 * melting temperatures under PCR conditions : deoxynucleotide Triphosphate
	 * and Dimethyl sulfoxide concentrations with comparison to alternative empirical 
	 * formulas", 2001, Clinical Chemistry, 47, 1956-1961.
	 * */

	public double getSodiumEquivalent(double Na, double Mg, double K, double Tris,
			double dNTP) {
		
		double NaEq;
		Na = Na * 1000;
		Mg = Mg * 1000;
		K = K * 1000;
		Tris = Tris / 2 * 1000;
		
		NaEq = (Na + K + Tris + 120 * Math.sqrt(Mg - dNTP)) / 1000;
		
		return NaEq;
	}

	public boolean isApplicable(HashMap<String, String> options) {
		String hybridization = options.get(OptionManagement.hybridization);
		
		if (hybridization.equals("dnadna") == false){
			System.out.println("WARNING : The Ahsen et al equation to have the sodium equivalent" +
					"concentration is established for DNA duplexes.");
			return false;
		}
		return true;
	}

}
