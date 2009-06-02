package melting.InternalLoopMethod;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Santalucia04InternalLoop extends PartialCalcul{

	/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */
	
	public static String defaultFileName = "Santalucia2004longmm.xml";
	
	private static String formulaEnthalpy = "delat H = H(right terminal mismath) + H(left terminal mismatch) + H(asymmetric loop)";
	private static String formulaEntropy = "delat S = S(right terminal mismath) + S(left terminal mismatch) + S(asymmetric loop) + S(loop)";

	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		OptionManagement.meltingLogger.log(Level.INFO, "The internal loop formulas fron Santalucia (2004) : " + formulaEnthalpy + "and" + formulaEntropy);

		double saltIndependentEntropy = result.getSaltIndependentEntropy();
		Thermodynamics rightMismatch =  collector.getMismatchvalue(sequences.getSequenceNNPair(pos1), sequences.getComplementaryNNPair(pos1));
		Thermodynamics leftMismatch =  collector.getMismatchvalue(sequences.getSequenceNNPair(pos2 - 1), sequences.getComplementaryNNPair(pos2 - 1));

		OptionManagement.meltingLogger.log(Level.INFO, "Right terminal mismatch : " + sequences.getSequenceNNPair(pos1) + "/" + sequences.getComplementaryNNPair(pos1) + " : enthalpy = " + rightMismatch.getEnthalpy() + "  entropy = " + rightMismatch.getEntropy());
		OptionManagement.meltingLogger.log(Level.INFO, "Left terminal mismatch : " + sequences.getSequenceNNPair(pos2 - 1) + "/" + sequences.getComplementaryNNPair(pos2 - 1) + " : enthalpy = " + leftMismatch.getEnthalpy() + "  entropy = " + leftMismatch.getEntropy());

		double enthalpy = result.getEnthalpy() + rightMismatch.getEnthalpy() + leftMismatch.getEnthalpy();
		double entropy = result.getEntropy()  + rightMismatch.getEntropy() + leftMismatch.getEntropy();
		
		Thermodynamics internalLoop = collector.getInternalLoopValue(Integer.toString(sequences.calculateLoopLength(pos1, pos2)));
		if (internalLoop != null){
			OptionManagement.meltingLogger.log(Level.INFO, "Internal loop of" + sequences.calculateLoopLength(pos1, pos2) + " :  enthalpy = " + internalLoop.getEnthalpy() + "  entropy = " + internalLoop.getEntropy());
			
			if (sequences.calculateLoopLength(pos1, pos2) > 4){
				saltIndependentEntropy += internalLoop.getEntropy();
			}
			else {
				entropy += internalLoop.getEntropy();
			}
		}
		else {
			double value = collector.getInternalLoopValue("30").getEntropy() + 2.44 * 1.99 * 310.15 * Math.log(sequences.calculateLoopLength(pos1, pos2)/30);
			
			OptionManagement.meltingLogger.log(Level.INFO, "Internal loop of" + sequences.calculateLoopLength(pos1, pos2) + " :  enthalpy = 0" + "  entropy = " + value);

			if (sequences.calculateLoopLength(pos1, pos2) > 4){				
				saltIndependentEntropy += value;
				
			}
			else {
				entropy += value;
			}		
		}
		
		if (sequences.isAsymmetricLoop(pos1, pos2)){
			Thermodynamics asymmetry = collector.getAsymmetry();
			
			OptionManagement.meltingLogger.log(Level.INFO, "asymetry : enthalpy = " + asymmetry.getEnthalpy() + "  entropy = " + asymmetry.getEntropy());
				
			enthalpy += asymmetry.getEnthalpy();

			if (sequences.calculateLoopLength(pos1, pos2) > 4){
				saltIndependentEntropy += asymmetry.getEntropy();
			}
			else {
				entropy += asymmetry.getEntropy();
			}	
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		result.setSaltIndependentEntropy(saltIndependentEntropy);
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("WARNING : the internal loop parameters of " +
					"Santalucia (2004) are originally established " +
					"for DNA sequences.");
			
			isApplicable = false;
		}

		if (environment.getSequences().calculateLoopLength(pos1, pos2) == 2){
			System.out.println("WARNING : The internal loop parameter of Santalucia (2004) are not estblished for single mismatches.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) { 
		boolean isMissingParameters = super.isMissingParameters(sequences, pos1, pos2);
		
		if (sequences.isAsymmetricLoop(pos1, pos2)){
			if (collector.getAsymmetry() == null) {
				isMissingParameters = true;
			}
		}
		return isMissingParameters;
	}
	
	private double calculateGibbs (String seq1, String seq2){
		double gibbs = Math.abs(seq1.length() - seq2.length()) * 0.3;
		return gibbs;
	}

}
