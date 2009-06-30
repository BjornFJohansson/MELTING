/*James G. Wetmur, "DNA Probes : applications of the principles of nucleic acid hybridization",
	1991, Critical reviews in biochemistry and molecular biology, 26, 227-259*/

package melting.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CorrectionMethod;
import melting.configuration.OptionManagement;

public class Wetmur91SodiumCorrection implements CorrectionMethod{
	
	private static String temperatureCorrection = "Tm(Na) = Tm(Na = 1M) + 16.6 x log10(Na / (1.0 + 0.7 * Na)) + 3.83";
	
	public ThermoResult correctMeltingResult(Environment environment) {
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The sodium correction is from Wetmur. (1991) : ");
		OptionManagement.meltingLogger.log(Level.FINE,temperatureCorrection);
		
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		double Tm = environment.getResult().getTm() + 16.6 * Math.log10(NaEq / (1.0 + 0.7 * NaEq)) + 3.83;
		environment.setResult(Tm);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = true;
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		if (NaEq == 0){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Wetmur (1991) is applicable for " +
					"strictky positive sodium concentrations.");
			isApplicable = false;
		}
		return isApplicable;
	}
	
}
