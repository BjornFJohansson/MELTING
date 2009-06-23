
/*REF: Martin J Serra et al. (2006). Nucleic Acids research 34: 3338-3344
	REF: Martin J Serra et al. (2008). Nucleic Acids research 36: 5652-5659 */

package melting.singleDanglingEndMethod;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Serra06_08SingleDanglingEnd extends SingleDanglingEndMethod {
	
	public static String defaultFileName = "Serra2006_2008de.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {

		if (environment.getHybridization().equals("rnarna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for dangling ends" +
					" of Serra et al. (2006 - 2008) are established for RNA sequences.");
		}
		return super.isApplicable(environment, pos1, pos2);
	}
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for single dangling end is from Serra et al. (2006, 2008) : ");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		return super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));
		return super.isMissingParameters(newSequences, 0, newSequences.getDuplexLength() - 1);
	}

}
