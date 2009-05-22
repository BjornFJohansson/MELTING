package melting.approximativeMethods;


import melting.ThermoResult;

public class MarmurSchildkrautDoty extends ApproximativeMode{

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
	
	public ThermoResult CalculateThermodynamics() {
		double Tm = 81.5 + 16.6 * Math.log10(this.environment.getNa()) + 0.41 * this.environment.getSequences().calculatePercentGC() - 675 / this.environment.getSequences().getDuplexLength();

		this.environment.setResult(Tm);
		
		return this.environment.getResult();
	}

	public boolean isApplicable() {
		boolean isApplicable = super.isApplicable();
		
		if (this.environment.getHybridization().equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : the Marmur-Schildkraut-Doty equation" +
					"was originally established for DNA duplexes.");
		}
		
		return isApplicable;
	}
	
}
