
/*Martin J Serra et al (2007). Biochemistry 46 : 15123-15135 */

package melting.patternModels.singleBulge;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.sequences.NucleotidSequences;

public class Serra07SingleBulgeLoop extends GlobalSingleBulgeLoopMethod{
	
	public static String defaultFileName = "Serra2007bulge.xml";
	
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

		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "the single bulge loop parameters of " +
					"Serra et al. (2007) are originally established " +
					"for RNA sequences.");
		}
	
		return super.isApplicable(environment, pos1, pos2);
	}
	
	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		int [] positions = super.correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("rna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for single bulge loop is from Serra et al. (2007) : ");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		return super.computeThermodynamics(newSequences, pos1, pos2, result);
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("rna");

		return super.isMissingParameters(newSequences, pos1, pos2);
	}

}
