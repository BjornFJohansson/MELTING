package melting.ApproximativeMethods;

import java.util.HashMap;

import melting.CompletCalculMethod;
import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Ahsen01 implements CompletCalculMethod{

	public ThermoResult CalculateThermodynamics(HashMap<String, String> options) {
		double Tm = 0;
		double Na = Double.parseDouble(options.get(OptionManagement.Na));
		String seq = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		int duplexLength = Math.min(seq.length(), seq2.length());
		int percentGC = Helper.CalculatePercentGC(seq, seq2);
		
		Tm = 80.4 + 0.345 * percentGC + Math.log10(Na) * (17.0 - 0.135 * percentGC) - 550 / duplexLength;
		
		ThermoResult result = new ThermoResult();
		result.setTm(Tm);
		
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options) {
		return false;
	}

}
