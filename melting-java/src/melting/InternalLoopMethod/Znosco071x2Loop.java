package melting.InternalLoopMethod;


import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;

public class Znosco071x2Loop extends PartialCalcul {

	/*REF: Brent M Znosko et al (2007). Biochemistry 46: 14715-14724. */
	
	public Znosco071x2Loop(){
		this.fileName = "Znosco20071x2loop.xml";
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		String mismatch1 = NucleotidSequences.getLoopFistMismatch(sequences.getSequence(pos1, pos2));
		String mismatch2 = NucleotidSequences.getLoopFistMismatch(sequences.getComplementary(pos1, pos2));
		NucleotidSequences internalLoop = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		int numberAU = internalLoop.calculateNumberOfTerminal('A', 'U');
		int numberGU = internalLoop.calculateNumberOfTerminal('G', 'U');
		
		double enthalpy = result.getEnthalpy() + this.collector.getInitiationLoopValue().getEnthalpy();
		double entropy = result.getEntropy() + this.collector.getInitiationLoopValue().getEntropy();
		
		if (sequences.isBasePairEqualsTo('G', 'A', pos1 + 1)){
			enthalpy += this.collector.getFirstMismatch("A", "G_not_RA/YG", "1x2").getEnthalpy();
			entropy += this.collector.getFirstMismatch("A", "G_not_RA/YG", "1x2").getEntropy();
		}
		else {
			enthalpy += this.collector.getFirstMismatch(mismatch1, mismatch2, "1x2").getEnthalpy();
			entropy += this.collector.getFirstMismatch(mismatch2, mismatch1, "1x2").getEntropy();
		}
		
		if (numberAU > 0){
			enthalpy += numberAU * this.collector.getClosureValue("A", "U").getEnthalpy();
			entropy += numberAU * this.collector.getClosureValue("A", "U").getEntropy();
			
		}
		
		if (numberGU > 0){
			enthalpy += numberGU * this.collector.getClosureValue("G", "U").getEnthalpy();
			entropy += numberGU * this.collector.getClosureValue("G", "U").getEntropy();
			
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		String loopType = environment.getSequences().getLoopType(pos1,pos2);
		
		if (environment.getHybridization().equals("rnarna") == false){
			System.out.println("WARNING : the internal 1x2 loop parameters of " +
					"Znosco et al. (2007) are originally established " +
					"for RNA sequences.");
			
			isApplicable = false;
		}
		
		if (loopType.equals("1x2") == false){
			System.out.println("WARNING : The thermodynamic parameters of Znosco et al. (2007) are" +
					"established only for 1x2 internal loop");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		String mismatch1 = NucleotidSequences.getLoopFistMismatch(sequences.getSequence(pos1, pos2));
		String mismatch2 = NucleotidSequences.getLoopFistMismatch(sequences.getComplementary(pos1, pos2));
		NucleotidSequences internalLoop = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		boolean isMissingParameters = super.isMissingParameters(sequences, pos1, pos2);
		
		if (this.collector.getInitiationLoopValue() == null){
			return true;
		}
		
		if (internalLoop.calculateNumberOfTerminal('A', 'U') > 0){
			if (this.collector.getClosureValue("A", "U") == null){
				return true;
			}
		}
		
		if (internalLoop.calculateNumberOfTerminal('G', 'U') > 0){
			if (this.collector.getClosureValue("G", "U") == null){
				return true;
			}
		}
		
		Thermodynamics firstMismatch = this.collector.getFirstMismatch(mismatch1, mismatch2, "1x2");
		
		if (sequences.isBasePairEqualsTo('G', 'A', pos1 + 1)){
			firstMismatch = this.collector.getFirstMismatch("A", "G_not_RA/YG", "1x2");
		}
		if (firstMismatch == null){
			return true;
		}
		return isMissingParameters;
	}

}
