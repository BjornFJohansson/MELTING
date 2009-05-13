package melting.cricksNNMethods;

import melting.Environment;
import melting.ThermoResult;

public abstract class GlobalInitiationNNMethod extends CricksNNMethod {
	
	public ThermoResult calculateInitiationHybridation(Environment environment){
		
		environment.setResult(super.calculateInitiationHybridation(environment));
		double enthalpy = environment.getResult().getEnthalpy();
		double entropy = environment.getResult().getEntropy();
		
		if (environment.getSequences().isOneGCBasePair()){
			enthalpy += this.collector.getInitiation("one_GC_pair").getEnthalpy();
			entropy += this.collector.getInitiation("one_GC_pair").getEntropy();
		}
		
		else {
			enthalpy += this.collector.getInitiation("all_AT_pair").getEnthalpy();
			entropy += this.collector.getInitiation("all_AT_pair").getEntropy();
		}
		
		if (environment.isSelfComplementarity()){
			enthalpy += this.collector.getSymetry().getEnthalpy();
			entropy += this.collector.getSymetry().getEntropy();
		}
		
		environment.setResult(enthalpy, entropy);
		
		return environment.getResult();
	}

}
