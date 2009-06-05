package melting.longDanglingEndMethod;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.configuration.OptionManagement;


public class Sugimoto02DNADanglingEnd extends SugimotoLongDanglingEndMethod {

	/*REF: Sugimoto et al. (2002). J. Am. Chem. Soc. 124: 10367-10372 */
	
	public static String defaultFileName = "Sugimoto2002longdde.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		
		if (environment.getHybridization().equals("dnadna")){
			OptionManagement.meltingLogger.log(Level.WARNING, "The following thermodynamic parameters for long dangling end of Sugimoto et al." +
					"(2002) are established for DNA sequences.");
			
			environment.modifieSequences(environment.getSequences().getSequence(pos1, pos2, "dna"), environment.getSequences().getSequence(pos1, pos2, "dna"));
		}
		
		return super.isApplicable(environment, pos1, pos2);
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));
		
		return super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
	}
}
