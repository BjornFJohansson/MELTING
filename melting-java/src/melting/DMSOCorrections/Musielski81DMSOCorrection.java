package melting.DMSOCorrections;

import melting.Environment;
import melting.ThermoResult;
import melting.CorrectionMethods.DMSOCorrections;

public class Musielski81DMSOCorrection extends DMSOCorrections {

	private static double parameter = 0.6;
	
	/* Musielski H., Mann W, Laue R, Michel S, "Influence of dimethylsulfoxide
	 * on transcription by bacteriophage T3-induced RNA polymerase.", Z allg Microbiol 1981; 21, 447-456.
	 * */
	
	public ThermoResult correctMeltingResult(Environment environment) {
		return super.correctMeltingResult(environment, parameter);
	}

}
