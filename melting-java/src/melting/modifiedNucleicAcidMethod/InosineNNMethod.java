package melting.modifiedNucleicAcidMethod;

import java.util.HashMap;

import melting.DataCollect;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;

public class InosineNNMethod implements PartialCalculMethod{

	protected DataCollect collector;
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		String seq1 = "";
		String complementarySeq = "";
		Thermodynamics parameter = new Thermodynamics(0,0);
		 
		for (int i = pos1; i <= pos2 - 1; i++){
			seq1 = seq.substring(i, i+2);
			complementarySeq = seq2.substring(i, i+2);
			parameter = collector.getModifiedvalue(seq1, complementarySeq);
			
			result.setEnthalpy(result.getEnthalpy() + parameter.getEnthalpy());
			result.setEntropy(result.getEntropy() + parameter.getEntropy());
		}
		return result;
	}

	public DataCollect getCollector() {
		return this.collector;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (isMissingParameters(seq1, seq2, pos1, pos2)) {
			System.err.println("Some thermodynamic parameters are missing to compute the" +
					"melting temperature.");
			return false;
		}
		
		return true;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		Thermodynamics parameter = new Thermodynamics(0,0);
		String seq;
		String complementarySeq; 
		for (int i = pos1; i < pos2; i++){
			seq = seq1.substring(i, i+2);
			complementarySeq = seq2.substring(i, i+2);
			parameter = collector.getModifiedvalue(seq, complementarySeq);
			
			if (parameter == null) {
				return true;
			}
		}
		return false;
	}

}
