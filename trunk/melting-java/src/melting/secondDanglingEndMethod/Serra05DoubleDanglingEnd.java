package melting.secondDanglingEndMethod;

import melting.Helper;
import melting.NucleotidSequences;
import melting.ThermoResult;

public class Serra05DoubleDanglingEnd extends SecondDanglingEndMethod {

	/*REF: Martin J Serra et al. (2005). RNA 11: 512-516*/ 
	
	public static String defaultFileName = "Serra2005doublede.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		result = calculateThermodynamicsWithoutSecondDanglingEnd(sequences, pos1, pos2, result);
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		String sequence = NucleotidSequences.convertToPyr_Pur(sequences.getSequenceContainig("-", pos1, pos2));
		String complementary = NucleotidSequences.convertToPyr_Pur(sequences.getComplementaryTo(sequence, pos1, pos2));
		
		if (Helper.isPyrimidine(complementary.charAt(1))){
			enthalpy += this.collector.getDanglingValue("Y","").getEnthalpy();
			entropy += this.collector.getDanglingValue("Y","").getEntropy();
		}
		else {
			enthalpy += this.collector.getDanglingValue(sequence.substring(1),"").getEnthalpy();
			entropy += this.collector.getDanglingValue(sequence.substring(1),"").getEntropy();
			}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {

		String sequence = NucleotidSequences.convertToPyr_Pur(sequences.getSequenceContainig("-", pos1, pos2));
		String complementary = NucleotidSequences.convertToPyr_Pur(sequences.getComplementaryTo(sequence, pos1, pos2));
		
		if (Helper.isPyrimidine(complementary.charAt(1))){
			if (this.collector.getDanglingValue("Y","") == null){
			return true;			
			}
		}
		else {
			if (this.collector.getDanglingValue(sequence.substring(1),"") == null){
				return true;			
				}
			}
		return super.isMissingParameters(sequences, pos1, pos2);
	}
}
