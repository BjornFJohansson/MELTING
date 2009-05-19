package melting.sodiumCorrections;

import melting.Environment;
import melting.ThermoResult;
import melting.ionCorrections.SodiumCorrections;

public class SchildkrautLifson65SodiumCorrection extends SodiumCorrections {

	/* Schildkraut, C., and Lifson, S. (1965) Dependence of the melting
	 *  temperature of DNA on salt concentration, Biopolymers 3, 195-208.
	 */
	
	public ThermoResult correctMeltingResult(Environment environment) {
		
		double NaEq = calculateNaEqEquivalent(environment);
		
		double Tm = environment.getResult().getTm() + 16.6 * Math.log10(NaEq);
		environment.setResult(Tm);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		double NaEq = calculateNaEqEquivalent(environment);
		
		if (NaEq < 0.07 || NaEq > 0.12){
			System.out.println("ERROR : The sodium correction of Schildkraut Lifson (1965) is applicable for " +
					"sodium concentrations between 0.01 and 0.2 M.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("ERROR : The sodium correction of Schildkraut Lifson (1965) is originally established for " +
			"DNA duplexes in Escherichia Coli.");
			isApplicable = false;
		}
		
		return isApplicable;
	}
}
