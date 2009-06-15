package melting.wobbleNNMethod;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Turner99Wobble extends PartialCalcul{
	
	/*REF: Douglas M Turner et al (1999). J.Mol.Biol.  288: 911_940 */
	
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

		OptionManagement.meltingLogger.log(Level.FINE, "\n The thermodynamic parameters for GU base pairs are from Turner et al. (1999) : ");
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics mismatchValue;
		for (int i = 0; i < newSequence.getDuplexLength() - 1; i++){
			mismatchValue = this.collector.getMismatchvalue(newSequence.getSequenceNNPair(i), newSequence.getComplementaryNNPair(i));
			OptionManagement.meltingLogger.log(Level.FINE, newSequence.getSequenceNNPair(i) + "/" + newSequence.getComplementaryNNPair(i) + " : enthalpy = " + mismatchValue.getEnthalpy() + "  entropy = " + mismatchValue.getEntropy());
			
			enthalpy += mismatchValue.getEnthalpy();
			entropy += mismatchValue.getEntropy(); 
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "the woddle base parameters of " +
					"Turner (1999) are originally established " +
					"for RNA sequences.");
		}
		return super.isApplicable(environment, pos1, pos2);
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		for (int i = 0; i < newSequences.getDuplexLength() - 1; i++){
			if (this.collector.getMismatchvalue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i)) == null){
				return true;
			}
		}
		return super.isMissingParameters(newSequences, 0, newSequences.getDuplexLength() - 1);
	}

}
