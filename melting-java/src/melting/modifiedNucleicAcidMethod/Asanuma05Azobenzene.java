package melting.modifiedNucleicAcidMethod;

import java.util.HashMap;

import melting.Helper;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Asanuma05Azobenzene extends PartialCalcul{

	/*Asanuma et al. (2005). Nucleic acids Symposium Series 49 : 35-36 */

	public Asanuma05Azobenzene(){
		Helper.loadData("Asanuma2005azobenmn.xml", this.collector);
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		Thermodynamics parameter = this.collector.getAzobenzeneValue(seq.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1));
		
		result.setEnthalpy(result.getEnthalpy() + parameter.getEnthalpy());
		result.setEntropy(result.getEntropy() + parameter.getEntropy());
		
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = super.isApplicable(options, pos1, pos2);
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (hybridization.equals("dnadna") == false) {
			System.err.println("WARNING : The thermodynamic parameters for azobenzene of" +
					"Asanuma (2005) are established for DNA sequences.");
			isApplicable = false;
		}
		
		if ((pos1 == 0 && (seq1.charAt(pos1) == 'X' || seq2.charAt(pos1) == 'X')) || (pos2 == Math.min(seq1.length(), seq2.length()) && (seq1.charAt(pos2) == 'X' || seq2.charAt(pos2) == 'X'))){
			System.err.println("WARNING : The thermodynamics parameters for azobenzene of " +
					"Asanuma (2005) are not established for terminal benzenes.");
			isApplicable = false;
		}
		return isApplicable;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		
		if (this.collector.getAzobenzeneValue(seq1.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1)) == null){
			return true;
		}
		return super.isMissingParameters(seq1, seq2, pos1, pos2);
	}

}
