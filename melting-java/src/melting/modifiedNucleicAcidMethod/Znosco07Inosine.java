package melting.modifiedNucleicAcidMethod;


import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;

public class Znosco07Inosine extends InosineNNMethod {

	/*Brent M Znosko et al. (2005). Biochemistry 46 : 4625-4634 */
	
	public Znosco07Inosine(){
		this.fileName = "Znosco2007inomn.xml";
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		result = super.calculateThermodynamics(sequences, pos1, pos2, result);
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		NucleotidSequences inosine = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		int numberIU = inosine.calculateNumberOfTerminal('I', 'U');
		
		if ((pos1 == 0 || pos2 == inosine.getDuplexLength() - 1) && numberIU > 0) {
			enthalpy += numberIU * this.collector.getTerminal("per_I/U").getEnthalpy();
			entropy += numberIU * this.collector.getTerminal("per_I/U").getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}
	
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("rnarna") == false) {
			System.err.println("WARNING : The thermodynamic parameters for inosine base of" +
					"Znosco (2007) are established for RNA sequences.");
			isApplicable = false;
		}
		
		for (int i = pos1; i < pos2; i++){
			if (environment.getSequences().isBasePairEqualsTo('I', 'U', i) == false){
				isApplicable = false;
				
				System.out.println("WARNING : The thermodynamic parameters of Znosco" +
						"(2007) are only established for IU base pairs.");
				break;
			}
		}
		return isApplicable;
	}
	
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences inosine = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		int numberIU = inosine.calculateNumberOfTerminal('I', 'U');
		
		if ((pos1 == 0 || pos2 == inosine.getDuplexLength() - 1) && numberIU > 0){
			if (this.collector.getTerminal("per_I/U") == null){
				return true;
			}
		}
		return false;
	}
	
}
