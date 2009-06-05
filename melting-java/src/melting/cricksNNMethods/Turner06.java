package melting.cricksNNMethods;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.exceptions.MethodNotApplicableException;
import melting.exceptions.SequenceException;

public class Turner06 extends CricksNNMethod {
	
	public static String defaultFileName = "Turner2006nn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics NNValue;
		 
		for (int i = pos1; i <= pos2 - 1; i++){
			
			NNValue = this.collector.getNNvalue("m" + sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i));
			
			OptionManagement.meltingLogger.log(Level.FINE, "m"+ sequences.getSequenceNNPair(i) + "/" + sequences.getComplementaryNNPair(i) + " : enthalpy = " + NNValue.getEnthalpy() + "  entropy = " + NNValue.getEntropy());
			
			enthalpy += NNValue.getEnthalpy();
			entropy += NNValue.getEntropy();
		}
		
		entropy = getEntropy1MNa(entropy, pos1, pos2);
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		boolean isApplicable = isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("mrnarna") == false){
			isApplicable = false;
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters of Turner et al. (2006)" +
			"are established for 2-0-methyl RNA/RNA sequences.");
		}
		
		if (environment.isSelfComplementarity()){
			throw new MethodNotApplicableException ( "The thermodynamic parameters of Turner et al. (2006)" +
					"are established for hybrid mRNA/RNA sequences.");
		}
		
		return isApplicable;
	}
	
	public ThermoResult calculateInitiationHybridation(Environment environment){
		environment.setResult(super.calculateInitiationHybridation(environment));

		NucleotidSequences withoutTerminalUnpairedNucleotides =  environment.getSequences().removeTerminalUnpairedNucleotides();
		
		if (withoutTerminalUnpairedNucleotides == null){
			throw new SequenceException("The two sequences can't be hybridized.");
		}
		int numberTerminalAU = withoutTerminalUnpairedNucleotides.calculateNumberOfTerminal('A', 'U');
		double enthalpy = 0;
		double entropy = 0;
		
		if (numberTerminalAU != 0) {
			Thermodynamics terminalAU = this.collector.getTerminal("per_A/U");
			
			OptionManagement.meltingLogger.log(Level.FINE, terminalAU + " x penalty per terminal AU : enthalpy = " + terminalAU.getEnthalpy() + "  entropy = " + terminalAU.getEntropy());
			
			enthalpy += numberTerminalAU * terminalAU.getEnthalpy();
			entropy += numberTerminalAU * terminalAU.getEntropy();
		}
		
		environment.setResult(enthalpy, entropy);
		
		return environment.getResult();
	}
	
	private double getEntropy1MNa(double entropy0_1MNa, int pos1, int pos2){
		//sodium correction of Santalucia 1998_2004.
		double entropy1MNa = entropy0_1MNa - 0.368 * (Math.abs(pos2 - pos1)) * Math.log(0.1);
		
		return entropy1MNa;
	}

}
