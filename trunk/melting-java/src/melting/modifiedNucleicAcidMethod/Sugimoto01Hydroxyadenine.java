package melting.modifiedNucleicAcidMethod;


import java.util.HashMap;

import melting.Environment;
import melting.Helper;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public class Sugimoto01Hydroxyadenine extends PartialCalcul{

	/*Sugimoto et al.(2001). Nucleic acids research 29 : 3289-3296*/
	
	public Sugimoto01Hydroxyadenine(){
		this.fileName = "Sugimoto2001hydroxyAmn.xml";
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		result = calculateThermodynamicsNoModifiedAcid(sequences, pos1, pos2, result);
		
		double enthalpy = result.getEnthalpy() + this.collector.getHydroxyadenosineValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEnthalpy();
		double entropy = result.getEntropy() + this.collector.getHydroxyadenosineValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEntropy();
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
			
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {

		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		NucleotidSequences modified = new NucleotidSequences(environment.getSequences().getSequence(pos1, pos2), environment.getSequences().getComplementary(pos1, pos2));
		
		if (environment.getHybridization().equals("dnadna") == false) {
			System.err.println("WARNING : The thermodynamic parameters for 2-hydroxyadenine base of" +
					"Sugimoto (2001) are established for DNA sequences.");
			isApplicable = false;
		}
		
		if (pos1 == 0 || pos2 == pos1 + modified.getDuplexLength() - 1){
			StringBuffer seq = new StringBuffer(modified.getDuplexLength());
			StringBuffer comp = new StringBuffer(modified.getDuplexLength());
			
			seq.append(environment.getSequences().getSequenceContainig("A*", pos1, pos2));
			comp.append(environment.getSequences().getComplementaryTo(seq.toString(), pos1, pos2));
			
			if (seq.toString().equals("CTA*A")== false && seq.toString().equals("TGA*C") == false){
				isApplicable = false;
				System.err.println("WARNING : The thermodynamic parameters for 2-hydroxyadenine terminal base of" +
				"Sugimoto (2001) are established for CTA*A/CT or TGA*C/GG sequences.");
			}
			else if ((seq.toString().equals("CTA*A")== true && comp.toString().contains("CT") == false) || (seq.toString().equals("TGA*C") == true && comp.toString().contains("GG") == false)){
				isApplicable = false;
				System.err.println("WARNING : The thermodynamic parameters for 2-hydroxyadenine terminal base of" +
				"Sugimoto (2001) are established for CTA*A/CT or TGA*C/GG sequences.");
			}
			
			else {
				if ((seq.toString().charAt(pos1) != 'T' && seq.toString().charAt(pos1) != 'G') || (seq.toString().charAt(pos2) != 'A' && seq.toString().charAt(pos2) != 'C')){
					isApplicable = false;
					System.err.println("WARNING : The thermodynamic parameters for 2-hydroxyadenine base of" +
					"Sugimoto (2001) are established for TA*A/ANT or GA*C/CNG sequences.");
				}
				else {
					if (Helper.isComplementaryBasePair(seq.toString().charAt(pos1), comp.toString().charAt(pos1)) == false || Helper.isComplementaryBasePair(seq.toString().charAt(pos2), comp.toString().charAt(pos2)) == false){
						isApplicable = false;
						System.err.println("WARNING : The thermodynamic parameters for 2-hydroxyadenine base of" +
						"Sugimoto (2001) are established for TA*A/ANT or GA*C/CNG sequences.");
					}
				}
			}
		}				
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) { 
			
		NucleotidSequences noModified = sequences.removeHydroxyadenine(pos1, pos2);
		for (int i = 0; i < noModified.getDuplexLength(); i++){
			if (this.collector.getNNvalue(noModified.getSequenceNNPair(i), noModified.getComplementaryNNPair(i)) == null){
				return true;
			}
		}
		
		if (this.collector.getHydroxyadenosineValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)) == null){
			return true;
		}
		return super.isMissingParameters(sequences, pos1, pos2);	
	}
	
	private ThermoResult calculateThermodynamicsNoModifiedAcid(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result){
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		NucleotidSequences noModified = sequences.removeHydroxyadenine(pos1, pos2);
		
		if (pos1 != 0 && pos2 != sequences.getDuplexLength()){
			
			for (int i = 0; i < noModified.getDuplexLength(); i++){
				enthalpy += this.collector.getNNvalue(noModified.getSequenceNNPair(i), noModified.getComplementaryNNPair(i)).getEnthalpy();
				entropy += this.collector.getNNvalue(noModified.getSequenceNNPair(i), noModified.getComplementaryNNPair(i)).getEntropy();
			}
		
			result.setEnthalpy(enthalpy);
			result.setEntropy(entropy);
			return result;
		}
		else{
			return null;
		}
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);
		
		RegisterCalculMethod register = new RegisterCalculMethod();
		PartialCalcul NNMethod = register.getPartialCalculMethod(OptionManagement.NNMethod, options);
		
		String NNFile = NNMethod.getFileName(OptionManagement.NNMethod);
		loadFile(NNFile, this.collector);
	}

}
