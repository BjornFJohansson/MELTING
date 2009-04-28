package melting.ApproximativeMethods;

import java.util.HashMap;

import melting.CompletCalculMethod;
import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Santalucia98 implements CompletCalculMethod{
	
	/* Santalucia J Jr, "A unified view of polymer, dumbbel, and 
	 * oligonucleotide DNA nearest-neighbor thermodynamics", Proc
	 * Nacl Acad Sci USA 1998, 95, 1460-1465.
	 * */

	public ThermoResult CalculateThermodynamics(HashMap<String, String> options) {
		double Tm = 0;
		double Na = Double.parseDouble(options.get(OptionManagement.Na));
		String seq = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		int duplexLength = Math.min(seq.length(), seq2.length());
		int percentGC = Helper.CalculatePercentGC(seq, seq2);
		
		Tm = 77.1 + 11.7 * Math.log10(Na) + 0.41 * percentGC - 528 / duplexLength;
		
		ThermoResult result = new ThermoResult();
		result.setTm(Tm);
		
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options) {
		return false;
	}

}
