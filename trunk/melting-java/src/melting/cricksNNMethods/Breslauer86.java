package melting.cricksNNMethods;

import java.util.HashMap;

import melting.configuration.OptionManagement;

public class Breslauer86 extends GlobalInitiationNNMethod{

	/* (1986). Proc Natl Acad Sci USA 83 : 3746-3750 */
	
	public Breslauer86() {
		super("Breslauer1986nn.xml");
	}	
	
	public boolean isApplicable(HashMap<String, String> options, int pos1, int pos2) {
		boolean isApplicable = isApplicable(options, pos1, pos2);
		String hybridization = options.get(OptionManagement.hybridization);
		
		
		if (hybridization.equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : The thermodynamic parameters of Breslauer et al (1986)" +
					"are established for DNA sequences ");
		}
		return isApplicable;
	}
}
