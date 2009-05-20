package melting.MagnesiumCorrections;

import melting.Environment;
import melting.ThermoResult;
import melting.calculMethodInterfaces.IonCorrectionMethod;

public class Owczarzy08MagnesiumCorrection implements IonCorrectionMethod{

	/* Richard Owczarzy, Bernardo G Moreira, Yong You, Mark A 
	 * Behlke, Joseph A walder, "Predicting stability of DNA duplexes in solutions
	 * containing magnesium and monovalent cations", 2008, Biochemistry, 47, 5336-5353.
	 * */
	
	public ThermoResult correctMeltingResult(Environment environment) {
		
		double a = 3.92 / 100000;
		double b = -9.11 / 1000000;
		double c = 6.26 / 100000;
		double d = 1.42 / 100000;
		double e = -4.82 / 10000;
		double f = 5.25 / 10000;
		double g = 8.31 / 100000;
		
		double Mg = environment.getMg();
		double square = Math.log(Mg) * Math.log(Mg);
		int Fgc = environment.getSequences().calculatePercentGC() / 100;
		
		double TmInverse = 1 / environment.getResult().getTm() + a + b * Math.log(Mg) + Fgc * (c + d * Math.log(Mg)) + 1 / (2 * (environment.getSequences().getDuplexLength() - 1)) * (e + f * Math.log(Mg) + g * square);
		environment.setResult(1 / TmInverse);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		
		if (environment.getMg() < 0.0005 || environment.getMg() > 0.6){
			System.out.println("WARNING : The magnesium correction of Owczarzy et al. " +
			"(2008) is accurate in the magnesium concentration range of 0.5mM to 600mM.");
			return false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("WARNING : The magnesium correction of Owczarzy et al. " +
					"(2008) is originally established for DNA duplexes.");
			return false;
		}
		return true;
	}

}
