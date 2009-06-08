package melting.cricksNNMethods;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class AllawiSantalucia97 extends DecomposedInitiationNNMethod {

	/*Allawi and SantaLucia (1997). Biochemistry 36 : 10581-10594 */
	
	public static String defaultFileName = "AllawiSantalucia1997nn.xml";

	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters of Allawi and Santalucia (1997)" +
					"are established for DNA sequences.");	
			
			environment.modifieSequences(environment.getSequences().getSequence(pos1, pos2, "dna"), environment.getSequences().getComplementary(pos1, pos2, "dna"));

		}
		return super.isApplicable(environment, pos1, pos2);
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		OptionManagement.meltingLogger.log(Level.FINE, "The thermodynamic parameters for the watson crick base pairs are from Allawi and Santalucia (1997).");
		
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));
		
		return super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
	}
	
}
