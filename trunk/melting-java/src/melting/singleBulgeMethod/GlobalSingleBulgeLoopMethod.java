package melting.singleBulgeMethod;

import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;

public abstract class GlobalSingleBulgeLoopMethod extends PartialCalcul{
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		double enthalpy = result.getEnthalpy() + this.collector.getSingleBulgeLoopvalue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEnthalpy();
		double entropy = result.getEntropy() + this.collector.getSingleBulgeLoopvalue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEntropy();
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);

		return result;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		if (this.collector.getSingleBulgeLoopvalue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)) == null){
			return true;
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}

}
