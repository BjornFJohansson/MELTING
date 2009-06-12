package melting.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CorrectionMethod;
import melting.configuration.OptionManagement;

public class Owczarzy04SodiumCorrection21 implements CorrectionMethod {

	/*Richard Owczarzy, Yong You, Bernardo G. Moreira, Jeffrey A.Manthey, Lingyan Huang, Mark A. Behlke and Joseph 
	 * A.Walder, "Effects of sodium ions on DNA duplex oligomers: Improved predictions of melting temperatures",
	 * Biochemistry, 2004, 43, 3537-3554.*/
	
	private static String temperatureCorrection = "1 / Tm(Na) = 1 / Tm(Na = 1M) + (-4.62 x Fgc + 4.52) x ln(NaEquivalent) - 0.985 x ln(Na)^2";
	
	public ThermoResult correctMeltingResult(Environment environment) {
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The sodium correction (21) is from Owczarzy et al. (2004) : ");
		OptionManagement.meltingLogger.log(Level.FINE,temperatureCorrection);
		
		double NaEq = Helper.calculateNaEquivalent(environment);
		double Fgc = environment.getSequences().calculatePercentGC() / 100;
		double square = Math.log(NaEq) * Math.log(NaEq);
		
		double TmInverse = 1 / environment.getResult().getTm() + (-4.62 * Fgc + 4.52) * Math.log(NaEq) - 0.985 * square;
		environment.setResult(1 / TmInverse);
		
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
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Owczarzy et al. (2004) 21 is originally established for " +
			"DNA duplexes.");

		}
		
		return isApplicable;
	}

}
