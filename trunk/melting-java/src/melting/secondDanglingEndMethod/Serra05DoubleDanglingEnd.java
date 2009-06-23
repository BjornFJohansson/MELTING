
/*REF: Martin J Serra et al. (2005). RNA 11: 512-516*/ 

package melting.secondDanglingEndMethod;

import java.util.logging.Level;

import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Serra05DoubleDanglingEnd extends SecondDanglingEndMethod {
	
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

		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for double dangling end is from Serra et al. (2005) :");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		result = calculateThermodynamicsWithoutSecondDanglingEnd(newSequences, 0, newSequences.getDuplexLength() - 1, result);
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		String sequence = sequences.getSequenceContainig("-", pos1, pos2);
		String complementary = NucleotidSequences.convertToPyr_Pur(sequences.getComplementaryTo(sequence, pos1, pos2));
		Thermodynamics doubleDanglingValue;
		String sens;
		
		if (complementary.charAt(1) == 'Y'){
			if (sequences.getSequenceSens(sequence, pos1, pos2).equals("5'3'")){
				sens = NucleotidSequences.getSens(sequence, complementary);

				doubleDanglingValue = this.collector.getSecondDanglingValue("","Y", sens);

			}
			else{
				sens = NucleotidSequences.getSens(complementary, sequence);
				doubleDanglingValue = this.collector.getSecondDanglingValue("Y","", sens);

			}
		}
		else {
			if (sequence.charAt(0) == '-'){
				complementary = complementary.substring(0, 2);
			}
			else{
				complementary = complementary.substring(1);
			}

			if (sequences.getSequenceSens(sequence, pos1, pos2).equals("5'3'")){
				sens = NucleotidSequences.getSens(sequence, complementary);

				doubleDanglingValue = this.collector.getSecondDanglingValue("",complementary, sens);

			}
			else{
				sens = NucleotidSequences.getSens(complementary, sequence);

				doubleDanglingValue = this.collector.getSecondDanglingValue(complementary,"", sens); 
			}
		}
		enthalpy += doubleDanglingValue.getEnthalpy();
		entropy += doubleDanglingValue.getEntropy();
		
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + ": incremented enthalpy = " + doubleDanglingValue.getEnthalpy() + "  incremented entropy = " + doubleDanglingValue.getEntropy());

		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		String sequence = sequences.getSequenceContainig("-", pos1, pos2);
		String complementary = NucleotidSequences.convertToPyr_Pur(sequences.getComplementaryTo(sequence, pos1, pos2));
		String sens;
		if (complementary.charAt(1) == 'Y'){
			if (sequences.getSequenceSens(sequence, pos1, pos2).equals("5'3'")){
				sens = NucleotidSequences.getSens(sequence, complementary);
				if (this.collector.getSecondDanglingValue("","Y", sens) == null){
					OptionManagement.meltingLogger.log(Level.WARNING, "The thermodymamic parameters for x-Y-x/x are missing. Check the second dangling ends parameters.");
					return true;			
				}
			}
			else{
				sens = NucleotidSequences.getSens(complementary, sequence);

				if (this.collector.getSecondDanglingValue("Y","", sens) == null){
					OptionManagement.meltingLogger.log(Level.WARNING, "The thermodymamic parameters for x-Y-x/x are missing. Check the second dangling ends parameters.");
					return true;			
				}
			}
		}
		else {
			if (sequence.charAt(0) == '-'){
				complementary = complementary.substring(0, 2);
			}
			else{
				
				complementary = complementary.substring(1);
			}
			if (sequences.getSequenceSens(sequence, pos1, pos2).equals("5'3'")){
				sens = NucleotidSequences.getSens(sequence, complementary);

				if (this.collector.getSecondDanglingValue("",complementary, sens) == null){
					OptionManagement.meltingLogger.log(Level.WARNING, "The thermodymamic parameters for x/" + complementary + " are missing. Check the second dangling ends parameters.");

					return true;			
					}
			}
			else{
				sens = NucleotidSequences.getSens(complementary, sequence);

				if (this.collector.getSecondDanglingValue(complementary,"", sens) == null){
					OptionManagement.meltingLogger.log(Level.WARNING, "The thermodymamic parameters for " + complementary.substring(1) + "/x are missing. Check the second dangling ends parameters.");

					return true;			
					}
			}
			
			}
		return super.isMissingParameters(newSequences, 0, newSequences.getDuplexLength() - 1);
	}
}
