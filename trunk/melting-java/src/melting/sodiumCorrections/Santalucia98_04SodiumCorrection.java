package melting.sodiumCorrections;

import melting.Environment;

public class Santalucia98_04SodiumCorrection extends EntropySodiumCorrection {

	/* John Santalucia, Jr., "A unified view of polymer, dumbbell, and oligonucleotide DNA nearest-neighbor
	 * thermodynamics.", 1998, Proc. Natl. Acad. Sci. USA, 95, 1460-1465*/
	
	/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		double NaEq = calculateNaEqEquivalent(environment);
		
		if (NaEq < 0.05 || NaEq > 1.1){
			System.out.println("ERROR : The sodium correction of Santalucia et al. (1998 - 2004) is only reliable for " +
					"sodium concentrations between 0.015M and 1.1M.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("ERROR : The sodium correction of Santalucia et al. (1998 - 2004) is originally established for " +
			"DNA duplexes.");
			isApplicable = false;
		}
		
		if (environment.getSequences().getDuplexLength() > 16){
			System.out.println("ERROR : The sodium correction of Santalucia et al. (1998 - 2004) begins to break down for " +
			"DNA duplexes longer than 16 bp.");
			isApplicable = false;
		}
		return isApplicable;
	}
	
	protected double correctEntropy(double Na, int duplexLength){
		double entropy = 0.368 * (duplexLength - 1) * Math.log(Na);
		
		return entropy;
	}
}
