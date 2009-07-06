package melting.cricksPair;


import java.util.logging.Level;

import Sequences.NucleotidSequences;

import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public abstract class DecomposedInitiationNNMethod extends CricksNNMethod {
	
	@Override
	public ThermoResult calculateInitiationHybridation(Environment environment){
		
		super.calculateInitiationHybridation(environment);
		
		NucleotidSequences sequences = environment.getSequences();
		int [] truncatedPositions = sequences.removeTerminalUnpairedNucleotides();
				
		double numberTerminalGC = environment.getSequences().calculateNumberOfTerminal("G", "C", truncatedPositions[0], truncatedPositions[1]);
		double numberTerminalAT = environment.getSequences().calculateNumberOfTerminal("A", "T", truncatedPositions[0], truncatedPositions[1]);
		double numberTerminalAU = environment.getSequences().calculateNumberOfTerminal("A", "U", truncatedPositions[0], truncatedPositions[1]);
		
		double enthalpy = 0.0;
		double entropy = 0.0;
		
		if (numberTerminalAT != 0){
			Thermodynamics initiationAT = this.collector.getInitiation("per_A/T");
			
			OptionManagement.meltingLogger.log(Level.FINE, "\n " + numberTerminalAT + " x Initiation per A/T : enthalpy = " + initiationAT.getEnthalpy() + "  entropy = " + initiationAT.getEntropy());
			
			enthalpy += numberTerminalAT * initiationAT.getEnthalpy();
			entropy += numberTerminalAT * initiationAT.getEntropy();
		}
		
		else if (numberTerminalAU != 0){
			Thermodynamics initiationAU = this.collector.getInitiation("per_A/U");
			
			OptionManagement.meltingLogger.log(Level.FINE, "\n " + numberTerminalAU + " x Initiation per A/U : enthalpy = " + initiationAU.getEnthalpy() + "  entropy = " + initiationAU.getEntropy());

			enthalpy += numberTerminalAU * this.collector.getInitiation("per_A/U").getEnthalpy();
			entropy += numberTerminalAU * this.collector.getInitiation("per_A/U").getEntropy();
		}
		
		if (numberTerminalGC != 0){
			Thermodynamics initiationGC = this.collector.getInitiation("per_G/C");

			OptionManagement.meltingLogger.log(Level.FINE, "\n " + numberTerminalGC + " x Initiation per G/C : enthalpy = " + initiationGC.getEnthalpy() + "  entropy = " + initiationGC.getEntropy());
			
			enthalpy += numberTerminalGC * initiationGC.getEnthalpy();
			entropy += numberTerminalGC * initiationGC.getEntropy();
		}
		
		environment.addResult(enthalpy, entropy);
		
		return environment.getResult();
	}

}
