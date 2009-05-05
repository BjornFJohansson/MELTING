package melting.cricksNNMethods;

import java.util.HashMap;

import melting.DataCollect;
import melting.Helper;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;

public abstract class CricksNNMethod implements PartialCalculMethod{

	protected DataCollect collector;
	
	public CricksNNMethod(String fileName){
		Helper.loadData(fileName, this.collector);
	}
	
	public DataCollect getCollector(){
		return this.collector;
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		String seq1 = "";
		String complementarySeq = "";
		Thermodynamics parameter = new Thermodynamics(0,0);
		 
		for (int i = pos1; i <= pos2 - 1; i++){
			seq1 = seq.substring(i, i+2);
			complementarySeq = seq2.substring(i, i+2);
			parameter = collector.getNNvalue(seq1, complementarySeq);
			
			result.setEnthalpy(result.getEnthalpy() + parameter.getEnthalpy());
			result.setEntropy(result.getEntropy() + parameter.getEntropy());
		}
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1, int pos2) {
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (isMissingParameters(seq1, seq2, pos1, pos2)) {
			System.err.println("Some thermodynamic parameters are missing to compute" +
					"melting temperature.");
			return false;
		}
		return true;
	}
	
	public ThermoResult calculateInitiationHybridation(HashMap<String, String> options, ThermoResult result){
		Thermodynamics parameter = this.collector.getInitiation();
		
		if (parameter != null) {
			result.setEnthalpy(parameter.getEnthalpy());
			result.setEntropy(parameter.getEntropy());
		}
		
		if (options.containsKey(OptionManagement.selfComplementarity)){
			parameter = this.collector.getSymetry();
			
			result.setEnthalpy(result.getEnthalpy() + parameter.getEnthalpy());
			result.setEntropy(result.getEntropy() + parameter.getEntropy());
		}
		
		return result;
	}
	
	public boolean isMissingParameters(String seq1, String seq2, int pos1, int pos2){
		for (int i = pos1; i < pos2; i++){
			String seq = seq1.substring(i, i+2);
			String complementarySeq = seq2.substring(i, i+2);
			Thermodynamics parameter = collector.getNNvalue(seq, complementarySeq);
			
			if (parameter == null) {
				return true;
			}
		}
		return false;
	}
}
