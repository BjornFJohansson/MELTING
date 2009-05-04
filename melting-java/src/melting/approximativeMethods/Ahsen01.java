package melting.approximativeMethods;

import melting.ThermoResult;

public class Ahsen01 extends ApproximativeMode{
	 
	/* Nicolas Von Ahsen, Carl T Wittwer and Ekkehard Schutz, "Oligonucleotide
	 * melting temperatures under PCR conditions : deoxynucleotide Triphosphate
	 * and Dimethyl sulfoxide concentrations with comparison to alternative empirical 
	 * formulas", 2001, Clinical Chemistry, 47, 1956-1961.
	 * */

	public ThermoResult CalculateThermodynamics() {
		ThermoResult result = super.CalculateThermodynamics();
		
		this.Tm = 80.4 + 0.345 * this.percentGC + Math.log10(this.Na) * (17.0 - 0.135 * this.percentGC) - 550 / this.duplexLength;

		result.setTm(this.Tm);		
		return result;
	}

	public boolean isApplicable() {
		boolean isApplicable = super.isApplicable();
		
		if (this.hybridization.equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : the Ahsen et al. equation" +
					"was originally established for DNA duplexes.");
		}
		return isApplicable;
	}

}
