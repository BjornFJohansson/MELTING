package melting.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CorrectionMethod;
import melting.configuration.OptionManagement;

public class Santalucia96SodiumCorrection implements CorrectionMethod {

	/*SantaLucia et al.(1996). Biochemistry 35 : 3555-3562*/
	
	private static String temperatureCorrection = "Tm(Na) = Tm(Na = 1M) + 12.5 x log10(NaEquivalent)";

	public ThermoResult correctMeltingResult(Environment environment) {
		
		OptionManagement.meltingLogger.log(Level.INFO, "The sodium correction is from Santalucia et al. (1996) : " + temperatureCorrection);

		double NaEq = Helper.calculateNaEquivalent(environment);
		
		double Tm = environment.getResult().getTm() + 12.5 * Math.log10(NaEq);
		environment.setResult(Tm);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = true;
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		if (NaEq < 0.1){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium correction of Santalucia et al. (1996) is not reliable for " +
					"sodium concentrations inferior to 0.1M.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){

			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Santalucia et al. (1996) is originally established for " +
			"DNA duplexes.");
		}
		return isApplicable;
	}
}
