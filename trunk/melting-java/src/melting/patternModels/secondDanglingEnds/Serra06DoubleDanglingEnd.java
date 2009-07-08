
/*REF: Martin J Serra et al. (2006). Nucleic Acids research 34: 3338-3344*/ 

package melting.patternModels.secondDanglingEnds;

import java.util.logging.Level;

import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.sequences.NucleotidSequences;

public class Serra06DoubleDanglingEnd extends SecondDanglingEndMethod {
	
	public static String defaultFileName = "Serra2006doublede.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		int [] positions = super.correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("rna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for double dangling end is from Serra et al. (2006) :");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		result = calculateThermodynamicsWithoutSecondDanglingEnd(newSequences, pos1, pos2, result);
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		String sequence = NucleotidSequences.convertToPyr_Pur(sequences.getSequenceContainig("-", pos1, pos2));
		String complementary = NucleotidSequences.convertToPyr_Pur(sequences.getComplementaryTo(sequences.getSequenceContainig("-", pos1, pos2), pos1, pos2));
		Thermodynamics doubleDanglingValue;

		if (sequence.charAt(0) == '-'){
			if (complementary.charAt(1) == 'Y' || (complementary.charAt(1) != 'Y' && complementary.charAt(2) == 'Y')){
				complementary = complementary.substring(1, 3);
			}
		}
		else {
			if (complementary.charAt(1) == 'Y' || (complementary.charAt(1) != 'Y' && complementary.charAt(0) == 'Y')){
				complementary = complementary.substring(0, 2);
			}
		} 
		
		if (sequences.getSequenceSens(sequences.getSequenceContainig("-", pos1, pos2), pos1, pos2).equals("5'3'")){
			doubleDanglingValue = this.collector.getDanglingValue(sequence,complementary);
		}
		else{
			doubleDanglingValue = this.collector.getDanglingValue(complementary,sequence);

		}
		
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + ": incremented enthalpy = " + doubleDanglingValue.getEnthalpy() + "  incremented entropy = " + doubleDanglingValue.getEntropy());
		
		enthalpy += doubleDanglingValue.getEnthalpy();
		entropy += doubleDanglingValue.getEntropy();
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("rna");
		
		String sequence = NucleotidSequences.convertToPyr_Pur(sequences.getSequenceContainig("-", pos1, pos2));
		String complementary = NucleotidSequences.convertToPyr_Pur(sequences.getComplementaryTo(sequences.getSequenceContainig("-", pos1, pos2), pos1, pos2));
		
		if (sequence.charAt(0) == '-'){

			if (complementary.charAt(1) == 'Y' || (complementary.charAt(1) != 'Y' && complementary.charAt(2) == 'Y')){
				complementary = complementary.substring(1, 3);
			}
		}
		else {
			if (complementary.charAt(1) == 'Y' || (complementary.charAt(1) != 'Y' && complementary.charAt(0) == 'Y')){
				complementary = complementary.substring(0, 2);
			}

		} 
		
		if (sequences.getSequenceSens(sequences.getSequenceContainig("-", pos1, pos2), pos1, pos2).equals("5'3'")){
			
			if (this.collector.getDanglingValue(sequence,complementary) == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodymamic parameters for " + sequence + "/" + complementary + " are missing. Check the second dangling ends parameters.");

				return true;			
			}
		}
		else{
			
			if (this.collector.getDanglingValue(complementary,sequence) == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodymamic parameters for " + complementary + "/" + sequence + " are missing. Check the second dangling ends parameters.");

				return true;			
			}
		}

		return super.isMissingParameters(newSequences, pos1, pos2);
	}

}
