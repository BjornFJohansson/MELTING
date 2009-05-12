package melting.secondDanglingEndMethod;

import melting.Helper;
import melting.NucleotidSequences;
import melting.ThermoResult;

public class Serra06DoubleDanglingEnd extends SecondDanglingEndMethod {

	/*REF: Martin J Serra et al. (2006). Nucleic Acids research 34: 3338-3344*/ 
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		result = super.calculateThermodynamicsWithoutSecondDanglingEnd(sequences, pos1, pos2, result);
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		String sequence = NucleotidSequences.convertToPyr_Pur(sequences.getSequenceContainig("-", pos1, pos2));
		String complementary = NucleotidSequences.convertToPyr_Pur(sequences.getComplementaryTo(sequence, pos1, pos2));
		
		if (Helper.isPyrimidine(complementary.charAt(1)) || (Helper.isPyrimidine(complementary.charAt(1)) == false && Helper.isPyrimidine(complementary.charAt(0)))){
			enthalpy += this.collector.getDanglingValue(sequence.substring(0, 2),complementary).getEnthalpy();
			entropy += this.collector.getDanglingValue(sequence.substring(0, 2),complementary).getEntropy();
		}
		else {
			enthalpy += this.collector.getDanglingValue(sequence,complementary).getEnthalpy();
			entropy += this.collector.getDanglingValue(sequence,complementary).getEntropy();
			}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {

		String sequence = NucleotidSequences.convertToPyr_Pur(sequences.getSequenceContainig("-", pos1, pos2));
		String complementary = NucleotidSequences.convertToPyr_Pur(sequences.getComplementaryTo(sequence, pos1, pos2));
		
		if (Helper.isPyrimidine(complementary.charAt(1)) || (Helper.isPyrimidine(complementary.charAt(1)) == false && Helper.isPyrimidine(complementary.charAt(0)))){
			if (this.collector.getDanglingValue(sequence.substring(0, 2),complementary) == null){
			return true;			
			}
		}
		else {
			if (this.collector.getDanglingValue(sequence,complementary) == null){
				return true;			
				}
			}
		return super.isMissingParameters(sequences, pos1, pos2);
	}

}
