package melting.singleMismatchMethods;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

public class Turner06mm extends PartialCalcul{
	
	/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924.*/

	public Turner06mm(){
		loadData("Turner1999_2006longmm.xml", this.collector);
	}

	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		double enthalpy = result.getEnthalpy() + this.collector.getInitiationLoopValue("2").getEnthalpy();
		double entropy = result.getEntropy() + this.collector.getInitiationLoopValue("2").getEntropy();
		NucleotidSequences mismatch = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		int numberAU = mismatch.calculateNumberOfTerminal('A', 'U');
		int numberGU = mismatch.calculateNumberOfTerminal('G', 'U');
		String mismatch1 = NucleotidSequences.getLoopFistMismatch(sequences.getSequence(pos1, pos2));
		String mismatch2 = NucleotidSequences.getLoopFistMismatch(sequences.getComplementary(pos1, pos2));
		
		if (numberAU > 0){
			enthalpy += numberAU * this.collector.getClosureValue("A", "U").getEnthalpy();
			entropy += numberAU * this.collector.getClosureValue("A", "U").getEntropy();
		}
		
		if (numberGU > 0){
			enthalpy += numberGU * this.collector.getClosureValue("G", "U").getEnthalpy();
			entropy += numberGU * this.collector.getClosureValue("G", "U").getEntropy();
		}
		
		if (sequences.isBasePairEqualsTo('G', 'G', pos1 + 1)){
			enthalpy += this.collector.getFirstMismatch("G", "G", "1x1").getEnthalpy();
			entropy += this.collector.getFirstMismatch("G", "G", "1x1").getEntropy();
		}
		
		else if (mismatch1.equals("RU") && mismatch2.equals("YU")){
			enthalpy += this.collector.getFirstMismatch("RU", "YU", "1x1").getEnthalpy();
			entropy += this.collector.getFirstMismatch("RU", "YU", "1x1").getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {

		boolean isApplicable = super.isApplicable(environment, pos1, pos2);

		if (environment.getHybridization().equals("rnarna") == false){
			System.out.println("WARNING : the single mismatches parameter of " +
					"Turner et al. (2006) are originally established " +
					"for RNA sequences.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		NucleotidSequences mismatch = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		int numberAU = mismatch.calculateNumberOfTerminal('A', 'U');
		int numberGU = mismatch.calculateNumberOfTerminal('G', 'U');
		String mismatch1 = NucleotidSequences.getLoopFistMismatch(sequences.getSequence(pos1, pos2));
		String mismatch2 = NucleotidSequences.getLoopFistMismatch(sequences.getComplementary(pos1, pos2));
		
		if (this.collector.getInitiationLoopValue("2") == null){
			return true;
		}
		
		if (numberAU > 0){
			if (this.collector.getClosureValue("A", "U") == null){
				return true;
			}
		}
		
		if (numberGU > 0){
			if (this.collector.getClosureValue("G", "U") == null){
				return true;
			}
		}
		
		if (sequences.isBasePairEqualsTo('G', 'G', pos1 + 1)){
			if (this.collector.getFirstMismatch("G", "G", "1x1") == null){
				return true;
			}
		}
		
		else if (mismatch1.equals("RU") && mismatch2.equals("YU")){
			if (this.collector.getFirstMismatch("RU", "YU", "1x1") == null){
				return true;
			}
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}

}
