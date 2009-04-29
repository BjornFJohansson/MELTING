package melting.sodiumEquivalence;

import java.util.HashMap;

import melting.SodiumEquivalent;
import melting.configuration.OptionManagement;

public class Owczarzy08 implements SodiumEquivalent{
	
	/* Richard Owczarzy, Bernardo G Moreira, Yong You, Mark A 
	 * Behlke, Joseph A walder, "Predicting stability of DNA duplexes in solutions
	 * containing magnesium and monovalent cations", 2008, Biochemistry, 47, 5336-5353.
	 * */

	public double getSodiumEquivalent(double Na, double Mg, double K,
			double Tris, double dNTP) {
		
		double NaEq;
		NaEq = Na + K + Tris/2 + 3.65 * Math.sqrt(Mg - dNTP);
		
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
