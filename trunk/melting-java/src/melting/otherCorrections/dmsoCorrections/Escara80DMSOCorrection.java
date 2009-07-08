/* Escara JF, Hutton Jr, "Thermal stability and renaturation of DNA
	 * in dimethyl sulfoxide solutions: acceleration of the renaturation rate"
	 * Biopolymers, 1980, 19, 1315-1327.
	 * */

package melting.otherCorrections.dmsoCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.correctionMethods.DMSOCorrections;

public class Escara80DMSOCorrection extends DMSOCorrections {

	private static double parameter = 0.675;
	
	private static String temperatureCorrection = "Tm (x % DMSO) = Tm(0 % DMSO) - 0.675 * x % DMSO";
	
	public ThermoResult correctMeltingResults(Environment environment) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n The DMSO correction from Escara et al.(1980) : ");
		OptionManagement.meltingLogger.log(Level.FINE,temperatureCorrection);
		
		return super.correctMeltingResult(environment, parameter);
	}

}
