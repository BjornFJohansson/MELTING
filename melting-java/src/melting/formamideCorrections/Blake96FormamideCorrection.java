package melting.formamideCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CorrectionMethod;
import melting.configuration.OptionManagement;

public class Blake96FormamideCorrection implements CorrectionMethod{

	private static String temperatureCorrection = "Tm (x mol formamide) = Tm(0 mole formamide) + (0.453 * Fgc - 2.88) * x mole formamide";
	
	public ThermoResult correctMeltingResult(Environment environment) {
		double Fgc = environment.getSequences().calculatePercentGC() / 100;
		double Tm = environment.getResult().getTm() + (0.453 * Fgc - 2.88) * environment.getFormamide();
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The formamide correction from Blake et al.(1996) : ");
		OptionManagement.meltingLogger.log(Level.FINE,temperatureCorrection);
		
		environment.setResult(Tm);
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The formamide correction from Blake et al.(1996) are established for DNA duplexes.");
		}
		return true;
	}

}
