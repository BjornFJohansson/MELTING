package melting.approximativeMethods;

import melting.ThermoResult;

public class Wetmur91 extends ApproximativeMode{
	
	/*James G. Wetmur, "DNA Probes : applications of the principles of nucleic acid hybridization",
	1991, Critical reviews in biochemistry and molecular biology, 26, 227-259*/
	
	public boolean isApplicable() {

		boolean isApplicable = super.isApplicable();
		
		if (this.environment.getHybridization().equals("rnarna") == false || this.environment.getHybridization().equals("dnadna") == false || this.environment.getHybridization().equals("dnarna") == false || this.environment.getHybridization().equals("rnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : the wetmur equation was originally established for DNA, RNA or hybrid DNA/RNA duplexes.");
		}
		
		return isApplicable;
	}
	
	public ThermoResult CalculateThermodynamics() {
		int percentGC = this.environment.getSequences().calculatePercentGC();
		int percentMismatching = this.environment.getSequences().getPercentMismatching();
		int duplexLength = this.environment.getSequences().getDuplexLength();
		double Tm = 0;
		
		if (this.environment.getHybridization().equals("dnadna")){
			Tm = 81.5 + 16.6 * Math.log10(this.environment.getNa() / (1.0 + 0.7 * this.environment.getNa())) + 0.41 * percentGC - 500 / duplexLength - percentMismatching;
		}
		else if (this.environment.getHybridization().equals("rnarna")){
			Tm = 78 + 16.6 * Math.log10(this.environment.getNa() / (1.0 + 0.7 * this.environment.getNa())) + 0.7 * percentGC - 500 / duplexLength - percentMismatching;
		}
		else if (this.environment.getHybridization().equals("dnarna") ||this.environment.getHybridization().equals("rnadna")){
			Tm = 67 + 16.6 * Math.log10(this.environment.getNa() / (1.0 + 0.7 *this.environment.getNa())) + 0.8 * percentGC - 500 / duplexLength - percentMismatching;
		}
		
		this.environment.setResult(Tm);
		return this.environment.getResult();
	}
}
