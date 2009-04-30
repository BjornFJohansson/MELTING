package melting.cricksNNMethods;

import java.util.HashMap;

import melting.Helper;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Santalucia04 extends CricksNNMethod {

	/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */
	
	public Santalucia04() {
		super("Santalucia2004nn.xml");
	}
	
	public boolean isApplicable(HashMap<String, String> options) {
		boolean isApplicable = isApplicable(options);
		String hybridization = options.get(OptionManagement.hybridization);
		
		
		if (hybridization.equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : The thermodynamic parameters of Santalucia (2004)" +
					"are established for DNA sequences ");
		}
		return isApplicable;
	}
	
	public ThermoResult calculateInitiationHybridation(HashMap<String, String> options, ThermoResult result){
		Thermodynamics parameter = new Thermodynamics(0,0);
		String seq1 = "";
		String complementarySeq = "";
		int duplexLength = Math.min(seq1.length(), complementarySeq.length());
		int numberParameter = 0;
		
		result = super.calculateInitiationHybridation(options, result);
		
		if ((seq1.charAt(0) == 'A' || seq1.charAt(0) == 'T') && Helper.isComplementaryBasePair(seq1.charAt(0), complementarySeq.charAt(0))) {
			parameter = this.collector.getInitiation("per_A/T");
			numberParameter++;
		}
		
		if ((seq1.charAt(duplexLength) == 'A' || seq1.charAt(duplexLength) == 'T') && Helper.isComplementaryBasePair(seq1.charAt(duplexLength), complementarySeq.charAt(duplexLength))) {
			parameter = this.collector.getInitiation("per_A/T");
			numberParameter++;
		}
		
		result.setEnthalpy(result.getEnthalpy() + numberParameter * parameter.getEnthalpy());
		result.setEntropy(result.getEntropy() + numberParameter * parameter.getEntropy());
		
		return result;
		
	}

}
