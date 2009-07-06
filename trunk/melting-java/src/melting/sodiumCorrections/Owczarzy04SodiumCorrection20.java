/*Richard Owczarzy, Yong You, Bernardo G. Moreira, Jeffrey A.Manthey, Lingyan Huang, Mark A. Behlke and Joseph 
	 * A.Walder, "Effects of sodium ions on DNA duplex oligomers: Improved predictions of melting temperatures",
	 * Biochemistry, 2004, 43, 3537-3554.*/

package melting.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.methodInterfaces.CorrectionMethod;

public class Owczarzy04SodiumCorrection20 implements CorrectionMethod {
	
	private static String temperatureCorrection = "1 / Tm(Na) = 1 / Tm(Na = 1M) + (3.85 x Fgc - 6.18) x 1 / 100000 x ln(Na)";
	
	public ThermoResult correctMeltingResult(Environment environment) {
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The sodium correction (20) is from Owczarzy et al. (2004) : ");
		OptionManagement.meltingLogger.log(Level.FINE,temperatureCorrection);
		
		double NaEq = Helper.calculateNaEquivalent(environment);
		double Fgc = environment.getSequences().calculatePercentGC() / 100.0;
		
		double TmInverse = 1.0 / (environment.getResult().getTm() + 273.15) + (3.85 * Fgc - 6.18) * 1 / 100000 * Math.log(NaEq);
		environment.setResult((1.0 / TmInverse) - 273.15);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = true;
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		if (NaEq == 0){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium concentration must be strictly positive.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Owczarzy et al. (2004) 20 is originally established for " +
			"DNA duplexes.");
		}
		
		return isApplicable;
	}
}
