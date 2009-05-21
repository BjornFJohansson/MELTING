package melting.sodiumCorrections;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.calculMethodInterfaces.IonCorrectionMethod;

public class FrankKamenetskii71SodiumCorrection implements IonCorrectionMethod {

	/* Frank-Kamenetskii, M. D. (1971) Simplification of the empirical
	 * relationship between melting temperature of DNA, its GC content
	 * and concentration of sodium ions in solution, Biopolymers 10,
	 * 2623-2624.
	 * */
	
	public ThermoResult correctMeltingResult(Environment environment) {
		
		double NaEq = Helper.calculateNaEquivalent(environment);
		int Fgc = environment.getSequences().calculatePercentGC();
		
		double Tm = environment.getResult().getTm() + (7.95 - 3.06 * Fgc) * Math.log(NaEq);
		environment.setResult(Tm);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = true;
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		if (NaEq < 0.069 || NaEq > 1.02){
			System.out.println("ERROR : The sodium correction of Frank Kamenetskii (1971)" +
					" is originally established for sodium concentrations between 0.069 and 1.02M.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("ERROR : The sodium correction of Frank Kamenetskii (1971) is originally established for " +
			"DNA duplexes.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

}
