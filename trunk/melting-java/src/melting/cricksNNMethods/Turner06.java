package melting.cricksNNMethods;

import java.util.HashMap;

import melting.Helper;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Turner06 extends CricksNNMethod {

	public Turner06() {
		super("Turner2006nn.xml");
	}
	
	public boolean isApplicable(HashMap<String, String> options, int pos1, int pos2) {
		boolean isApplicable = isApplicable(options, pos1, pos2);
		String hybridization = options.get(OptionManagement.hybridization);
		
		if (hybridization.equals("mrnarna") == false){
			isApplicable = false;
			System.out.println("WARNING : The thermodynamic parameters of Turner et al. (2006)" +
					"are established for 2-O-methylRNA/RNA sequences ");
		}
		return isApplicable;
	}
	
	public ThermoResult calculateInitiationHybridation(HashMap<String, String> options, ThermoResult result){
		result = super.calculateInitiationHybridation(options, result);
		
		Thermodynamics parameter = new Thermodynamics(0,0);
		String seq1 = "";
		String complementarySeq = "";
		int duplexLength = Math.min(seq1.length(), complementarySeq.length());
		int numberParameter = 0;
		
		result = super.calculateInitiationHybridation(options, result);
		
		if ((seq1.charAt(0) == 'A' || seq1.charAt(0) == 'U') && Helper.isComplementaryBasePair(seq1.charAt(0), complementarySeq.charAt(0))) {
			parameter = this.collector.getTerminal("per_A/U");
			numberParameter++;
		}
		
		if ((seq1.charAt(duplexLength) == 'A' || seq1.charAt(duplexLength) == 'U') && Helper.isComplementaryBasePair(seq1.charAt(duplexLength), complementarySeq.charAt(duplexLength))) {
			numberParameter++;
		}
		
		result.setEnthalpy(result.getEnthalpy() + numberParameter * parameter.getEnthalpy());
		result.setEntropy(result.getEntropy() + numberParameter * parameter.getEntropy());
		
		return result;
	}

}
