package melting.singleBulge;

import java.util.logging.Level;

import Sequences.NucleotidSequences;

import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public abstract class GlobalSingleBulgeLoopMethod extends PartialCalcul{
	
	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		Thermodynamics singleBulge = this.collector.getSingleBulgeLoopvalue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		double enthalpy = result.getEnthalpy() + singleBulge.getEnthalpy();
		double entropy = result.getEntropy() + singleBulge.getEntropy();
		
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " : enthalpy = " + singleBulge.getEnthalpy() + "  entropy = " + singleBulge.getEntropy());
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);

		return result;
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		if (this.collector.getSingleBulgeLoopvalue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)) == null){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " are missing. Check the single bulge loop parameters.");

			return true;
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
