
/*Xia et al (1998) Biochemistry 37: 14719-14735 */

package melting.cricksNNMethods;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Xia98 extends CricksNNMethod {
	
	public static String defaultFileName = "Xia1998nn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	@Override
	public boolean isApplicable(Environment environment, int pos1, int pos2) {

		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The model of Xia et al. (1998)" +
			"is established for RNA/RNA sequences.");
		}
		return super.isApplicable(environment, pos1, pos2);
	}
	
	@Override
	public ThermoResult calculateInitiationHybridation(Environment environment){

		NucleotidSequences newSequences = environment.getSequences().getEquivalentSequences("rna");
		
		super.calculateInitiationHybridation(environment);

		int [] truncatedPositions = newSequences.removeTerminalUnpairedNucleotides();
		
		double numberTerminalAU = newSequences.calculateNumberOfTerminal("A", "U", truncatedPositions[0], truncatedPositions[1]);
		double enthalpy = 0.0;
		double entropy = 0.0;
		
		if (numberTerminalAU != 0) {
			Thermodynamics terminalAU = this.collector.getTerminal("per_A/U");
			
			OptionManagement.meltingLogger.log(Level.FINE, numberTerminalAU + " x penalty per terminal AU : enthalpy = " + terminalAU.getEnthalpy() + "  entropy = " + terminalAU.getEntropy());
			
			enthalpy += numberTerminalAU * terminalAU.getEnthalpy();
			entropy += numberTerminalAU * terminalAU.getEntropy();
		}
		
		environment.addResult(enthalpy, entropy);
		
		return environment.getResult();
	}

	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model is from Xia (1998).");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		NucleotidSequences newSequences = sequences.getEquivalentSequences("rna");
				
		return super.calculateThermodynamics(newSequences, pos1, pos2, result);
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = sequences.getEquivalentSequences("rna");
		
		return super.isMissingParameters(newSequences, pos1, pos2);
	}
}
