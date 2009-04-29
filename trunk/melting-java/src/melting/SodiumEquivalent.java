package melting;

import java.util.HashMap;

public interface SodiumEquivalent {

	public double getSodiumEquivalent(double Na, double Mg, double K, double Tris, double dNTP);
	public boolean isApplicable(HashMap<String, String> options);
	
}
