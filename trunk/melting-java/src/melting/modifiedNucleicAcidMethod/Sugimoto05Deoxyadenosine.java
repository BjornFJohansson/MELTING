package melting.modifiedNucleicAcidMethod;


import java.util.HashMap;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
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
		
		for (int i = 0; i < noModified.getDuplexLength(); i++){
			enthalpy += this.collector.getNNvalue(noModified.getSequenceNNPair(i), noModified.getComplementaryNNPair(i)).getEnthalpy();
			entropy += this.collector.getNNvalue(noModified.getSequenceNNPair(i), noModified.getComplementaryNNPair(i)).getEntropy();
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
