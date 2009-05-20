package melting.sodiumCorrections;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.ionCorrections.EntropyCorrection;

public class Tan06SodiumCorrection extends EntropyCorrection {

	/* Zhi-Jie Tan and Shi-Jie Chen, "Nucleic acid helix stability: effects of Salt concentration, 
	 * cation valence and size, and chain length", 2006, Biophysical Journal, 90, 1175-1190. 
	 * */
	
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		if (NaEq < 0.001 && NaEq > 1){
			System.out.println("ERROR : The sodium correction of Zhi-Jie Tan et al. (2006)" +
					"is reliable for sodium concentrations between 0.001M and 1M.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("ERROR : The sodium correction of Zhi-Jie Tan et al. (2006) is originally established for " +
			"DNA duplexes.");
			isApplicable = false;
		}
		return isApplicable;
	}
	
	protected double correctEntropy(double Na, int duplexLength){
		double square = Math.log(Na) * Math.log(Na);
		double a = -0.07 * Math.log(Na) + 0.012 * square;
		double b = 0.013 * square;
		
		double g = a + b / duplexLength;
		
		double entropy = -3.22 * (duplexLength - 1) * g;
		
		return entropy;
	}
	
	public ThermoResult correctMeltingResult(Environment environment) {
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		return super.correctMeltingResult(environment, NaEq);
	}
}
