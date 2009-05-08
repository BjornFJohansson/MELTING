package melting.cricksNNMethods;


import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.configuration.OptionManagement;

public abstract class DecomposedInitiationNNMethod extends CricksNNMethod {

	public DecomposedInitiationNNMethod(String fileName) {
		super(fileName);
	}
	
	public ThermoResult calculateInitiationHybridation(Environment environment){
		
		environment.setResult(super.calculateInitiationHybridation(environment));
		
		NucleotidSequences sequences = new NucleotidSequences(environment.getOptions().get(OptionManagement.sequence), environment.getOptions().get(OptionManagement.complementarySequence));
		int numberTerminalGC = sequences.calculateNumberOfTerminal('G', 'C');
		int numberTerminalAT = sequences.calculateNumberOfTerminal('A', 'T');
		int numberTerminalAU = sequences.calculateNumberOfTerminal('A', 'U');
		
		double enthalpy = 0;
		double entropy = 0;
		
		if (numberTerminalAT != 0){
			enthalpy += numberTerminalAT * this.collector.getInitiation("per_A/T").getEnthalpy();
			entropy += numberTerminalAT * this.collector.getInitiation("per_A/T").getEntropy();
		}
		
		if (numberTerminalAU != 0){
			enthalpy += numberTerminalAU * this.collector.getInitiation("per_A/U").getEnthalpy();
			entropy += numberTerminalAU * this.collector.getInitiation("per_A/U").getEntropy();
		}
		
		if (numberTerminalGC != 0){
			enthalpy += numberTerminalGC * this.collector.getInitiation("per_G/C").getEnthalpy();
			entropy += numberTerminalGC * this.collector.getInitiation("per_G/C").getEntropy();
		}
		
		environment.setResult(enthalpy, entropy);
		
		return environment.getResult();
	}

}
