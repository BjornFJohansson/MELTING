/* Schildkraut, C., and Lifson, S. (1965) Dependence of the melting
	 *  temperature of DNA on salt concentration, Biopolymers 3, 195-208.
	 */

package melting.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.methodInterfaces.CorrectionMethod;

public class SchildkrautLifson65SodiumCorrection implements CorrectionMethod{

	private static String temperatureCorrection = "	Tm(Na) = Tm(Na = 1M) + 16.6 x log10(Na)";
	
	public ThermoResult correctMeltingResults(Environment environment) {
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The sodium correction is from Schildkraut Lifson. (1965) : ");
		OptionManagement.meltingLogger.log(Level.FINE,temperatureCorrection);

		double NaEq = Helper.calculateNaEquivalent(environment);
		
		double Tm = environment.getResult().getTm() + 16.6 * Math.log10(NaEq);
		environment.setResult(Tm);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = true;
		double NaEq = Helper.calculateNaEquivalent(environment);
		if (NaEq == 0){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium concentration must be a positive numeric value.");
			isApplicable = false;
		} 
		
		else if (NaEq < 0.07 || NaEq > 0.12){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Schildkraut Lifson (1965) is applicable for " +
			"sodium concentrations between 0.01 and 0.2 M.");
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Schildkraut Lifson (1965) is originally established for " +
			"DNA duplexes in Escherichia Coli.");
		}
		
		return isApplicable;
	}
}
