package melting.calculMethodInterfaces;

import java.util.HashMap;

public interface SodiumEquivalentMethod {

	public double getSodiumEquivalent(double Na, double Mg, double K, double Tris, double dNTP);
	public boolean isApplicable(HashMap<String, String> options);
	
}
