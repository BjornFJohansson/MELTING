package melting.cricksNNMethods;

import melting.Environment;
import melting.ThermoResult;

public class Santalucia04 extends CricksNNMethod {

	/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */
	
	public Santalucia04() {
		this.fileName = "Santalucia2004nn.xml";
	}
	
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		boolean isApplicable = isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : The thermodynamic parameters of Santalucia (2004)" +
					"are established for DNA sequences ");
		}
		return isApplicable;
	}
	
	public ThermoResult calculateInitiationHybridation(Environment environment){
		
		environment.setResult(super.calculateInitiationHybridation(environment));
		int numberTerminalAT = environment.getSequences().calculateNumberOfTerminal('A', 'T');
		double enthalpy = 0;
		double entropy = 0;
		
		if (numberTerminalAT != 0){
			enthalpy += numberTerminalAT * this.collector.getTerminal("per_A/T").getEnthalpy();
			entropy += numberTerminalAT * this.collector.getTerminal("per_A/T").getEntropy();
		}
		
		environment.setResult(enthalpy, entropy);
		
		return environment.getResult();
		
	}

}
