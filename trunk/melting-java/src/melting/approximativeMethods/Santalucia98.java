package melting.approximativeMethods;

import java.util.logging.Level;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Santalucia98 extends ApproximativeMode{
	
	/* Santalucia J Jr, "A unified view of polymer, dumbbel, and 
	 * oligonucleotide DNA nearest-neighbor thermodynamics", Proc
	 * Nacl Acad Sci USA 1998, 95, 1460-1465.
	 * */
	
	private static String temperatureEquation = "Tm = 77.1 + 11.7 * log10(Na) + 0.41 * PercentGC - 528 / duplexLength.";

	public ThermoResult CalculateThermodynamics() {
		double Tm = super.CalculateThermodynamics().getTm(); 
		Tm = 77.1 + 11.7 * Math.log10(this.environment.getNa()) + 0.41 * this.environment.getSequences().calculatePercentGC() - 528 / this.environment.getSequences().getDuplexLength();
		
		this.environment.setResult(Tm);
		
		OptionManagement.meltingLogger.log(Level.FINE, " from Santalucia et al. (1998)");
		OptionManagement.meltingLogger.log(Level.FINE, temperatureEquation);
		
		return this.environment.getResult();
	}

	public boolean isApplicable() {
		boolean isApplicable = super.isApplicable();
		
		if (environment.getSequences().getPercentMismatching() != 0){
			isApplicable = false;
		}
		
		if (this.environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, " The Santalucia equation for polymers" +
					"was originally established for DNA duplexes.");
		}
		
		return isApplicable;
	}
}
