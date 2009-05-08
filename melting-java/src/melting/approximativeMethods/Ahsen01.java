package melting.approximativeMethods;

import melting.ThermoResult;

public class Ahsen01 extends ApproximativeMode{
	 
	/* Nicolas Von Ahsen, Carl T Wittwer and Ekkehard Schutz, "Oligonucleotide
	 * melting temperatures under PCR conditions : deoxynucleotide Triphosphate
	 * and Dimethyl sulfoxide concentrations with comparison to alternative empirical 
	 * formulas", 2001, Clinical Chemistry, 47, 1956-1961.
	 * */

	public ThermoResult CalculateThermodynamics() {
		int percentGC = this.environment.getSequences().calculatePercentGC();
		double Tm = 80.4 + 0.345 * percentGC + Math.log10(this.environment.getNa()) * (17.0 - 0.135 * percentGC) - 550 / this.environment.getSequences().getDuplexLength();
		environment.setResult(Tm);
		
		return environment.getResult();
	}

	public boolean isApplicable() {
		boolean isApplicable = super.isApplicable();
		
		if (environment.getHybridization().equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : the Ahsen et al. equation" +
					"was originally established for DNA duplexes.");
		}
		return isApplicable;
	}

}
