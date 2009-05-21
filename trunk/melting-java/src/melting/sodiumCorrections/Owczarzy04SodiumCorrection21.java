package melting.sodiumCorrections;

import melting.Environment;
import melting.Helper;
import melting.ThermoResult;
import melting.calculMethodInterfaces.IonCorrectionMethod;

public class Owczarzy04SodiumCorrection21 implements IonCorrectionMethod {

	/*Richard Owczarzy, Yong You, Bernardo G. Moreira, Jeffrey A.Manthey, Lingyan Huang, Mark A. Behlke and Joseph 
	 * A.Walder, "Effects of sodium ions on DNA duplex oligomers: Improved predictions of melting temperatures",
	 * Biochemistry, 2004, 43, 3537-3554.*/
	
	public ThermoResult correctMeltingResult(Environment environment) {
		
		double NaEq = Helper.calculateNaEquivalent(environment);
		int Fgc = environment.getSequences().calculatePercentGC() / 100;
		double square = Math.log(NaEq) * Math.log(NaEq);
		
		double TmInverse = 1 / environment.getResult().getTm() + (-4.62 * Fgc + 4.52) * Math.log(NaEq) - 0.985 * square;
		environment.setResult(1 / TmInverse);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = true;
		double NaEq = Helper.calculateNaEquivalent(environment);
		
		if (NaEq == 0){
			System.out.println("ERROR : The sodium concentration must be strictly positive.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("ERROR : The sodium correction of Owczarzy et al. (2004) 21 is originally established for " +
			"DNA duplexes.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

}
