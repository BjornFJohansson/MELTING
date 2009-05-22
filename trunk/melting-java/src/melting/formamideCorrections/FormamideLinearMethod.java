package melting.formamideCorrections;

import melting.Environment;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CorrectionMethod;

public class FormamideLinearMethod implements CorrectionMethod{

	/*McConaughy, B.L., Laird, C.D. and McCarthy, B.I., 1969, Biochemistry
	 * 8, 3289-3295.
	 * 
	 * Record, M.T., Jr, 1967, Biopolymers, 5, 975-992.
	 * 
	 * Casey J., and Davidson N., 1977, Nucleic acids research, 4, 1539-1532.
	 * 
	 * Hutton Jr, 1977, Nucleic acids research, 4, 3537-3555.
	 * */
	
	public ThermoResult correctMeltingResult(Environment environment) {
		double Tm = environment.getResult().getTm() - 0.65 * environment.getFormamide();
		environment.setResult(Tm);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("WARNING : the implemented formamide correction methods are established for DNA duplexes.");
			return false;
		}
		return true;
		
	}

}
