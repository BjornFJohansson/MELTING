package melting.singleDanglingEndMethod;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public abstract class SingleDanglingEndMethod extends PartialCalcul {
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		Thermodynamics danglingValue = this.collector.getDanglingValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		double enthalpy = result.getEnthalpy() + danglingValue.getEnthalpy();
		double entropy = result.getEntropy() + danglingValue.getEntropy();		
			
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " : enthalpy = " + danglingValue.getEnthalpy() + "  entropy = " + danglingValue.getEntropy());
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (pos1 != 0 && pos2 != environment.getSequences().getDuplexLength() - 1){
			isApplicable = false;
			OptionManagement.meltingLogger.log(Level.WARNING, "It is possible to use Thermodynamic parameters only for dangling" +
					"ends.");
		}
		return isApplicable;
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1, int pos2){
		if (this.collector.getDanglingValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)) == null){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " are missing. Check the dangling ends prameters.");
			return true;
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}
}
