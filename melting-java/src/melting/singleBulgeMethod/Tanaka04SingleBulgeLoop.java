package melting.singleBulgeMethod;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Tanaka04SingleBulgeLoop extends GlobalSingleBulgeLoopMethod{

	/*Tanaka Fumiaki et al (2004). Biochemistry 43 : 7143-7150*/
	
	public static String defaultFileName = "Tanaka2004Bulge.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {

		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The single bulge loop parameters of " +
					"Tanaka (2004) are originally established " +
					"for DNA sequences.");
		}
	
		return super.isApplicable(environment, pos1, pos2);
	}

	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));

		OptionManagement.meltingLogger.log(Level.FINE, "The thermodynamic parameters for single bulge loop are from Tanaka et al. (2004) : ");
		
		return super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
	}
	
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));
		return super.isMissingParameters(newSequences, 0, newSequences.getDuplexLength() - 1);
	}
}
