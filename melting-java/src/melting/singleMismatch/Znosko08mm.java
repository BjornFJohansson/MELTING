package melting.singleMismatch;

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

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		int [] positions = correctPositions(pos1, pos2, environment.getSequences().getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];

		NucleotidSequences mismatch = environment.getSequences().getEquivalentSequences("rna");
		
		if (mismatch.calculateNumberOfTerminal("G", "U", pos1, pos2) == 0){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters of Znosco (2008)" +
			"are originally established for single mismatches with GU nearest neighbors.");
			isApplicable = false;
		}
	
		return isApplicable;
	}
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		int [] positions = super.correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];

		NucleotidSequences newSequences = sequences.getEquivalentSequences("rna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The model for single mismatches is from Znosco et al. (2008) : ");
		OptionManagement.meltingLogger.log(Level.FINE,formulaEnthalpy + " (entropy formula is similar)");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		return super.calculateThermodynamics(newSequences, pos1, pos2, result);
	}
	
}
