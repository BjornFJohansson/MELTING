/*James G. Wetmur, "DNA Probes : applications of the principles of nucleic acid hybridization",
	*1991, Critical reviews in biochemistry and molecular biology, 26, 227-259
	
	 * Marmur J, Doty P, "Determination of the base composition of 
	 * deoxyribonucleic acid from its thermal denaturation temperature", 
	 * 1962, Journal of molecular biology, 5, 109-118.
	  
	 * Chester N, Marshak DR, "dimethyl sulfoxide-mediated primer Tm reduction : 
	 * a method for analyzing the role of renaturation temperature in the polymerase 
	 * chain reaction", 1993, Analytical Biochemistry, 209, 284-290.
	 
	 * Schildkraut C, Lifson S, "Dependance of the melting temperature of DNA on salt 
	 * concentration", 1965, Biopolymers, 3, 95-110.
	 
	 *  Wahl GM, Berger SL, Kimmel AR. Molecular hybridization of
     *  immobilized nucleic acids: theoretical concepts and practical
     *  considerations. Methods Enzymol 1987;152:399 – 407.
                  
     *  Britten RJ, Graham DE, Neufeld BR. Analysis of repeating DNA
     *  sequences by reassociation. Methods Enzymol 1974;29:363–418.
	         
	 *  Hall TJ, Grula JW, Davidson EH, Britten RJ. Evolution of sea urchin
     *  non-repetitive DNA. J Mol Evol 1980;16:95–110.
	 */

package melting.approximativeMethods;

import java.util.logging.Level;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class MarmurSchildkrautDoty extends ApproximativeMode{
	
	private static String temperatureEquation = "Tm = 81.5 + 16.6 * log10(Na) + 0.41 * percentGC - 675 / duplexLength.";
	
	@Override
	public ThermoResult calculateThermodynamics() {
		double Tm = super.calculateThermodynamics().getTm();
		Tm = 81.5 + 16.6 * Math.log10(this.environment.getNa()) + 0.41 * this.environment.getSequences().calculatePercentGC() - 675.0 / this.environment.getSequences().getDuplexLength();

		this.environment.setResult(Tm);
		
		OptionManagement.meltingLogger.log(Level.FINE, " from Marmur, Schildkraut and Doty (1965 - 1993)");
		OptionManagement.meltingLogger.log(Level.FINE, temperatureEquation);
		
		return this.environment.getResult();
	}

	@Override
	public boolean isApplicable() {
		boolean isApplicable = super.isApplicable();
		
		if (environment.getSequences().getPercentMismatching() != 0){
			isApplicable = false;
		}
		
		if (this.environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "the Marmur-Schildkraut-Doty equation" +
					"was originally established for DNA duplexes.");
		}
		
		return isApplicable;
	}
	
}
