package melting.singleDanglingEndMethod;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Bommarito00SingleDanglingEnd extends SingleDanglingEndMethod {

	/*REF: Bommarito et al. (2000). Nuc Acids Res 28: 1929-1934 */
	
	public static String defaultFileName = "Bommarito2000de.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		if (environment.getHybridization().equals("dnadna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for dangling ends" +
					"of Bommarito (2000) are established for DNA sequences.");
			
			environment.modifieSequences(environment.getSequences().getSequence(pos1, pos2, "dna"), environment.getSequences().getSequence(pos1, pos2, "dna"));

					}
		return super.isApplicable(environment, pos1, pos2);
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));

		OptionManagement.meltingLogger.log(Level.FINE, "The thermodynamic parameters for single dangling end are from Bommarito et al. (2000) : ");
		
		return super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
	}
	
}
