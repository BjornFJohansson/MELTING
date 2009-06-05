package melting.modifiedNucleicAcidMethod;


import java.util.HashMap;
import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public class Sugimoto05Deoxyadenosine extends PartialCalcul{
	
	/*Sugimoto et al. (2005). Analytical sciences 21 : 77-82*/ 
	
	public static String defaultFileName = "Sugimoto2005LdeoxyAmn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));

		OptionManagement.meltingLogger.log(Level.FINE, "The L-deoxyadenine thermodynamic parameters are from Sugimoto et al. (2005) (delta delta H and delta delta S): ");

		result = calculateThermodynamicsNoModifiedAcid(newSequences, 0, newSequences.getDuplexLength() - 1, result);
		
		Thermodynamics deoxyadenineValue = this.collector.getModifiedvalue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		double enthalpy = result.getEnthalpy() + deoxyadenineValue.getEnthalpy();
		double entropy = result.getEntropy() + deoxyadenineValue.getEntropy();
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " : incremented enthalpy = " + deoxyadenineValue.getEnthalpy() + "  incremented entropy = " + deoxyadenineValue.getEntropy());
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		NucleotidSequences modified = new NucleotidSequences(environment.getSequences().getSequence(pos1, pos2), environment.getSequences().getComplementary(pos1, pos2));
		
		if (environment.getHybridization().equals("dnadna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for L-deoxyadenosine of" +
					"Sugimoto et al. (2005) are established for DNA sequences.");
			environment.modifieSequences(environment.getSequences().getSequence(pos1, pos2, "dna"), environment.getSequences().getSequence(pos1, pos2, "dna"));

		}
		
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (modified.calculateNumberOfTerminal("_A"," T") > 0 || modified.calculateNumberOfTerminal("_X"," A") > 0){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamics parameters for L-deoxyadenosine of " +
					"Santaluciae (2005) are not established for terminal L-deoxyadenosine.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		NucleotidSequences noModified = sequences.removeDeoxyadenine(pos1, pos2);

		for (int i = 0; i < noModified.getDuplexLength(); i++){
			if (this.collector.getNNvalue(noModified.getSequenceNNPair(i), noModified.getComplementaryNNPair(i)) == null){
				return true;
			}
		} 
		
		if (this.collector.getDeoxyadenosineValue(sequences.getSequence(pos1, pos2),sequences.getComplementary(pos1, pos2)) == null){
			return true;
		}
		
		return super.isMissingParameters(sequences, pos1, pos2);
	}
	
	private ThermoResult calculateThermodynamicsNoModifiedAcid(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result){
		NucleotidSequences noModified = sequences.removeDeoxyadenine(pos1, pos2);
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics NNValue;
		for (int i = 0; i < noModified.getDuplexLength(); i++){
			
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
		
		String deoxyadenineName = options.get(OptionManagement.deoxyadenineMethod);
		RegisterCalculMethod register = new RegisterCalculMethod();
		PartialCalculMethod deoxyadenine = register.getPartialCalculMethod(OptionManagement.deoxyadenineMethod, deoxyadenineName);
		String fileDeoxyadenine = deoxyadenine.getDataFileName(deoxyadenineName);
		
		
		loadFile(fileDeoxyadenine, this.collector);
	}

}
