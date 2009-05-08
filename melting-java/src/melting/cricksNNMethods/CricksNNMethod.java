package melting.cricksNNMethods;


import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public abstract class CricksNNMethod extends PartialCalcul{
	
	public CricksNNMethod(String fileName){
		loadData(fileName, this.collector);
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		 
		for (int i = pos1; i <= pos2 - 1; i++){
			enthalpy += this.collector.getNNvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)).getEnthalpy();
			entropy += this.collector.getNNvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)).getEntropy();
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
			enthalpy += initiation.getEnthalpy();
			entropy += initiation.getEntropy();
		}
		
		if (environment.getOptions().containsKey(OptionManagement.selfComplementarity)){
			enthalpy += this.collector.getSymetry().getEnthalpy();
			entropy += this.collector.getSymetry().getEntropy();
		}
		
		environment.setResult(enthalpy, entropy);
		
		return environment.getResult();
	}
	
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1, int pos2){
		
		for (int i = pos1; i < pos2; i++){
			
			if (collector.getNNvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)) == null) {
				return true;
			}
		}
		return false;
	}
}
