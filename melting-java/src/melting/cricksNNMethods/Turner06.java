package melting.cricksNNMethods;


import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;

public class Turner06 extends CricksNNMethod {
	
	public static String defaultFileName = "Turner2006nn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		 
		for (int i = pos1; i <= pos2 - 1; i++){
			enthalpy += this.collector.getNNvalue("m" + sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)).getEnthalpy();
			entropy += this.collector.getNNvalue("m" + sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)).getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		boolean isApplicable = isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("mrnarna") == false){
			if (environment.getHybridization().equals("rnarna") == false){
				isApplicable = false;
			}
			System.out.println("WARNING : It is possible to use the thermodynamic parameters of Turner et al. (2006)" +
					"for RNA dulexes but these parameters are originally established for 2-O-methylRNA/RNA sequences.");
		}
		return isApplicable;
	}
	
	public ThermoResult calculateInitiationHybridation(Environment environment){
		environment.setResult(super.calculateInitiationHybridation(environment));
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
