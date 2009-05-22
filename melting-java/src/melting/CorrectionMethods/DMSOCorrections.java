package melting.CorrectionMethods;

import melting.Environment;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CorrectionMethod;

public abstract class DMSOCorrections implements CorrectionMethod{
	
	public ThermoResult correctMeltingResult(Environment environment, double parameter) {
		double Tm = environment.getResult().getTm() - parameter * environment.getDMSO();
		
		environment.setResult(Tm);
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("WARNING : The current DMSO corrections are established for DNA duplexes.");
			return false;
		}
		return false;
	}

}
