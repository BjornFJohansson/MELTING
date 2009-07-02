/*Blake, R. D., and Delcourt, S. G. (1998) Thermal stability of DNA,
	 * Nucleic Acids Res. 26, 3323-3332 and corrigendum.
	 * 
	 *  Marmur, J., and Doty, P. (1962) Determination of the base
	 *  composition of deoxyribonucleic acid from its thermal denaturation
	 *  temperature, J. Mol. Biol. 5, 109-118.
	 * */

package melting.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CorrectionMethod;
import melting.configuration.OptionManagement;

public class MarmurSchildkrautDoty98_62SodiumCorrection implements CorrectionMethod{
	
	private static String temperatureCorrection = "Tm(Na) = Tm(Na = 1M) + (8.75 - 2.83 x Fgc) x ln(Na)";

	public ThermoResult correctMeltingResult(Environment environment) {
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The sodium correction is from Marmur, Schildkraut and Doty. (1962, 1998) : ");
		OptionManagement.meltingLogger.log(Level.FINE,temperatureCorrection);

		double NaEq = Helper.calculateNaEquivalent(environment);
		double Fgc = environment.getSequences().calculatePercentGC() / 100.0;
		
		double Tm = environment.getResult().getTm() + (8.75 - 2.83 * Fgc) * Math.log(NaEq);
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
		
		else if (NaEq < 0.069 || NaEq > 1.02){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium correction of Marmur Schildkraut and Doty (1962, 1998)" +
					" is originally established for sodium concentrations between 0.069 and 1.02M.");
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium correction of Marmur Schildkraut and Doty (1962, 1998) is originally established for " +
			"DNA duplexes.");
		}
		
		return isApplicable;
	}


}
