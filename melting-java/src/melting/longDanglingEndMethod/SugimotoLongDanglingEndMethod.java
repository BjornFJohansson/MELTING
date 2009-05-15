package melting.longDanglingEndMethod;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

public abstract class SugimotoLongDanglingEndMethod extends PartialCalcul {

	/*REF: Sugimoto et al. (2002). J. Am. Chem. Soc. 124: 10367-10372 */

	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		double enthalpy = result.getEnthalpy() + this.collector.getDanglingValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEnthalpy();
		double entropy = result.getEntropy() + this.collector.getDanglingValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEntropy();
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.isSelfComplementarity() == false){
			isApplicable = false;
			System.out.println("WARNING : the thermodynamic parameters for long dangling end of Sugimoto et al." +
					"(2002) is only established for self-complementary sequences.");
		}
		
		for (int i = 1; i < environment.getSequences().getDuplexLength(); i++){
			if (environment.getSequences().isBasePairEqualsTo('A', '-', i) == false){
				isApplicable = false;
				System.out.println("WARNING : the thermodynamic parameters for long dangling end of Sugimoto et al." +
						"(2002) is only established for sequences with poly-A dangling ends.");
				break;
			}
		}

		return isApplicable;
	}
	
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		if (this.collector.getDanglingValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)) == null){
			return true;			
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}

}
