package melting.DMSOCorrections;

import melting.Environment;
import melting.ThermoResult;
import melting.CorrectionMethods.DMSOCorrections;

public class Cullen76DMSOCorrection extends DMSOCorrections {

	private static double parameter = 0.5;
	
	/* Cullen Br, Bick Md, "Thermal denaturation of DNA from bromodeoxyuridine substitued cells."
	 * Nucleic acids research, 1976, 3, 49-62.
	 * */
	
	public ThermoResult correctMeltingResult(Environment environment) {
		return super.correctMeltingResult(environment, parameter);
	}

}
