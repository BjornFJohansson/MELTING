package melting.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.CorrectionMethods.EntropyCorrection;
import melting.configuration.OptionManagement;

public class Ahsen01SodiumCorrection extends EntropyCorrection {

	/* Nicolas Von Ahsen, Carl T Wittwer and Ekkehard Schutz, "Oligonucleotide
	 * melting temperatures under PCR conditions : deoxynucleotide Triphosphate
	 * and Dimethyl sulfoxide concentrations with comparison to alternative empirical 
	 * formulas", 2001, Clinical Chemistry, 47, 1956-1961.
	 * */
	
	private static String entropyCorrection = "delat S(Na) = delta S(Na = 1M) + 0.847 x (duplexLength - 1) x ln(Na)";
	
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		if (NaEq == 0){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium concentration must be strictly positive.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Ahsen et al. (2001) is originally established for " +
			"DNA duplexes.");
		}
		return isApplicable;
	}
	
	protected double correctEntropy(Environment environment){
		
		OptionManagement.meltingLogger.log(Level.FINE, "The sodium correction is from Ahsen et al. (2001) : " + entropyCorrection);
		
		double entropy = 0.847 * (environment.getSequences().getDuplexLength() - 1) * Math.log(environment.getNa());
		
		return entropy;
	}
}