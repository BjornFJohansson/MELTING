package melting.approximativeMethods;

import java.util.HashMap;

import melting.CompletCalculMethod;
import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class ApproximativeMode implements CompletCalculMethod{

	protected double Tm;
	protected double Na;
	protected double Mg;
	protected double K;
	protected double Tris;
	protected double dNTP;
	protected String hybridization;
	protected String seq;
	protected String seq2;
	protected int duplexLength;
	protected int percentGC;
	
	protected HashMap<String, String> optionSet;
	
	public ThermoResult CalculateThermodynamics() {
		ThermoResult result = new ThermoResult();
		result.setTm(Tm);
		
		return result;
	}

	public boolean isApplicable() {
		if (Helper.getPercentMismatching(seq, seq2) != 0){
			System.out.println("WARNING : The approximative mode formulas" +
					"cannot properly account for the presence of mismatches" +
					" and unpaired nucleotides.");
			return false;
		}
		return true;
	}

	public void setUpVariable(HashMap<String, String> options) {
		this.Tm = 0;
		this.Na = Double.parseDouble(options.get(OptionManagement.Na));
		this.seq = options.get(OptionManagement.sequence);
		this.seq2 = options.get(OptionManagement.complementarySequence);
		this.duplexLength = Math.min(seq.length(), seq2.length());
		this.percentGC = Helper.CalculatePercentGC(seq, seq2);
		this.hybridization = options.get(OptionManagement.hybridization);
		
		this.optionSet = options;
	}

}
