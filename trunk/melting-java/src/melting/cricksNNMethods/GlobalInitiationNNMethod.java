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
			
			OptionManagement.meltingLogger.log(Level.FINE, "The initiation if there is at least one GC base pair : enthalpy = " + initiationOneGC.getEnthalpy() + "  entropy = " + initiationOneGC.getEntropy());

			enthalpy += initiationOneGC.getEnthalpy();
			entropy += initiationOneGC.getEntropy();
		}
		
		else {
			Thermodynamics initiationAllAT = this.collector.getInitiation("all_AT_pair");
			
			OptionManagement.meltingLogger.log(Level.FINE, "The initiation if there is only AT base pairs : enthalpy = " + initiationAllAT.getEnthalpy() + "  entropy = " + initiationAllAT.getEntropy());
			enthalpy += initiationAllAT.getEnthalpy();
			entropy += initiationAllAT.getEntropy();
		}
		
		if (environment.isSelfComplementarity()){
			Thermodynamics symmetry = this.collector.getsymmetry();
			
			OptionManagement.meltingLogger.log(Level.FINE, "self complementarity : enthalpy = " + symmetry.getEnthalpy() + "  entropy = " + symmetry.getEntropy());
			enthalpy += symmetry.getEnthalpy();
			entropy += symmetry.getEntropy();
		}
		
		environment.setResult(enthalpy, entropy);
		
		return environment.getResult();
	}

}
