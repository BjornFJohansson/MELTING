package melting.ionCorrections;

import melting.Environment;
import melting.ThermoResult;
import melting.calculMethodInterfaces.IonCorrectionMethod;
import melting.nearestNeighborModel.NearestNeighborMode;

public abstract class EntropyCorrection implements IonCorrectionMethod{

	public ThermoResult correctMeltingResult(Environment environment) {
		double entropy = correctEntropy(environment);
		
		environment.setResult(0, entropy);
		
		double Tm = NearestNeighborMode.calculateMeltingTemperature(environment);
		environment.setResult(Tm);
		
		return environment.getResult();
	}
	
	public boolean isApplicable(Environment environment){
		return true;
	}
	
	protected double correctEntropy(Environment environment){
		return 0;
	}

}
