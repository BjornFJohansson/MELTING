package melting.singleBulgeMethod;

import java.util.HashMap;
import java.util.logging.Level;

import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;
import melting.longBulgeMethod.Santalucia04LongBulgeLoop;

public class Santalucia04SingleBulgeLoop extends Santalucia04LongBulgeLoop{

	/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */
	
	private static StringBuffer formulaH = new StringBuffer();
	private static StringBuffer formulaS = new StringBuffer();
	
	public Santalucia04SingleBulgeLoop(){

		formulaH.append(formulaEnthalpy);
		formulaS.append(formulaEntropy);

		formulaH.append(" + H(intervening NN)");
		formulaS.append(" + S(intervening NN)");
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));

		OptionManagement.meltingLogger.log(Level.FINE, "The formula and thermodynamic parameters for single bulge loop are from Santalucia (2004) : " + formulaH.toString() + "and" + formulaS.toString());
		
		result = super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
		
		Thermodynamics NNValue = this.collector.getNNvalue(NucleotidSequences.getSingleBulgeNeighbors(newSequences.getSequence()), NucleotidSequences.getSingleBulgeNeighbors(newSequences.getComplementary()));
		double enthalpy = result.getEnthalpy() + NNValue.getEnthalpy();
		double entropy = result.getEntropy() + NNValue.getEntropy();
		
		OptionManagement.meltingLogger.log(Level.FINE, "NN intervening"+ NucleotidSequences.getSingleBulgeNeighbors(newSequences.getSequence()) + "/" + NucleotidSequences.getSingleBulgeNeighbors(newSequences.getComplementary()) +" :  enthalpy = " + NNValue.getEnthalpy() + "  entropy = " + NNValue.getEntropy());

		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		if (this.collector.getNNvalue(NucleotidSequences.getSingleBulgeNeighbors(sequences.getSequence(pos1, pos2)), NucleotidSequences.getSingleBulgeNeighbors(sequences.getComplementary(pos1, pos2))) == null){
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
