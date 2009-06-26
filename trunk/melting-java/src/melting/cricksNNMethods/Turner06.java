/*Turner et al (2006)
 * Nucleic acids research 34: 3609-3614
 */

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
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics NNValue;
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model is from Turner et al. (2006)");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);
 
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
	
	@Override
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		if (environment.getHybridization().equals("mrnarna") == false && environment.getHybridization().equals("rnamrna") == false){
			isApplicable = false;
			OptionManagement.meltingLogger.log(Level.WARNING, "The model of Turner et al. (2006)" +
			"is established for 2-0-methyl RNA/RNA sequences.");
		}
		
		if (environment.isSelfComplementarity()){
			throw new MethodNotApplicableException ( "The thermodynamic parameters of Turner et al. (2006)" +
					"are established for hybrid mRNA/RNA sequences.");
		}
		return isApplicable;
	}
	
	@Override
	public ThermoResult calculateInitiationHybridation(Environment environment){
		NucleotidSequences withoutTerminalUnpairedNucleotides =  environment.getSequences().removeTerminalUnpairedNucleotides();

		super.calculateInitiationHybridation(environment);
		
		if (withoutTerminalUnpairedNucleotides == null){
			throw new SequenceException("The two sequences can't be hybridized.");
		}
		int numberTerminalAU = withoutTerminalUnpairedNucleotides.calculateNumberOfTerminal('A', 'U');
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
	
	private double getEntropy1MNa(double entropy0_1MNa, int pos1, int pos2){
		//sodium correction of Santalucia 1998_2004.
		double entropy1MNa = entropy0_1MNa - 0.368 * (Math.abs(pos2 - pos1)) * Math.log(0.1);
		
		return entropy1MNa;
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {

		boolean isMissing = false;
		for (int i = pos1; i <= pos2 - 1; i++){
			if (this.collector.getNNvalue("m" + sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)) == null){
				isMissing = true;
			}
		}
		return isMissing;
	}

}
