/* John Santalucia, Jr., "A unified view of polymer, dumbbell, and oligonucleotide DNA nearest-neighbor
	 * thermodynamics.", 1998, Proc. Natl. Acad. Sci. USA, 95, 1460-1465*/
	
	/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */

package melting.ionCorrection.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.correctionMethods.EntropyCorrection;

public class Santalucia98_04SodiumCorrection extends EntropyCorrection {

	private static String entropyCorrection = "delta S(Na) = delta S(Na = 1M) + 0.368 * (duplexLength - 1) x ln(Na)";

	@Override
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		double NaEq = Helper.computesNaEquivalent(environment);
		if (NaEq == 0){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium concentration must be a positive numeric value.");
			isApplicable = false;
		}
		
		else if (NaEq < 0.05 || NaEq > 1.1){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Santalucia et al. (1998 - 2004) is only reliable for " +
					"sodium concentrations between 0.015M and 1.1M.");
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Santalucia et al. (1998 - 2004) is originally established for " +
			"DNA duplexes.");
		}
		
		if (environment.getSequences().getDuplexLength() > 16){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Santalucia et al. (1998 - 2004) begins to break down for " +
			"DNA duplexes longer than 16 bp.");
		}
		return isApplicable;
	}
	
	@Override
	protected double correctEntropy(Environment environment){
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n he sodium correction is from Santalucia et al. (1998) : ");
		OptionManagement.meltingLogger.log(Level.FINE,entropyCorrection);

		double Na = environment.getNa();
		double entropy = 0.368 * ((double)environment.getSequences().getDuplexLength() - 1.0) * Math.log(Na);
		
		return entropy;
	}
	
	@Override
	public ThermoResult correctMeltingResults(Environment environment) {
		double NaEq = Helper.computesNaEquivalent(environment);
		environment.setNa(NaEq);
		
		return super.correctMeltingResults(environment);
	}
}
