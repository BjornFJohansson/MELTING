package melting.InternalLoopMethod;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Turner06InternalLoop extends PartialCalcul{

	/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924.*/
	
	public static String defaultFileName = "Turner1999_2006longmm.xml";
	
	private static String formulaEnthalpy = "delat H = [H(first mismath) if loop length of 1 x n, n <= 2 or 2 x n, n != 2] + H(initiation loop of n) + (n1 - n2) x H(asymmetric loop) + number AU closing x H(closing AU) + number GU closing x H(closing GU)";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
	
		NucleotidSequences internalLoop = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));
		
		OptionManagement.meltingLogger.log(Level.FINE, "The internal loop formulas from Turner et al. (2006) : " + formulaEnthalpy + " (entropy formula is similar)");

		double saltIndependentEntropy = result.getSaltIndependentEntropy();
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		boolean needFirstMismatchEnergy = true;
		String loopType = internalLoop.getLoopType(pos1, pos2);
		
		String mismatch1 = NucleotidSequences.getLoopFistMismatch(internalLoop.getSequence());
		String mismatch2 = NucleotidSequences.getLoopFistMismatch(internalLoop.getComplementary());
		int numberAU = internalLoop.calculateNumberOfTerminal('A', 'U');
		int numberGU = internalLoop.calculateNumberOfTerminal('G', 'U');
		
		
		if (loopType.charAt(0) == '1' && Integer.parseInt(loopType.substring(2, 3)) > 2){
			
			needFirstMismatchEnergy = false;
		}
		
		int loopLength = sequences.calculateLoopLength(pos1, pos2);
		Thermodynamics initiationLoop = this.collector.getInitiationLoopValue(Integer.toString(loopLength));
		if (initiationLoop != null){
			OptionManagement.meltingLogger.log(Level.FINE, loopType + "Internal loop :  enthalpy = " + initiationLoop.getEnthalpy() + "  entropy = " + initiationLoop.getEntropy());

			enthalpy += initiationLoop.getEnthalpy();
			if (loopLength > 4){
				saltIndependentEntropy += initiationLoop.getEntropy();
			}
			else {
				entropy += initiationLoop.getEntropy();
			}
		}
		else {
			initiationLoop = this.collector.getInitiationLoopValue(">6");
			OptionManagement.meltingLogger.log(Level.FINE, loopType + "Internal loop :  enthalpy = " + initiationLoop.getEnthalpy() + "  entropy = " + initiationLoop.getEntropy() + " - 1.08 x ln(loopLength / 6)");

			enthalpy += initiationLoop.getEnthalpy();

			if (loopLength > 4){
				saltIndependentEntropy += initiationLoop.getEntropy() - 1.08 * Math.log(loopLength / 6);
			}
			else {
				entropy += initiationLoop.getEntropy() - 1.08 * Math.log(loopLength / 6);
			}
		}
		
		
		if (numberAU > 0){

			Thermodynamics closureAU = this.collector.getClosureValue("A", "U");
			
			OptionManagement.meltingLogger.log(Level.FINE, numberAU + " x AU closure : enthalpy = " + closureAU.getEnthalpy() + "  entropy = " + closureAU.getEntropy());

			enthalpy += numberAU * closureAU.getEnthalpy();
			entropy += numberAU * closureAU.getEntropy();
			
		}
		
		if (numberGU > 0){
			
			Thermodynamics closureGU = this.collector.getClosureValue("G", "U");
			
			OptionManagement.meltingLogger.log(Level.FINE, numberGU + " x GU closure : enthalpy = " + closureGU.getEnthalpy() + "  entropy = " + closureGU.getEntropy());

			enthalpy += numberGU *  closureGU.getEnthalpy();
			entropy += numberGU * closureGU.getEntropy();
		}
		
		if (sequences.isAsymmetricLoop(pos1, pos2)){
			
			Thermodynamics asymmetry = this.collector.getAsymmetry();
			int asymetricValue = Math.abs(Integer.parseInt(loopType.substring(0, 1)) - Integer.parseInt(loopType.substring(2, 3)));
			OptionManagement.meltingLogger.log(Level.FINE, asymetricValue + " x asymmetry : enthalpy = " + asymmetry.getEnthalpy() + "  entropy = " + asymmetry.getEntropy());
			
			enthalpy += asymetricValue * asymmetry.getEnthalpy();
			
			if (loopLength > 4){
				saltIndependentEntropy += asymetricValue * asymmetry.getEntropy();
			}
			else {
				entropy += asymetricValue * asymmetry.getEntropy();
			}
		}
		
		if (needFirstMismatchEnergy == true){
			
			Thermodynamics firstMismatch;
			if ((mismatch1.charAt(1) == 'G' && mismatch2.charAt(1) == 'G') || (mismatch1.charAt(1) == 'U' && mismatch2.charAt(1) == 'U')){	
				firstMismatch = this.collector.getFirstMismatch(mismatch1.substring(1, 2), mismatch2.substring(1, 2), loopType);

				OptionManagement.meltingLogger.log(Level.FINE, "First mismatch : " + mismatch1.substring(1, 2) + "/" + mismatch2.substring(1, 2) + " : enthalpy = " + firstMismatch.getEnthalpy() + "  entropy = " + firstMismatch.getEntropy());
			}
			else {
				firstMismatch = this.collector.getFirstMismatch(mismatch1, mismatch2, loopType);

				OptionManagement.meltingLogger.log(Level.FINE, "First mismatch : " + mismatch1 + "/" + mismatch2 + " : enthalpy = " + firstMismatch.getEnthalpy() + "  entropy = " + firstMismatch.getEntropy());
			}
			enthalpy += firstMismatch.getEnthalpy();
			entropy += firstMismatch.getEntropy();
			
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		result.setSaltIndependentEntropy(saltIndependentEntropy);
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		String loopType = environment.getSequences().getLoopType(pos1,pos2);

		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, " The internal loop parameters of " +
					"Turner et al. (2006) are originally established " +
					"for RNA sequences.");
		}
		
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (loopType.charAt(0) == '3' && loopType.charAt(2) == '3' && environment.getSequences().isBasePairEqualsTo('A', 'G', pos1 + 2)){
			OptionManagement.meltingLogger.log(Level.WARNING, " The thermodynamic parameters of Turner (2006) excluded" +
					"3 x 3 internal loops with a middle GA pair. The middle GA pair is shown to enhance " +
					"stability and this extra stability cannot be predicted by this nearest neighbor" +
					"parameter set.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
			NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		boolean isMissingParameters = super.isMissingParameters(newSequences, pos1, pos2);
		
		if (this.collector.getInitiationLoopValue(Integer.toString(sequences.calculateLoopLength(pos1,pos2))) == null){
			if (this.collector.getInitiationLoopValue("6") == null){
				return true;
			}
		}
		
		if (newSequences.calculateNumberOfTerminal('A', 'U') > 0){
			if (this.collector.getClosureValue("A", "U") == null){
				return true;
			}
		}
		
		if (newSequences.calculateNumberOfTerminal('G', 'U') > 0){
			if (this.collector.getClosureValue("G", "U") == null){
				return true;
			}
		}
		
		if (sequences.isAsymmetricLoop(pos1, pos2)){
			if (this.collector.getAsymmetry() == null){
				return true;
			}
		}
		
		String mismatch1 = NucleotidSequences.getLoopFistMismatch(newSequences.getSequence());
		String mismatch2 = NucleotidSequences.getLoopFistMismatch(newSequences.getComplementary());
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
