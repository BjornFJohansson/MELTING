package melting.ApproximativeMethods;

import java.util.HashMap;

import melting.CompletCalculMethod;
import melting.Helper;
import melting.ThermoResult;

public class MarmurSchildkrautDotyApproximativeMode implements CompletCalculMethod{

	/*James G. Wetmur, "DNA Probes : applications of the principles of nucleic acid hybridization",
	*1991, Critical reviews in biochemistry and molecular biology, 26, 227-259
	
	 * Marmur J, Doty P, "Determination of the base composition of 
	 * deoxyribonucleic acid from its thermal denaturation temperature", 
	 * 1962, Journal of molecular biology, 5, 109-118.
	  
	 * Chester N, Marshak DR, "dimethyl sulfoxide-mediated primer Tm reduction : 
	 * a method for analyzing the role of renaturation temperature in the polymerase 
	 * chain reaction", 1993, Analytical Biochemistry, 209, 284-290.
	 
	 * Schildkraut C, Lifson S, "Dependance of the melting temperature of DNA on salt 
	 * concentration", 1965, Biopolymers, 3, 95-110.
	 
	 *  Wahl GM, Berger SL, Kimmel AR. Molecular hybridization of
     *  immobilized nucleic acids: theoretical concepts and practical
     *  considerations. Methods Enzymol 1987;152:399 – 407.
                  
     *  Britten RJ, Graham DE, Neufeld BR. Analysis of repeating DNA
     *  sequences by reassociation. Methods Enzymol 1974;29:363–418.
	         
	 *  Hall TJ, Grula JW, Davidson EH, Britten RJ. Evolution of sea urchin
     *  non-repetitive DNA. J Mol Evol 1980;16:95–110.
	 */
	
	private double parameter;
	
	public MarmurSchildkrautDotyApproximativeMode(double parameter){
		this.parameter = parameter;
	}
	
	public ThermoResult CalculateThermodynamics(HashMap<String, String> options) {
		double Tm = 0;
		String seq1 = options.get("sequence");
		String seq2 = options.get("complementarySequence");
		double Na = Double.parseDouble(options.get("Na"));
		int percentGC = Helper.CalculatePercentGC(seq1, seq2);
		int duplexLength = Math.min(seq1.length(), seq2.length());
		
		Tm = 81.5 + 16.6 * Math.log10(Na) + 0.41 * percentGC - this.parameter / duplexLength;
		ThermoResult result = new ThermoResult();
		result.setTm(Tm);
		
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options) {
		String hybridization = options.get("hybridization");
		
		if (hybridization.equals("dnadna")){
			return true;
		}
		return false;
	}

}
