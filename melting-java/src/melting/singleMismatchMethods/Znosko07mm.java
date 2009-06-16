package melting.singleMismatchMethods;

import java.util.logging.Level;

import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Znosko07mm extends ZnoskoMethod {

	/*REF: Brent M Znosko et al (2007). Biochemistry 46: 13425-13436.*/
	
	public static String defaultFileName = "Znosko2007mm.xml";
	
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

		OptionManagement.meltingLogger.log(Level.FINE, "\n The thermodynamic parameters for single mismatches are from Znosco et al. (2007) : ");
		OptionManagement.meltingLogger.log(Level.FINE,formulaEnthalpy + " (entropy formula is similar)");
		
		return super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
	}

}