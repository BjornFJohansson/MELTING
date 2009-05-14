package melting.calculMethodInterfaces;

import java.util.HashMap;

import melting.DataCollect;
import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;

public interface PartialCalculMethod {
	
	public boolean isApplicable(Environment environment, int pos1, int pos2);
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences, int pos1, int pos2, ThermoResult result);
	
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1, int pos2);
	
	public DataCollect getCollector();
	
	public void loadData(HashMap<String, String> options);
		
	public void loadFile(String name, DataCollect collector);
	
	public String getDataFileName(String methodName);
	
	public void initializeFileName(String methodName);

}
