
/*McTigue et al.(2004). Biochemistry 43 : 5388-5405 */

package melting.modifiedNucleicAcidMethod;

import java.util.HashMap;
import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;

public class McTigue04LockedAcid extends PartialCalcul{
		
	public static String defaultFileName = "McTigue2004lockedmn.xml";
	
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
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The locked acid nuceic model is from McTigue et al. (2004) (delta delta H and delta delta S): ");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		result = calculateThermodynamicsNoModifiedAcid(newSequences, pos1, pos2, result);
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics lockedAcidValue;
		
		for (int i = pos1; i < pos2; i++){
			lockedAcidValue = this.collector.getLockedAcidValue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i));

			OptionManagement.meltingLogger.log(Level.FINE, newSequences.getSequenceNNPair(i) + "/" + newSequences.getComplementaryNNPair(i) + " : incremented enthalpy = " + lockedAcidValue.getEnthalpy() + "  incremented entropy = " + lockedAcidValue.getEntropy());

			enthalpy += lockedAcidValue.getEnthalpy();
			entropy += lockedAcidValue.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);

		int [] positions = correctPositions(pos1, pos2, environment.getSequences().getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		if (environment.getHybridization().equals("dnadna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for locked acid nucleiques of" +
					"McTigue et al. (2004) are established for DNA sequences.");
		}
				
		if ((pos1 == 0 || pos2 == environment.getSequences().getDuplexLength() - 1) && environment.getSequences().calculateNumberOfTerminal("L", "-", pos1, pos2) > 0){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamics parameters for locked acid nucleiques of " +
					"McTigue (2004) are not established for terminal locked acid nucleiques.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		if (this.collector.getNNvalue(newSequences.getSequenceNNPairUnlocked(pos1), newSequences.getComplementaryNNPairUnlocked(pos1)) == null || this.collector.getNNvalue(newSequences.getSequenceNNPairUnlocked(pos1 + 1), newSequences.getComplementaryNNPairUnlocked(pos1 + 1)) == null){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + newSequences.getSequenceNNPairUnlocked(pos1) + "/" + newSequences.getComplementaryNNPairUnlocked(pos1) + " or " + newSequences.getSequenceNNPairUnlocked(pos1 + 1) + "/" + newSequences.getComplementaryNNPairUnlocked(pos1 + 1) +
			" are missing. Check the locked nucleic acid parameters.");
			return true;
		}
		
		for (int i = pos1; i < pos2; i++){
			if (this.collector.getLockedAcidValue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)) == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + sequences.getSequenceNNPair(i) + "/" + sequences.getComplementaryNNPair(i) +
				"are missing. Check the locked nucleic acid parameters.");
				return true;
			}
		}
		
		return super.isMissingParameters(newSequences, pos1, pos2);
	}

	private ThermoResult calculateThermodynamicsNoModifiedAcid(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result){

		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		Thermodynamics firstNNValue = collector.getNNvalue(newSequences.getSequenceNNPairUnlocked(pos1), newSequences.getComplementaryNNPairUnlocked(pos1));
		Thermodynamics secondNNValue = collector.getNNvalue(newSequences.getSequenceNNPairUnlocked(pos1 + 1), newSequences.getComplementaryNNPairUnlocked(pos1 + 1));
		double enthalpy = result.getEnthalpy() + firstNNValue.getEnthalpy() + secondNNValue.getEnthalpy();
		double entropy = result.getEntropy() + firstNNValue.getEntropy()  + secondNNValue.getEntropy();
		
		OptionManagement.meltingLogger.log(Level.FINE, newSequences.getSequenceNNPairUnlocked(pos1) + "/" + newSequences.getComplementaryNNPairUnlocked(pos1) + " : enthalpy = " + firstNNValue.getEnthalpy() + "  entropy = " + firstNNValue.getEntropy());
		OptionManagement.meltingLogger.log(Level.FINE, newSequences.getSequenceNNPairUnlocked(pos1+1) + "/" + newSequences.getComplementaryNNPairUnlocked(pos1+1) + " : enthalpy = " + secondNNValue.getEnthalpy() + "  entropy = " + secondNNValue.getEntropy());

		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);
		
		String crickName = options.get(OptionManagement.NNMethod);

		
		
	}
	
	private int[] correctPositions(int pos1, int pos2, int duplexLength){
		if (pos1 > 0){
			pos1 --;
		}
		if (pos2 < duplexLength - 1){
			pos2 ++;
		}
		int [] positions = {pos1, pos2};
		return positions;
	}
}
