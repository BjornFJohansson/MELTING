package melting.sodiumCorrections;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.calculMethodInterfaces.IonCorrectionMethod;

public class Santalucia96SodiumCorrection implements IonCorrectionMethod {

	/*SantaLucia et al.(1996). Biochemistry 35 : 3555-3562*/
	
	public ThermoResult correctMeltingResult(Environment environment) {
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		double Tm = environment.getResult().getTm() + 12.5 * Math.log10(NaEq);
		environment.setResult(Tm);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = true;
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		if (NaEq < 0.1){
			System.out.println("ERROR : The sodium correction of Santalucia et al. (1996) is not reliable for " +
					"sodium concentrations inferior to 0.1M.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("ERROR : The sodium correction of Santalucia et al. (1996) is originally established for " +
			"DNA duplexes.");
			isApplicable = false;
		}
		return isApplicable;
	}
}
