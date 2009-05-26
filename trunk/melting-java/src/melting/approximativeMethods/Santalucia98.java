package melting.approximativeMethods;

import melting.ThermoResult;

public class Santalucia98 extends ApproximativeMode{
	
	/* Santalucia J Jr, "A unified view of polymer, dumbbel, and 
	 * oligonucleotide DNA nearest-neighbor thermodynamics", Proc
	 * Nacl Acad Sci USA 1998, 95, 1460-1465.
	 * */
	
	private static String temperatureEquation = "Tm = 77.1 + 11.7 * log10(Na) + 0.41 * PercentGC - 528 / duplexLength.";

	public ThermoResult CalculateThermodynamics() {
		double Tm = 77.1 + 11.7 * Math.log10(this.environment.getNa()) + 0.41 * this.environment.getSequences().calculatePercentGC() - 528 / this.environment.getSequences().getDuplexLength();
		
		this.environment.setResult(Tm);
		
		return this.environment.getResult();
	}

	public boolean isApplicable() {
		boolean isApplicable = super.isApplicable();
		
		if (this.environment.getHybridization().equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : the polymer Santalucia equation" +
					"was originally established for DNA duplexes.");
		}
		
		return isApplicable;
	}
	
	public String getEquationMeltingTemperature() {
		return temperatureEquation;
	}
}
