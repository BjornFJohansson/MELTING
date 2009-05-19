package melting.sodiumCorrections;

import melting.Environment;

public class Ahsen01SodiumCorrection extends EntropySodiumCorrection {

	/* Nicolas Von Ahsen, Carl T Wittwer and Ekkehard Schutz, "Oligonucleotide
	 * melting temperatures under PCR conditions : deoxynucleotide Triphosphate
	 * and Dimethyl sulfoxide concentrations with comparison to alternative empirical 
	 * formulas", 2001, Clinical Chemistry, 47, 1956-1961.
	 * */
	
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		double NaEq = calculateNaEqEquivalent(environment);
		
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
	
	protected double correctEntropy(double Na, int duplexLength){
		double entropy = 0.847 * (duplexLength - 1) * Math.log(Na);
		
		return entropy;
	}
}
