
/*Santalucia et al.(2005). Nucleic acids research 33 : 6258-6267*/

package melting.wobbleNNMethod;

import java.util.ArrayList;
import java.util.logging.Level;

import melting.BasePair;
import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Santalucia05Inosine extends InosineNNMethod{
	
	public static String defaultFileName = "Santalucia2005inomn.xml";
	
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

		if (environment.getHybridization().equals("dnadna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for dangling ends" +
			"of Bommarito (2000) are established for DNA sequences.");
		}
		
		return super.isApplicable(environment, pos1, pos2);
	}
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		int [] positions = super.correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for inosine is from Santalucia et al. (2005) : ");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		return super.calculateThermodynamics(newSequences, pos1, pos2, result);
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");

		return super.isMissingParameters(newSequences, pos1, pos2);
	}
	
}
