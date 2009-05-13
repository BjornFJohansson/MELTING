package melting.InternalLoopMethod;


import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;

public class Turner06InternalLoop extends PartialCalcul{

	/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924.*/
	
	public Turner06InternalLoop(){
		this.fileName = "Turner1999_2006longmm.xml";
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
	
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		boolean needFirstMismatchEnergy = true;
		String loopType = sequences.getLoopType(pos1, pos2);
		NucleotidSequences internalLoop = new NucleotidSequences(sequences.getSequence(), sequences.getComplementary());
		String mismatch1 = NucleotidSequences.getLoopFistMismatch(internalLoop.getSequence());
		String mismatch2 = NucleotidSequences.getLoopFistMismatch(internalLoop.getComplementary());
		int numberAU = internalLoop.calculateNumberOfTerminal('A', 'U');
		int numberGU = internalLoop.calculateNumberOfTerminal('G', 'U');
		
		
		if (loopType.charAt(0) == '1' && Integer.getInteger(loopType.substring(2, 3)) > 2){
			
			needFirstMismatchEnergy = false;
		}
		
		if (this.collector.getInitiationLoopValue(Integer.toString(sequences.calculateLoopLength(pos1, pos2))) != null){
			enthalpy += this.collector.getInitiationLoopValue(Integer.toString(sequences.calculateLoopLength(pos1, pos2))).getEnthalpy();
			entropy += this.collector.getInitiationLoopValue(Integer.toString(sequences.calculateLoopLength(pos1, pos2))).getEntropy();
		}
		else {
			enthalpy += this.collector.getInitiationLoopValue(">6").getEnthalpy();
			entropy += this.collector.getInitiationLoopValue(">6").getEntropy() - 1.08 * Math.log(sequences.calculateLoopLength(pos1, pos2) / 6);
		}
		
		
		if (numberAU > 0){

			enthalpy += numberAU * this.collector.getClosureValue("A", "U").getEnthalpy();
			entropy += numberAU * this.collector.getClosureValue("A", "U").getEntropy();
			
		}
		
		if (numberGU > 0){
			
			enthalpy += numberGU *  this.collector.getClosureValue("G", "U").getEnthalpy();
			entropy += numberGU * this.collector.getClosureValue("G", "U").getEntropy();
		}
		
		if (sequences.isAsymetricLoop(pos1, pos2)){
				
			enthalpy += this.collector.getAsymetry().getEnthalpy();
			entropy += this.collector.getAsymetry().getEntropy();
		}
		
		if (needFirstMismatchEnergy == true){
			
			if ((mismatch1.charAt(1) == 'G' && mismatch2.charAt(1) == 'G') || (mismatch1.charAt(1) == 'U' && mismatch2.charAt(1) == 'U')){
				enthalpy += this.collector.getFirstMismatch(mismatch1.substring(1, 2), mismatch2.substring(1, 2), loopType).getEnthalpy();
				entropy += this.collector.getFirstMismatch(mismatch1.substring(1, 2), mismatch2.substring(1, 2), loopType).getEntropy();
			}
			else {
				enthalpy += this.collector.getFirstMismatch(mismatch1, mismatch2, loopType).getEnthalpy();
				entropy += this.collector.getFirstMismatch(mismatch1, mismatch2, loopType).getEntropy();
			}
			
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
			System.out.println("WARNING : the internal loop parameters of " +
					"Turner et al. (2006) are originally established " +
					"for RNA sequences.");
			
			isApplicable = false;
		}
		
		if (loopType.charAt(0) == '3' && loopType.charAt(2) == '3' && environment.getSequences().isBasePairEqualsTo('A', 'G', pos1 + 2)){
			System.out.println("WARNING : The thermodynamic parameters of Turner (2006) excluded" +
					"3 x 3 internal loops with a middle GA pair. The middle GA pair is shown to enhance " +
					"stability and this extra stability cannot be predicted by this nearest neighbor" +
					"parameter set.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		boolean isMissingParameters = super.isMissingParameters(sequences, pos1, pos2);
		
		if (this.collector.getInitiationLoopValue(Integer.toString(sequences.calculateLoopLength(pos1,pos2))) == null){
			if (this.collector.getInitiationLoopValue("6") == null){
				return true;
			}
		}
		
		if (sequences.calculateNumberOfTerminal('A', 'U') > 0){
			if (this.collector.getClosureValue("A", "U") == null){
				return true;
			}
		}
		
		if (sequences.calculateNumberOfTerminal('G', 'U') > 0){
			if (this.collector.getClosureValue("G", "U") == null){
				return true;
			}
		}
		
		if (sequences.isAsymetricLoop(pos1, pos2)){
			if (this.collector.getAsymetry() == null){
				return true;
			}
		}
		
		NucleotidSequences internalLoop = new NucleotidSequences(sequences.getSequence(), sequences.getComplementary());
		String mismatch1 = NucleotidSequences.getLoopFistMismatch(internalLoop.getSequence());
		String mismatch2 = NucleotidSequences.getLoopFistMismatch(internalLoop.getComplementary());
		String loopType = sequences.getLoopType(pos1, pos2);
		
		Thermodynamics firstMismatch = this.collector.getFirstMismatch(mismatch1, mismatch2, loopType);
		
		if ((mismatch1.charAt(1) == 'G' && mismatch2.charAt(1) == 'G') || (mismatch1.charAt(1) == 'U' && mismatch2.charAt(1) == 'U')){
			firstMismatch = this.collector.getFirstMismatch(mismatch1.substring(1, 2), mismatch2.substring(1, 2), loopType);
		}
		
		if (firstMismatch == null){
			return true;
		}
		return isMissingParameters;
	}
}
