package melting.InternalLoopMethod;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

public class Santalucia04InternalLoop extends PartialCalcul{

	/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */
	
	public static String defaultFileName = "Santalucia2004longmm.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		double enthalpy = result.getEnthalpy() + collector.getMismatchvalue(sequences.getSequenceNNPair(pos1), sequences.getComplementaryNNPair(pos1)).getEnthalpy() + collector.getMismatchvalue(sequences.getSequenceNNPair(pos2 - 1), sequences.getComplementaryNNPair(pos2 - 1)).getEnthalpy();
		double entropy = result.getEntropy()  + collector.getMismatchvalue(sequences.getSequenceNNPair(pos1), sequences.getComplementaryNNPair(pos1)).getEntropy() + collector.getMismatchvalue(sequences.getSequenceNNPair(pos2 - 1), sequences.getComplementaryNNPair(pos2 - 1)).getEntropy();
				
		if (collector.getInternalLoopValue(Integer.toString(sequences.calculateLoopLength(pos1, pos2))) != null){
			entropy += collector.getInternalLoopValue(Integer.toString(sequences.calculateLoopLength(pos1, pos2))).getEntropy();
		}
		else {
			entropy += collector.getInternalLoopValue("30").getEntropy() + 2.44 * 1.99 * 310.15 * Math.log(30 / (Math.abs(pos2 - pos1) + 1));
		}
		
		if (sequences.isAsymetricLoop(pos1, pos2)){
			enthalpy += collector.getAsymetry().getEnthalpy();
			entropy += collector.getAsymetry().getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
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
		
		if (sequences.isAsymetricLoop(pos1, pos2)){
			if (collector.getAsymetry() == null) {
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
