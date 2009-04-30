package melting.cricksNNMethods;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import melting.DataCollect;
import melting.FileReader;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;

public class CricksNNMethod implements PartialCalculMethod{

	protected DataCollect collector;
	
	public CricksNNMethod(String fileName){
		File dataFile = new File(OptionManagement.dataPathway + "/filename");
		FileReader reader = new FileReader();
		try {
			reader.readFile(dataFile, collector.getDatas());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		String seq1 = "";
		String complementarySeq = "";
		Thermodynamics parameter = new Thermodynamics(0,0);
		 
		for (int i = pos1; i < pos2; i++){
			seq1 = seq.substring(i, i+2);
			complementarySeq = seq2.substring(i, i+2);
			parameter = collector.getNNvalue(seq1, complementarySeq);
			
			result.setEnthalpy(result.getEnthalpy() + parameter.getEnthalpy());
			result.setEntropy(result.getEntropy() + parameter.getEntropy());
		}
		return null;
	}

	public boolean isApplicable(HashMap<String, String> options) {
		String seq = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		int duplexLength = Math.min(seq.length(), seq2.length());
		
		if (Integer.getInteger(options.get(OptionManagement.threshold)) >= duplexLength){
			System.out.println("WARNING : the Nearest Neighbor model is accurate for " +
			"shorter sequences. (length superior to 6 and inferior to" +
			 options.get(OptionManagement.threshold) +")");
			
			return false;
		}
		
		if (options.containsKey(OptionManagement.selfComplementarity) && Integer.getInteger(options.get(OptionManagement.factor)) != 1){
			System.out.println("ERROR : When the oligonucleotides are self-complementary, the correction factor F must be equal to 1.");
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
	
}
