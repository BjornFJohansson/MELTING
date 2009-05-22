package melting.DMSOCorrections;

import melting.Environment;
import melting.ThermoResult;
import melting.CorrectionMethods.DMSOCorrections;

public class Escara80DMSOCorrection extends DMSOCorrections {

	private static double parameter = 0.675;
	
	/* Escara JF, Hutton Jr, "Thermal stability and renaturation of DNA
	 * in dimethyl sulfoxide solutions: acceleration of the renaturation rate"
	 * Biopolymers, 1980, 19, 1315-1327.
	 * */
	
	public ThermoResult correctMeltingResult(Environment environment) {
		return super.correctMeltingResult(environment, parameter);
	}

}
