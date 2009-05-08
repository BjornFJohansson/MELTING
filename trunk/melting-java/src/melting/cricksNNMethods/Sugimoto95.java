package melting.cricksNNMethods;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;

public class Sugimoto95 extends CricksNNMethod {

	public Sugimoto95() {
		super("Sugimoto1995nn.xml");
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		 
		for (int i = pos1; i <= pos2 - 1; i++){
			enthalpy += this.collector.getNNvalue("d" + sequences.getSequenceNNPair(i), "r" + sequences.getComplementaryNNPair(i)).getEnthalpy();
			entropy += this.collector.getNNvalue("d" + sequences.getSequenceNNPair(i), "r" + sequences.getComplementaryNNPair(i)).getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		boolean isApplicable = isApplicable(environment, pos1, pos2);		
		
		if (environment.getHybridization().equals("dnarna") == false || environment.getHybridization().equals("rnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : The thermodynamic parameters of Sugimoto et al. (1995)" +
					"are established for hybrid DNA/RNA sequences ");
		}
		
		if (environment.isSelfComplementarity()){
			isApplicable = false;
			System.out.println("ERROR : The thermodynamic parameters of Sugimoto et al. (1995)" +
					"are established for hybrid DNA/RNA sequences and they can't be self complementary sequence.");
		}
		return isApplicable;
	}
}
