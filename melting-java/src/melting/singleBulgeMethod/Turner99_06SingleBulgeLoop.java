/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924. 
	REF: Douglas M Turner et al (1999). J.Mol.Biol.  288: 911_940.*/ 
	
package melting.singleBulgeMethod;

import java.util.HashMap;
import java.util.logging.Level;

import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;
import melting.longBulgeMethod.Turner99_06LongBulgeLoop;

public class Turner99_06SingleBulgeLoop extends Turner99_06LongBulgeLoop{
	
	private static String formulaH = "delat H = H(bulge of 1 initiation) + H(NN intervening)";
	
	@Override
	protected boolean isClosingPenaltyNecessary(){
		return false;
	}
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for single bulge loop is from Turner et al. (1999-2006) : ");
		OptionManagement.meltingLogger.log(Level.FINE, formulaH + " (entropy formula is similar)");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		result = super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength() - 1, result);
		
		String sequenceNeighbors = NucleotidSequences.getSingleBulgeNeighbors(newSequences.getSequence());
		String complementaryNeighbors = NucleotidSequences.getSingleBulgeNeighbors(newSequences.getComplementary());
		Thermodynamics NNValue;
		
		if (newSequences.isBasePairEqualsTo('G', 'U', 0) || newSequences.isBasePairEqualsTo('G', 'U', 2)){
			NNValue = this.collector.getMismatchValue(sequenceNeighbors, complementaryNeighbors);
		}
		else{
			NNValue = this.collector.getNNvalue(sequenceNeighbors, complementaryNeighbors);
		}
		double enthalpy = result.getEnthalpy() + NNValue.getEnthalpy();
		double entropy = result.getEntropy() + NNValue.getEntropy();

		OptionManagement.meltingLogger.log(Level.FINE, NucleotidSequences.getSingleBulgeNeighbors(sequences.getSequence(pos1, pos2)) + "/" + NucleotidSequences.getSingleBulgeNeighbors(sequences.getComplementary(pos1, pos2)) + " : enthalpy = " + NNValue.getEnthalpy() + "  entropy = " + NNValue.getEntropy());

		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));
		
		String sequenceNeighbors = NucleotidSequences.getSingleBulgeNeighbors(newSequences.getSequence());
		String complementaryNeighbors = NucleotidSequences.getSingleBulgeNeighbors(newSequences.getComplementary());
		if (this.collector.getNNvalue(sequenceNeighbors, complementaryNeighbors) == null && this.collector.getMismatchValue(sequenceNeighbors, complementaryNeighbors) == null){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + sequenceNeighbors + "/" + complementaryNeighbors + " are missing. Check the single bulge loop thermodynamic parameters.");

			return true;
		}
		
		return super.isMissingParameters(newSequences, 0, newSequences.getDuplexLength() - 1);
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);
		
		String crickName = options.get(OptionManagement.NNMethod);
		String wobbleName = options.get(OptionManagement.wobbleBaseMethod);
		
		RegisterCalculMethod register = new RegisterCalculMethod();
		PartialCalculMethod NNMethod = register.getPartialCalculMethod(OptionManagement.NNMethod, crickName);
		PartialCalculMethod wobbleMethod = register.getPartialCalculMethod(OptionManagement.wobbleBaseMethod, wobbleName);

		NNMethod.initializeFileName(crickName);
		wobbleMethod.initializeFileName(wobbleName);
		
		String NNfile = NNMethod.getDataFileName(crickName);
		String wobbleFile = wobbleMethod.getDataFileName(wobbleName);
		
		loadFile(NNfile, this.collector);
		loadFile(wobbleFile, this.collector);
	}

}
