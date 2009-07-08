	/*SantaLucia et al.(1996). Biochemistry 35 : 3555-3562*/

package melting.ionCorrection.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.methodInterfaces.CorrectionMethod;

public class Santalucia96SodiumCorrection implements CorrectionMethod {
	
	private static String temperatureCorrection = "Tm(Na) = Tm(Na = 1M) + 12.5 x log10(Na)";

	public ThermoResult correctMeltingResults(Environment environment) {
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The sodium correction is from Santalucia et al. (1996) : ");
		OptionManagement.meltingLogger.log(Level.FINE, temperatureCorrection);

		double NaEq = Helper.computesNaEquivalent(environment);
		
		double Tm = environment.getResult().getTm() + 12.5 * Math.log10(NaEq);
		environment.setResult(Tm);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = true;
		double NaEq = Helper.computesNaEquivalent(environment);
		if (NaEq == 0){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium concentration must be a positive numeric value.");
			isApplicable = false;
		}
		
		else if (NaEq < 0.1){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium correction of Santalucia et al. (1996) is not reliable for " +
					"sodium concentrations inferior to 0.1M.");
		}
		
		if (environment.getHybridization().equals("dnadna") == false){

			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Santalucia et al. (1996) is originally established for " +
			"DNA duplexes.");
		}
		return isApplicable;
	}
}
