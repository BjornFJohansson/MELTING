package melting.InternalLoopMethod;

import java.util.HashMap;

import melting.DataCollect;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public class Santalucia04InternalLoop implements PartialCalculMethod{

	/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */
	
	private DataCollect collector;
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		Thermodynamics 	internalSize = collector.getInternalLoopValue(Integer.toString(calculateLoopLength(seq.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1))));
		Thermodynamics leftTerminalMismatch = collector.getMismatchvalue(seq.substring(pos1, pos1 + 2), seq2.substring(pos1, pos1 + 2));
		Thermodynamics rightTerminalMismatch = collector.getMismatchvalue(seq.substring(pos2 - 1, pos2 + 1), seq2.substring(pos2 - 1, pos2 + 1));
		
		if (internalSize == null){
			double entropyLoop = collector.getInternalLoopValue("30").getEntropy() + 2.44 * 1.99 * 310.15 * Math.log(30 / (Math.abs(pos2 - pos1) + 1));
			internalSize = new Thermodynamics(0,entropyLoop);
		}
		
		result.setEnthalpy(result.getEnthalpy() + leftTerminalMismatch.getEnthalpy() + rightTerminalMismatch.getEnthalpy());
		result.setEntropy(result.getEntropy() + internalSize.getEntropy() + leftTerminalMismatch.getEntropy() + rightTerminalMismatch.getEntropy());
		
		if (isAsymetricLoop(seq.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1))){
			Thermodynamics 	asymetry = collector.getAsymetry();
			
			result.setEnthalpy(result.getEnthalpy() + asymetry.getEnthalpy());
			result.setEntropy(result.getEntropy() + asymetry.getEnthalpy());
		}
		return result;
	}

	public DataCollect getCollector() {
		return this.collector;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = true;
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (hybridization.equals("dnadna") == false){
			System.out.println("WARNING : the internal loop parameters of " +
					"Santalucia (2004) are originally established " +
					"for DNA sequences.");
			
			isApplicable = false;
		}

		if (calculateLoopLength(seq1.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1)) == 2){
			System.out.println("WARNING : The internal loop parameter of Santalucia (2004) are not estblished for single mismatches.");
			isApplicable = false;
		}
		
		if (isMissingParameters(seq1, seq2, pos1, pos2)){
			System.out.println("WARNING : some thermodynamic parameters are missing.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		
		String seq = seq1.substring(pos1, pos2+1);
		String complementarySeq = seq2.substring(pos1, pos2+1); 
		
		if (isAsymetricLoop(seq, complementarySeq)){
			Thermodynamics 	asymetry = collector.getAsymetry();
			if (asymetry == null) {
				return true;
			}
		}
		return false;
	}
	
	public void loadSingleMismatchData(HashMap<String, String> optionSet,int pos1, int pos2, ThermoResult result){
		RegisterCalculMethod getData = new RegisterCalculMethod();
		PartialCalculMethod singleMismatchMethod = getData.getSingleMismatchMethod(optionSet, result, pos1, pos2);
		
		this.collector.getDatas().putAll(singleMismatchMethod.getCollector().getDatas());
	}
	
	private boolean isAsymetricLoop(String seq1, String seq2){
		
		for (int i= 1; i < seq1.length() - 1; i++){
			if (seq1.charAt(i) == '-' || seq2.charAt(i) == '-'){
				return true;
			}
		}
		return false;
	}
	
	private double calculateGibbs (String seq1, String seq2){
		double gibbs = Math.abs(seq1.length() - seq2.length()) * 0.3;
		return gibbs;
	}
	
	private int calculateLoopLength (String seq1, String seq2){
		int loop = seq1.length() - 2 + seq2.length() - 2;
		
		for (int i = 1; i < seq1.length() - 1; i++){
			if (seq1.charAt(i) == '-'){
				loop--;
			}
			if (seq2.charAt(i) == '-'){
				loop--;
			}
		}
		return loop;
	}
	
	public ThermoResult calculateSelIndependentThermodynamics(String seq, String seq2,
			int pos1, int pos2) { 
		
	}

}
