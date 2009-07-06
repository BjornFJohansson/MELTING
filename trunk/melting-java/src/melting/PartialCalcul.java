package melting;

import java.io.File;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import melting.configuration.OptionManagement;
import melting.exceptions.FileException;
import melting.exceptions.ThermodynamicParameterError;
import melting.methodInterfaces.PartialCalculMethod;

public abstract class PartialCalcul implements PartialCalculMethod{

	protected DataCollect collector = new DataCollect();
	protected String fileName;

	public abstract ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result);
	
	public DataCollect getCollector() {
		return this.collector;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		NucleotidSequences sequences = environment.getSequences();

		if (isMissingParameters(sequences, pos1, pos2)) {
			throw new ThermodynamicParameterError("Some thermodynamic parameters are missing to compute " +
					"melting temperature.");
		}
		return true;
	}
	
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		return false;
	}

	public void loadData(HashMap<String, String> options){

		loadFile(this.fileName, this.collector);
	}
	
	public void loadFile(String name, DataCollect collector){

		File dataFile = new File(OptionManagement.dataPathwayValue + "/" + name);
		try {
			collector.setData(FileReader.readFile(dataFile, collector.getData()));

		} catch (ParserConfigurationException e) {
			throw new FileException("One of the files containing the thermodynamic parameters can't be parsed.");
		} catch (SAXException e) {
			throw new FileException("One of the files containing the thermodynamic parameters can't be parsed.");
		}
	}
	
	public String getDataFileName(String methodName){
		return this.fileName; 
	}
	
	public void initializeFileName(String methodName){
		if (Helper.useOtherDataFile(methodName)){
			this.fileName = Helper.getOptionFileName(methodName);
		}
		else{
			this.fileName = null;
		}
	}
		
	public static String getData(){
		return null;
	}
	
}
