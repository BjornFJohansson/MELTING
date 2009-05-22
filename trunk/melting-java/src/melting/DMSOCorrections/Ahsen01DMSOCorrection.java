package melting.DMSOCorrections;

import melting.Environment;
import melting.ThermoResult;
import melting.CorrectionMethods.DMSOCorrections;

public class Ahsen01DMSOCorrection extends DMSOCorrections {

	private static double parameter = 0.75;
	
	/* Nicolas Von Ahsen, Carl T Wittwer and Ekkehard Schutz, "Oligonucleotide
	 * melting temperatures under PCR conditions : deoxynucleotide Triphosphate
	 * and Dimethyl sulfoxide concentrations with comparison to alternative empirical 
	 * formulas", 2001, Clinical Chemistry, 47, 1956-1961.
	 * */
	
	public ThermoResult correctMeltingResult(Environment environment) {
		return super.correctMeltingResult(environment, parameter);
	}

}
