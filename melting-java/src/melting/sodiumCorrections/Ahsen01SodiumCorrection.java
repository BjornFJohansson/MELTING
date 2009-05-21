package melting.sodiumCorrections;

import melting.Environment;
import melting.Helper;
import melting.ionCorrections.EntropyCorrection;

public class Ahsen01SodiumCorrection extends EntropyCorrection {

	/* Nicolas Von Ahsen, Carl T Wittwer and Ekkehard Schutz, "Oligonucleotide
	 * melting temperatures under PCR conditions : deoxynucleotide Triphosphate
	 * and Dimethyl sulfoxide concentrations with comparison to alternative empirical 
	 * formulas", 2001, Clinical Chemistry, 47, 1956-1961.
	 * */
	
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		if (NaEq == 0){
			System.out.println("ERROR : The sodium concentration must be strictly positive.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("ERROR : The sodium correction of Ahsen et al. (2001) is originally established for " +
			"DNA duplexes.");
			isApplicable = false;
		}
		return isApplicable;
	}
	
	protected double correctEntropy(Environment environment){
		double entropy = 0.847 * (environment.getSequences().getDuplexLength() - 1) * Math.log(environment.getNa());
		
		return entropy;
	}
}
