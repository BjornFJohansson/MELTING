package melting.sodiumEquivalence;

import java.util.HashMap;
import java.util.logging.Level;

import melting.calculMethodInterfaces.SodiumEquivalentMethod;
import melting.configuration.OptionManagement;

public abstract class SodiumEquivalent implements SodiumEquivalentMethod{

	public double getSodiumEquivalent(double Na, double Mg, double K, double Tris,
			double dNTP, double b) {
		
		double NaEq = Na + K + Tris / 2 + b * Math.sqrt(Mg - dNTP);
		
		return NaEq;
	}

	public boolean isApplicable(HashMap<String, String> options) {
		String hybridization = options.get(OptionManagement.hybridization);
		
		if (hybridization.equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The current equations to have the sodium equivalent" +
					"concentration are established for DNA duplexes.");
		}
		return true;
	}
}
