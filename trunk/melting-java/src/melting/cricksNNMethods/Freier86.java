package melting.cricksNNMethods;

import java.util.HashMap;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Freier86 extends CricksNNMethod {
	
	/*Freier et al (1986) Proc Natl Acad Sci USA 83: 9373-9377 */

	public Freier86() {
		super("Freier1986nn.xml");
	}
	
	public boolean isApplicable(HashMap<String, String> options, int pos1, int pos2) {
		boolean isApplicable = isApplicable(options, pos1, pos2);
		String hybridization = options.get(OptionManagement.hybridization);
		
		
		if (hybridization.equals("rnarna") == false){
			isApplicable = false;
			System.out.println("WARNING : The thermodynamic parameters of Freier et al. (1986)" +
					"are established for RNA/RNA sequences ");
		}
		return isApplicable;
	}
	
	public ThermoResult calculateInitiationHybridation(HashMap<String, String> options, ThermoResult result){
		return super.calculateInitiationHybridation(options, result);
	}

}
