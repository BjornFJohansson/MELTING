package melting.secondDanglingEndMethod;

import java.util.logging.Level;

import melting.Helper;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Serra06DoubleDanglingEnd extends SecondDanglingEndMethod {

	/*REF: Martin J Serra et al. (2006). Nucleic Acids research 34: 3338-3344*/ 
	
	public static String defaultFileName = "Serra2006doublede.xml";
	
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
		
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		OptionManagement.meltingLogger.log(Level.INFO, "The thermodynamic parameters for double dangling end are from Serra et al. (2006) :");
		
		result = calculateThermodynamicsWithoutSecondDanglingEnd(newSequences, 0, newSequences.getDuplexLength() - 1, result);
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		String sequence = NucleotidSequences.convertToPyr_Pur(sequences.getSequenceContainig("-", pos1, pos2));
		String complementary = NucleotidSequences.convertToPyr_Pur(sequences.getComplementaryTo(sequence, pos1, pos2));
		
		Thermodynamics doubleDanglingValue;
		if (Helper.isPyrimidine(complementary.charAt(1)) || (Helper.isPyrimidine(complementary.charAt(1)) == false && Helper.isPyrimidine(complementary.charAt(0)))){
			doubleDanglingValue = this.collector.getDanglingValue(sequence.substring(0, 2),complementary);
		}
		else {
			doubleDanglingValue = this.collector.getDanglingValue(sequence,complementary);
			}
		OptionManagement.meltingLogger.log(Level.INFO, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + ": incremented enthalpy = " + doubleDanglingValue.getEnthalpy() + "  incremented entropy = " + doubleDanglingValue.getEntropy());
		
		enthalpy += doubleDanglingValue.getEnthalpy();
		entropy += doubleDanglingValue.getEntropy();
		
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
