package melting.CorrectionMethods;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CorrectionMethod;
import melting.configuration.OptionManagement;


public abstract class DMSOCorrections implements CorrectionMethod{
	
	public ThermoResult correctMeltingResult(Environment environment, double parameter) {
		double Tm = environment.getResult().getTm() - parameter * environment.getDMSO();
		
		environment.setResult(Tm);
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The current DMSO corrections are established for DNA duplexes.");
		}
		return true;
	}

}
