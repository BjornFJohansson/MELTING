package melting.sodiumCorrections;

import melting.Environment;
import melting.ThermoResult;
import melting.ionCorrections.SodiumCorrections;

public class Santalucia96SodiumCorrection extends SodiumCorrections {

	/*SantaLucia et al.(1996). Biochemistry 35 : 3555-3562*/
	
	@Override
	public ThermoResult correctMeltingResult(Environment environment) {
		double NaEq = calculateNaEqEquivalent(environment);
		
		double Tm = environment.getResult().getTm() + 12.5 * Math.log10(NaEq);
		environment.setResult(Tm);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = super.isApplicable(environment);
		double NaEq = calculateNaEqEquivalent(environment);
		
		if (NaEq < 0.1){
			System.out.println("ERROR : The sodium correction of Santalucia et al. (1996) is not reliable for " +
					"sodium concentrations inferior to 0.1M.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("ERROR : The sodium correction of Santalucia et al. (1996) is originally established for " +
			"DNA duplexes.");
			isApplicable = false;
		}
		return isApplicable;
	}
	
	protected double calculateNaEqEquivalent(Environment environment){
		double NaEq = 0;
		
		if (environment.getK() > 0 && environment.getNa() == 0 && environment.getMg() == 0 && environment.getTris() == 0){
			NaEq = environment.getK();
		}
		
		if (NaEq == 0){
			NaEq = super.calculateNaEqEquivalent(environment);
		}
		return NaEq;
	}
}
