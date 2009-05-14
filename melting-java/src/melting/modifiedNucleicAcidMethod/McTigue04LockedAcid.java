package melting.modifiedNucleicAcidMethod;


import java.util.HashMap;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public class McTigue04LockedAcid extends PartialCalcul{
	
	/*McTigue et al.(2004). Biochemistry 43 : 5388-5405 */
	
	public static String defaultFileName = "McTigue2004lockedmn.xml";
	
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
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		for (int i = pos1; i < pos2 -1; i++){
			enthalpy += this.collector.getLockedAcidValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEnthalpy();
			entropy += this.collector.getLockedAcidValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)).getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		NucleotidSequences modified = new NucleotidSequences(environment.getSequences().getSequence(pos1, pos2), environment.getSequences().getComplementary(pos1, pos2));
		
		if (environment.getHybridization().equals("dnadna") == false) {
			System.err.println("WARNING : The thermodynamic parameters for locked acid nucleiques of" +
					"McTigue et al. (2004) are established for DNA sequences.");
			isApplicable = false;
		}
		
		if (modified.calculateNumberOfTerminal('L', '-') > 0){
			System.err.println("WARNING : The thermodynamics parameters for locked acid nucleiques of " +
					"McTigue (2004) are not established for terminal locked acid nucleiques.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		if (this.collector.getLockedAcidValue(sequences.getSequence(pos1, pos2),sequences.getComplementary(pos1, pos2)) == null){
			return true;
		}
		
		if (this.collector.getNNvalue(sequences.getSequenceNNPair(pos1), sequences.getComplementaryNNPair(pos1)) == null || this.collector.getNNvalue(sequences.getSequence(pos1 + 1,pos1 + 1) + sequences.getSequence(pos1 + 3, pos1 + 3), sequences.getComplementary(pos1 + 1, pos1 + 1) + sequences.getComplementary(pos1 + 3, pos1 + 3)) == null){
			return true;
		}
		
		for (int i = pos1; i < pos2 -1; i++){
			if (this.collector.getLockedAcidValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)) == null){
				return true;
			}
		}
		
		return super.isMissingParameters(sequences, pos1, pos2);
	}

	private ThermoResult calculateThermodynamicsNoModifiedAcid(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result){
		
		double enthalpy = result.getEnthalpy() + collector.getNNvalue(sequences.getSequenceNNPair(pos1), sequences.getComplementaryNNPair(pos1)).getEnthalpy() + collector.getNNvalue(sequences.getSequence(pos1 + 1,pos1 + 1) + sequences.getSequence(pos1 + 3, pos1 + 3), sequences.getComplementary(pos1 + 1, pos1 + 1) + sequences.getComplementary(pos1 + 3, pos1 + 3)).getEnthalpy();
		double entropy = result.getEntropy() + collector.getNNvalue(sequences.getSequenceNNPair(pos1), sequences.getComplementaryNNPair(pos1)).getEntropy()  + collector.getNNvalue(sequences.getSequence(pos1 + 1,pos1 + 1) + sequences.getSequence(pos1 + 3, pos1 + 3), sequences.getComplementary(pos1 + 1, pos1 + 1) + sequences.getComplementary(pos1 + 3, pos1 + 3)).getEntropy();
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);
		
		String azobenzeneName = options.get(OptionManagement.azobenzeneMethod);
		RegisterCalculMethod register = new RegisterCalculMethod();
		PartialCalculMethod azobenzene = register.getPartialCalculMethod(OptionManagement.azobenzeneMethod, azobenzeneName);
		String fileAzobenzene = azobenzene.getDataFileName(azobenzeneName);
		
		
		loadFile(fileAzobenzene, this.collector);
	}
	
}
