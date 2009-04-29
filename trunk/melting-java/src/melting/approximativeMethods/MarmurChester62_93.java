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
	
	public MarmurChester62_93(double parameter){
		this.parameter = parameter;
	}
	
	public ThermoResult CalculateThermodynamics() {
		ThermoResult result = super.CalculateThermodynamics();
		
		this.Tm = 69.3 + 0.41 * this.percentGC - this.parameter / this.duplexLength;
		
		result.setTm(this.Tm);
		return result;
	}

	public boolean isApplicable() {
		boolean isApplicable = super.isApplicable();
		
		if (this.hybridization.equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : the formula of Marmur, Doty, Chester " +
					"and Marshak is originally established for DNA duplexes.");
		}
		
		if (this.Na != 0 || this.Mg != 0.0015 || this.Tris != 0.01 || this.K == 0.05){
			isApplicable = false;
			System.out.println("WARNING : the formula of Marmur, Doty, Chester " +
			"and Marshak is originally established at a given ionic strength : " +
			"Na = 0 M, Mg = 0.0015 M, Tris = 0.01 M and k = 0.05 M");
		}
		
		if (Integer.getInteger(optionSet.get(OptionManagement.threshold)) <= this.duplexLength){
			isApplicable = false;
			System.out.println("WARNING : the formula of Marmur, Doty, Chester " +
			"and Marshak is originally established for long DNA duplexes. (length superior to " +
			 optionSet.get(OptionManagement.threshold) +")");
		}
		
		return isApplicable;
	}
	
	public void setUpVariable(HashMap<String, String> options) {
		super.setUpVariable(options);
	}

}
