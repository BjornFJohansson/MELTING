package melting.cricksNNMethods;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.exceptions.SequenceException;

public class Santalucia04 extends CricksNNMethod {

	/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */
	
	public static String defaultFileName = "Santalucia2004nn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters of Santalucia (2004)" +
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
		int numberTerminalAT = withoutTerminalUnpairedNucleotides.calculateNumberOfTerminal('A', 'T');
		double enthalpy = 0;
		double entropy = 0;
		
		if (numberTerminalAT != 0){
			Thermodynamics terminalAT = this.collector.getTerminal("per_A/T");
			
			OptionManagement.meltingLogger.log(Level.FINE, terminalAT + " x penalty per terminal AT : enthalpy = " + terminalAT.getEnthalpy() + "  entropy = " + terminalAT.getEntropy());
			
			enthalpy += numberTerminalAT * terminalAT.getEnthalpy();
			entropy += numberTerminalAT * terminalAT.getEntropy();
		}
		
		environment.setResult(enthalpy, entropy);
		
		return environment.getResult();
		
	}
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		OptionManagement.meltingLogger.log(Level.FINE, "The thermodynamic parameters for the watson crick base pairs are from Santalucia et al (2004).");
		
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));
		
		return super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
	}

}