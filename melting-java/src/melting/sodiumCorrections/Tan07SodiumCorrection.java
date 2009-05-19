package melting.sodiumCorrections;

import melting.Environment;

public class Tan07SodiumCorrection extends EntropySodiumCorrection {
	
	/* Zhi-Jie Tan and Shi-Jie Chen," RNA helix stability in Mixed Na+/Mg2+ solutions", 2007, 
	 * Biophysical Journal, 92, 3615-3632.
	 * */
	
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		double NaEq = calculateNaEqEquivalent(environment);
		
		if (NaEq < 0.003 && NaEq > 1){
			System.out.println("ERROR : The sodium correction of Zhi-Jie Tan et al. (2007)" +
					"is reliable for sodium concentrations between 0.003M and 1M.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("rnarna") == false){
			System.out.println("ERROR : The sodium correction of Zhi-Jie Tan et al. (2007) is originally established for " +
			"RNA duplexes.");
			isApplicable = false;
		}
		return isApplicable;
	}
	
	protected double correctEntropy(double Na, int duplexLength){
		double square = Math.log(Na) * Math.log(Na);
		double a = -0.075 * Math.log(Na) + 0.012 * square;
		double b = 0.018 * square;
		
		double g = a + b / duplexLength;
		
		double entropy = -3.22 * (duplexLength - 1) * g;
		
		return entropy;
	}
}
