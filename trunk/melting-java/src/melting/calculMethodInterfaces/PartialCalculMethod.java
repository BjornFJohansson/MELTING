package melting.calculMethodInterfaces;

import java.util.HashMap;

import melting.DataCollect;
import melting.ThermoResult;

public interface PartialCalculMethod {
	
	public boolean isApplicable(HashMap<String, String> options, int pos1, int pos2);
	
	public ThermoResult calculateThermodynamics(String seq, String seq2, int pos1, int pos2, ThermoResult result);
	
	public boolean isMissingParameters(String seq1, String seq2, int pos1, int pos2);
	
	public DataCollect getCollector();

}
