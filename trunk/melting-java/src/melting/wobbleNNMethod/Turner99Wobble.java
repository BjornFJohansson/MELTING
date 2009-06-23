	
/*REF: Douglas M Turner et al (1999). J.Mol.Biol.  288: 911_940 */

package melting.wobbleNNMethod;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Turner99Wobble extends PartialCalcul{
		
	public static String defaultFileName = "Turner1999wobble.xml";
	
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
		NucleotidSequences newSequence = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for GU base pairs is from Turner et al. (1999) : ");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics mismatchValue;
		String closing = "not_G/C";
		
		if (newSequence.getDuplexLength() - 1 == 4 && newSequence.getSequence().equals("GGUC") && newSequence.getComplementary().equals("CUGG")){
			closing = "G/C";
			mismatchValue = this.collector.getMismatchValue(newSequence.getSequence(), newSequence.getComplementary(), closing);
			OptionManagement.meltingLogger.log(Level.FINE, newSequence.getSequence() + "/" + newSequence.getComplementary() + " : enthalpy = " + mismatchValue.getEnthalpy() + "  entropy = " + mismatchValue.getEntropy());
			
			enthalpy += mismatchValue.getEnthalpy();
			entropy += mismatchValue.getEntropy(); 
		}
		else{
			for (int i = 0; i < newSequence.getDuplexLength() - 1; i++){
				if (newSequence.getSequenceNNPair(i).equals("GU") && newSequence.getComplementaryNNPair(i).equals("UG")){
					mismatchValue = this.collector.getMismatchValue(newSequence.getSequenceNNPair(i), newSequence.getComplementaryNNPair(i), closing);
				}
				else {
					mismatchValue = this.collector.getMismatchValue(newSequence.getSequenceNNPair(i), newSequence.getComplementaryNNPair(i));
				}
				OptionManagement.meltingLogger.log(Level.FINE, newSequence.getSequenceNNPair(i) + "/" + newSequence.getComplementaryNNPair(i) + " : enthalpy = " + mismatchValue.getEnthalpy() + "  entropy = " + mismatchValue.getEntropy());
				
				enthalpy += mismatchValue.getEnthalpy();
				entropy += mismatchValue.getEntropy(); 
			}
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "the model of " +
					"Turner (1999) is only established " +
					"for RNA sequences.");
		}
		return super.isApplicable(environment, pos1, pos2);
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));
		String closing = "not_G/C";
		
		if (newSequences.getDuplexLength() - 1 == 4 && newSequences.getSequence().equals("GGUC") && newSequences.getComplementary().equals("CUGG")){
			closing = "G/C";
			if (this.collector.getMismatchValue(newSequences.getSequence(), newSequences.getComplementary(), closing) == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameter for " + newSequences.getSequence() + "/" + newSequences.getComplementary() + " is missing. Check the parameters for wobble base pairs.");
				return true;
			}
			return super.isMissingParameters(newSequences, 0, newSequences.getDuplexLength() - 1);
		}
		for (int i = 0; i < newSequences.getDuplexLength() - 1; i++){
			if (newSequences.getSequenceNNPair(i).equals("GU") && newSequences.getComplementaryNNPair(i).equals("UG")){
				if (this.collector.getMismatchValue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i), closing) == null){
					OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameter for " + newSequences.getSequenceNNPair(i) + "/" + newSequences.getComplementaryNNPair(i) + " is missing. Check the parameters for wobble base pairs.");
					return true;
				}
			}
			else{
				if (this.collector.getMismatchValue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i)) == null){
					OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameter for " + newSequences.getSequenceNNPair(i) + "/" + newSequences.getComplementaryNNPair(i) + " is missing. Check the parameters for wobble base pairs.");
					return true;
				}
			}
		}
		return super.isMissingParameters(newSequences, 0, newSequences.getDuplexLength() - 1);
	}

}
