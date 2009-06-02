package melting.cricksNNMethods;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public abstract class GlobalInitiationNNMethod extends CricksNNMethod {
	
	public ThermoResult calculateInitiationHybridation(Environment environment){
		
		environment.setResult(super.calculateInitiationHybridation(environment));
		double enthalpy = environment.getResult().getEnthalpy();
		double entropy = environment.getResult().getEntropy();
		
		if (environment.getSequences().isOneGCBasePair()){
			Thermodynamics initiationOneGC = this.collector.getInitiation("one_GC_pair");
			
			OptionManagement.meltingLogger.log(Level.INFO, "The initiation if there is at least one GC base pair : enthalpy = " + initiationOneGC.getEnthalpy() + "  entropy = " + initiationOneGC.getEntropy());

			enthalpy += initiationOneGC.getEnthalpy();
			entropy += initiationOneGC.getEntropy();
		}
		
		else {
			Thermodynamics initiationAllAT = this.collector.getInitiation("all_AT_pair");
			
			OptionManagement.meltingLogger.log(Level.INFO, "The initiation if there is only AT base pairs : enthalpy = " + initiationAllAT.getEnthalpy() + "  entropy = " + initiationAllAT.getEntropy());
			enthalpy += initiationAllAT.getEnthalpy();
			entropy += initiationAllAT.getEntropy();
		}
		
		if (environment.isSelfComplementarity()){
			Thermodynamics symetry = this.collector.getSymetry();
			
			OptionManagement.meltingLogger.log(Level.INFO, "self complementarity : enthalpy = " + symetry.getEnthalpy() + "  entropy = " + symetry.getEntropy());
			enthalpy += symetry.getEnthalpy();
			entropy += symetry.getEntropy();
		}
		
		environment.setResult(enthalpy, entropy);
		
		return environment.getResult();
	}

}
