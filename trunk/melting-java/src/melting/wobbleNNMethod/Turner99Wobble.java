	
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
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength(), sequences);
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequence = sequences.getEquivalentSequences("rna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for GU base pairs is from Turner et al. (1999) : ");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics mismatchValue;
		String closing = "not_G/C";
		
		if (newSequence.getDuplexLength() - 1 == 4 && newSequence.getSequence(pos1,pos2).equals("GGUC") && newSequence.getComplementary(pos1,pos2).equals("CUGG")){
			closing = "G/C";
			mismatchValue = this.collector.getMismatchValue(newSequence.getSequence(pos1, pos2), newSequence.getComplementary(pos1, pos2), closing);
			OptionManagement.meltingLogger.log(Level.FINE, newSequence.getSequence(pos1, pos2) + "/" + newSequence.getComplementary(pos1, pos2) + " : enthalpy = " + mismatchValue.getEnthalpy() + "  entropy = " + mismatchValue.getEntropy());
			
			enthalpy += mismatchValue.getEnthalpy();
			entropy += mismatchValue.getEntropy(); 
		}
		else{
			for (int i = pos1; i < pos2; i++){
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
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength(), sequences);
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("rna");
		
		String closing = "not_G/C";
		
		if (pos2-pos1 == 4 && newSequences.getSequence(pos1, pos2).equals("GGUC") && newSequences.getComplementary(pos1, pos2).equals("CUGG")){
			closing = "G/C";
			if (this.collector.getMismatchValue(newSequences.getSequence(pos1, pos2), newSequences.getComplementary(pos1, pos2), closing) == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameter for " + newSequences.getSequence(pos1, pos2) + "/" + newSequences.getComplementary(pos1, pos2) + " is missing. Check the parameters for wobble base pairs.");
				return true;
			}
			return super.isMissingParameters(newSequences, pos1, pos2);
		}
		for (int i = pos1; i < pos2; i++){
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
		return super.isMissingParameters(newSequences, pos1, pos2);
	}
	
	private int[] correctPositions(int pos1, int pos2, int duplexLength, NucleotidSequences sequences){

		if (pos1 > 0){
			if (sequences.getDuplex().get(pos1 - 1).isComplementaryBasePair()){
				pos1 --;
			}
		}
		if (pos2 < duplexLength - 1){
			if (sequences.getDuplex().get(pos2 + 1).isComplementaryBasePair()){
				pos2 ++;
			}
		}
		int [] positions = {pos1, pos2};
		return positions;
	}

}
