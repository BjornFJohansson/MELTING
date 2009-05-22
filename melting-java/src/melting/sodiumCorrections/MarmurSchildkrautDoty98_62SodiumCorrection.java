package melting.sodiumCorrections;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CorrectionMethod;

public class MarmurSchildkrautDoty98_62SodiumCorrection implements CorrectionMethod{

	/*Blake, R. D., and Delcourt, S. G. (1998) Thermal stability of DNA,
	 * Nucleic Acids Res. 26, 3323-3332 and corrigendum.
	 * 
	 *  Marmur, J., and Doty, P. (1962) Determination of the base
	 *  composition of deoxyribonucleic acid from its thermal denaturation
	 *  temperature, J. Mol. Biol. 5, 109-118.
	 * */
	
	public ThermoResult correctMeltingResult(Environment environment) {
		
		double NaEq = Helper.calculateNaEquivalent(environment);
		int Fgc = environment.getSequences().calculatePercentGC();
		
		double Tm = environment.getResult().getTm() + (8.75 - 2.83 * Fgc) * Math.log(NaEq);
		environment.setResult(Tm);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = true;
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		if (NaEq < 0.069 || NaEq > 1.02){
			System.out.println("ERROR : The sodium correction of Marmur Schildkraut and Doty (1962, 1998)" +
					" is originally established for sodium concentrations between 0.069 and 1.02M.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("ERROR : The sodium correction of Marmur Schildkraut and Doty (1962, 1998) is originally established for " +
			"DNA duplexes.");
			isApplicable = false;
		}
		
		return isApplicable;
	}


}
