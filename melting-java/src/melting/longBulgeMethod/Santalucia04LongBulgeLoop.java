package melting.longBulgeMethod;

import java.util.HashMap;

import melting.Helper;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Santalucia04LongBulgeLoop extends PartialCalcul{

/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */
	
	public Santalucia04LongBulgeLoop(){
		Helper.loadData("Santalucia2004longbulge.xml", this.collector);
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		String bulgeSize = Integer.toString(Math.abs(pos2 - pos1) - 1);
		Thermodynamics bulge = this.collector.getBulgeLoopvalue(bulgeSize);
		
		result.setEntropy(result.getEntropy() + bulge.getEntropy());
		
		if ((seq.charAt(pos1) == 'A' || seq.charAt(pos1) == 'T') && Helper.isComplementaryBasePair(seq.charAt(pos1), seq2.charAt(pos1))){
			Thermodynamics closure = this.collector.getClosureValue("A", "T");
			
			result.setEnthalpy(result.getEnthalpy() + closure.getEnthalpy());
			result.setEntropy(result.getEntropy() + closure.getEntropy());
		}
		
		if ((seq.charAt(pos2) == 'A' || seq.charAt(pos2) == 'T') && Helper.isComplementaryBasePair(seq.charAt(pos2), seq2.charAt(pos2))){
			Thermodynamics closure = this.collector.getClosureValue("A", "T");
			
			result.setEnthalpy(result.getEnthalpy() + closure.getEnthalpy());
			result.setEntropy(result.getEntropy() + closure.getEntropy());
		}
		
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = super.isApplicable(options, pos1, pos2);
		
		if (hybridization.equals("dnadna") == false){
			System.out.println("WARNING : the single bulge loop parameters of " +
					"Santalucia (2004) are originally established " +
					"for DNA sequences.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		
		if (((seq1.charAt(pos1) == 'A' || seq1.charAt(pos1) == 'T') && Helper.isComplementaryBasePair(seq1.charAt(pos1), seq2.charAt(pos1))) || ((seq1.charAt(pos2) == 'A' || seq1.charAt(pos2) == 'T') && Helper.isComplementaryBasePair(seq1.charAt(pos2), seq2.charAt(pos2)))){
			if (this.collector.getClosureValue("A", "T") == null){
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
