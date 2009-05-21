package melting.MagnesiumCorrections;

import melting.Environment;
import melting.ionCorrections.EntropyCorrection;

public class Tan07MagnesiumCorrection extends EntropyCorrection {

	/* Zhi-Jie Tan and Shi-Jie Chen," RNA helix stability in Mixed Na+/Mg2+ solutions", 2007, 
	 * Biophysical Journal, 92, 3615-3632.
	 * */
	
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		
		if (environment.getMg() < 0.1 && environment.getMg() > 0.3){
			System.out.println("ERROR : The sodium correction of Zhi-Jie Tan et al. (2007)" +
					"is reliable for magnseium concentrations between 0.1M and 0.3M.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("rnarna") == false){
			System.out.println("ERROR : The magnesium correction of Zhi-Jie Tan et al. (2007) is originally established for " +
			"RNA duplexes.");
			isApplicable = false;
		}
		return isApplicable;
	}
	
	protected double correctEntropy(Environment environment){
		
		double entropy = -3.22 * (environment.getSequences().getDuplexLength() - 1) * calculateFreeEnergyPerBaseStack(environment);
		
		return entropy;
	}
	
	public static double calculateFreeEnergyPerBaseStack(Environment environment){
		double Mg = environment.getMg() - environment.getDNTP();
		int duplexLength = environment.getSequences().getDuplexLength();
		
		double square = Math.log(Mg) * Math.log(Mg);
		double a = -0.6 / duplexLength + 0.025 * Math.log(Mg) + 0.0068 * square;
		double b = Math.log(Mg) + 0.38 * square;
		double g = a + b / (duplexLength * duplexLength);
		
		return g;
	}
}
