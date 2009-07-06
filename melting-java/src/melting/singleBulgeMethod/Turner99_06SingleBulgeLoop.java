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
		int [] positions = super.correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("rna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for single bulge loop is from Turner et al. (1999-2006) : ");
		OptionManagement.meltingLogger.log(Level.FINE, formulaH + " (entropy formula is similar)");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		result = super.calculateThermodynamics(newSequences, pos1, pos2, result);
		
		String[] NNNeighbors = newSequences.getSingleBulgeNeighbors(pos1);

		Thermodynamics NNValue;
		
		if (newSequences.getDuplex().get(pos1).isBasePairEqualTo("G", "U") || newSequences.getDuplex().get(pos1+2).isBasePairEqualTo("G", "U")){
			NNValue = this.collector.getMismatchValue(NNNeighbors[0], NNNeighbors[1]);
		}
		else{
			NNValue = this.collector.getNNvalue(NNNeighbors[0], NNNeighbors[1]);
		}
		double enthalpy = result.getEnthalpy() + NNValue.getEnthalpy();
		double entropy = result.getEntropy() + NNValue.getEntropy();

		OptionManagement.meltingLogger.log(Level.FINE, NNNeighbors[0] + "/" + NNNeighbors[1] + " : enthalpy = " + NNValue.getEnthalpy() + "  entropy = " + NNValue.getEntropy());

		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("rna");
		
		String[] NNNeighbors = newSequences.getSingleBulgeNeighbors(pos1);

		if (this.collector.getNNvalue(NNNeighbors[0], NNNeighbors[1]) == null && this.collector.getMismatchValue(NNNeighbors[0], NNNeighbors[1]) == null){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + NNNeighbors[0] + "/" + NNNeighbors[1] + " are missing. Check the single bulge loop thermodynamic parameters.");

			return true;
		}
		
		return super.isMissingParameters(newSequences, pos1, pos2);
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);
		
		String crickName = options.get(OptionManagement.NNMethod);
		String wobbleName = options.get(OptionManagement.wobbleBaseMethod);
		
		
	}

}
