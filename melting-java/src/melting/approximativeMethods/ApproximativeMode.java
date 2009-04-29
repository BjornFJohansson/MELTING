package melting.approximativeMethods;

import java.util.HashMap;

import melting.CompletCalculMethod;
import melting.Helper;
import melting.SodiumEquivalent;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.sodiumEquivalence.Ahsen01;
import melting.sodiumEquivalence.Owczarzy08;

public class ApproximativeMode implements CompletCalculMethod{

	protected double Tm;
	protected double Na;
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
		this.Na = Double.parseDouble(options.get(OptionManagement.Na));
		
		double Mg = Double.parseDouble(options.get(OptionManagement.Mg));
		double K = Double.parseDouble(options.get(OptionManagement.K));
		double Tris = Double.parseDouble(options.get(OptionManagement.Tris));
		double dNTP = Double.parseDouble(options.get(OptionManagement.dNTP));
		String NaEqMethod = options.get(OptionManagement.NaEquivalentMethod);
		
		if (Mg > 0 || K > 0 || Tris > 0){
			SodiumEquivalent method = setNaEqMethod(NaEqMethod);
			this.Na = method.getSodiumEquivalent(this.Na, Mg, K, Tris, dNTP);
		}
		
		this.Tm = 0;
		this.seq = options.get(OptionManagement.sequence);
		this.seq2 = options.get(OptionManagement.complementarySequence);
		this.duplexLength = Math.min(seq.length(), seq2.length());
		this.percentGC = Helper.CalculatePercentGC(seq, seq2);
		this.hybridization = options.get(OptionManagement.hybridization);
		
		this.optionSet = options;
	}
	
	private SodiumEquivalent setNaEqMethod (String method){
		
		if (method.equals("Ahsen_2001")){
			return new Ahsen01();
		}
		else if (method.equals("Owczarzy_2008")){
			return new Owczarzy08();
		}
		
		return null;
	}

}
