/*McConaughy, B.L., Laird, C.D. and McCarthy, B.I., 1969, Biochemistry
	 * 8, 3289-3295.
	 * 
	 * Record, M.T., Jr, 1967, Biopolymers, 5, 975-992.
	 * 
	 * Casey J., and Davidson N., 1977, Nucleic acids research, 4, 1539-1532.
	 * 
	 * Hutton Jr, 1977, Nucleic acids research, 4, 3537-3555.
	 * */

package melting.otherCorrections.formamideCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.methodInterfaces.CorrectionMethod;

public class FormamideLinearMethod implements CorrectionMethod{
	
	private static String temperatureCorrection = "Tm (x % formamide) = Tm(0 % formamide) - 0.65 * x % formamide";
	
	public ThermoResult correctMeltingResults(Environment environment) {
		double Tm = environment.getResult().getTm() - 0.65 * environment.getFormamide();
		environment.setResult(Tm);
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The linear formamide correction : ");
		OptionManagement.meltingLogger.log(Level.FINE,temperatureCorrection);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The implemented formamide correction methods are established for DNA duplexes..");

			return false;
		}
		return true;
		
	}

}
