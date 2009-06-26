
/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */

package melting.cricksNNMethods;

import java.util.logging.Level;


import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.exceptions.SequenceException;

public class Santalucia04 extends CricksNNMethod {
	
	public static String defaultFileName = "Santalucia2004nn.xml";
	
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
			OptionManagement.meltingLogger.log(Level.WARNING, "The model of Santalucia (2004)" +
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
		int numberTerminalAT = withoutTerminalUnpairedNucleotides.calculateNumberOfTerminal('A', 'T');
		double enthalpy = 0.0;
		double entropy = 0.0;
		
		if (numberTerminalAT != 0){
			Thermodynamics terminalAT = this.collector.getTerminal("per_A/T");
			
			OptionManagement.meltingLogger.log(Level.FINE, numberTerminalAT + " x penalty per terminal AT : enthalpy = " + terminalAT.getEnthalpy() + "  entropy = " + terminalAT.getEntropy());
			
			enthalpy += numberTerminalAT * terminalAT.getEnthalpy();
			entropy += numberTerminalAT * terminalAT.getEntropy();
		}
		
		environment.addResult(enthalpy, entropy);
		
		return environment.getResult();
		
	}
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model is from Santalucia et al (2004).");
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
