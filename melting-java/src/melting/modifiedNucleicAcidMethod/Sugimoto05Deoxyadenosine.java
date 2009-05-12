package melting.modifiedNucleicAcidMethod;


import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

public class Sugimoto05Deoxyadenosine extends PartialCalcul{
	
	/*Sugimoto et al. (2005). Analytical sciences 21 : 77-82*/ 
	
	public Sugimoto05Deoxyadenosine(){
		loadData("Sugimoto2005LdeoxyAmn.xml", this.collector);
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		result = calculateThermodynamicsNoModifiedAcid(sequences, pos1, pos2, result);
		double enthalpy = result.getEnthalpy() + this.collector.getModifiedvalue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEnthalpy();
		double entropy = result.getEntropy() + this.collector.getModifiedvalue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEntropy();
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		NucleotidSequences modified = new NucleotidSequences(environment.getSequences().getSequence(pos1, pos2), environment.getSequences().getComplementary(pos1, pos2));
		
		if (environment.getHybridization().equals("dnadna") == false) {
			System.err.println("WARNING : The thermodynamic parameters for L-deoxyadenosine of" +
					"Sugimoto et al. (2005) are established for DNA sequences.");
			isApplicable = false;
		}
		
		if (modified.calculateNumberOfTerminal("_A"," T") > 0 || modified.calculateNumberOfTerminal("_X"," A") > 0){
			System.err.println("WARNING : The thermodynamics parameters for L-deoxyadenosine of " +
					"Santaluciae (2005) are not established for terminal L-deoxyadenosine.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		if (this.collector.getDeoxyadenosineValue(sequences.getSequence(pos1, pos2),sequences.getComplementary(pos1, pos2)) == null){
			return true;
		}
		
		return super.isMissingParameters(sequences, pos1, pos2);
	}
	
	private ThermoResult calculateThermodynamicsNoModifiedAcid(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result){
		
		StringBuffer seq = new StringBuffer();
		StringBuffer comp = new StringBuffer();

		seq.append(sequences.getSequenceContainig("_", pos1, pos2));
		comp.append(sequences.getComplementaryTo(seq.toString(), pos1, pos2));
		
		int index = seq.toString().indexOf("_");
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		seq.deleteCharAt(index);
		comp.deleteCharAt(index);
			
		if (seq.toString().contains("X")){
			int Xpos = seq.toString().indexOf("X");
				comp.deleteCharAt(Xpos);
				comp.insert(Xpos, 'T');
			}
		
		for (int i = pos1; i < pos2; i++){
			enthalpy += this.collector.getNNvalue(seq.toString().substring(i, i + 2), comp.toString().substring(i, i + 2)).getEnthalpy();
			entropy += this.collector.getNNvalue(seq.toString().substring(i, i + 2), comp.toString().substring(i, i + 2)).getEntropy();
		} 
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

}
