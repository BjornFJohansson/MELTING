package melting.MixedNaMgCorrections;

import melting.Environment;
import melting.MagnesiumCorrections.Tan06MagnesiumCorrection;
import melting.MagnesiumCorrections.Tan07MagnesiumCorrection;
import melting.ionCorrections.EntropyCorrection;
import melting.sodiumCorrections.Tan06SodiumCorrection;
import melting.sodiumCorrections.Tan07SodiumCorrection;

public class Tan07MixedNaMgCorrection extends EntropyCorrection {

	/* Zhi-Jie Tan and Shi-Jie Chen," RNA helix stability in Mixed Na+/Mg2+ solutions", 2007, 
	 * Biophysical Journal, 92, 3615-3632.
	 * */
	
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		
		if (environment.getMg() < 0.1 && environment.getMg() > 0.3){
			System.out.println("ERROR : The sodium correction of Zhi-Jie Tan et al. (2007)" +
					"is reliable for magnesium concentrations between 0.1M and 0.3M.");
			isApplicable = false;
		}
		
		if (environment.getNa() < 0 && environment.getNa() > 1){
			System.out.println("ERROR : The sodium correction of Zhi-Jie Tan et al. (2007)" +
					"is reliable for sodium concentrations between 0M and 1M.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("rnarna") == false || environment.getHybridization().equals("dnadna") == false){
			System.out.println("ERROR : The magnesium correction of Zhi-Jie Tan et al. (2007)is originally established for " +
			"RNA or DNA duplexes.");
			isApplicable = false;
		}
		return isApplicable;
	}
	
	protected double correctEntropy(Environment environment){
		double Na = environment.getNa();
		double Mg = environment.getMg() - environment.getDNTP();
		int duplexLength = environment.getSequences().getDuplexLength();
		
		double x1 = Na / (Na + (8.1 - 32.4 / duplexLength) * (5.2 - Math.log(Na)) * Mg);
		double x2 = 1 - x1;
		double g12 = -0.6 * x1 * x2 * Math.log(Na) * Math.log((1 / x1 - 1) * Na) / duplexLength;
		
		double g1 = 0;
		double g2 = 0;
		if (environment.getHybridization().equals("rnarna")){
			g1 = Tan07SodiumCorrection.calculateFreeEnergyPerBaseStack(environment);
			g2 = Tan07MagnesiumCorrection.calculateFreeEnergyPerBaseStack(environment);
		}
		else if (environment.getHybridization().equals("dnadna")){
			g1 = Tan06SodiumCorrection.calculateFreeEnergyPerBaseStack(environment);
			g2 = Tan06MagnesiumCorrection.calculateFreeEnergyPerBaseStack(environment);
		}
				
		double entropy = -3.22 * ((duplexLength - 1) * (x1 * g1 + x2 * g2) + g12);
		
		return entropy;
	}
}
