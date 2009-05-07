package melting.modifiedNucleicAcidMethod;

import java.util.HashMap;

import melting.Helper;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Sugimoto01Hydroxyadenine extends PartialCalcul{

	/*Sugimoto et al.(2001). Nucleic acids research 29 : 3289-3296*/
	
	public Sugimoto01Hydroxyadenine(){
		Helper.loadData("Sugimoto2001hydroxyAmn.xml", this.collector);
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		Thermodynamics increment = this.collector.getHydroxyadenosineValue(seq.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1));
		
		result = calculateThermodynamicsNoModifiedAcid(seq, seq2, pos1, pos2, result);
		
		result.setEnthalpy(result.getEnthalpy() + increment.getEnthalpy());
		result.setEntropy(result.getEntropy() + increment.getEntropy());
			
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = super.isApplicable(options, pos1, pos2);
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (hybridization.equals("dnadna") == false) {
			System.err.println("WARNING : The thermodynamic parameters for 2-hydroxyadenine base of" +
					"Sugimoto (2001) are established for DNA sequences.");
			isApplicable = false;
		}
		
		if (pos1 == 0 || pos2 == Math.min(seq1.length(), seq2.length())){
			if (seq1.contains("A*")){
				if (seq1.substring(pos1, pos2 + 1).equals("CTA*A")== false && seq1.substring(pos1, pos2 + 1).equals("TGA*C") == false){
					isApplicable = false;
					System.err.println("WARNING : The thermodynamic parameters for 2-hydroxyadenine terminal base of" +
					"Sugimoto (2001) are established for CTA*A/CT or TGA*C/GG sequences.");
				}
				else if ((seq1.substring(pos1, pos2 + 1).equals("CTA*A")== true && seq2.contains("CT") == false) || (seq1.substring(pos1, pos2 + 1).equals("TGA*C") == true && seq2.contains("GG") == false)){
					isApplicable = false;
					System.err.println("WARNING : The thermodynamic parameters for 2-hydroxyadenine terminal base of" +
					"Sugimoto (2001) are established for CTA*A/CT or TGA*C/GG sequences.");
				}
			}
			else if (seq2.contains("A*")){
				if (seq2.substring(pos1, pos2 + 1).equals("CTA*A")== false && seq2.substring(pos1, pos2 + 1).equals("TGA*C") == false){
					isApplicable = false;
					System.err.println("WARNING : The thermodynamic parameters for 2-hydroxyadenine terminal base of" +
					"Sugimoto (2001) are established for CTA*A/CT or TGA*C/GG sequences.");
				}
				else if ((seq2.substring(pos1, pos2 + 1).equals("CTA*A")== true && seq1.contains("CT") == false) || (seq2.substring(pos1, pos2 + 1).equals("TGA*C") == true && seq1.contains("GG") == false)){
					isApplicable = false;
					System.err.println("WARNING : The thermodynamic parameters for 2-hydroxyadenine terminal base of" +
					"Sugimoto (2001) are established for CTA*A/CT or TGA*C/GG sequences.");
				}
			}
			
			else {
				if ((seq1.charAt(pos1) != 'T' && seq1.charAt(pos1) != 'G') || (seq1.charAt(pos2) != 'A' && seq1.charAt(pos2) != 'C')){
					isApplicable = false;
					System.err.println("WARNING : The thermodynamic parameters for 2-hydroxyadenine base of" +
					"Sugimoto (2001) are established for TA*A/ANT or GA*C/CNG sequences.");
				}
				else {
					if (Helper.isComplementaryBasePair(seq1.charAt(pos1), seq2.charAt(pos1)) == false || Helper.isComplementaryBasePair(seq1.charAt(pos2), seq2.charAt(pos2)) == false){
						isApplicable = false;
						System.err.println("WARNING : The thermodynamic parameters for 2-hydroxyadenine base of" +
						"Sugimoto (2001) are established for TA*A/ANT or GA*C/CNG sequences.");
					}
				}
			}
		}				
		
		return isApplicable;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
			
		if (this.collector.getHydroxyadenosineValue(seq1.substring(pos1, pos2 + 1), seq2.substring(pos1, pos2 + 1)) == null){
			return true;
		}
		return super.isMissingParameters(seq1, seq2, pos1, pos2);	
	}
	
	private ThermoResult calculateThermodynamicsNoModifiedAcid(String seq, String seq2,
			int pos1, int pos2, ThermoResult result){
		
		if (pos1 != 0 && pos2 != Math.min(seq.length(), seq2.length())){
			if (seq.substring(pos1, pos2 + 1).contains("A*")){
				seq.substring(pos1, pos2 + 1).replace("A*", "A");
				seq2.substring(pos1, pos2 + 1).replace(seq2.charAt(pos1 + 1), 'T');	
			}
			
			else if (seq2.substring(pos1, pos2 + 1).contains("A*")){
				seq2.substring(pos1, pos2 + 1).replace("A*", "A");
				seq.substring(pos1, pos2 + 1).replace(seq.charAt(pos1 + 1), 'T');	
			}
			Thermodynamics parameter1 = this.collector.getNNvalue(seq.substring(pos1, pos2), seq2.substring(pos1, pos2));
			Thermodynamics parameter2 = this.collector.getNNvalue(seq.substring(pos1 + 1, pos2 + 1), seq2.substring(pos1 + 1, pos2 + 1));
			
			
			result.setEnthalpy(result.getEnthalpy() + parameter1.getEnthalpy() + parameter2.getEnthalpy());
			result.setEntropy(result.getEntropy() + parameter1.getEntropy() + parameter2.getEntropy());
			return result;
		}
		else{
			return null;
		}
	}

}
