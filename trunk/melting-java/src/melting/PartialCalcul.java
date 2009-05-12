package melting;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public abstract class PartialCalcul implements PartialCalculMethod{

	protected DataCollect collector;
	
	public abstract ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result);
	

	public DataCollect getCollector() {
		return this.collector;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		NucleotidSequences sequences = new NucleotidSequences(environment.getOptions().get(OptionManagement.sequence),environment.getOptions().get(OptionManagement.complementarySequence));
		
		if (isMissingParameters(sequences, pos1, pos2)) {
			System.err.println("Some thermodynamic parameters are missing to compute" +
					"melting temperature.");
			return false;
		}
		return true;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
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
	
	public void loadSingleDanglingEndData(HashMap<String, String> optionSet,int pos1, int pos2, ThermoResult result){
		RegisterCalculMethod getData = new RegisterCalculMethod();
		PartialCalculMethod singleDanglingEndMethod = getData.getSingleDanglingEndMethod(optionSet, result, pos1, pos2);
		
		this.collector.getDatas().putAll(singleDanglingEndMethod.getCollector().getDatas());
	}

	public void loadData(String fileName, DataCollect collector){
		File dataFile = new File(OptionManagement.dataPathway + "/" + fileName);
		FileReader reader = new FileReader();
		try {
			reader.readFile(dataFile, collector.getDatas());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
}
