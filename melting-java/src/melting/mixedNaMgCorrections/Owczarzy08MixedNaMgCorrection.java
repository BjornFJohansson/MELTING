/* Richard Owczarzy, Bernardo G Moreira, Yong You, Mark A 
	 * Behlke, Joseph A walder, "Predicting stability of DNA duplexes in solutions
	 * containing magnesium and monovalent cations", 2008, Biochemistry, 47, 5336-5353.
	 * */

package melting.mixedNaMgCorrections;

import java.util.logging.Level;

import melting.Environment;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.magnesiumCorrections.Owczarzy08MagnesiumCorrection;

public class Owczarzy08MixedNaMgCorrection extends Owczarzy08MagnesiumCorrection{
	
	private static String aFormula = "a = -0.6 / duplexLength + 0.025 x ln(Mg) + 0.0068 x ln(Mg)^2";
	private static String dFormula = "d = ln(Mg) + 0.38 x ln(Mg)^2";
	private static String gFormula = "g = a + b / (duplexLength^2)";

	
	@Override
	public ThermoResult correctMeltingResults(Environment environment) {
		
		double monovalent = environment.getNa() + environment.getK() + environment.getTris() / 2;
		double square = Math.log(monovalent) * Math.log(monovalent);
		double cube = Math.log(monovalent) * Math.log(monovalent) * Math.log(monovalent);
		
		this.a = 3.92 / 100000 * (0.843 - 0.352 * Math.sqrt(monovalent) * Math.log(monovalent));
		this.d = 1.42 / 100000 * (1.279 - 4.03 / 1000 * Math.log(monovalent) - 8.03 / 1000 * square);
		this.g = 8.31 / 100000 * (0.486 - 0.258 * Math.log(monovalent) + 5.25 / 1000 * cube);
		
		double Tm = super.correctTemperature(environment);
		environment.setResult(Tm);
		return environment.getResult();
	}

	@Override
	protected void displayVariable(){
		OptionManagement.meltingLogger.log(Level.FINE, aFormula);
		OptionManagement.meltingLogger.log(Level.FINE, dFormula);
		OptionManagement.meltingLogger.log(Level.FINE, gFormula);
	}
}
