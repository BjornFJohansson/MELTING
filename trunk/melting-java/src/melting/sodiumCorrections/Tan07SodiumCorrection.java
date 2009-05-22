package melting.sodiumCorrections;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.CorrectionMethods.EntropyCorrection;

public class Tan07SodiumCorrection extends EntropyCorrection {
	
	/* Zhi-Jie Tan and Shi-Jie Chen," RNA helix stability in Mixed Na+/Mg2+ solutions", 2007, 
	 * Biophysical Journal, 92, 3615-3632.
	 * */
	
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		double NaEq = Helper.calculateNaEquivalent(environment);
		
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
	
	protected double correctEntropy(Environment environment){
		
		double entropy = -3.22 * (environment.getSequences().getDuplexLength() - 1) * calculateFreeEnergyPerBaseStack(environment);
		
		return entropy;
	}
	
	public ThermoResult correctMeltingResult(Environment environment) {
		double NaEq = Helper.calculateNaEquivalent(environment);
		environment.setNa(NaEq);
		
		return super.correctMeltingResult(environment);
	}
	
	public static double calculateFreeEnergyPerBaseStack(Environment environment){
		double Na = environment.getNa();
		double square = Math.log(Na) * Math.log(Na);
		double a = -0.075 * Math.log(Na) + 0.012 * square;
		double b = 0.018 * square;
		
		double g = a + b / environment.getSequences().getDuplexLength();
		
		return g;
	}
}
