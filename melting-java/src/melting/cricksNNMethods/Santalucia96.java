package melting.cricksNNMethods;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.exceptions.SequenceException;

public class Santalucia96 extends GlobalInitiationNNMethod {

	/*SantaLucia et al.(1996). Biochemistry 35 : 3555-3562*/                    
	
	public static String defaultFileName = "Santalucia1996nn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters of Santalucia (1996)" +
			"are established for DNA sequences.");
			
			environment.modifieSequences(environment.getSequences().getSequence(pos1, pos2, "dna"), environment.getSequences().getSequence(pos1, pos2, "dna"));

		}
		return super.isApplicable(environment, pos1, pos2);
	}
	
	public ThermoResult calculateInitiationHybridation(Environment environment){
		NucleotidSequences newSequences = new NucleotidSequences(environment.getSequences().getSequence(0, environment.getSequences().getDuplexLength() - 1, "dna"), environment.getSequences().getComplementary(0, environment.getSequences().getDuplexLength() - 1, "dna"));

		environment.modifieSequences(newSequences.getSequence(), newSequences.getComplementary());
		
		environment.setResult(super.calculateInitiationHybridation(environment));

		NucleotidSequences withoutTerminalUnpairedNucleotides =  newSequences.removeTerminalUnpairedNucleotides();
				
		if (withoutTerminalUnpairedNucleotides == null){
			throw new SequenceException("The two sequences can't be hybridized.");
		}
		double enthalpy = 0;
		double entropy = 0;
		
		if (withoutTerminalUnpairedNucleotides.isTerminal5AT()) {
			Thermodynamics terminal5AT = this.collector.getTerminal("5_T/A");
			
			OptionManagement.meltingLogger.log(Level.FINE, terminal5AT + " x penalty per 5' terminal AT : enthalpy = " + terminal5AT.getEnthalpy() + "  entropy = " + terminal5AT.getEntropy());
			
			enthalpy += terminal5AT.getEnthalpy();
			entropy += terminal5AT.getEntropy();
		}
		
		environment.setResult(enthalpy, entropy);
		return environment.getResult();
	}
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		OptionManagement.meltingLogger.log(Level.FINE, "The thermodynamic parameters for the watson crick base pairs are from Santalucia et al (1996).");
		
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));
		
		return super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
	}
}
