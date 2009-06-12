package melting.InternalLoopMethod;


import java.util.HashMap;
import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

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
		
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));
		
		OptionManagement.meltingLogger.log(Level.FINE, "The internal loop formulas fron Santalucia (2004) : " + formulaEnthalpy + " and " + formulaEntropy);

		Thermodynamics rightMismatch =  collector.getMismatchvalue(newSequences.getSequenceNNPair(pos1), newSequences.getComplementaryNNPair(pos1));
		Thermodynamics leftMismatch =  collector.getMismatchvalue(newSequences.getSequenceNNPair(pos2 - 1), newSequences.getComplementaryNNPair(pos2 - 1));

		OptionManagement.meltingLogger.log(Level.FINE, "Right terminal mismatch : " + sequences.getSequenceNNPair(pos1) + "/" + sequences.getComplementaryNNPair(pos1) + " : enthalpy = " + rightMismatch.getEnthalpy() + "  entropy = " + rightMismatch.getEntropy());
		OptionManagement.meltingLogger.log(Level.FINE, "Left terminal mismatch : " + sequences.getSequenceNNPair(pos2 - 1) + "/" + sequences.getComplementaryNNPair(pos2 - 1) + " : enthalpy = " + leftMismatch.getEnthalpy() + "  entropy = " + leftMismatch.getEntropy());

		double saltIndependentEntropy = result.getSaltIndependentEntropy();
		double enthalpy = result.getEnthalpy() + rightMismatch.getEnthalpy() + leftMismatch.getEnthalpy();
		double entropy = result.getEntropy()  + rightMismatch.getEntropy() + leftMismatch.getEntropy();
		
		int loopLength = sequences.calculateLoopLength(pos1, pos2);
		Thermodynamics internalLoop = collector.getInternalLoopValue(Integer.toString(loopLength));
		if (internalLoop != null){
			OptionManagement.meltingLogger.log(Level.FINE, "Internal loop of" + loopLength + " :  enthalpy = " + internalLoop.getEnthalpy() + "  entropy = " + internalLoop.getEntropy());
			
			if (loopLength > 4){
				saltIndependentEntropy += internalLoop.getEntropy();
			}
			else {
				entropy += internalLoop.getEntropy();
			}
		}
		else {
			double value = collector.getInternalLoopValue("30").getEntropy() + 2.44 * 1.99 * 310.15 * Math.log(loopLength/30.0);
			
			OptionManagement.meltingLogger.log(Level.FINE, "Internal loop of" + loopLength + " :  enthalpy = 0" + "  entropy = " + value);

			if (loopLength > 4){				
				saltIndependentEntropy += value;
				
			}
			else {
				entropy += value;
			}		
		}
		
		if (sequences.isAsymmetricLoop(pos1, pos2)){
			Thermodynamics asymmetry = collector.getAsymmetry();
			
			OptionManagement.meltingLogger.log(Level.FINE, "asymmetry : enthalpy = " + asymmetry.getEnthalpy() + "  entropy = " + asymmetry.getEntropy());
				
			enthalpy += asymmetry.getEnthalpy();

			if (loopLength > 4){
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
		Environment newEnvironment = environment;

		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, " the internal loop parameters of " +
					"Santalucia (2004) are originally established " +
					"for DNA sequences.");
			
			newEnvironment = Environment.modifieSequences(newEnvironment, environment.getSequences().getSequence(pos1, pos2, "dna"), environment.getSequences().getComplementary(pos1, pos2, "dna"));
			pos1 = 0;
			pos2 = newEnvironment.getSequences().getDuplexLength() - 1;
		}
		
		boolean isApplicable = super.isApplicable(newEnvironment, pos1, pos2);

		if (environment.getSequences().calculateLoopLength(pos1, pos2) == 2){
			OptionManagement.meltingLogger.log(Level.WARNING, "The internal loop parameter of Santalucia (2004) are not estblished for single mismatches.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) { 
			NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));

		boolean isMissingParameters = super.isMissingParameters(newSequences, pos1, pos2);
		
		if (sequences.isAsymmetricLoop(pos1, pos2)){
			if (collector.getAsymmetry() == null) {
				isMissingParameters = true;
			}
		}
		return isMissingParameters;
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);
		
		String singleMismatchName = options.get(OptionManagement.singleMismatchMethod);
		RegisterCalculMethod register = new RegisterCalculMethod();
		PartialCalculMethod singleMismatch = register.getPartialCalculMethod(OptionManagement.singleMismatchMethod, singleMismatchName);
		singleMismatch.initializeFileName(singleMismatchName);
		String fileSingleMismatch = singleMismatch.getDataFileName(singleMismatchName);
		
		loadFile(fileSingleMismatch, this.collector);
	}
	
	private double calculateGibbs (String seq1, String seq2){
		double gibbs = Math.abs(seq1.length() - seq2.length()) * 0.3;
		return gibbs;
	}

}
