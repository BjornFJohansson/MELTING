
/*SantaLucia et al.(1996). Biochemistry 35 : 3555-3562*/                    

package melting.cricksNNMethods;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.exceptions.SequenceException;

public class Santalucia96 extends GlobalInitiationNNMethod {
	
	public static String defaultFileName = "Santalucia1996nn.xml";
	
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
			OptionManagement.meltingLogger.log(Level.WARNING, "The model of Santalucia (1996)" +
			"is established for DNA sequences.");
		}
		return super.isApplicable(environment, pos1, pos2);
	}
	
	@Override
	public ThermoResult calculateInitiationHybridation(Environment environment){

		NucleotidSequences newSequences = new NucleotidSequences(environment.getSequences().getSequence(0, environment.getSequences().getDuplexLength() - 1, "dna"), environment.getSequences().getComplementary(0, environment.getSequences().getDuplexLength() - 1, "dna"));
		
		super.calculateInitiationHybridation(environment);

		NucleotidSequences withoutTerminalUnpairedNucleotides =  newSequences.removeTerminalUnpairedNucleotides();
				
		if (withoutTerminalUnpairedNucleotides == null){
			throw new SequenceException("The two sequences can't be hybridized.");
		}
		double enthalpy = 0.0;
		double entropy = 0.0;
		int number5AT = withoutTerminalUnpairedNucleotides.getNumberTerminal5AT();
		
		if (number5AT > 0) {
			Thermodynamics terminal5AT = this.collector.getTerminal("5_T/A");
			OptionManagement.meltingLogger.log(Level.FINE,number5AT + " x  penalty for 5' terminal AT : enthalpy = " + terminal5AT.getEnthalpy() + "  entropy = " + terminal5AT.getEntropy());
			
			enthalpy += number5AT * terminal5AT.getEnthalpy();
			entropy += number5AT * terminal5AT.getEntropy();
		}
		
		environment.addResult(enthalpy, entropy);
		return environment.getResult();
	}
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model is from Santalucia et al (1996).");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);
		
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));
		
		return super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));
		return super.isMissingParameters(newSequences, 0, newSequences.getDuplexLength() - 1);
	}
}
