package melting.cricksNNMethods;

import java.util.HashMap;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class AllawiSantalucia97 extends DecomposedInitiationNNMethod {

	/*Allawi and SantaLucia (1997). Biochemistry 36 : 10581-10594 */
	
	public AllawiSantalucia97() {
		super("AllawiSantalucia1997nn.xml");
	}
	
	public boolean isApplicable(HashMap<String, String> options, int pos1, int pos2) {
		boolean isApplicable = isApplicable(options, pos1, pos2);
		String hybridization = options.get(OptionManagement.hybridization);
		
		
		if (hybridization.equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : The thermodynamic parameters of Allawi and Santalucia (1997)" +
					"are established for DNA sequences ");
		}
		return isApplicable;
	}
	
	public ThermoResult calculateInitiationHybridation(HashMap<String, String> options, ThermoResult result){
		return super.calculateInitiationHybridation(options, result);
	}

}
