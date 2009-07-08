
/*Allawi and SantaLucia (1997). Biochemistry 36 : 10581-10594 */

package melting.patternModels.cricksPair;

import java.util.logging.Level;


import melting.Environment;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.sequences.NucleotidSequences;

public class AllawiSantalucia97 extends DecomposedInitiationNNMethod {
	
	public static String defaultFileName = "AllawiSantalucia1997nn.xml";

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
			OptionManagement.meltingLogger.log(Level.WARNING, "The model of Allawi and Santalucia (1997)" +
					"is established for DNA sequences.");	
			
		}
		return super.isApplicable(environment, pos1, pos2);
	}
	
	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model is  from Allawi and Santalucia (1997). \n");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		return super.computeThermodynamics(newSequences, pos1, pos2, result);
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		return super.isMissingParameters(newSequences,pos1, pos2);
	}
	
}
