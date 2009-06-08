package melting.approximativeMethods;

import java.util.logging.Level;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Ahsen01 extends ApproximativeMode{
	 
	/* Nicolas Von Ahsen, Carl T Wittwer and Ekkehard Schutz, "Oligonucleotide
	 * melting temperatures under PCR conditions : deoxynucleotide Triphosphate
	 * and Dimethyl sulfoxide concentrations with comparison to alternative empirical 
	 * formulas", 2001, Clinical Chemistry, 47, 1956-1961.
	 * */

	private static String temperatureEquation = "Tm = 80.4 + 0.345 * percentGC + log10(Na) * (17.0 - 0.135 * percentGC) - 550 / duplexLength.";
	
	public ThermoResult CalculateThermodynamics() {
		int percentGC = this.environment.getSequences().calculatePercentGC();
		double Tm = 80.4 + 0.345 * percentGC + Math.log10(this.environment.getNa()) * (17.0 - 0.135 * percentGC) - 550 / this.environment.getSequences().getDuplexLength();
		environment.setResult(Tm);
		
		OptionManagement.meltingLogger.log(Level.FINE, " from Ahsen et al. (2001) \n");
		OptionManagement.meltingLogger.log(Level.FINE, temperatureEquation);
		
		return environment.getResult();
	}

	public boolean isApplicable() {
		boolean isApplicable = super.isApplicable();
		
		if (environment.getSequences().getPercentMismatching() != 0){
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "the Ahsen et al. equation" +
					"was originally established for DNA duplexes.");
		}
		return isApplicable;
	}
}