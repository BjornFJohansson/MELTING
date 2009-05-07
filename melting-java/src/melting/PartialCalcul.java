package melting;

import java.util.HashMap;

import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public abstract class PartialCalcul implements PartialCalculMethod{

	protected DataCollect collector;
	
	public abstract ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result);
	

	public DataCollect getCollector() {
		return this.collector;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (isMissingParameters(seq1, seq2, pos1, pos2)) {
			System.err.println("Some thermodynamic parameters are missing to compute" +
					"melting temperature.");
			return false;
		}
		return true;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		return false;
	}
	
	public void loadSingleMismatchData(HashMap<String, String> optionSet,int pos1, int pos2, ThermoResult result){
		RegisterCalculMethod getData = new RegisterCalculMethod();
		PartialCalculMethod singleMismatchMethod = getData.getSingleMismatchMethod(optionSet, result, pos1, pos2);
		
		this.collector.getDatas().putAll(singleMismatchMethod.getCollector().getDatas());
	}
	
	public void loadCrickNNData(HashMap<String, String> optionSet,int pos1, int pos2, ThermoResult result){
		RegisterCalculMethod getData = new RegisterCalculMethod();
		PartialCalculMethod crickNNMethod = getData.getWatsonCrickMethod(optionSet, result, pos1, pos2);
		
		this.collector.getDatas().putAll(crickNNMethod.getCollector().getDatas());
	}
	
	protected String getSens(String seq1, String seq2, int pos1, int pos2){
		if (pos1 == 0){
			if (seq2.contains("-")){
				return "5";
			}
			else if (seq1.contains("-")){
				return "3";
			}
		}
		else if (pos2 == Math.min(seq1.length(), seq2.length())){
			if (seq2.contains("-")){
				return "3";
			}
			else if (seq1.contains("-")){
				return "5";
			}
		}
		return null;
	}

}
