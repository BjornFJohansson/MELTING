
/* Cullen Br, Bick Md, "Thermal denaturation of DNA from bromodeoxyuridine substitued cells."
	 * Nucleic acids research, 1976, 3, 49-62.
	 * */

package melting.DMSOCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.CorrectionMethods.DMSOCorrections;
import melting.configuration.OptionManagement;

public class Cullen76DMSOCorrection extends DMSOCorrections {

	private static double parameter = 0.5;
	
	private static String temperatureCorrection = "Tm (x % DMSO) = Tm(0 % DMSO) - 0.5 * x % DMSO";
	
	public ThermoResult correctMeltingResult(Environment environment) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n The DMSO correction from Cullen et al.(1976) : ");
		OptionManagement.meltingLogger.log(Level.FINE,temperatureCorrection);
		
		return super.correctMeltingResult(environment, parameter);
	}

}
