package melting.modifiedNucleicAcidMethod;

import java.util.HashMap;

import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Znosco07Inosine extends InosineNNMethod {

	/*Brent M Znosko et al. (2005). Biochemistry 46 : 4625-4634 */
	
	public Znosco07Inosine(){
		loadData("Znosco2007inomn.xml", this.collector);
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		result = super.calculateThermodynamics(seq, seq2, pos1, pos2, result);
		Thermodynamics terminalGU = new Thermodynamics(0,0);
		
		if (pos1 == 0 && ((seq.charAt(pos1) == 'I' && seq2.charAt(pos1) == 'U') || (seq2.charAt(pos1) == 'I' && seq.charAt(pos1) == 'U'))) {
			terminalGU = this.collector.getTerminal("per_I/U");
			result.setEnthalpy(result.getEnthalpy() + terminalGU.getEnthalpy());
			result.setEntropy(result.getEntropy() + terminalGU.getEntropy());
		}
		
		if (pos2 == Math.min(seq.length(), seq2.length()) && ((seq.charAt(pos2) == 'I' && seq2.charAt(pos2) == 'U') || (seq2.charAt(pos2) == 'I' && seq.charAt(pos2) == 'U'))) {
			terminalGU = this.collector.getTerminal("per_I/U");
			result.setEnthalpy(result.getEnthalpy() + terminalGU.getEnthalpy());
			result.setEntropy(result.getEntropy() + terminalGU.getEntropy());
		}
		
		return result;
	}
	
	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = super.isApplicable(options, pos1, pos2);
		String seq = options.get(OptionManagement.sequence);
		String complementarySeq = options.get(OptionManagement.complementarySequence); 
		
		if (hybridization.equals("rnarna") == false) {
			System.err.println("WARNING : The thermodynamic parameters for inosine base of" +
					"Znosco (2007) are established for RNA sequences.");
			isApplicable = false;
		}
		
		for (int i = pos1; i < pos2; i++){
			if ((seq.charAt(i) == 'I' && complementarySeq.charAt(i) != 'U') || (complementarySeq.charAt(i) == 'I' && seq.charAt(i) != 'U')){
				isApplicable = false;
				
				System.out.println("WARNING : The thermodynamic parameters of Znosco" +
						"(2007) are only established for IU base pairs.");
				break;
			}
		}
		return isApplicable;
	}
	
	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		if (pos1 == 0 && ((seq1.charAt(pos1) == 'I' && seq2.charAt(pos1) == 'U') || (seq2.charAt(pos1) == 'I' && seq1.charAt(pos1) == 'U'))){
			if (this.collector.getTerminal("per_I/U") == null){
				return true;
			}
		}
		
		if (pos2 == Math.min(seq1.length(), seq2.length()) && ((seq1.charAt(pos2) == 'I' && seq2.charAt(pos2) == 'U') || (seq2.charAt(pos2) == 'I' && seq1.charAt(pos2) == 'U'))){
			if (this.collector.getTerminal("per_I/U") == null){
				return true;
			}
		}
		return false;
	}
	
}
