package melting.singleMismatchMethods;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

public class Sugimoto00mm extends PartialCalcul {

	/*Sugimoto Naoki, Mariko Nakano, Shu-ichi Nakano, Thermodynamics- structure relationship of single mismatches in RNA/DNA duplexes,
	 * Biochemistry 2000, 39: 11270-11281*/

	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {

		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		for (int i = pos1; i <= pos2; i++){
			enthalpy += collector.getMismatchvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)).getEnthalpy();
			entropy += collector.getMismatchvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)).getEnthalpy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("dnarna") == false && environment.getHybridization().equals("rnadna") == false){
			System.out.println("WARNING : the single mismatch approximation of " +
					"Sugimoto et al. are originally established " +
					"for RNA/DNA duplexes.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
			
		for (int i = pos1; i <= pos2; i++){
			if (this.collector.getMismatchvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)) == null){
				return true;
			}
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}
	

}
