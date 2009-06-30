/* Marmur J, Doty P, "Determination of the base composition of 
	 * deoxyribonucleic acid from its thermal denaturation temperature", 
	 * 1962, Journal of molecular biology, 5, 109-118.
	 * 
	 * Chester N, Marshak DR, "dimethyl sulfoxide-mediated primer Tm reduction : 
	 * a method for analyzing the role of renaturation temperature in the polymerase 
	 * chain reaction", 1993, Analytical Biochemistry, 209, 284-290.
	 */

package melting.approximativeMethods;

import java.util.HashMap;
import java.util.logging.Level;

import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.exceptions.NoExistingMethodException;

public class MarmurChester62_93 extends ApproximativeMode{
	
	private double parameter;
	private String temperatureEquation = "Tm = 69.3 + 0.41 * PercentGC - parameter / duplexLength.";
	
	@Override
	public ThermoResult calculateThermodynamics() {
		double Tm = super.calculateThermodynamics().getTm();
		
		Tm = 69.3 + 0.41 * this.environment.getSequences().calculatePercentGC() - parameter / this.environment.getSequences().getDuplexLength();
		
		this.environment.setResult(Tm);
		
		OptionManagement.meltingLogger.log(Level.FINE, " from Marmur et al. (1962) and Chester et al (1993)");
		OptionManagement.meltingLogger.log(Level.FINE, temperatureEquation);
		OptionManagement.meltingLogger.log(Level.FINE, "Where parameter = " + parameter);

		return this.environment.getResult();
	}

	@Override
	public boolean isApplicable() {
		boolean isApplicable = super.isApplicable();
		
		if (environment.getSequences().getPercentMismatching() != 0){
			isApplicable = false;
		}
		
		if (this.environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "the formula of Marmur, Doty, Chester " +
					"and Marshak is originally established for DNA duplexes.");
		}
		if (this.environment.getNa() != 0.0 || this.environment.getMg() != 0.0015 || this.environment.getTris() != 0.01 || this.environment.getK() != 0.05){
			isApplicable = false;
			OptionManagement.meltingLogger.log(Level.WARNING,"the formula of Marmur, Doty, Chester " +
			"and Marshak is originally established at a given ionic strength : " +
			"Na = 0 M, Mg = 0.0015 M, Tris = 0.01 M and k = 0.05 M");
		}
		
		return isApplicable;
	}
	
	@Override
	protected boolean isNaEqPossible(){
		return false;
	}
	
	@Override
	public void setUpVariable(HashMap<String, String> options) {
		String method = options.get(OptionManagement.approximativeMode);
		
		super.setUpVariable(options);
		if (method.equals("che93corr")){
			parameter = 535;
		}
		else if (method.equals("che93")){
			parameter = 650;
		}
		else {
			throw new NoExistingMethodException("The two possible methods for Marmur and Chester, 1962-1993 are MarmurChester62_93_corr and MarmurChester62_93." +
					"The formula is the same but one factor value change : 535 or 650.");
		}
	}

}
