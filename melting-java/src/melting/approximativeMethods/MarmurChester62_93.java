package melting.approximativeMethods;

import java.util.HashMap;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class MarmurChester62_93 extends ApproximativeMode{

	/* Marmur J, Doty P, "Determination of the base composition of 
	 * deoxyribonucleic acid from its thermal denaturation temperature", 
	 * 1962, Journal of molecular biology, 5, 109-118.
	 * 
	 * Chester N, Marshak DR, "dimethyl sulfoxide-mediated primer Tm reduction : 
	 * a method for analyzing the role of renaturation temperature in the polymerase 
	 * chain reaction", 1993, Analytical Biochemistry, 209, 284-290.
	 */
	
	private double parameter;
	
	public ThermoResult CalculateThermodynamics() {
		ThermoResult result = super.CalculateThermodynamics();
		
		this.Tm = 69.3 + 0.41 * this.percentGC - this.parameter / this.duplexLength;
		
		result.setTm(this.Tm);
		return result;
	}

	public boolean isApplicable() {
		boolean isApplicable = super.isApplicable();
		double Mg = Double.parseDouble(optionSet.get(OptionManagement.Mg));
		double K = Double.parseDouble(optionSet.get(OptionManagement.K));
		double Tris = Double.parseDouble(optionSet.get(OptionManagement.Tris));
		
		if (this.hybridization.equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : the formula of Marmur, Doty, Chester " +
					"and Marshak is originally established for DNA duplexes.");
		}
		
		if (this.Na != 0 || Mg != 0.0015 || Tris != 0.01 || K == 0.05){
			isApplicable = false;
			System.out.println("WARNING : the formula of Marmur, Doty, Chester " +
			"and Marshak is originally established at a given ionic strength : " +
			"Na = 0 M, Mg = 0.0015 M, Tris = 0.01 M and k = 0.05 M");
		}
		
		return isApplicable;
	}
	
	public void setUpVariable(HashMap<String, String> options) {
		String method = options.get(OptionManagement.approximativeMode);
		
		super.setUpVariable(options);
		
		if (method.equals("MarmurChester62_93_corr")){
			this.parameter = 535;
		}
		else if (method.equals("MarmurChester62_93")){
			this.parameter = 650;
		}
	}

}
