package melting.singleMismatchMethods;

import java.util.HashMap;

import melting.DataCollect;
import melting.Helper;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;

public class AllawiSantaluciaPeyret97_98_99mm implements PartialCalculMethod{

	private DataCollect collector;
	
	public AllawiSantaluciaPeyret97_98_99mm(){
		Helper.loadData("AllawiSantaluciaPeyret1997_1998_1999mm.xml", this.collector);
	}

	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		
		String seq1 = "";
		String complementarySeq = "";
		Thermodynamics parameter = new Thermodynamics(0,0);
		 
		for (int i = pos1; i <= pos2 - 2; i++){
			seq1 = seq.substring(i, i+2);
			complementarySeq = seq2.substring(i, i+2);
			parameter = collector.getMismatchvalue(seq1, complementarySeq);
			
			result.setEnthalpy(result.getEnthalpy() + parameter.getEnthalpy());
			result.setEntropy(result.getEntropy() + parameter.getEntropy());
		}
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1, int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = true;
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (hybridization.equals("dnadna") == false){
			System.out.println("WARNING : the single mismatches parameter of " +
					"Allawi, Santalucia and Peyret are originally established " +
					"for DNA duplexes.");
			
			isApplicable = false;
		}
		
		if (isMissingParameters(seq1, seq2, pos1, pos2)){
			System.out.println("WARNING : some thermodynamic parameters are missing.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(String seq1, String seq2, int pos1,
			int pos2) {
		String seq = "";
		String complementarySeq = "";
	
		for (int i = 0; i < pos2 - 2; i++){
			seq = seq1.substring(i, i+2);
			complementarySeq = seq2.substring(i, i+2);
			
			if (this.collector.getMismatchvalue(seq, complementarySeq) == null){
				return true;
			}
		}
		return false;
	}
	
}
