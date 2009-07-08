/* Nicolas Von Ahsen, Carl T Wittwer and Ekkehard Schutz, "Oligonucleotide
	 * melting temperatures under PCR conditions : deoxynucleotide Triphosphate
	 * and Dimethyl sulfoxide concentrations with comparison to alternative empirical 
	 * formulas", 2001, Clinical Chemistry, 47, 1956-1961.
	 * */
package melting.otherCorrections.dmsoCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.correctionMethods.DMSOCorrections;

public class Ahsen01DMSOCorrection extends DMSOCorrections {

	private static double parameter = 0.75;
	
	private static String temperatureCorrection = "Tm (x % DMSO) = Tm(0 % DMSO) - 0.75 * x % DMSO";
	
	public ThermoResult correctMeltingResults(Environment environment) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n The DMSO correction from Ahsen et al. (2001) : ");
		OptionManagement.meltingLogger.log(Level.FINE,temperatureCorrection);
		
		return super.correctMeltingResult(environment, parameter);
	}

}
