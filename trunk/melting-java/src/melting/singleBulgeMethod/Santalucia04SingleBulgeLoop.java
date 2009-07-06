	
/*Santalucia et al (2004). Annu. Rev. Biophys. Biomol. Struct 33 : 415-440 */

package melting.singleBulgeMethod;

import java.util.HashMap;
import java.util.logging.Level;

import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.longBulgeMethod.Santalucia04LongBulgeLoop;

public class Santalucia04SingleBulgeLoop extends Santalucia04LongBulgeLoop{
	
	private static StringBuffer formulaH = new StringBuffer();
	private static StringBuffer formulaS = new StringBuffer();
	
	public Santalucia04SingleBulgeLoop(){

		formulaH.append(formulaEnthalpy);
		formulaS.append(formulaEntropy);

		formulaH.append(" + H(intervening NN)");
		formulaS.append(" + S(intervening NN)");
	}
	
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		int [] positions = super.correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		OptionManagement.meltingLogger.log(Level.FINE, "The nearest neighbor model for single bulge loop is from Santalucia (2004) : " + formulaH.toString() + "and" + formulaS.toString());
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		result = super.calculateThermodynamics(newSequences, pos1, pos2, result);
		String[] NNNeighbors = newSequences.getSingleBulgeNeighbors(pos1);
		
		Thermodynamics NNValue = this.collector.getNNvalue(NNNeighbors[0], NNNeighbors[1]);
		double enthalpy = result.getEnthalpy() + NNValue.getEnthalpy();
		double entropy = result.getEntropy() + NNValue.getEntropy();
		
		OptionManagement.meltingLogger.log(Level.FINE, "NN intervening"+ NNNeighbors[0] + "/" + NNNeighbors[1] +" :  enthalpy = " + NNValue.getEnthalpy() + "  entropy = " + NNValue.getEntropy());

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
		
		NucleotidSequences newSequences = sequences.getEquivalentSequences("dna");
		
		String[] NNNeighbors = newSequences.getSingleBulgeNeighbors(pos1);

		if (this.collector.getNNvalue(NNNeighbors[0], NNNeighbors[1]) == null){
			OptionManagement.meltingLogger.log(Level.FINE, "The thermodynamic parameters for " + NNNeighbors[0] + "/" + NNNeighbors[1] + " are missing. Check the single bulge loop parameters.");

			return true;
		}
		return super.isMissingParameters(newSequences, pos1, pos2);
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);
		
		
		
	}
}
