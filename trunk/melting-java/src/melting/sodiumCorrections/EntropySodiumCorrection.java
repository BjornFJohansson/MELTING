package melting.sodiumCorrections;

import melting.Environment;
import melting.ThermoResult;
import melting.ionCorrections.SodiumCorrections;
import melting.nearestNeighborModel.NearestNeighborMode;

public abstract class EntropySodiumCorrection extends SodiumCorrections {

	@Override
	public ThermoResult correctMeltingResult(Environment environment) {
		double NaEq = calculateNaEqEquivalent(environment);
		double entropy = correctEntropy(NaEq, environment.getSequences().getDuplexLength());
		
		environment.setResult(0, entropy);
		
		double Tm = NearestNeighborMode.calculateMeltingTemperature(environment);
		environment.setResult(Tm);
		
		return environment.getResult();
	}
	
	protected double calculateNaEqEquivalent(Environment environment){
		double NaEq = 0;
		
		if (environment.getK() > 0 && environment.getNa() == 0 && environment.getMg() == 0 && environment.getTris() == 0){
			NaEq = environment.getK();
		}
		
		if (NaEq == 0){
			NaEq = super.calculateNaEqEquivalent(environment);
		}
		return NaEq;
	}
	
	protected double correctEntropy(double Na, int duplexLength){
		return 0;
	}

}
