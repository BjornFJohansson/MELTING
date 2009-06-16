package melting.cricksNNMethods;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public abstract class CricksNNMethod extends PartialCalcul{
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics NNValue;
		for (int i = pos1; i <= pos2 - 1; i++){
			NNValue = this.collector.getNNvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i));
			OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequenceNNPair(i) + "/" + sequences.getComplementaryNNPair(i) + " : enthalpy = " + NNValue.getEnthalpy() + "  entropy = " + NNValue.getEntropy());
			
			enthalpy += NNValue.getEnthalpy();
			entropy += NNValue.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
				
		return result;
	}
	
	public ThermoResult calculateInitiationHybridation(Environment environment){
		double enthalpy = 0;
		double entropy = 0;

		Thermodynamics initiation = this.collector.getInitiation();

		
		if (initiation != null) {
			OptionManagement.meltingLogger.log(Level.FINE, "\n Initiation : enthalpy = " + initiation.getEnthalpy() + "  entropy = " + initiation.getEntropy());
			
			enthalpy += initiation.getEnthalpy();
			entropy += initiation.getEntropy();
		}
		
		if (environment.getOptions().containsKey(OptionManagement.selfComplementarity)){
			Thermodynamics symmetry = this.collector.getsymmetry();
			
			OptionManagement.meltingLogger.log(Level.FINE, "Self complementarity : enthalpy = " + symmetry.getEnthalpy() + "  entropy = " + symmetry.getEntropy());
			
			enthalpy += symmetry.getEnthalpy();
			entropy += symmetry.getEntropy();
		}
		
		environment.setResult(enthalpy, entropy);
		
		return environment.getResult();
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1, int pos2){

		for (int i = pos1; i <= pos2 - 1; i++){

			if (collector.getNNvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)) == null) {
				return true;
			}
		}
		return false;
	}
}
