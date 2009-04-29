package melting.calculMethodInterfaces;

import java.util.HashMap;

import melting.ThermoResult;

public interface InitiationNeighborMethod {

	public boolean isApplicable(HashMap<String, String> options);
	
	public ThermoResult calculateInitiation(String seq, String seq2);
}
