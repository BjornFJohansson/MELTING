package melting.approximativeMethods;

import java.util.logging.Level;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class WetmurRNA91 extends Wetmur91 {

	/*James G. Wetmur, "DNA Probes : applications of the principles of nucleic acid hybridization",
	1991, Critical reviews in biochemistry and molecular biology, 26, 227-259*/
	
	private static String temperatureEquation = "Tm = 78 + 16.6 * log10(Na / (1.0 + 0.7 * Na)) + 0.7 * percentGC - 500 / duplexLength - percentMismatching";
	
	public ThermoResult CalculateThermodynamics() {
		int percentGC = this.environment.getSequences().calculatePercentGC();
		int percentMismatching = this.environment.getSequences().getPercentMismatching();
		int duplexLength = this.environment.getSequences().getDuplexLength();
		double Tm = super.CalculateThermodynamics().getTm();
		
		Tm = 78 + 16.6 * Math.log10(this.environment.getNa() / (1.0 + 0.7 * this.environment.getNa())) + 0.7 * percentGC - 500 / duplexLength - percentMismatching;

		this.environment.setResult(Tm);
		
		OptionManagement.meltingLogger.log(Level.FINE, temperatureEquation);
		
		return this.environment.getResult();
	}
	
	public boolean isApplicable() {
		
		if (this.environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The wetmur equation used here was originally established for RNA duplexes.");
		}
		
		return super.isApplicable();
	}
}
