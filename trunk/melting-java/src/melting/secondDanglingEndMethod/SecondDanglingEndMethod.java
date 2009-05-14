package melting.secondDanglingEndMethod;

import java.util.HashMap;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public abstract class SecondDanglingEndMethod extends PartialCalcul {

	public abstract ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result);
	
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("rnarna") == false){
			isApplicable = false;
			System.out.println("WARNING : the thermodynamic parameters for second dangling end of Serra et al." +
					"(2005 and 2006) is established for RNA sequences.");
		}
		
		if (NucleotidSequences.getSens(environment.getSequences().getSequence(pos1, pos2), environment.getSequences().getComplementary(pos1, pos2)).equals("5")){
			isApplicable = false;
			System.out.println("WARNING : the thermodynamic parameters for second dangling end of Serra et al." +
					"(2005 and 2006) is only established for 3' second dangling end.");
		}
		
		return isApplicable;
	}
	
	public ThermoResult calculateThermodynamicsWithoutSecondDanglingEnd(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		double enthalpy = result.getEnthalpy() + this.collector.getDanglingValue(sequences.getSequence(pos1, pos2 - 1),sequences.getComplementary(pos1, pos2 - 1)).getEnthalpy();
		double entropy = result.getEntropy() + this.collector.getDanglingValue(sequences.getSequence(pos1, pos2 - 1),sequences.getComplementary(pos1, pos2 - 1)).getEntropy();
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);
		
		String doubleDanglingName = options.get(OptionManagement.doubleDanglingEndMethod);
		RegisterCalculMethod register = new RegisterCalculMethod();
		PartialCalculMethod secondDangling = register.getPartialCalculMethod(OptionManagement.doubleDanglingEndMethod, doubleDanglingName);
		String fileDoubleDangling = secondDangling.getDataFileName(doubleDanglingName);
		
		
		loadFile(fileDoubleDangling, this.collector);
	}

}
