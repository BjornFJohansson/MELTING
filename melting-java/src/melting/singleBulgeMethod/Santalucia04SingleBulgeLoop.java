package melting.singleBulgeMethod;

import java.util.HashMap;

import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;
import melting.longBulgeMethod.Santalucia04LongBulgeLoop;

public class Santalucia04SingleBulgeLoop extends Santalucia04LongBulgeLoop{

	/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		result = super.calculateThermodynamics(sequences, pos1, pos2, result);
		double enthalpy = result.getEnthalpy() + this.collector.getNNvalue(sequences.getSingleBulgeNeighbors(sequences.getSequence(pos1, pos2)), sequences.getSingleBulgeNeighbors(sequences.getComplementary(pos1, pos2))).getEnthalpy();
		double entropy = result.getEntropy() + this.collector.getNNvalue(sequences.getSingleBulgeNeighbors(sequences.getSequence(pos1, pos2)), sequences.getSingleBulgeNeighbors(sequences.getComplementary(pos1, pos2))).getEntropy();

		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		if (this.collector.getNNvalue(sequences.getSingleBulgeNeighbors(sequences.getSequence(pos1, pos2)), sequences.getSingleBulgeNeighbors(sequences.getComplementary(pos1, pos2))) == null){
			return true;
		}
		
		return super.isMissingParameters(sequences, pos1, pos2);
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);
		
		String singleBulgeName = options.get(OptionManagement.singleBulgeLoopMethod);
		RegisterCalculMethod register = new RegisterCalculMethod();
		PartialCalculMethod singleBulge = register.getPartialCalculMethod(OptionManagement.singleBulgeLoopMethod, singleBulgeName);
		String fileSingleBulge = singleBulge.getDataFileName(singleBulgeName);
		
		
		loadFile(fileSingleBulge, this.collector);
	}
}
