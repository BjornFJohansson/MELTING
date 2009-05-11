package melting.modifiedNucleicAcidMethod;


import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

public class Asanuma05Azobenzene extends PartialCalcul{

	/*Asanuma et al. (2005). Nucleic acids Symposium Series 49 : 35-36 */

	public Asanuma05Azobenzene(){
		loadData("Asanuma2005azobenmn.xml", this.collector);
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		double enthalpy = result.getEnthalpy() + this.collector.getAzobenzeneValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEnthalpy();
		double entropy = result.getEnthalpy() + this.collector.getAzobenzeneValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEntropy();
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		NucleotidSequences modified = new NucleotidSequences(environment.getSequences().getSequence(pos1, pos2), environment.getSequences().getComplementary(pos1, pos2));
		
		if (environment.getHybridization().equals("dnadna") == false) {
			System.err.println("WARNING : The thermodynamic parameters for azobenzene of" +
					"Asanuma (2005) are established for DNA sequences.");
			isApplicable = false;
		}
		
		if (modified.calculateNumberOfTerminal('X', '-') > 0){
			System.err.println("WARNING : The thermodynamics parameters for azobenzene of " +
					"Asanuma (2005) are not established for terminal benzenes.");
			isApplicable = false;
		}
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		if (this.collector.getAzobenzeneValue(sequences.getSequence(pos1, pos2),sequences.getComplementary(pos1, pos2)) == null){
			return true;
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}

}
