
/*Sugimoto et al.(2001). Nucleic acids research 29 : 3289-3296*/

package melting.modifiedNucleicAcidMethod;

import java.util.HashMap;
import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public class Sugimoto01Hydroxyadenine extends PartialCalcul{
	
	public static String defaultFileName = "Sugimoto2001hydroxyAmn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));

		OptionManagement.meltingLogger.log(Level.FINE, "\n The hydroxyadenine model is from Sugimoto et al. (2001) (delta delta H and delta delta S): ");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		result = calculateThermodynamicsNoModifiedAcid(newSequences, 0, newSequences.getDuplexLength() - 1, result);
		Thermodynamics hydroxyAdenineValue = this.collector.getHydroxyadenosineValue(newSequences.getSequence(), newSequences.getComplementary());
		double enthalpy = result.getEnthalpy() + hydroxyAdenineValue.getEnthalpy();
		double entropy = result.getEntropy() + hydroxyAdenineValue.getEntropy();
		
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " : incremented enthalpy = " + hydroxyAdenineValue.getEnthalpy() + "  incremented entropy = " + hydroxyAdenineValue.getEntropy());

		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
			
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {

		NucleotidSequences modified = new NucleotidSequences(environment.getSequences().getSequence(pos1, pos2, "dna"), environment.getSequences().getComplementary(pos1, pos2, "dna"));

		if (environment.getHybridization().equals("dnadna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for 2-hydroxyadenine base of" +
					"Sugimoto (2001) are established for DNA sequences.");
					}
		
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (pos1 != 0 && pos2 != pos1 + modified.getDuplexLength() - 1){
			StringBuffer seq = new StringBuffer(modified.getDuplexLength());
			StringBuffer comp = new StringBuffer(modified.getDuplexLength());
			
			seq.append(modified.getSequenceContainig("A*", 0, modified.getDuplexLength() - 1));
			comp.append(modified.getComplementaryTo(seq.toString(),  0, modified.getDuplexLength() - 1));
			
			if (seq.toString().equals("CTA*A")== false && seq.toString().equals("TGA*C") == false){
				isApplicable = false;
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for 2-hydroxyadenine terminal base of" +
				"Sugimoto (2001) are established for CTA*A/CT or TGA*C/GG sequences.");
			}
			else if ((seq.toString().equals("CTA*A")== true && comp.toString().contains("CT") == false) || (seq.toString().equals("TGA*C") == true && comp.toString().contains("GG") == false)){
				isApplicable = false;
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for 2-hydroxyadenine terminal base of" +
				"Sugimoto (2001) are established for CTA*A/CT or TGA*C/GG sequences.");
			}
			
			else {
				if ((seq.toString().charAt(pos1) != 'T' && seq.toString().charAt(pos1) != 'G') || (seq.toString().charAt(pos2) != 'A' && seq.toString().charAt(pos2) != 'C')){
					isApplicable = false;
					OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for 2-hydroxyadenine base of" +
					"Sugimoto (2001) are established for TA*A/ANT or GA*C/CNG sequences.");
				}
				else {
					if (Helper.isComplementaryBasePair(seq.toString().charAt(pos1), comp.toString().charAt(pos1)) == false || Helper.isComplementaryBasePair(seq.toString().charAt(pos2), comp.toString().charAt(pos2)) == false){
						isApplicable = false;
						OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for 2-hydroxyadenine base of" +
						"Sugimoto (2001) are established for TA*A/ANT or GA*C/CNG sequences.");
					}
				}
			}
		}				
		
		return isApplicable;
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) { 
		
			NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));

		NucleotidSequences noModified = newSequences.removeHydroxyadenine(0, newSequences.getDuplexLength() - 1);
		if (pos1 != 0 && pos2 != pos1 + sequences.getDuplexLength() - 1){
			for (int i = 0; i < noModified.getDuplexLength() - 1; i++){
				if (this.collector.getNNvalue(noModified.getSequenceNNPair(i), noModified.getComplementaryNNPair(i)) == null){
					OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + noModified.getSequenceNNPair(i) + "/" + noModified.getComplementaryNNPair(i) + " are missing. Check the hydroxyadenine parameters.");
					return true;
				}
			}
		}
		else {
			return true;
		}
		
		if (this.collector.getHydroxyadenosineValue(newSequences.getSequence(), newSequences.getComplementary()) == null){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + newSequences.getSequence() + "/" + newSequences.getComplementary() + " are missing. Check the hydroxyadenine parameters.");

			return true;
		}
		return super.isMissingParameters(newSequences, 0, newSequences.getDuplexLength() - 1);	
	}
	
	private ThermoResult calculateThermodynamicsNoModifiedAcid(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result){
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		NucleotidSequences noModified = sequences.removeHydroxyadenine(pos1, pos2);

		Thermodynamics NNValue;

		for (int i = 0; i < noModified.getDuplexLength() - 1; i++){
			NNValue = this.collector.getNNvalue(noModified.getSequenceNNPair(i), noModified.getComplementaryNNPair(i));
			enthalpy += NNValue.getEnthalpy();
			entropy += NNValue.getEntropy();
				
			OptionManagement.meltingLogger.log(Level.FINE, noModified.getSequenceNNPair(i) + "/" + noModified.getComplementaryNNPair(i) + " : enthalpy = " + NNValue.getEnthalpy() + "  entropy = " + NNValue.getEntropy());

		}
		
			result.setEnthalpy(enthalpy);
			result.setEntropy(entropy);
			return result;
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);
		
		String crickName = options.get(OptionManagement.NNMethod);
		RegisterCalculMethod register = new RegisterCalculMethod();
		PartialCalculMethod NNMethod = register.getPartialCalculMethod(OptionManagement.NNMethod, crickName);
		NNMethod.initializeFileName(crickName);

		String NNfile = NNMethod.getDataFileName(crickName);
		
		
		loadFile(NNfile, this.collector);
	}

}
