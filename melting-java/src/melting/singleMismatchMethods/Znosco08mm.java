package melting.singleMismatchMethods;

import java.util.HashMap;

import melting.Helper;
import melting.configuration.OptionManagement;

public class Znosco08mm extends ZnoscoMethod {

	/*REF: Brent M Znosko et al (2008). Biochemistry 47: 10178-10187.*/
	
	public Znosco08mm() {
		Helper.loadData("Znosco2008mm.xml", this.collector);
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(options, pos1, pos2);
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if ((seq1.charAt(pos1) != 'G' && seq1.charAt(pos1) != 'U') || (seq1.charAt(pos2) != 'G' && seq1.charAt(pos2) != 'U')){
			isApplicable = false;
		}
		else if ((seq1.charAt(pos1) == 'G' && seq2.charAt(pos1) != 'U') || (seq1.charAt(pos2) == 'U' && seq2.charAt(pos2) != 'G')){
			isApplicable = false;
		}
		
		if (isApplicable == false){
			System.out.println("WARNING : The thermodynamic parameters of Znosco (2008)" +
					"are originally established for single mismatches with GU nearest neighbors.");
		}
		
		return isApplicable;
	}
}
