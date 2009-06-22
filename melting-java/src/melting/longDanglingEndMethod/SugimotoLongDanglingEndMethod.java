/*REF: Sugimoto et al. (2002). J. Am. Chem. Soc. 124: 10367-10372 */

package melting.longDanglingEndMethod;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public abstract class SugimotoLongDanglingEndMethod extends PartialCalcul {

	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The long dangling end parameters are from Sugimoto et al. (2002). (delta delta H and delta delta S)");
		Thermodynamics longDangling = this.collector.getDanglingValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		double enthalpy = result.getEnthalpy() + longDangling.getEnthalpy();
		double entropy = result.getEntropy() + longDangling.getEntropy();
		
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " : enthalpy = " + longDangling.getEnthalpy() + "  entropy = " + longDangling.getEntropy());

		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.isSelfComplementarity() == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The Thermodynamic parameters for long dangling end of Sugimoto et al." +
					"(2002) is only established for self-complementary sequences.");
		}
		int numberOtherADangling = 0;
		for (int i = pos1; i <= pos2; i++){
			if (environment.getSequences().isBasePairEqualsTo('A', '-', i) == false){
				numberOtherADangling ++;
			}
		}
		if (numberOtherADangling > 1){
			isApplicable = false;
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for long dangling end of Sugimoto et al." +
			"(2002) is only established for sequences with poly-A dangling ends.");
		}
		return isApplicable;
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {

		if (this.collector.getDanglingValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)) == null){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " are missing. Check the long dangling ends parameters.");
			return true;			
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}
}
