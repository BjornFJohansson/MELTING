package melting.wobbleNNMethod;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

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

	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		for (int i = pos1; i <= pos2; i++){
			enthalpy += this.collector.getMismatchvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)).getEnthalpy();
			entropy += this.collector.getMismatchvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)).getEntropy(); 
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("rnarna") == false){
			System.out.println("WARNING : the woddle base parameters of " +
					"Turner (1999) are originally established " +
					"for RNA sequences.");
			isApplicable = false;
		}
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
 
		for (int i = pos1; i <= pos2; i++){
			if (this.collector.getMismatchvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)) == null){
				return true;
			}
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}

}
