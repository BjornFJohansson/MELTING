package melting.secondDanglingEndMethod;

import java.util.logging.Level;

import melting.Helper;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

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
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		OptionManagement.meltingLogger.log(Level.FINE, "The thermodynamic parameters for double dangling end are from Serra et al. (2005) :");
		
		result = calculateThermodynamicsWithoutSecondDanglingEnd(newSequences, 0, newSequences.getDuplexLength() - 1, result);
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		String sequence = NucleotidSequences.convertToPyr_Pur(sequences.getSequenceContainig("-", pos1, pos2));
		String complementary = NucleotidSequences.convertToPyr_Pur(sequences.getComplementaryTo(sequence, pos1, pos2));
		
		Thermodynamics doubleDanglingValue;
		if (Helper.isPyrimidine(complementary.charAt(1))){
			doubleDanglingValue = this.collector.getDanglingValue("Y","");			
		}
		else {
			doubleDanglingValue = this.collector.getDanglingValue(sequence.substring(1),"");
			enthalpy += doubleDanglingValue.getEnthalpy();
			entropy += doubleDanglingValue.getEntropy();
			}
		
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + ": incremented enthalpy = " + doubleDanglingValue.getEnthalpy() + "  incremented entropy = " + doubleDanglingValue.getEntropy());

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
