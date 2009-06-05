package melting.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.CorrectionMethods.EntropyCorrection;
import melting.configuration.OptionManagement;

public class Santalucia98_04SodiumCorrection extends EntropyCorrection {

	/* John Santalucia, Jr., "A unified view of polymer, dumbbell, and oligonucleotide DNA nearest-neighbor
	 * thermodynamics.", 1998, Proc. Natl. Acad. Sci. USA, 95, 1460-1465*/
	
	/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */

	private static String entropyCorrection = "delta S(Na) = delta S(Na = 1M) + 0.368 * (duplexLength - 1) x ln(Na)";

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		if (NaEq < 0.05 || NaEq > 1.1){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Santalucia et al. (1998 - 2004) is only reliable for " +
					"sodium concentrations between 0.015M and 1.1M.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Santalucia et al. (1998 - 2004) is originally established for " +
			"DNA duplexes.");
		}
		
		if (environment.getSequences().getDuplexLength() > 16){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Santalucia et al. (1998 - 2004) begins to break down for " +
			"DNA duplexes longer than 16 bp.");
			isApplicable = false;
		}
		return isApplicable;
	}
	
	protected double correctEntropy(Environment environment){
		
		OptionManagement.meltingLogger.log(Level.FINE, "The sodium correction is from Santalucia et al. (1998) : " + entropyCorrection);

		double Na = environment.getNa();
		double entropy = 0.368 * (environment.getSequences().getDuplexLength() - 1) * Math.log(Na);
		
		return entropy;
	}
	
	public ThermoResult correctMeltingResult(Environment environment) {
		double NaEq = Helper.calculateNaEquivalent(environment);
		environment.setNa(NaEq);
		
		return super.correctMeltingResult(environment);
	}
}
