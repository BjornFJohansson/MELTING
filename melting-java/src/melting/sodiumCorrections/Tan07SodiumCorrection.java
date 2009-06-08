package melting.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.CorrectionMethods.EntropyCorrection;
import melting.configuration.OptionManagement;

public class Tan07SodiumCorrection extends EntropyCorrection {
	
	/* Zhi-Jie Tan and Shi-Jie Chen," RNA helix stability in Mixed Na+/Mg2+ solutions", 2007, 
	 * Biophysical Journal, 92, 3615-3632.
	 * */
	
	private static String entropyCorrection = "delta S(Na) = delta S(Na = 1M) - 3.22 x (duplexLength - 1) x g"; 
	private static String aFormula = "a1 = -0.075 x ln(Na) + 0.012 x ln(Mg)^2";
	private static String bFormula = "b1 = 0.018 x ln(Mg)^2";
	private static String gFormula = "g1 = a1 + b1 / duplexLength";
	
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		if (NaEq < 0.003 && NaEq > 1){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Zhi-Jie Tan et al. (2007)" +
					"is reliable for sodium concentrations between 0.003M and 1M.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Zhi-Jie Tan et al. (2007) is originally established for " +
			"RNA duplexes.");
		}
		return isApplicable;
	}
	
	protected double correctEntropy(Environment environment){
		
		double entropy = -3.22 * (environment.getSequences().getDuplexLength() - 1) * calculateFreeEnergyPerBaseStack(environment);
		
		return entropy;
	}
	
	public ThermoResult correctMeltingResult(Environment environment) {
		
		OptionManagement.meltingLogger.log(Level.FINE, "The sodium correction from Zhi-Jie Tan et al. (2007) : " + entropyCorrection);

		double NaEq = Helper.calculateNaEquivalent(environment);
		environment.setNa(NaEq);
		
		return super.correctMeltingResult(environment);
	}
	
	public static double calculateFreeEnergyPerBaseStack(Environment environment){
		
		OptionManagement.meltingLogger.log(Level.FINE, "where : ");
		OptionManagement.meltingLogger.log(Level.FINE, gFormula);
		OptionManagement.meltingLogger.log(Level.FINE, aFormula);
		OptionManagement.meltingLogger.log(Level.FINE, bFormula);
		
		double Na = environment.getNa();
		double square = Math.log(Na) * Math.log(Na);
		double a = -0.075 * Math.log(Na) + 0.012 * square;
		double b = 0.018 * square;
		
		double g = a + b / environment.getSequences().getDuplexLength();
		
		return g;
	}
}