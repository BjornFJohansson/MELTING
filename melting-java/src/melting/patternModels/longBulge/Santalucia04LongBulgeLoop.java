
/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */

package melting.patternModels.longBulge;

import java.util.logging.Level;


import melting.Environment;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.patternModels.PatternComputation;
import melting.sequences.NucleotidSequences;

public class Santalucia04LongBulgeLoop extends PatternComputation{
	
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
	
	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];

		NucleotidSequences bulgeLoop = sequences.getEquivalentSequences("dna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The long bulge loop model is from Santalucia. (2004) : ");
		OptionManagement.meltingLogger.log(Level.FINE,formulaEnthalpy + " and " + formulaEntropy);
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		double numberAT = bulgeLoop.calculateNumberOfTerminal("A", "T", pos1, pos2);
		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		
		Thermodynamics bulgeLoopValue = this.collector.getBulgeLoopvalue(bulgeSize);
		if (bulgeLoopValue == null){
			bulgeLoopValue = this.collector.getBulgeLoopvalue("30");
			
			OptionManagement.meltingLogger.log(Level.FINE, "bulge loop of " + bulgeSize + " :  enthalpy = " + bulgeLoopValue.getEnthalpy() + "  entropy = " + bulgeLoopValue.getEntropy() + " - 2.44 x 1.99 x ln(bulgeSize / 30)");

			entropy += bulgeLoopValue.getEntropy() - 2.44 * 1.99 * Math.log(Double.parseDouble(bulgeSize) / 30.0);
		}
		else {
			OptionManagement.meltingLogger.log(Level.FINE, "bulge loop of " + bulgeSize + " :  enthalpy = " + bulgeLoopValue.getEnthalpy() + "  entropy = " + bulgeLoopValue.getEntropy());

			entropy += bulgeLoopValue.getEntropy();
		}
		
		if (numberAT> 0 && this.collector.getClosureValue("A", "T") != null){
			Thermodynamics closingAT = this.collector.getClosureValue("A", "T");

			OptionManagement.meltingLogger.log(Level.FINE, numberAT + " x AT closing : enthalpy = " + closingAT.getEnthalpy() + "  entropy = " + closingAT.getEntropy());

			enthalpy += numberAT * closingAT.getEnthalpy();
			enthalpy += numberAT * closingAT.getEntropy();
		
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {

		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "the single bulge loop parameters of " +
					"Santalucia (2004) are originally established " +
					"for DNA sequences.");
		}
		
		return super.isApplicable(environment, pos1, pos2);
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		boolean isMissingParameters = super.isMissingParameters(sequences, pos1, pos2);
		NucleotidSequences bulgeLoop = sequences.getEquivalentSequences("dna");
		
		double numberAT = bulgeLoop.calculateNumberOfTerminal("A", "T", pos1, pos2);
		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		
		if (numberAT > 0){
			if (this.collector.getClosureValue("A", "T") == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The parameters for AT closing base pair are missing. The results can lose accuracy.");
			}
		}
		if (this.collector.getBulgeLoopvalue(bulgeSize) == null){
			if (this.collector.getBulgeLoopvalue("30") == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The parameters for a bulge loop of " + bulgeSize + " are missing. Check the long bulge loop parameters.");

				return true;
			}
		}
		return isMissingParameters;
	}
	
	protected int[] correctPositions(int pos1, int pos2, int duplexLength){
		if (pos1 > 0){
			pos1 --;
		}
		if (pos2 < duplexLength - 1){
			pos2 ++;
		}
		int [] positions = {pos1, pos2};
		return positions;
	}
}
