package melting.MagnesiumCorrections;

import melting.Environment;
import melting.ThermoResult;
import melting.ionCorrections.EntropyCorrection;

public class Tan06MagnesiumCorrection extends EntropyCorrection {

	/* Zhi-Jie Tan and Shi-Jie Chen, "Nucleic acid helix stability: effects of Salt concentration, 
	 * cation valence and size, and chain length", 2006, Biophysical Journal, 90, 1175-1190. 
	 * */
	
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		
		if (environment.getMg() < 0.0001 && environment.getMg() > 1){
			System.out.println("ERROR : The magnesium correction of Zhi-Jie Tan et al. (2006)" +
					"is reliable for magnesium concentrations between 0.001M and 1M.");
			isApplicable = false;
		}
		
		if (environment.getSequences().getDuplexLength() < 6){
			System.out.println("ERROR : The magnesium correction of Zhi-Jie Tan et al. (2006)" +
			"is valid for oligonucleotides with a number of base pairs superior or equal to 6.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("ERROR : The sodium correction of Zhi-Jie Tan et al. (2006) is originally established for " +
			"DNA duplexes.");
			isApplicable = false;
		}
		return isApplicable;
	}
	
	protected double correctEntropy(double Mg, int duplexLength){
		double square = Math.log(Mg) * Math.log(Mg);
		double a = 0.02 * Math.log(Mg) + 0.0068 * square;
		double b = 1.18 * Math.log(Mg) + 0.344 * square;
		
		double g = a + b / (duplexLength * duplexLength);
		
		double entropy = -3.22 * (duplexLength - 1) * g;
		
		return entropy;
	}

	public ThermoResult correctMeltingResult(Environment environment) {
		return super.correctMeltingResult(environment, environment.getMg());
	}
}
