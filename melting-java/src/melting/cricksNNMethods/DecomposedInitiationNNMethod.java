package melting.cricksNNMethods;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.exceptions.SequenceException;

public abstract class DecomposedInitiationNNMethod extends CricksNNMethod {
	
	@Override
	public ThermoResult calculateInitiationHybridation(Environment environment){
		
		environment.setResult(super.calculateInitiationHybridation(environment));
		
		NucleotidSequences sequences = environment.getSequences();
		NucleotidSequences withoutTerminalUnpairedNucleotides = sequences.removeTerminalUnpairedNucleotides();
		
		if (withoutTerminalUnpairedNucleotides == null){
			throw new SequenceException("The two sequences can't be hybridized.");
		}
		int numberTerminalGC = withoutTerminalUnpairedNucleotides.calculateNumberOfTerminal('G', 'C');
		int numberTerminalAT = withoutTerminalUnpairedNucleotides.calculateNumberOfTerminal('A', 'T');
		int numberTerminalAU = withoutTerminalUnpairedNucleotides.calculateNumberOfTerminal('A', 'U');
		
		double enthalpy = 0;
		double entropy = 0;
		
		if (numberTerminalAT != 0){
			Thermodynamics initiationAT = this.collector.getInitiation("per_A/T");
			
			OptionManagement.meltingLogger.log(Level.FINE, "/n " + numberTerminalAT + " x Initiation per A/T : enthalpy = " + initiationAT.getEnthalpy() + "  entropy = " + initiationAT.getEntropy());
			
			enthalpy += numberTerminalAT * initiationAT.getEnthalpy();
			entropy += numberTerminalAT * initiationAT.getEntropy();
		}
		
		else if (numberTerminalAU != 0){
			Thermodynamics initiationAU = this.collector.getInitiation("per_A/U");
			
			OptionManagement.meltingLogger.log(Level.FINE, "/n " + numberTerminalAU + " x Initiation per A/U : enthalpy = " + initiationAU.getEnthalpy() + "  entropy = " + initiationAU.getEntropy());

			enthalpy += numberTerminalAU * this.collector.getInitiation("per_A/U").getEnthalpy();
			entropy += numberTerminalAU * this.collector.getInitiation("per_A/U").getEntropy();
		}
		
		if (numberTerminalGC != 0){
			Thermodynamics initiationGC = this.collector.getInitiation("per_G/C");

			OptionManagement.meltingLogger.log(Level.FINE, "/n " + numberTerminalGC + " x Initiation per G/C : enthalpy = " + initiationGC.getEnthalpy() + "  entropy = " + initiationGC.getEntropy());
			
			enthalpy += numberTerminalGC * initiationGC.getEnthalpy();
			entropy += numberTerminalGC * initiationGC.getEntropy();
		}
		
		environment.setResult(enthalpy, entropy);
		
		return environment.getResult();
	}

}
