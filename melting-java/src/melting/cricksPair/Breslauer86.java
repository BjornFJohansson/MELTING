
/* (1986). Proc Natl Acad Sci USA 83 : 3746-3750 */

package melting.cricksPair;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Breslauer86 extends GlobalInitiationNNMethod{
	
	public static String defaultFileName = "Breslauer1986nn.xml";
	
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
			OptionManagement.meltingLogger.log(Level.WARNING, "The model of Breslauer86 et al (1986)" +
					"is established for DNA sequences.");			
		}
		return super.isApplicable(environment, pos1, pos2);
	}
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model is from Breslauer86 et al (1986).");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
				
		return super.calculateThermodynamics(newSequences, pos1, pos2, result);
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
				
		return super.isMissingParameters(newSequences, pos1, pos2);
	}
}
