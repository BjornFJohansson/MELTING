package melting.MagnesiumCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.calculMethodInterfaces.CorrectionMethod;
import melting.configuration.OptionManagement;

public class Owczarzy08MagnesiumCorrection implements CorrectionMethod{

	/* Richard Owczarzy, Bernardo G Moreira, Yong You, Mark A 
	 * Behlke, Joseph A walder, "Predicting stability of DNA duplexes in solutions
	 * containing magnesium and monovalent cations", 2008, Biochemistry, 47, 5336-5353.
	 * */
	
	protected double a = 3.92 / 100000;
	protected double b = -9.11 / 1000000;
	protected double c = 6.26 / 100000;
	protected double d = 1.42 / 100000;
	protected double e = -4.82 / 10000;
	protected double f = 5.25 / 10000;
	protected double g = 8.31 / 100000;
	
	protected static String temperatureCorrection = "1 / Tm(Mg) = 1 / Tm(Na = 1M) + a +b x ln(Mg) + Fgc x (c + d x ln(Mg)) + 1 / (2 x (duplexLength - 1)) x (e + f x ln(Mg) + g x (ln(mg)))^2"; 
	
	public ThermoResult correctMeltingResult(Environment environment) {

		double Tm = correctTemperature(environment);
		environment.setResult(Tm);
		
		return environment.getResult();
	}

	public boolean isApplicable(Environment environment) {
		boolean isApplicable = true;
		
		if (environment.getMg() < 0.0005 || environment.getMg() > 0.6){
			OptionManagement.meltingLogger.log(Level.WARNING, "The magnesium correction of Owczarzy et al. " +
			"(2008) is accurate in the magnesium concentration range of 0.5mM to 600mM.");
			isApplicable = false;
		}
		
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The magnesium correction of Owczarzy et al. " +
					"(2008) is originally established for DNA duplexes.");
		}
		return isApplicable;
	}
	
	protected double correctTemperature(Environment environment) {
		OptionManagement.meltingLogger.log(Level.FINE, "\n The magnesium correction from Owkzarzy et al. (2008) : ");
		OptionManagement.meltingLogger.log(Level.FINE,temperatureCorrection);
		OptionManagement.meltingLogger.log(Level.FINE, "where : ");
		OptionManagement.meltingLogger.log(Level.FINE, "b = " + this.b);
		OptionManagement.meltingLogger.log(Level.FINE, "c = " + this.c);
		OptionManagement.meltingLogger.log(Level.FINE, "e = " + this.e);
		OptionManagement.meltingLogger.log(Level.FINE, "f = " + this.f);
		displayVariable();

		double Mg = environment.getMg() - environment.getDNTP();
		double square = Math.log(Mg) * Math.log(Mg);
		int Fgc = environment.getSequences().calculatePercentGC() / 100;
		
		double TmInverse = 1 / environment.getResult().getTm() + this.a +this.b * Math.log(Mg) + Fgc * (this.c + this.d * Math.log(Mg)) + 1 / (2 * (environment.getSequences().getDuplexLength() - 1)) * (this.e + this.f * Math.log(Mg) + this.g * square);
		return 1 / TmInverse;
	}
	
	protected void displayVariable(){
		OptionManagement.meltingLogger.log(Level.FINE, "a = " + this.a);
		OptionManagement.meltingLogger.log(Level.FINE, "d = " + this.d);
		OptionManagement.meltingLogger.log(Level.FINE, "g = " + this.g);
	}

}
