package melting.sodiumCorrections;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CorrectionMethod;

public class Wetmur91SodiumCarrection implements CorrectionMethod{

	/*James G. Wetmur, "DNA Probes : applications of the principles of nucleic acid hybridization",
	1991, Critical reviews in biochemistry and molecular biology, 26, 227-259*/
	
	public ThermoResult correctMeltingResult(Environment environment) {
		
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		double Tm = environment.getResult().getTm() + 16.6 * Math.log10(NaEq / (1.0 + 0.7 * NaEq)) + 3.83;
		environment.setResult(Tm);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = true;
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		if (NaEq == 0){
			System.out.println("ERROR : The sodium correction of Wetmur (1991) is applicable for " +
					"strictky positive sodium concentrations.");
			isApplicable = false;
		}
		return isApplicable;
	}
	
}
