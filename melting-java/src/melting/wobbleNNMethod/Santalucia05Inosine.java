package melting.wobbleNNMethod;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Santalucia05Inosine extends InosineNNMethod{

	/*Santalucia et al.(2005). Nucleic acids research 33 : 6258-6267*/
	
	public static String defaultFileName = "Santalucia2005inomn.xml";
	
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
		
		if (environment.getHybridization().equals("dnadna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for dangling ends" +
			"of Bommarito (2000) are established for DNA sequences.");
			
			environment.modifieSequences(environment.getSequences().getSequence(pos1, pos2, "dna"), environment.getSequences().getSequence(pos1, pos2, "dna"));

		}
		
		return isApplicable;
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));

		OptionManagement.meltingLogger.log(Level.FINE, "The thermodynamic parameters for inosine are from Santalucia et al. (2005) : ");
		
		return super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
	}
	
}
