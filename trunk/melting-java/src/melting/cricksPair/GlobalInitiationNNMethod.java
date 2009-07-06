package melting.cricksPair;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public abstract class GlobalInitiationNNMethod extends CricksNNMethod {
	
	@Override
	public ThermoResult calculateInitiationHybridation(Environment environment){
		
		super.calculateInitiationHybridation(environment);

		double enthalpy = 0.0;
		double entropy = 0.0;

		if (environment.getSequences().isOneGCBasePair()){
			Thermodynamics initiationOneGC = this.collector.getInitiation("one_GC_Pair");

			OptionManagement.meltingLogger.log(Level.FINE, "\n The initiation if there is at least one GC base pair : enthalpy = " + initiationOneGC.getEnthalpy() + "  entropy = " + initiationOneGC.getEntropy());

			enthalpy += initiationOneGC.getEnthalpy();
			entropy += initiationOneGC.getEntropy();
		}
		
		else {
			Thermodynamics initiationAllAT = this.collector.getInitiation("all_AT_pairs");
			
			OptionManagement.meltingLogger.log(Level.FINE, "\n The initiation if there is only AT base pairs : enthalpy = " + initiationAllAT.getEnthalpy() + "  entropy = " + initiationAllAT.getEntropy());
			enthalpy += initiationAllAT.getEnthalpy();
			entropy += initiationAllAT.getEntropy();
		}
		
		environment.addResult(enthalpy, entropy);
		
		return environment.getResult();
	}

}
