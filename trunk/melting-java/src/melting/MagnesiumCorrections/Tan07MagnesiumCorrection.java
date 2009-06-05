package melting.MagnesiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.CorrectionMethods.EntropyCorrection;
import melting.configuration.OptionManagement;

public class Tan07MagnesiumCorrection extends EntropyCorrection {

	/* Zhi-Jie Tan and Shi-Jie Chen," RNA helix stability in Mixed Na+/Mg2+ solutions", 2007, 
	 * Biophysical Journal, 92, 3615-3632.
	 * */
	
	protected static String entropyCorrection = "delta S(Mg) = delta S(Na = 1M) - 3.22 x (duplexLength - 1) x g"; 
	protected static String aFormula = "a2 = -0.6 / duplexLength + 0.025 x ln(Mg) + 0.0068 x ln(Mg)^2";
	protected static String bFormula = "b2 = ln(Mg) + 0.38 x ln(Mg)^2";
	protected static String gFormula = "g2 = a2 + b2 / (duplexLength^2)";
	
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		
		if (environment.getMg() < 0.1 && environment.getMg() > 0.3){
			OptionManagement.meltingLogger.log(Level.WARNING, "The magnesium correction of Zhi-Jie Tan et al. (2007)" +
					"is reliable for magnseium concentrations between 0.1M and 0.3M.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The magnesium correction of Zhi-Jie Tan et al. (2007) is originally established for " +
			"RNA duplexes.");
		}
		return isApplicable;
	}
	
	protected double correctEntropy(Environment environment){

		OptionManagement.meltingLogger.log(Level.FINE, "The magnesium correction from Zhi-Jie Tan et al. (2007) : " + entropyCorrection);

		double entropy = -3.22 * (environment.getSequences().getDuplexLength() - 1) * calculateFreeEnergyPerBaseStack(environment);
		
		return entropy;
	}
	
	public static double calculateFreeEnergyPerBaseStack(Environment environment){
		OptionManagement.meltingLogger.log(Level.FINE, "where : ");
		OptionManagement.meltingLogger.log(Level.FINE, gFormula);
		OptionManagement.meltingLogger.log(Level.FINE, aFormula);
		OptionManagement.meltingLogger.log(Level.FINE, bFormula);
		
		double Mg = environment.getMg() - environment.getDNTP();
		int duplexLength = environment.getSequences().getDuplexLength();
		
		double square = Math.log(Mg) * Math.log(Mg);
		double a = -0.6 / duplexLength + 0.025 * Math.log(Mg) + 0.0068 * square;
		double b = Math.log(Mg) + 0.38 * square;
		double g = a + b / (duplexLength * duplexLength);
		
		return g;
	}
}
