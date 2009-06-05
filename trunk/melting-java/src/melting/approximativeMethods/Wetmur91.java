package melting.approximativeMethods;

import java.util.logging.Level;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

public abstract class Wetmur91 extends ApproximativeMode{
	
	/*James G. Wetmur, "DNA Probes : applications of the principles of nucleic acid hybridization",
	1991, Critical reviews in biochemistry and molecular biology, 26, 227-259*/
	
	public ThermoResult CalculateThermodynamics() {
		
		OptionManagement.meltingLogger.log(Level.FINE, " from Wetmur (1991) \n");
		
		return this.environment.getResult();
	}
}
