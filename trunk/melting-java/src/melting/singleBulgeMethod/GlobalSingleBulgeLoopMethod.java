package melting.singleBulgeMethod;

import java.util.logging.Level;

import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public abstract class GlobalSingleBulgeLoopMethod extends PartialCalcul{
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		Thermodynamics singleBulge = this.collector.getSingleBulgeLoopvalue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		double enthalpy = result.getEnthalpy() + singleBulge.getEnthalpy();
		double entropy = result.getEntropy() + singleBulge.getEntropy();
		
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " : enthalpy = " + singleBulge.getEnthalpy() + "  entropy = " + singleBulge.getEntropy());
		
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
