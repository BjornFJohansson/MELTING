package melting.approximativeMethods;

import java.util.logging.Level;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class WetmurDNARNA91 extends Wetmur91 {

	/*James G. Wetmur, "DNA Probes : applications of the principles of nucleic acid hybridization",
	1991, Critical reviews in biochemistry and molecular biology, 26, 227-259*/
	
	private static String temperatureEquation = "Tm = 67 + 16.6 * log10(Na / (1.0 + 0.7 * Na)) + 0.8 * percentGC - 500 / duplexLength - percentMismatching";
	
	public ThermoResult CalculateThermodynamics() {
		double percentGC = this.environment.getSequences().calculatePercentGC();
		double percentMismatching = this.environment.getSequences().getPercentMismatching();
		int duplexLength = this.environment.getSequences().getDuplexLength();
		double Tm = super.CalculateThermodynamics().getTm();
		
		Tm = 67.0 + 16.6 * Math.log10(this.environment.getNa() / (1.0 + 0.7 *this.environment.getNa())) + 0.8 * percentGC - 500.0 / duplexLength - percentMismatching;

		this.environment.setResult(Tm);
		
		OptionManagement.meltingLogger.log(Level.FINE, temperatureEquation);
		
		return this.environment.getResult();
	}
	
	public boolean isApplicable() {
		
		if (this.environment.getHybridization().equals("rnarna") == false || this.environment.getHybridization().equals("dnadna") == false || this.environment.getHybridization().equals("dnarna") == false || this.environment.getHybridization().equals("rnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The wetmur equation used here was originally established for hybrid DNA/RNA duplexes.");
		}
		
		return super.isApplicable();
	}
}
