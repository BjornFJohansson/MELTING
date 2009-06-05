package melting.longBulgeMethod;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Santalucia04LongBulgeLoop extends PartialCalcul{

/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */
	
	public static String defaultFileName = "Santalucia2004longbulge.xml";
	
	protected static String formulaEnthalpy = "delat H = number AT closing x H(closing AT penalty)";
	protected static String formulaEntropy = "delat S = number AT closing x S(closing AT penalty) + S(bulge loop of n)";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		NucleotidSequences bulgeLoop = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));
		
		OptionManagement.meltingLogger.log(Level.FINE, "The long bulge loop formulas from Santalucia. (2004) : " + formulaEnthalpy + " and " + formulaEntropy);
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		int numberAT = bulgeLoop.calculateNumberOfTerminal('A', 'T');
		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		
		Thermodynamics bulgeLoopValue = this.collector.getBulgeLoopvalue(bulgeSize);
		if (bulgeLoopValue == null){
			bulgeLoopValue = this.collector.getBulgeLoopvalue("30");
			
			OptionManagement.meltingLogger.log(Level.FINE, "bulge loop of " + bulgeSize + " :  enthalpy = " + bulgeLoopValue.getEnthalpy() + "  entropy = " + bulgeLoopValue.getEntropy() + " + 2.44 x 1.99 x 310.15 x ln(bulgeSize / 30)");

			entropy += bulgeLoopValue.getEntropy() + 2.44 * 1.99 * 310.15 * Math.log(Integer.getInteger(bulgeSize) / 30);
		}
		else {
			OptionManagement.meltingLogger.log(Level.FINE, "bulge loop of " + bulgeSize + " :  enthalpy = " + bulgeLoopValue.getEnthalpy() + "  entropy = " + bulgeLoopValue.getEntropy());

			entropy += bulgeLoopValue.getEntropy();
		}
		
		if (numberAT> 0){
			Thermodynamics closingAT = this.collector.getClosureValue("A", "T");

			OptionManagement.meltingLogger.log(Level.FINE, numberAT + " x AT closing : enthalpy = " + closingAT.getEnthalpy() + "  entropy = " + closingAT.getEntropy());

			enthalpy += numberAT * closingAT.getEnthalpy();
			enthalpy += numberAT * closingAT.getEntropy();
		
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "the single bulge loop parameters of " +
					"Santalucia (2004) are originally established " +
					"for DNA sequences.");
			
			environment.modifieSequences(environment.getSequences().getSequence(pos1, pos2, "dna"), environment.getSequences().getSequence(pos1, pos2, "dna"));

		}
		
		return super.isApplicable(environment, pos1, pos2);
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		boolean isMissingParameters = super.isMissingParameters(sequences, pos1, pos2);
		NucleotidSequences bulgeLoop = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		int numberAT = bulgeLoop.calculateNumberOfTerminal('A', 'T');
		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		
		if (numberAT > 0){
			if (this.collector.getClosureValue("A", "T") == null){
				return true;
			}
		}

		if (this.collector.getBulgeLoopvalue(bulgeSize) == null){
			if (this.collector.getBulgeLoopvalue("30") == null){
			return true;
			}
		}
		return isMissingParameters;
	}
}
