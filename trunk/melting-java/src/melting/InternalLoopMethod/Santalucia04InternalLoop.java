package melting.InternalLoopMethod;

import java.util.HashMap;

import melting.Helper;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Santalucia04InternalLoop extends PartialCalcul{

	/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */
	
	public Santalucia04InternalLoop(){
		Helper.loadData("Santalucia2004longmm.xml", this.collector);
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		Thermodynamics 	internalSize = collector.getInternalLoopValue(Integer.toString(Helper.calculateLoopLength(seq.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1))));
		Thermodynamics leftTerminalMismatch = collector.getMismatchvalue(seq.substring(pos1, pos1 + 2), seq2.substring(pos1, pos1 + 2));
		Thermodynamics rightTerminalMismatch = collector.getMismatchvalue(seq.substring(pos2 - 1, pos2 + 1), seq2.substring(pos2 - 1, pos2 + 1));
		
		if (internalSize == null){
			double entropyLoop = collector.getInternalLoopValue("30").getEntropy() + 2.44 * 1.99 * 310.15 * Math.log(30 / (Math.abs(pos2 - pos1) + 1));
			internalSize = new Thermodynamics(0,entropyLoop);
		}
		
		result.setEnthalpy(result.getEnthalpy() + leftTerminalMismatch.getEnthalpy() + rightTerminalMismatch.getEnthalpy());
		result.setEntropy(result.getEntropy() + internalSize.getEntropy() + leftTerminalMismatch.getEntropy() + rightTerminalMismatch.getEntropy());
		
		if (Helper.isAsymetricLoop(seq.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1))){
			Thermodynamics 	asymetry = collector.getAsymetry();
			
			result.setEnthalpy(result.getEnthalpy() + asymetry.getEnthalpy());
			result.setEntropy(result.getEntropy() + asymetry.getEnthalpy());
		}
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = super.isApplicable(options, pos1, pos2);
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (hybridization.equals("dnadna") == false){
			System.out.println("WARNING : the internal loop parameters of " +
					"Santalucia (2004) are originally established " +
					"for DNA sequences.");
			
			isApplicable = false;
		}

		if (Helper.calculateLoopLength(seq1.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1)) == 2){
			System.out.println("WARNING : The internal loop parameter of Santalucia (2004) are not estblished for single mismatches.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		
		String seq = seq1.substring(pos1, pos2+1);
		String complementarySeq = seq2.substring(pos1, pos2+1); 
		boolean isMissingParameters = super.isMissingParameters(seq1, seq2, pos1, pos2);
		
		if (Helper.isAsymetricLoop(seq, complementarySeq)){
			Thermodynamics 	asymetry = collector.getAsymetry();
			if (asymetry == null) {
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
