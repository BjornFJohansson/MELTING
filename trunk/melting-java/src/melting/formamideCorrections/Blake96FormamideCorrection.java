package melting.formamideCorrections;

import melting.Environment;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CorrectionMethod;

public class Blake96FormamideCorrection implements CorrectionMethod{

	public ThermoResult correctMeltingResult(Environment environment) {
		double Fgc = environment.getSequences().calculatePercentGC() / 100;
		double Tm = environment.getResult().getTm() + (0.453 * Fgc - 2.88) * environment.getFormamide();
		
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
