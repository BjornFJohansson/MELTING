package melting.cricksNNMethods;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Freier86 extends CricksNNMethod {
	
	/*Freier et al (1986) Proc Natl Acad Sci USA 83: 9373-9377 */
	
	public static String defaultFileName = "Freier1986nn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		Environment newEnvironment = environment;

		if (environment.getHybridization().equals("rnarna") == false){

			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters of Freier et al. (1986)" +
			"are established for RNA sequences.");	

			newEnvironment = Environment.modifieSequences(newEnvironment, environment.getSequences().getSequence(pos1, pos2, "rna"), environment.getSequences().getComplementary(pos1, pos2, "rna"));
			pos1 = 0;
			pos2 = newEnvironment.getSequences().getDuplexLength() - 1;
			
		}

		return super.isApplicable(newEnvironment, pos1, pos2);
	}
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n The thermodynamic parameters for the watson crick base pairs are from Freier et al. (1986).");
		
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));
		
		return super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
	}
	
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));
		return super.isMissingParameters(newSequences, 0, newSequences.getDuplexLength() - 1);
	}

}
