package melting.singleMismatchMethods;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Znosko08mm extends ZnoskoMethod {

	/*REF: Brent M Znosko et al (2008). Biochemistry 47: 10178-10187.*/
	
	public static String defaultFileName = "Znosko2008mm.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		NucleotidSequences mismatch = new NucleotidSequences(environment.getSequences().getSequence(pos1, pos2), environment.getSequences().getComplementary(pos1, pos2));
		if (mismatch.calculateNumberOfTerminal('G', 'U') == 0){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters of Znosco (2008)" +
			"are originally established for single mismatches with GU nearest neighbors.");
			isApplicable = false;
		}
	
		return isApplicable;
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		OptionManagement.meltingLogger.log(Level.FINE, "\n The thermodynamic parameters for single mismatches are from Znosco et al. (2008) : ");
		OptionManagement.meltingLogger.log(Level.FINE,formulaEnthalpy + " (entropy formula is similar)");
		
		return super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
	}
	
}
