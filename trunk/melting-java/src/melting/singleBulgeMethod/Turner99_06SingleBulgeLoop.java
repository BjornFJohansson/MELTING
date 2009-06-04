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
	
	/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924. 
	REF: Douglas M Turner et al (1999). J.Mol.Biol.  288: 911_940.*/ 
	
	private static String formulaH = "delat H = H(bulge of 1 initiation) + H(NN intervening)";
	private static String formulaS = "delat S = S(bulge of 1 initiation) + S(NN intervening)";
	
	@Override
	protected boolean isClosingPenaltyNecessary(){
		return false;
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		OptionManagement.meltingLogger.log(Level.INFO, "The formulas and thermodynamic parameters for single bulge loop are from Turner et al. (1999-2006) : " + formulaH + "and" + formulaS);
		
		result = super.calculateThermodynamics(newSequences, 0, newSequences.getDuplexLength(), result);
		
		Thermodynamics NNValue = this.collector.getNNvalue(sequences.getSingleBulgeNeighbors(newSequences.getSequence()), sequences.getSingleBulgeNeighbors(newSequences.getComplementary()));
		double enthalpy = result.getEnthalpy() + NNValue.getEnthalpy();
		double entropy = result.getEntropy() + NNValue.getEntropy();

		OptionManagement.meltingLogger.log(Level.INFO, sequences.getSingleBulgeNeighbors(sequences.getSequence(pos1, pos2)) + "/" + sequences.getSingleBulgeNeighbors(sequences.getComplementary(pos1, pos2)) + " : enthalpy = " + NNValue.getEnthalpy() + "  entropy = " + NNValue.getEntropy());

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
