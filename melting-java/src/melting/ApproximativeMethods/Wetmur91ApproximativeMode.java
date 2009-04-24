package melting.ApproximativeMethods;

import java.util.HashMap;

import melting.CompletCalculMethod;
import melting.Helper;
import melting.ThermoResult;

public class Wetmur91ApproximativeMode implements CompletCalculMethod{
	
	/*James G. Wetmur, "DNA Probes : applications of the principles of nucleic acid hybridization",
	1991, Critical reviews in biochemistry and molecular biology, 26, 227-259*/

	public boolean isApplicable(HashMap<String, String> options) {
		String hybridization = options.get("hybridization");
		
		if (hybridization == "rnarna" || hybridization == "dnadna" || hybridization == "dnarna"){
			return true;
		}
		return false;
	}
	
	public ThermoResult CalculateThermodynamics(
			HashMap<String, String> options) {
		String hybridization = options.get("hybridization");
		double Tm = 0;
		double Na = Double.parseDouble(options.get("Na"));
		String seq1 = options.get("sequence");
		String seq2 = options.get("complementarySequence");
		int percentGC = Helper.CalculatePercentGC(seq1, seq2);
		int percentMismatching = getPercentMismatching(seq1, seq2);
		int duplexLength = Math.min(seq1.length(),seq2.length());
		
		if (hybridization.equals("dnadna")){
			Tm = 81.5 + 16.6 * Math.log10(Na / (1.0 + 0.7 * Na)) + 0.41 * percentGC - 500/duplexLength - percentMismatching;
		}
		else if (hybridization.equals("rnarna")){
			Tm = 78 + 16.6 * Math.log10(Na / (1.0 + 0.7 * Na)) + 0.7 * percentGC - 500/duplexLength - percentMismatching;
		}
		else if (hybridization.equals("dnarna")){
			Tm = 67 + 16.6 * Math.log10(Na / (1.0 + 0.7 * Na)) + 0.8 * percentGC - 500/duplexLength - percentMismatching;
		}
		
		ThermoResult result = new ThermoResult();
		result.setTm(Tm);
		return result;
	}
	
	private int getPercentMismatching(String seq1, String seq2){
		int numberMismatching = 0;
		for (int i = 0; i < Math.min(seq1.length(), seq2.length()); i++){
			if (Helper.isComplementaryBasePair(seq1.charAt(i), seq2.charAt(i)) == false){
				numberMismatching++;
			}
		}
		return numberMismatching / Math.min(seq1.length(), seq2.length()) * 100;
	}

}
