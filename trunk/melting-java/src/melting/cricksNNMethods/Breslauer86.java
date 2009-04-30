package melting.cricksNNMethods;

import java.util.HashMap;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Breslauer86 extends GlobalInitiationNNMethod{

	/* (1986). Proc Natl Acad Sci USA 83 : 3746-3750 */
	
	public Breslauer86() {
		super("Breslauer1986nn.xml");
	}	
	
	public boolean isApplicable(HashMap<String, String> options) {
		boolean isApplicable = isApplicable(options);
		String hybridization = options.get(OptionManagement.hybridization);
		
		
		if (hybridization.equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : The thermodynamic parameters of Breslauer et al (1986)" +
					"are established for DNA sequences ");
		}
		return isApplicable;
	}
	
	public ThermoResult calculateInitiationHybridation(HashMap<String, String> options, ThermoResult result){
		return super.calculateInitiationHybridation(options, result);
	}
}
