package melting.approximativeMethods;

import java.util.HashMap;

import melting.Helper;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CompletCalculMethod;
import melting.calculMethodInterfaces.SodiumEquivalentMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

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
		boolean isApplicable = true;
		
		if (Helper.getPercentMismatching(seq, seq2) != 0){
			System.out.println("WARNING : The approximative mode formulas" +
					"cannot properly account for the presence of mismatches" +
					" and unpaired nucleotides.");
			isApplicable = false;
		}
		if (Integer.getInteger(optionSet.get(OptionManagement.threshold)) <= this.duplexLength){
			
			if (this.optionSet.get(OptionManagement.completMethod).equals("default") == false){
				isApplicable = false;
			}
			
			System.out.println("WARNING : the approximative equations " +
			"were originally established for long DNA duplexes. (length superior to " +
			 optionSet.get(OptionManagement.threshold) +")");
		}
		return isApplicable;
	}

	public void setUpVariable(HashMap<String, String> options) {
		this.Na = Double.parseDouble(options.get(OptionManagement.Na));
		
		double Mg = Double.parseDouble(options.get(OptionManagement.Mg));
		double K = Double.parseDouble(options.get(OptionManagement.K));
		double Tris = Double.parseDouble(options.get(OptionManagement.Tris));
		double dNTP = Double.parseDouble(options.get(OptionManagement.dNTP));
		
		if (Mg > 0 || K > 0 || Tris > 0){
			RegisterCalculMethod setNaEqMethod = new RegisterCalculMethod();
			SodiumEquivalentMethod method = setNaEqMethod.getNaEqMethod(options);
			this.Na = method.getSodiumEquivalent(this.Na, Mg, K, Tris, dNTP);
		}
		
		this.Tm = 0;
		this.seq = options.get(OptionManagement.sequence);
		this.seq2 = options.get(OptionManagement.complementarySequence);
		this.duplexLength = Math.min(seq.length(), seq2.length());
		this.percentGC = Helper.calculatePercentGC(seq, seq2);
		this.hybridization = options.get(OptionManagement.hybridization);
		
		this.optionSet = options;
	}

}
