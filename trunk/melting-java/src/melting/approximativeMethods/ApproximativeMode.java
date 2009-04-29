package melting.approximativeMethods;

import java.util.HashMap;

import melting.Helper;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.calculMethodInterfaces.SodiumEquivalentMethod;
import melting.configuration.OptionManagement;
import melting.configuration.SetCalculMethod;
import melting.sodiumEquivalence.Ahsen01_NaEquivalent;
import melting.sodiumEquivalence.Owczarzy08_NaEquivalent;

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
		
		if (Mg > 0 || K > 0 || Tris > 0){
			SodiumEquivalentMethod method = SetCalculMethod.setNaEqMethod(options);
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
	
	private SodiumEquivalentMethod setNaEqMethod (String method){
		
		if (method.equals("Ahsen_2001")){
			return new Ahsen01_NaEquivalent();
		}
		else if (method.equals("Owczarzy_2008")){
			return new Owczarzy08_NaEquivalent();
		}
		
		return null;
	}

}
