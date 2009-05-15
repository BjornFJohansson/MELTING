package melting.woddleNNMethod;

import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

public abstract class InosineNNMethod extends PartialCalcul{
	
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
			 
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		for (int i = pos1; i <= pos2; i++){
			enthalpy += collector.getModifiedvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)).getEnthalpy();
			entropy += collector.getModifiedvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)).getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		for (int i = pos1; i <= pos2; i++){
	
			if (collector.getModifiedvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)) == null) {
				return true;
			}
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}

}
