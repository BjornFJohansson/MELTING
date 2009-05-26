package melting.approximativeMethods;

import melting.ThermoResult;

public class Owen69 extends ApproximativeMode{

	/* Owen RJ, Hill LR, Lapage SP. Determination of DNA base 
	 * compositions from melting profiles in dilute buffers. Biopolymers 1969;
	 * 7:503–16.
	 
	 * Frank-Kamenetskii MD. Simplification of the empirical relationship 
	 * between melting temperature of DNA, its GC content and concentration 
	 * of sodium ions in solution. Biopolymers 1971;10:2623– 4.
	 
	 * Blake RD. Denaturation of DNA. In: Meyers RA, ed. Encyclopedia
	 * of molecular biology and molecular medicine, Vol. 2. Weinheim,
	 * Germany: VCH Verlagsgesellschaft, 1996:1–19.
	 
	 * Blake RD, Delcourt SG. Thermal stability of DNA. Nucleic Acids
	 * Res 1998;26:3323–32.
*/
	
	private static String temperatureEquation = "Tm = 87.16 + 0.345 * percentGC + log10(Na) * (20.17 - 0.066 * percentGC)";
	
	public ThermoResult CalculateThermodynamics() {
		int percentGC = this.environment.getSequences().calculatePercentGC();
		double Tm = 87.16 + 0.345 * percentGC + Math.log10(this.environment.getNa()) * (20.17 - 0.066 * percentGC);
		
		this.environment.setResult(Tm);
		
		return this.environment.getResult();
	}

	public boolean isApplicable() {
		boolean isApplicable = super.isApplicable();
		
		if (this.environment.getHybridization().equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : the Owen et al. equation" +
					"was originally established for DNA duplexes.");
		}
		
		return isApplicable;
	}
	
	public String getEquationMeltingTemperature() {
		return temperatureEquation;
	}
}
