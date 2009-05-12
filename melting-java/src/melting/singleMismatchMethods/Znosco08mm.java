package melting.singleMismatchMethods;

import melting.Environment;
import melting.NucleotidSequences;

public class Znosco08mm extends ZnoscoMethod {

	/*REF: Brent M Znosko et al (2008). Biochemistry 47: 10178-10187.*/
	
	public Znosco08mm() {
		loadData("Znosco2008mm.xml", this.collector);
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		NucleotidSequences mismatch = new NucleotidSequences(environment.getSequences().getSequence(pos1, pos2), environment.getSequences().getComplementary(pos1, pos2));
		
		if (mismatch.calculateNumberOfTerminal('G', 'U') == 0){
			isApplicable = false;
			System.out.println("WARNING : The thermodynamic parameters of Znosco (2008)" +
			"are originally established for single mismatches with GU nearest neighbors.");
		}
	
		return isApplicable;
	}
}
