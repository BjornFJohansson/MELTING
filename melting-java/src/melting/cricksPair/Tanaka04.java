	
/*Tanaka Fumiaki et al (2004). Biochemistry 43 : 7143-7150 */

package melting.cricksPair;

import java.util.logging.Level;

import Sequences.NucleotidSequences;

import melting.Environment;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Tanaka04 extends DecomposedInitiationNNMethod {
	
	public static String defaultFileName = "Tanaka2004nn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1, int pos2) {

		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The model of Tanaka (2004)" +
					"is established for DNA sequences ");
		}

		return super.isApplicable(environment, pos1, pos2);
	}
	
	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model is from Tanaka (2004).");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
				
		return super.computeThermodynamics(newSequences, pos1, pos2, result);
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		return super.isMissingParameters(newSequences, pos1, pos2);
	}
}
