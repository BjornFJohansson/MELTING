package melting.wobble;

import java.util.logging.Level;

import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public abstract class InosineNNMethod extends PartialCalcul{
	
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
			 
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics modifiedValue; 
		for (int i = pos1; i <= pos2 - 1; i++){
			modifiedValue = this.collector.getModifiedvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i));
			
			OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequenceNNPair(i) + "/" + sequences.getComplementaryNNPair(i) + " : enthalpy = " + modifiedValue.getEnthalpy() + "  entropy = " + modifiedValue.getEntropy());

			enthalpy += modifiedValue.getEnthalpy();
			entropy += modifiedValue.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		for (int i = pos1; i <= pos2 - 1; i++){
	
			if (collector.getModifiedvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)) == null) {
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic values for " + sequences.getSequenceNNPair(i) + "/" + sequences.getComplementaryNNPair(i) + " are missing. Check the inosine file.");

				return true;
			}
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}

	protected int[] correctPositions(int pos1, int pos2, int duplexLength){
		if (pos1 > 0){
			pos1 --;
		}
		if (pos2 < duplexLength - 1){
			pos2 ++;
		}
		int [] positions = {pos1, pos2};
		return positions;
	}
}
