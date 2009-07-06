
/*SantaLucia et al.(1996). Biochemistry 35 : 3555-3562*/                    

package melting.cricksPair;

import java.util.logging.Level;

import Sequences.NucleotidSequences;

import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

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

		NucleotidSequences newSequences = environment.getSequences().getEquivalentSequences("dna");
		super.calculateInitiationHybridation(environment);

		int [] truncatedPositions =  newSequences.removeTerminalUnpairedNucleotides();
		
		double enthalpy = 0.0;
		double entropy = 0.0;
		double number5AT = newSequences.getNumberTerminal5TA(truncatedPositions[0], truncatedPositions[1]);
		
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
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model is from Santalucia et al (1996).");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");

		return super.computeThermodynamics(newSequences, pos1, pos2, result);
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
				
		return super.isMissingParameters(newSequences, pos1, pos2);
	}
}
