package melting.singleBulgeMethod;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Serra07SingleBulgeLoop extends GlobalSingleBulgeLoopMethod{

	/*Martin J Serra et al (2007). Biochemistry 46 : 15123-15135 */
	
	public static String defaultFileName = "Serra07bulge.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		
		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "the single bulge loop parameters of " +
					"Serra et al. (2007) are originally established " +
					"for RNA sequences.");
		}
	
		return super.isApplicable(environment, pos1, pos2);
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		OptionManagement.meltingLogger.log(Level.INFO, "The thermodynamic parameters for single bulge loop are from Serra et al. (2007) : ");
		
		return super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
	}
}
