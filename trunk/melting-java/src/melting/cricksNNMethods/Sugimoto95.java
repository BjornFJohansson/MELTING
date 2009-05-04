package melting.cricksNNMethods;

import java.util.HashMap;

import melting.configuration.OptionManagement;

public class Sugimoto95 extends CricksNNMethod {

	public Sugimoto95() {
		super("Sugimoto1995nn.xml");
	}
	
	public boolean isApplicable(HashMap<String, String> options, int pos1, int pos2) {
		boolean isApplicable = isApplicable(options, pos1, pos2);
		String hybridization = options.get(OptionManagement.hybridization);
		
		
		if (hybridization.equals("dnarna") == false || hybridization.equals("rnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : The thermodynamic parameters of Sugimoto et al. (1995)" +
					"are established for hybrid DNA/RNA sequences ");
		}
		
		if (options.containsKey(OptionManagement.selfComplementarity)){
			isApplicable = false;
			System.out.println("ERROR : The thermodynamic parameters of Sugimoto et al. (1995)" +
					"are established for hybrid DNA/RNA sequences and they can't be self complementary sequence.");
		}
		return isApplicable;
	}
}
