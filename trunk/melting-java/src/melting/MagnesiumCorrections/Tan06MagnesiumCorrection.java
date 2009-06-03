package melting.MagnesiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.CorrectionMethods.EntropyCorrection;
import melting.configuration.OptionManagement;

public class Tan06MagnesiumCorrection extends EntropyCorrection {

	/* Zhi-Jie Tan and Shi-Jie Chen, "Nucleic acid helix stability: effects of Salt concentration, 
	 * cation valence and size, and chain length", 2006, Biophysical Journal, 90, 1175-1190. 
	 * */
	
	protected static String entropyCorrection = "delta S(Na) = delta S(Na = 1M) - 3.22 x (duplexLength - 1) x g"; 
	protected static String aFormula = "a1 = 0.02 x ln(Mg) + 0.0068 x ln(Mg)^2";
	protected static String bFormula = "b1 = 1.18 x ln(Mg) + 0.344 * ln(Mg)^2";
	protected static String gFormula = "g1 = a1 + b1 / (duplexLength^2)";
	
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		
		if (environment.getMg() < 0.0001 && environment.getMg() > 1){
			OptionManagement.meltingLogger.log(Level.WARNING, "The magnesium correction of Zhi-Jie Tan et al. (2006)" +
					"is reliable for magnesium concentrations between 0.001M and 1M.");
			isApplicable = false;
		}
		
		if (environment.getSequences().getDuplexLength() < 6){
			OptionManagement.meltingLogger.log(Level.WARNING, "The magnesium correction of Zhi-Jie Tan et al. (2006)" +
			"is valid for oligonucleotides with a number of base pairs superior or equal to 6.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The magnesium correction of Zhi-Jie Tan et al. (2006) is originally established for " +
			"DNA duplexes.");
		}
		return isApplicable;
	}
	
	protected double correctEntropy(Environment environment){
		
		OptionManagement.meltingLogger.log(Level.INFO, "The magnesium correction from Zhi-Jie Tan et al. (2006) : " + entropyCorrection);

		double entropy = -3.22 * (environment.getSequences().getDuplexLength() - 1) * calculateFreeEnergyPerBaseStack(environment);
		
		return entropy;
	}
	
	public static double calculateFreeEnergyPerBaseStack(Environment environment){
		OptionManagement.meltingLogger.log(Level.INFO, "where : ");
		OptionManagement.meltingLogger.log(Level.INFO, gFormula);
		OptionManagement.meltingLogger.log(Level.INFO, aFormula);
		OptionManagement.meltingLogger.log(Level.INFO, bFormula);
		
		double Mg = environment.getMg() - environment.getDNTP();
		int duplexLength = environment.getSequences().getDuplexLength();
		
		double square = Math.log(Mg) * Math.log(Mg);
		double a = 0.02 * Math.log(Mg) + 0.0068 * square;
		double b = 1.18 * Math.log(Mg) + 0.344 * square;
		
		double g = a + b / (duplexLength * duplexLength);
		
		return g;
	}

}
