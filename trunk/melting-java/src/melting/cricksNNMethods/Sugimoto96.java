package melting.cricksNNMethods;

import java.util.HashMap;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Sugimoto96 extends CricksNNMethod {
	
	/*Sugimoto et al. (1996). Nuc Acids Res 24 : 4501-4505*/ 

	public Sugimoto96() {
		super("Sugimoto1996nn.xml");
	}
	
	public boolean isApplicable(HashMap<String, String> options, int pos1, int pos2) {
		boolean isApplicable = isApplicable(options, pos1, pos2);
		String hybridization = options.get(OptionManagement.hybridization);
		
		
		if (hybridization.equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : The thermodynamic parameters of Sugimoto et al (1996)" +
					"are established for DNA sequences ");
		}
		return isApplicable;
	}
	
	public ThermoResult calculateInitiationHybridation(HashMap<String, String> options, ThermoResult result){
		return super.calculateInitiationHybridation(options, result);
	}

}
