package melting.approximativeMethods;

import java.util.logging.Level;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class WetmurDNA91 extends Wetmur91 {
	
	private static String temperatureEquation = "Tm = 81.5 + 16.6 * log10(Na / (1.0 + 0.7 * Na)) + 0.41 * percentGC - 500 / duplexLength - percentMismatching";
	
	@Override
	public ThermoResult computesThermodynamics() {
		double percentGC = this.environment.getSequences().computesPercentGC();
		double percentMismatching = this.environment.getSequences().computesPercentMismatching();
		double duplexLength = (double)this.environment.getSequences().getDuplexLength();
		double Tm = super.computesThermodynamics().getTm(); 
		
		Tm = 81.5 + 16.6 * Math.log10(this.environment.getNa() / (1.0 + 0.7 * this.environment.getNa())) + 0.41 * percentGC - 500.0 / duplexLength - percentMismatching;

		this.environment.setResult(Tm);
		
		OptionManagement.meltingLogger.log(Level.FINE, temperatureEquation);
		
		return this.environment.getResult();
	}
	
	@Override
	public boolean isApplicable() {
		
		if (this.environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The wetmur equation used here was originally established for DNA duplexes.");
		}
		
		return super.isApplicable();
	}
}
