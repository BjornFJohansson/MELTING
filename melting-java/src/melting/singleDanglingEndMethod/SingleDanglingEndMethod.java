package melting.singleDanglingEndMethod;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

public abstract class SingleDanglingEndMethod extends PartialCalcul {
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		double enthalpy = result.getEnthalpy() + this.collector.getDanglingValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEnthalpy();
		double entropy = result.getEnthalpy() + this.collector.getDanglingValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEntropy();		
					
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (pos1 != 0 && pos2 != environment.getSequences().getDuplexLength() - 1){
			isApplicable = false;
			System.out.println("ERROR : It is possible to use Thermodynamic parameters only for dangling" +
					"ends");
		}
		return isApplicable;
	}
	
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1, int pos2){
		if (this.collector.getDanglingValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)) == null){
			return true;
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}
}
