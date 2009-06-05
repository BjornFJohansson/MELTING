package melting.DMSOCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.CorrectionMethods.DMSOCorrections;
import melting.configuration.OptionManagement;

public class Cullen76DMSOCorrection extends DMSOCorrections {

	private static double parameter = 0.5;
	
	private static String temperatureCorrection = "Tm (x % DMSO) = Tm(0 % DMSO) - 0.5 * x % DMSO";
	
	/* Cullen Br, Bick Md, "Thermal denaturation of DNA from bromodeoxyuridine substitued cells."
	 * Nucleic acids research, 1976, 3, 49-62.
	 * */
	
	public ThermoResult correctMeltingResult(Environment environment) {
		OptionManagement.meltingLogger.log(Level.FINE, "The DMSO correction from Cullen et al.(1976) : " + temperatureCorrection);
		
		return super.correctMeltingResult(environment, parameter);
	}

}
