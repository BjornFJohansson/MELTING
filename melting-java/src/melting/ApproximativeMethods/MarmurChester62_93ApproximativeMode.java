package melting.ApproximativeMethods;

import java.util.HashMap;

import melting.CompletCalculMethod;
import melting.Helper;
import melting.ThermoResult;

public class MarmurChester62_93ApproximativeMode implements CompletCalculMethod{

	/* Marmur J, Doty P, "Determination of the base composition of 
	 * deoxyribonucleic acid from its thermal denaturation temperature", 
	 * 1962, Journal of molecular biology, 5, 109-118.
	 * 
	 * Chester N, Marshak DR, "dimethyl sulfoxide-mediated primer Tm reduction : 
	 * a method for analyzing the role of renaturation temperature in the polymerase 
	 * chain reaction", 1993, Analytical Biochemistry, 209, 284-290.
	 */
	public ThermoResult CalculateThermodynamics(HashMap<String, String> options) {
		String seq1 = options.get("sequence");
		String seq2 = options.get("complementarySequence");
		int duplexLength = Math.min(seq1.length(), seq2.length());
		int percentGC = Helper.CalculatePercentGC(seq1, seq2);
		double Tm = 0;
		
		Tm = 69.3 + 0.41 * percentGC - 650 / duplexLength;
		ThermoResult result = new ThermoResult();
		result.setTm(Tm);
		
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options) {
		String hybridization = options.get("hybridization");
		double Na = Double.parseDouble(options.get("Na"));
		double Mg = Double.parseDouble(options.get("Mg"));
		double K = Double.parseDouble(options.get("K"));
		double Tris = Double.parseDouble(options.get("Tris"));
		
		if (hybridization == "dnadna" && Na == 0 && Mg == 0.0015 && Tris == 0.01 && K == 0.05){
			return true;
		}
		return false;
	}

}
