package melting.tandemMismatchMethod;

import java.util.HashMap;

import melting.DataCollect;
import melting.Helper;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public class AllawiSantaluciaPeyret97_98_99tanmm implements PartialCalculMethod{

	/*REF: Allawi and SantaLucia (1997). Biochemistry 36: 10581-10594. 
	REF: Allawi and SantaLucia (1998). Biochemistry 37: 2170-2179.
	REF: Allawi and SantaLucia (1998). Nuc Acids Res 26: 2694-2701. 
	REF: Allawi and SantaLucia (1998). Biochemistry 37: 9435-9444.
	REF: Peyret et al. (1999). Biochemistry 38: 3468-3477*/
	
	private DataCollect collector;
	
	public AllawiSantaluciaPeyret97_98_99tanmm(){
		Helper.loadData("AllawiSantaluciaPeyret1997_1998_1999tanmm.xml", this.collector);
	}
	
	public DataCollect getCollector(){
		return this.collector;
	}
	
	public ThermoResult calculateThermodynamics(String seq, String seq2,
			int pos1, int pos2, ThermoResult result) {
		String seq1 ="";
		String complementarySeq = "";
		Thermodynamics parameter = new Thermodynamics(0,0);
		
		for (int i = pos1; i < pos2; i++){
		seq1 = seq1.substring(i, i+2);
		complementarySeq = seq2.substring(i, i+2);
		parameter = collector.getMismatchvalue(seq1, complementarySeq);
			
		result.setEnthalpy(result.getEnthalpy() + parameter.getEnthalpy());
		result.setEntropy(result.getEntropy() + parameter.getEntropy());
		}
		return result;
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = true;
		String seq1 = options.get(OptionManagement.sequence);
		String seq2 = options.get(OptionManagement.complementarySequence);
		
		if (hybridization.equals("dnadna") == false){
			System.out.println("WARNING : the tandem mismatch parameters of " +
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

		String seq = seq1.substring(pos1, pos2+1);
		String complementarySeq = seq2.substring(pos1, pos2+1);
			
		if (this.collector.getMismatchvalue(seq, complementarySeq) == null){
			return true;
		}
		return false;
	}
	
	public void loadSingleMismatchData(HashMap<String, String> optionSet,int pos1, int pos2, ThermoResult result){
		RegisterCalculMethod getData = new RegisterCalculMethod();
		PartialCalculMethod singleMismatchMethod = getData.getSingleMismatchMethod(optionSet, result, pos1, pos2);
		
		this.collector.getDatas().putAll(singleMismatchMethod.getCollector().getDatas());
	}
}