/* Zhi-Jie Tan and Shi-Jie Chen, "Nucleic acid helix stability: effects of Salt concentration, 
	 * cation valence and size, and chain length", 2006, Biophysical Journal, 90, 1175-1190. 
	 * */

package melting.ionCorrection.sodiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.correctionMethods.EntropyCorrection;

public class Tan06SodiumCorrection extends EntropyCorrection {
	
	private static String entropyCorrection = "delta S(Na) = delta S(Na = 1M) - 3.22 x (duplexLength - 1) x g"; 
	private static String aFormula = "a1 = -0.07 x ln(Na) + 0.012 x ln(Mg)^2";
	private static String bFormula = "b1 = 0.013 x ln(Mg)^2";
	private static String gFormula = "g1 = a1 + b1 / duplexLength";
	
	@Override
	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		double NaEq = Helper.calculateNaEquivalent(environment);
		if (NaEq == 0){
			OptionManagement.meltingLogger.log(Level.WARNING, " The sodium concentration must be a positive numeric value.");
			isApplicable = false;
		}
		
		else if (NaEq < 0.001 && NaEq > 1){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Zhi-Jie Tan et al. (2006)" +
					"is reliable for sodium concentrations between 0.001M and 1M.");
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The sodium correction of Zhi-Jie Tan et al. (2006) is originally established for " +
			"DNA duplexes.");
		}
		return isApplicable;
	}
	
	@Override
	protected double correctEntropy(Environment environment){
		
		double entropy = -3.22 * ((double)environment.getSequences().getDuplexLength() - 1.0) * calculateFreeEnergyPerBaseStack(environment);
		
		return entropy;
	}
	
	@Override
	public ThermoResult correctMeltingResults(Environment environment) {
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The sodium correction from Zhi-Jie Tan et al. (2006) : ");
		OptionManagement.meltingLogger.log(Level.FINE,entropyCorrection);

		double NaEq = Helper.calculateNaEquivalent(environment);
		environment.setNa(NaEq);
		
		return super.correctMeltingResults(environment);
	}
	
	public static double calculateFreeEnergyPerBaseStack(Environment environment){
		OptionManagement.meltingLogger.log(Level.FINE, "where : ");
		OptionManagement.meltingLogger.log(Level.FINE, gFormula);
		OptionManagement.meltingLogger.log(Level.FINE, aFormula);
		OptionManagement.meltingLogger.log(Level.FINE, bFormula);
		
		double Na = environment.getNa();
		double square = Math.log(Na) * Math.log(Na);
		double a = -0.07 * Math.log(Na) + 0.012 * square;
		double b = 0.013 * square;
		
		double g = a + b / (double)environment.getSequences().getDuplexLength();
		
		return g;
	}
}
