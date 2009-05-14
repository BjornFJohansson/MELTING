package melting.cricksNNMethods;

import melting.Environment;
import melting.ThermoResult;

public class Xia98 extends CricksNNMethod {

	/*Xia et al (1998) Biochemistry 37: 14719-14735 */
	
	public static String defaultFileName = "Xia1998nn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		boolean isApplicable = isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("rnarna") == false){
			if (environment.getHybridization().equals("mrnarna") == false){
				isApplicable = false;
			}
			System.out.println("WARNING : It is possible to use the thermodynamic parameters of Xia et al. (1998)" +
					"for 2_O methyl RNA dulexes but these parameters are originally established for RNA/RNA sequences.");
		}
		return isApplicable;
	}
	
	public ThermoResult calculateInitiationHybridation(Environment environment){
		environment.setResult(calculateInitiationHybridation(environment));
		
		int numberTerminalAU = environment.getSequences().calculateNumberOfTerminal('A', 'U');
		double enthalpy = 0;
		double entropy = 0;
		
		if (numberTerminalAU != 0) {
			enthalpy += numberTerminalAU * this.collector.getTerminal("per_A/U").getEnthalpy();
			entropy += numberTerminalAU * this.collector.getTerminal("per_A/U").getEntropy();
		}
		
		environment.setResult(enthalpy, entropy);
		
		return environment.getResult();
	}

}
