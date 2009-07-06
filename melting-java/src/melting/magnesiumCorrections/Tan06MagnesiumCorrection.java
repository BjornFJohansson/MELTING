/* Zhi-Jie Tan and Shi-Jie Chen, "Nucleic acid helix stability: effects of Salt concentration, 
	 * cation valence and size, and chain length", 2006, Biophysical Journal, 90, 1175-1190. 
	 * */

package melting.magnesiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.configuration.OptionManagement;
import melting.correctionMethods.EntropyCorrection;

public class Tan06MagnesiumCorrection extends EntropyCorrection {
	
	protected static String entropyCorrection = "delta S(Mg) = delta S(Na = 1M) - 3.22 x (duplexLength - 1) x g"; 
	protected static String aFormula = "a2 = 0.02 x ln(Mg) + 0.0068 x ln(Mg)^2";
	protected static String bFormula = "b2 = 1.18 x ln(Mg) + 0.344 * ln(Mg)^2";
	protected static String gFormula = "g2 = a2 + b2 / (duplexLength^2)";
	
	@Override
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		if (environment.getMg() == 0){
			OptionManagement.meltingLogger.log(Level.WARNING, " The magnesium concentration must be a positive numeric value.");
			isApplicable = false;
		}
		
		else if (environment.getMg() < 0.0001 && environment.getMg() > 1){
			OptionManagement.meltingLogger.log(Level.WARNING, "The magnesium correction of Zhi-Jie Tan et al. (2006)" +
					"is reliable for magnesium concentrations between 0.001M and 1M.");
		}
		
		if (environment.getSequences().getDuplexLength() < 6){
			OptionManagement.meltingLogger.log(Level.WARNING, "The magnesium correction of Zhi-Jie Tan et al. (2006)" +
			"is valid for oligonucleotides with a number of base pairs superior or equal to 6.");
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The magnesium correction of Zhi-Jie Tan et al. (2006) is originally established for " +
			"DNA duplexes.");
		}
		return isApplicable;
	}
	
	@Override
	protected double correctEntropy(Environment environment){
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The magnesium correction from Zhi-Jie Tan et al. (2006) : ");
		OptionManagement.meltingLogger.log(Level.FINE,entropyCorrection);

		double entropy = -3.22 * ((double)environment.getSequences().getDuplexLength() - 1) * calculateFreeEnergyPerBaseStack(environment);
		
		return entropy;
	}
	
	public static double calculateFreeEnergyPerBaseStack(Environment environment){
		OptionManagement.meltingLogger.log(Level.FINE, "where : ");
		OptionManagement.meltingLogger.log(Level.FINE, gFormula);
		OptionManagement.meltingLogger.log(Level.FINE, aFormula);
		OptionManagement.meltingLogger.log(Level.FINE, bFormula);
		
		double Mg = environment.getMg() - environment.getDNTP();
		double duplexLength = (double)environment.getSequences().getDuplexLength();
		
		double square = Math.log(Mg) * Math.log(Mg);
		double a = 0.02 * Math.log(Mg) + 0.0068 * square;
		double b = 1.18 * Math.log(Mg) + 0.344 * square;
		
		double g = a + b / (duplexLength * duplexLength);
		
		return g;
	}

}