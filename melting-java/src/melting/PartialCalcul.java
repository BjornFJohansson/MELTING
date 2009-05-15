package melting;

import java.io.File;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;

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

	public void loadData(HashMap<String, String> options){
		loadFile(this.fileName, this.collector);
	}
	
	public void loadFile(String name, DataCollect collector){
		File dataFile = new File(OptionManagement.dataPathway + "/" + name);
		FileReader reader = new FileReader();
		try {
			collector.setData(reader.readFile(dataFile, collector.getData()));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
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
