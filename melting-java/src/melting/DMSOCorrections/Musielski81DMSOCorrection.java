package melting.DMSOCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.CorrectionMethods.DMSOCorrections;
import melting.configuration.OptionManagement;

public class Musielski81DMSOCorrection extends DMSOCorrections {

	private static double parameter = 0.6;
	
	private static String temperatureCorrection = "Tm (x % DMSO) = Tm(0 % DMSO) - 0.6 * x % DMSO";
	
	/* Musielski H., Mann W, Laue R, Michel S, "Influence of dimethylsulfoxide
	 * on transcription by bacteriophage T3-induced RNA polymerase.", Z allg Microbiol 1981; 21, 447-456.
	 * */
	
	public ThermoResult correctMeltingResult(Environment environment) {
		OptionManagement.meltingLogger.log(Level.INFO, "The DMSO correction from Musielski et al.(1981) : " + temperatureCorrection);
		
		return super.correctMeltingResult(environment, parameter);
	}

}
