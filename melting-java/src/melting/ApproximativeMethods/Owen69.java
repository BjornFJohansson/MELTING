package melting.ApproximativeMethods;

import java.util.HashMap;

import melting.CompletCalculMethod;
import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Owen69 implements CompletCalculMethod{

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
	
	public ThermoResult CalculateThermodynamics(HashMap<String, String> options) {
		double Tm = 0;
		double Na = Double.parseDouble(options.get(OptionManagement.Na));
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		int percentGC = Helper.CalculatePercentGC(seq1, seq2);
		
		Tm = 87.16 + 0.345 * percentGC + Math.log10(Na) * (20.17 - 0.066 * percentGC);
		
		ThermoResult result = new ThermoResult();
		result.setTm(Tm);
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options) {
		return false;
	}

}
