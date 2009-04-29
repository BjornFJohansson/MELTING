package melting.approximativeMethods;

import java.util.HashMap;

import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Wetmur91 extends ApproximativeMode{
	
	/*James G. Wetmur, "DNA Probes : applications of the principles of nucleic acid hybridization",
	1991, Critical reviews in biochemistry and molecular biology, 26, 227-259*/

	private int percentMismatching;
	
	public boolean isApplicable() {

		boolean isApplicable = super.isApplicable();
		
		if (this.hybridization.equals("rnarna") == false || this.hybridization.equals("dnadna") == false || this.hybridization.equals("dnarna") == false || this.hybridization.equals("rnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : the wetmur equation was originally established for DNA, RNA or hybrid DNA/RNA duplexes.");
		}
		
		if (Integer.getInteger(optionSet.get(OptionManagement.threshold)) <= this.duplexLength){
			isApplicable = false;
			System.out.println("WARNING : the Marmur-Schildkraut-Doty equation " +
			"was originally established for long DNA duplexes. (length superior to " +
			 optionSet.get(OptionManagement.threshold) +")");
		}
		
		return isApplicable;
	}
	
	public ThermoResult CalculateThermodynamics() {
		ThermoResult result = super.CalculateThermodynamics();
		
		if (this.hybridization.equals("dnadna")){
			this.Tm = 81.5 + 16.6 * Math.log10(this.Na / (1.0 + 0.7 * this.Na)) + 0.41 * this.percentGC - 500 / this.duplexLength - this.percentMismatching;
		}
		else if (this.hybridization.equals("rnarna")){
			this.Tm = 78 + 16.6 * Math.log10(this.Na / (1.0 + 0.7 * this.Na)) + 0.7 * this.percentGC - 500 / this.duplexLength - this.percentMismatching;
		}
		else if (this.hybridization.equals("dnarna") ||this. hybridization.equals("rnadna")){
			this.Tm = 67 + 16.6 * Math.log10(this.Na / (1.0 + 0.7 *this.Na)) + 0.8 * this.percentGC - 500 / this.duplexLength - this.percentMismatching;
		}
		
		result.setTm(this.Tm);
		return result;
	}
	
	public void setUpVariable(HashMap<String, String> options) {
		super.setUpVariable(options);
		
		this.percentMismatching = Helper.getPercentMismatching(this.seq, this.seq2);
	}

}
