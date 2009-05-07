package melting.longBulgeMethod;

import java.util.HashMap;

import melting.Helper;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Turner99_06LongBulgeLoop extends PartialCalcul{

	/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924. 
	REF: Douglas M Turner et al (1999). J.Mol.Biol.  288: 911_940.*/ 
	
	public Turner99_06LongBulgeLoop(){
		Helper.loadData("Turner1999_2006longbulge.xml", this.collector);
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		Thermodynamics bulge = this.collector.getInitiationBulgevalue(bulgeSize);
		
		result.setEnthalpy(result.getEnthalpy() + bulge.getEnthalpy());
		result.setEntropy(result.getEntropy() + bulge.getEntropy());
		
		if ((seq.charAt(pos1) == 'A' || seq.charAt(pos1) == 'U') && Helper.isComplementaryBasePair(seq.charAt(pos1), seq2.charAt(pos1))){
			Thermodynamics closure = this.collector.getClosureValue("A", "T");
			
			result.setEnthalpy(result.getEnthalpy() + closure.getEnthalpy());
			result.setEntropy(result.getEntropy() + closure.getEntropy());
		}
		
		if ((seq.charAt(pos2) == 'A' || seq.charAt(pos2) == 'U') && Helper.isComplementaryBasePair(seq.charAt(pos2), seq2.charAt(pos2))){
			Thermodynamics closure = this.collector.getClosureValue("A", "T");
			
			result.setEnthalpy(result.getEnthalpy() + closure.getEnthalpy());
			result.setEntropy(result.getEntropy() + closure.getEntropy());
		}
		
		if ((seq.charAt(pos1) == 'G' && seq2.charAt(pos1) == 'U') || (seq.charAt(pos1) == 'U' && seq2.charAt(pos1) == 'G')){
			Thermodynamics closure = this.collector.getClosureValue("G", "U");
			
			result.setEnthalpy(result.getEnthalpy() + closure.getEnthalpy());
			result.setEntropy(result.getEntropy() + closure.getEntropy());
		}
		
		if ((seq.charAt(pos2) == 'G' && seq2.charAt(pos2) == 'U') || (seq.charAt(pos2) == 'U' && seq2.charAt(pos2) == 'G')){
			Thermodynamics closure = this.collector.getClosureValue("G", "U");
			
			result.setEnthalpy(result.getEnthalpy() + closure.getEnthalpy());
			result.setEntropy(result.getEntropy() + closure.getEntropy());
		}
		
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = super.isApplicable(options, pos1, pos2);
		
		if (hybridization.equals("rnarna") == false){
			System.out.println("WARNING : the single bulge loop parameters of " +
					"Turner (1999-2006) are originally established " +
					"for RNA sequences.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		if (((seq1.charAt(pos1) == 'A' || seq1.charAt(pos1) == 'U') && Helper.isComplementaryBasePair(seq1.charAt(pos1), seq2.charAt(pos1))) || ((seq1.charAt(pos2) == 'A' || seq1.charAt(pos2) == 'U') && Helper.isComplementaryBasePair(seq1.charAt(pos2), seq2.charAt(pos2)))){
			if (this.collector.getClosureValue("A", "U") == null){
				return true;
			}
		}
		
		if ((seq1.charAt(pos1) == 'G' && seq2.charAt(pos1) == 'U') || (seq1.charAt(pos1) == 'U' && seq2.charAt(pos1) == 'G') || (seq1.charAt(pos2) == 'G' && seq2.charAt(pos2) == 'U') || (seq1.charAt(pos2) == 'U' && seq2.charAt(pos2) == 'G')){
			if (this.collector.getClosureValue("G", "U") == null){
				return true;
			}
		}
		
		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		if (this.collector.getBulgeLoopvalue(bulgeSize) == null){
			return true;
		}
		return super.isMissingParameters(seq1, seq2, pos1, pos2);
	}
}
