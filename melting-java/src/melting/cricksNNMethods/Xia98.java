package melting.cricksNNMethods;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.exceptions.SequenceException;

public class Xia98 extends CricksNNMethod {

	/*Xia et al (1998) Biochemistry 37: 14719-14735 */
	
	public static String defaultFileName = "Xia1998nn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		
		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters of Xia et al. (1998)" +
			"are established for RNA/RNA sequences.");
			
			environment.modifieSequences(environment.getSequences().getSequence(pos1, pos2, "rna"), environment.getSequences().getComplementary(pos1, pos2, "rna"));

		}
		return super.isApplicable(environment, pos1, pos2);
	}
	
	public ThermoResult calculateInitiationHybridation(Environment environment){
		NucleotidSequences newSequences = new NucleotidSequences(environment.getSequences().getSequence(0, environment.getSequences().getDuplexLength() - 1, "rna"), environment.getSequences().getComplementary(0, environment.getSequences().getDuplexLength() - 1, "rna"));

		environment.modifieSequences(newSequences.getSequence(), newSequences.getComplementary());

		environment.setResult(super.calculateInitiationHybridation(environment));

		NucleotidSequences withoutTerminalUnpairedNucleotides =  newSequences.removeTerminalUnpairedNucleotides();
		
		if (withoutTerminalUnpairedNucleotides == null){
			throw new SequenceException("The two sequences can't be hybridized.");
		}
		
		int numberTerminalAU = withoutTerminalUnpairedNucleotides.calculateNumberOfTerminal('A', 'U');
		double enthalpy = 0;
		double entropy = 0;
		
		if (numberTerminalAU != 0) {
			Thermodynamics terminalAU = this.collector.getTerminal("per_A/U");
			
			OptionManagement.meltingLogger.log(Level.FINE, numberTerminalAU + " x penalty per terminal AU : enthalpy = " + terminalAU.getEnthalpy() + "  entropy = " + terminalAU.getEntropy());
			
			enthalpy += numberTerminalAU * terminalAU.getEnthalpy();
			entropy += numberTerminalAU * terminalAU.getEntropy();
		}
		
		environment.setResult(enthalpy, entropy);
		
		return environment.getResult();
	}

	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n The thermodynamic parameters for the watson crick base pairs are from Xia (1998).");
		
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));
		
		return super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
	}
}
