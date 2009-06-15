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
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));

		OptionManagement.meltingLogger.log(Level.FINE, "The locked acid nuceic thermodynamic parameters are from McTigue et al. (2004) (delta delta H and delta delta S): ");

		result = calculateThermodynamicsNoModifiedAcid(newSequences, 0, newSequences.getDuplexLength() - 1, result);
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics lockedAcidValue;
		
		for (int i = 0; i < newSequences.getDuplexLength() - 1; i++){
			lockedAcidValue = this.collector.getLockedAcidValue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i));
			
			OptionManagement.meltingLogger.log(Level.FINE, sequences.getSequenceNNPair(i) + "/" + sequences.getComplementaryNNPair(i) + " : incremented enthalpy = " + lockedAcidValue.getEnthalpy() + "  incremented entropy = " + lockedAcidValue.getEntropy());

			enthalpy += lockedAcidValue.getEnthalpy();
			entropy += lockedAcidValue.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		
		if (environment.getHybridization().equals("dnadna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for locked acid nucleiques of" +
					"McTigue et al. (2004) are established for DNA sequences.");
		}
		
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if ((pos1 == 0 || pos2 == environment.getSequences().getDuplexLength() - 1) && environment.getSequences().calculateNumberOfTerminal('L', '-') > 0){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamics parameters for locked acid nucleiques of " +
					"McTigue (2004) are not established for terminal locked acid nucleiques.");
			isApplicable = false;
		}
		
		return isApplicable;
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));

		if (this.collector.getNNvalue(newSequences.getSequenceNNPair(0), newSequences.getComplementaryNNPair(0)) == null || this.collector.getNNvalue(newSequences.getSequence(1,1) + newSequences.getSequence(3, 3), newSequences.getComplementary(1, 1) + newSequences.getComplementary(3, 3)) == null){
			return true;
		}
		
		for (int i = 0; i < newSequences.getDuplexLength() - 1; i++){
			if (this.collector.getLockedAcidValue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i)) == null){
				return true;
			}
		}
		
		return super.isMissingParameters(newSequences, 0, newSequences.getDuplexLength() - 1);
	}

	private ThermoResult calculateThermodynamicsNoModifiedAcid(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result){

			NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));

		Thermodynamics firstNNValue = collector.getNNvalue(newSequences.getSequenceNNPair(0), newSequences.getComplementaryNNPair(0));
		Thermodynamics secondNNValue = collector.getNNvalue(newSequences.getSequence(1,1) + newSequences.getSequence(3, 3), newSequences.getComplementary(1, 1) + newSequences.getComplementary(3, 3));
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
