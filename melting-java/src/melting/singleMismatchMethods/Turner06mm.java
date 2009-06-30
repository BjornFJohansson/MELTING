	
/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924.*/

package melting.singleMismatchMethods;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Turner06mm extends PartialCalcul{
	
	public static String defaultFileName = "Turner1999_2006longmm.xml";
	
	private static String formulaEnthalpy = "delat H = H(loop initiation n=2) + number AU closing x H(closing AU) + number GU closing x H(closing GU) + H(bonus if GG mismatch) + H(bonus if 5'RU/3'YU)";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	@Override
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences mismatch = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		OptionManagement.meltingLogger.log(Level.FINE, "\n The model for single mismatches is from Turner et al. (2006) : ");
		OptionManagement.meltingLogger.log(Level.FINE, formulaEnthalpy + " (entropy formula is similar)");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		Thermodynamics initiationValue = this.collector.getInitiationLoopValue("2");
		double enthalpy = result.getEnthalpy() + initiationValue.getEnthalpy();
		double entropy = result.getEntropy() + initiationValue.getEntropy();
		int numberAU = mismatch.calculateNumberOfTerminal('A', 'U');
		int numberGU = mismatch.calculateNumberOfTerminal('G', 'U');
		String mismatch1 = NucleotidSequences.getLoopFistMismatch(mismatch.getSequence());
		String mismatch2 = NucleotidSequences.getLoopFistMismatch(mismatch.getComplementary());
		
		OptionManagement.meltingLogger.log(Level.FINE, "initiation loop of 2 : enthalpy = " + initiationValue.getEnthalpy() + "  entropy = " + initiationValue.getEntropy());
		
		if (numberAU > 0){
			
			Thermodynamics closingAU = this.collector.getClosureValue("A", "U");
			
			OptionManagement.meltingLogger.log(Level.FINE, numberAU + " x closing AU : enthalpy = " + closingAU.getEnthalpy() + "  entropy = " + closingAU.getEntropy());

			enthalpy += numberAU * closingAU.getEnthalpy();
			entropy += numberAU * closingAU.getEntropy();
		}
		
		if (numberGU > 0){
			
			Thermodynamics closingGU = this.collector.getClosureValue("G", "U");
			
			OptionManagement.meltingLogger.log(Level.FINE, numberGU + " x closing GU : enthalpy = " + closingGU.getEnthalpy() + "  entropy = " + closingGU.getEntropy());

			enthalpy += numberGU * closingGU.getEnthalpy();
			entropy += numberGU * closingGU.getEntropy();
		}
		
		if (sequences.isBasePairEqualsTo('G', 'G', pos1 + 1)){
			Thermodynamics GGMismatch = this.collector.getFirstMismatch("G", "G", "1x1");
			
			OptionManagement.meltingLogger.log(Level.FINE, " GG mismatch bonus : enthalpy = " + GGMismatch.getEnthalpy() + "  entropy = " + GGMismatch.getEntropy());

			enthalpy += GGMismatch.getEnthalpy();
			entropy += GGMismatch.getEntropy();
		}
		
		else if (mismatch1.equals("RU") && mismatch2.equals("YU")){
			Thermodynamics RUMismatch = this.collector.getFirstMismatch("RU", "YU", "1x1");
			
			OptionManagement.meltingLogger.log(Level.FINE, " RU mismatch bonus : enthalpy = " + RUMismatch.getEnthalpy() + "  entropy = " + RUMismatch.getEntropy());
			
			enthalpy += RUMismatch.getEnthalpy();
			entropy += RUMismatch.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {

		if (environment.getHybridization().equals("rnarna") == false){

			OptionManagement.meltingLogger.log(Level.WARNING, "The single mismatches parameter of " +
					"Turner et al. (2006) are originally established " +
					"for RNA sequences.");
		}
		
		return super.isApplicable(environment, pos1, pos2);
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int [] positions = correctPositions(pos1, pos2, sequences.getDuplexLength());
		pos1 = positions[0];
		pos2 = positions[1];
		
		NucleotidSequences mismatch = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));
		int numberAU = mismatch.calculateNumberOfTerminal('A', 'U');
		int numberGU = mismatch.calculateNumberOfTerminal('G', 'U');
		String mismatch1 = NucleotidSequences.getLoopFistMismatch(mismatch.getSequence());
		String mismatch2 = NucleotidSequences.getLoopFistMismatch(mismatch.getComplementary());
		if (this.collector.getInitiationLoopValue("2") == null){
			OptionManagement.meltingLogger.log(Level.WARNING, "The initiation parameters for a loop of 2 are missing. Check the single mismatch parameters.");
			return true;
		}
		if (numberAU > 0){
			if (this.collector.getClosureValue("A", "U") == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The parameters for AU closing base pair are missing. Check the single mismatch parameters.");
				return true;
			}
		}
		
		if (numberGU > 0){
			if (this.collector.getClosureValue("G", "U") == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The parameters for GU closing base pair are missing. Check the single mismatch parameters.");
				return true;
			}
		}
		
		if (sequences.isBasePairEqualsTo('G', 'G', pos1 + 1)){
			if (this.collector.getFirstMismatch("G", "G", "1x1") == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The bonus parameters for GG mismatch are missing. Check the single mismatch parameters.");
				return true;
			}
		}
		
		else if (mismatch1.equals("RU") && mismatch2.equals("YU")){
			if (this.collector.getFirstMismatch("RU", "YU", "1x1") == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The bonus parameters for RU/YU mismatch are missing. Check the single mismatch parameters.");
				return true;
			}
		}
		return super.isMissingParameters(mismatch, 0, mismatch.getDuplexLength() - 1);
	}
	
	private int[] correctPositions(int pos1, int pos2, int duplexLength){
		if (pos1 > 0){
			pos1 --;
		}
		if (pos2 < duplexLength - 1){
			pos2 ++;
		}
		int [] positions = {pos1, pos2};
		return positions;
	}

}
