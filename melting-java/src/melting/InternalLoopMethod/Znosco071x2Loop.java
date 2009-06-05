package melting.InternalLoopMethod;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Znosco071x2Loop extends PartialCalcul {

	/*REF: Brent M Znosko et al (2007). Biochemistry 46: 14715-14724. */
	
	public static String defaultFileName = "Znosco20071x2loop.xml";
	private static String formulaEnthalpy = "delat H = H(first mismath) + H(initiation 1x2 loop) + number AU closing x H(closing AU) + number GU closing x H(closing GU)";
	
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
		
		OptionManagement.meltingLogger.log(Level.FINE, "The 1 x 2 internal loop formulas from Znosco et al. (2007) : " + formulaEnthalpy + " (entropy formula is similar)");
		
		String mismatch1 = NucleotidSequences.getLoopFistMismatch(internalLoop.getSequence());
		String mismatch2 = NucleotidSequences.getLoopFistMismatch(internalLoop.getComplementary());
		int numberAU = internalLoop.calculateNumberOfTerminal('A', 'U');
		int numberGU = internalLoop.calculateNumberOfTerminal('G', 'U');
		
		Thermodynamics initiationLoop = this.collector.getInitiationLoopValue();
		
		OptionManagement.meltingLogger.log(Level.FINE, "1x2 Internal loop :  enthalpy = " + initiationLoop.getEnthalpy() + "  entropy = " + initiationLoop.getEntropy());

		double enthalpy = result.getEnthalpy() + initiationLoop.getEnthalpy();
		double entropy = result.getEntropy() + initiationLoop.getEntropy();
		
		Thermodynamics firstMismatch; 
		if (sequences.isBasePairEqualsTo('G', 'A', pos1 + 1)){
			firstMismatch = this.collector.getFirstMismatch("A", "G_not_RA/YG", "1x2");

			OptionManagement.meltingLogger.log(Level.FINE, "First mismatch A/G, not RA/YG : enthalpy = " + firstMismatch.getEnthalpy() + "  entropy = " + firstMismatch.getEntropy());
		}
		else {
			firstMismatch = this.collector.getFirstMismatch(mismatch1, mismatch2, "1x2");
			
			OptionManagement.meltingLogger.log(Level.FINE, "First mismatch " + mismatch1 + "/" + mismatch2 + " : enthalpy = " + firstMismatch.getEnthalpy() + "  entropy = " + firstMismatch.getEntropy());
		}
		enthalpy += firstMismatch.getEnthalpy();
		entropy += firstMismatch.getEntropy();
		
		if (numberAU > 0){
			Thermodynamics closureAU = this.collector.getClosureValue("A", "U");
			
			OptionManagement.meltingLogger.log(Level.FINE, numberAU + " x AU closure : enthalpy = " + closureAU.getEnthalpy() + "  entropy = " + closureAU.getEntropy());

			enthalpy += numberAU * closureAU.getEnthalpy();
			entropy += numberAU * closureAU.getEntropy();
			
		}
		
		if (numberGU > 0){
			Thermodynamics closureGU = this.collector.getClosureValue("G", "U");
			
			OptionManagement.meltingLogger.log(Level.FINE, numberGU + " x GU closure : enthalpy = " + closureGU.getEnthalpy() + "  entropy = " + closureGU.getEntropy());

			enthalpy += numberGU * closureGU.getEnthalpy();
			entropy += numberGU * closureGU.getEntropy();
			
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		String loopType = environment.getSequences().getLoopType(pos1,pos2);
		
		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, " The internal 1x2 loop parameters of " +
					"Znosco et al. (2007) are originally established " +
					"for RNA sequences.");
			
			environment.modifieSequences(environment.getSequences().getSequence(pos1, pos2, "rna"), environment.getSequences().getSequence(pos1, pos2, "rna"));

		}
		
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (loopType.equals("1x2") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, " The thermodynamic parameters of Znosco et al. (2007) are" +
					"established only for 1x2 internal loop.");
			
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
