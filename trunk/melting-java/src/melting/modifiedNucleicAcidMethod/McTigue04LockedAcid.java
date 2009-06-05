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
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));

		OptionManagement.meltingLogger.log(Level.FINE, "The locked acid nuceic thermodynamic parameters are from McTigue et al. (2004) (delta delta H and delta delta S): ");

		result = calculateThermodynamicsNoModifiedAcid(newSequences, 0, newSequences.getDuplexLength() - 1, result);
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics lockedAcidValue;
		
		for (int i = pos1; i <= pos2; i++){
			lockedAcidValue = this.collector.getLockedAcidValue(newSequences.getSequence(), newSequences.getComplementary());
			
			OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " : incremented enthalpy = " + lockedAcidValue.getEnthalpy() + "  incremented entropy = " + lockedAcidValue.getEntropy());

			enthalpy += lockedAcidValue.getEnthalpy();
			entropy += lockedAcidValue.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
				
		if (environment.getHybridization().equals("dnadna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for locked acid nucleiques of" +
					"McTigue et al. (2004) are established for DNA sequences.");
			environment.modifieSequences(environment.getSequences().getSequence(pos1, pos2, "dna"), environment.getSequences().getSequence(pos1, pos2, "dna"));

		}
		
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if ((pos1 == 0 || pos2 == environment.getSequences().getDuplexLength() - 1) && environment.getSequences().calculateNumberOfTerminal('L', '-') > 0){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamics parameters for locked acid nucleiques of " +
					"McTigue (2004) are not established for terminal locked acid nucleiques.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		if (this.collector.getNNvalue(sequences.getSequenceNNPair(pos1), sequences.getComplementaryNNPair(pos1)) == null || this.collector.getNNvalue(sequences.getSequence(pos1 + 1,pos1 + 1) + sequences.getSequence(pos1 + 3, pos1 + 3), sequences.getComplementary(pos1 + 1, pos1 + 1) + sequences.getComplementary(pos1 + 3, pos1 + 3)) == null){
			return true;
		}
		
		for (int i = pos1; i <= pos2; i++){
			if (this.collector.getLockedAcidValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2)) == null){
				return true;
			}
		}
		
		return super.isMissingParameters(sequences, pos1, pos2);
	}

	private ThermoResult calculateThermodynamicsNoModifiedAcid(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result){
		
		Thermodynamics firstNNValue = collector.getNNvalue(sequences.getSequenceNNPair(pos1), sequences.getComplementaryNNPair(pos1));
		Thermodynamics secondNNValue = collector.getNNvalue(sequences.getSequence(pos1 + 1,pos1 + 1) + sequences.getSequence(pos1 + 3, pos1 + 3), sequences.getComplementary(pos1 + 1, pos1 + 1) + sequences.getComplementary(pos1 + 3, pos1 + 3));
		double enthalpy = result.getEnthalpy() + firstNNValue.getEnthalpy() + secondNNValue.getEnthalpy();
		double entropy = result.getEntropy() + firstNNValue.getEntropy()  + secondNNValue.getEntropy();
		
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequenceNNPair(pos1) + "/" + sequences.getComplementaryNNPair(pos1) + " : enthalpy = " + firstNNValue.getEnthalpy() + "  entropy = " + firstNNValue.getEntropy());
		OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequence(pos1 + 1,pos1 + 1) + sequences.getSequence(pos1 + 3, pos1 + 3) + "/" + sequences.getComplementary(pos1 + 1, pos1 + 1) + sequences.getComplementary(pos1 + 3, pos1 + 3) + " : enthalpy = " + secondNNValue.getEnthalpy() + "  entropy = " + secondNNValue.getEntropy());

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
