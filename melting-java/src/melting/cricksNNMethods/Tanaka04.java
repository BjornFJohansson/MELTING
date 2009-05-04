package melting.cricksNNMethods;

import java.util.HashMap;

import melting.configuration.OptionManagement;

public class Tanaka04 extends DecomposedInitiationNNMethod {

	/*Tanaka Fumiaki et al (2004). Biochemistry 43 : 7143-7150 */
	
	public Tanaka04() {
		super("Tanaka2004nn.xml");
	}
	
	public boolean isApplicable(HashMap<String, String> options, int pos1, int pos2) {
		boolean isApplicable = isApplicable(options, pos1, pos2);
		String hybridization = options.get(OptionManagement.hybridization);
		
		
		if (hybridization.equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : The thermodynamic parameters of Tanaka (2004)" +
					"are established for DNA sequences ");
		}
		return isApplicable;
	}
}
