package melting.DMSOCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.CorrectionMethods.DMSOCorrections;
import melting.configuration.OptionManagement;

public class Escara80DMSOCorrection extends DMSOCorrections {

	private static double parameter = 0.675;
	
	private static String temperatureCorrection = "Tm (x % DMSO) = Tm(0 % DMSO) - 0.675 * x % DMSO";
	
	/* Escara JF, Hutton Jr, "Thermal stability and renaturation of DNA
	 * in dimethyl sulfoxide solutions: acceleration of the renaturation rate"
	 * Biopolymers, 1980, 19, 1315-1327.
	 * */
	
	public ThermoResult correctMeltingResult(Environment environment) {
		OptionManagement.meltingLogger.log(Level.FINE, "The DMSO correction from Escara et al.(1980) : " + temperatureCorrection);
		
		return super.correctMeltingResult(environment, parameter);
	}

}