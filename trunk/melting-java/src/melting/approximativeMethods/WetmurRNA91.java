package melting.approximativeMethods;

import java.util.logging.Level;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class WetmurRNA91 extends Wetmur91 {
	
	private static String temperatureEquation = "Tm = 78 + 16.6 * log10(Na / (1.0 + 0.7 * Na)) + 0.7 * percentGC - 500 / duplexLength - percentMismatching";
	
	@Override
	public ThermoResult calculateThermodynamics() {
		double percentGC = this.environment.getSequences().calculatePercentGC();
		double percentMismatching = this.environment.getSequences().getPercentMismatching();
		double duplexLength = (double)this.environment.getSequences().getDuplexLength();
		double Tm = super.calculateThermodynamics().getTm();
		
		Tm = 78.0 + 16.6 * Math.log10(this.environment.getNa() / (1.0 + 0.7 * this.environment.getNa())) + 0.7 * percentGC - 500.0 / duplexLength - percentMismatching;

		this.environment.setResult(Tm);
		
		OptionManagement.meltingLogger.log(Level.FINE, temperatureEquation);
		
		return this.environment.getResult();
	}
	
	@Override
	public boolean isApplicable() {
		
		if (this.environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The wetmur equation used here was originally established for RNA duplexes.");
		}
		
		return super.isApplicable();
	}
}
