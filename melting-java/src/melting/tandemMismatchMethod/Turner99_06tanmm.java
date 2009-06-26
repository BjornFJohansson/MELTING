/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924.
	REF: Douglas M Turner et al (1999). J.Mol.Biol.  288: 911_940 */

package melting.tandemMismatchMethod;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Turner99_06tanmm extends PartialCalcul{
	
	public static String defaultFileName = "Turner1999_2006tanmm.xml";
	private static String enthalpyFormula = "delta H(5'PXYS/3'QWZT) = (H(5'PXWQ/3'QWXP) + H(5'TZYS/3'SYZT)) / 2 + penalty1( = H(GG pair adjacent to an AA or any non canonical pair with a pyrimidine) + penalty2 (= H(AG or GA pair adjacent to UC,CU or CC pairs or with a UU pair adjacent to a AA.))";

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
		
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		OptionManagement.meltingLogger.log(Level.FINE, "\n The nearest neighbor model for tandem mismatches is from Turner et al. (1999, 2006). If the sequences are not symmetric, we use this formula : ");
		OptionManagement.meltingLogger.log(Level.FINE,enthalpyFormula + "(entropy formula is similar.)");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		StringBuffer closing = new StringBuffer();
	
		if (sequences.isSymetric(pos1, pos2)){
			closing.append(newSequences.getSequence().charAt(0));
			closing.append("/");
			closing.append(newSequences.getComplementary().charAt(0));
			
			Thermodynamics mismatchValue = this.collector.getMismatchValue(newSequences.getSequence(1, newSequences.getDuplexLength() - 2), newSequences.getComplementary(1, newSequences.getDuplexLength() - 2), closing.toString());
			
			OptionManagement.meltingLogger.log(Level.FINE, "symmetric tandem mismatches " + sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " : enthalpy = " + mismatchValue.getEnthalpy() + "  entropy = " + mismatchValue.getEntropy());
			
			enthalpy += mismatchValue.getEnthalpy();
			entropy += mismatchValue.getEntropy();
		}
		else {
			String symetricSequence1 = NucleotidSequences.buildSymetricSequence(newSequences.getSequence(), newSequences.getComplementary());
			String symetricComplementary1 = NucleotidSequences.buildSymetricComplementary(newSequences.getSequence(), newSequences.getComplementary());
			String symetricSequence2 = NucleotidSequences.buildSymetricSequence(NucleotidSequences.getInversedSequence(newSequences.getComplementary()),NucleotidSequences.getInversedSequence(newSequences.getSequence()));
			String symetricComplementary2 = NucleotidSequences.buildSymetricComplementary(NucleotidSequences.getInversedSequence(newSequences.getComplementary()), NucleotidSequences.getInversedSequence(newSequences.getSequence()));
			
			NucleotidSequences sequences1 = new NucleotidSequences(symetricSequence1, symetricComplementary1);
			NucleotidSequences sequences2 = new NucleotidSequences(symetricSequence2, symetricComplementary2);

			OptionManagement.meltingLogger.log(Level.FINE, "asymmetric tandem mismatches (other formula used): ");

			ThermoResult result1 = new ThermoResult(0,0,0);
			ThermoResult result2 = new ThermoResult(0,0,0);
			
			result1 = calculateThermodynamics(sequences1, 0, sequences1.getDuplexLength() - 1, result1);
			result2 = calculateThermodynamics(sequences2, 0, sequences2.getDuplexLength() - 1, result2);

			enthalpy += (result1.getEnthalpy() + result2.getEnthalpy()) / 2;
			entropy += (result1.getEntropy() + result2.getEntropy()) / 2;
			
			if (newSequences.isTandemMismatchGGPenaltyNecessary(1)){
				Thermodynamics penalty1 = this.collector.getPenalty("G/G_adjacent_AA_or_nonCanonicalPyrimidine");
				OptionManagement.meltingLogger.log(Level.FINE, "penalty1 : enthalpy = " + penalty1.getEnthalpy() + "  entropy = " + penalty1.getEntropy());
				enthalpy += penalty1.getEnthalpy();
				entropy += penalty1.getEntropy();
			}
			
			else if (newSequences.isTandemMismatchDeltaPPenaltyNecessary(1)){
				Thermodynamics penalty2 = this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA");
				
				enthalpy += penalty2.getEnthalpy();
				entropy += penalty2.getEntropy();
				OptionManagement.meltingLogger.log(Level.FINE, "penalty2 : enthalpy = " + penalty2.getEnthalpy() + "  entropy = " + penalty2.getEntropy());
			}
		}
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
		}


	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {

		if (environment.getHybridization().equals("rnarna") == false){
			
			OptionManagement.meltingLogger.log(Level.WARNING, "the tandem mismatch parameters of " +
					"Turner (1999-2006) are originally established " +
					"for RNA sequences.");
		}
		
		return super.isApplicable(environment, pos1, pos2);
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		if (newSequences.isSymetric(0, newSequences.getDuplexLength() - 1) == false){
			String symetricSequence1 = NucleotidSequences.buildSymetricSequence(newSequences.getSequence(), newSequences.getComplementary());
			String symetricComplementary1 = NucleotidSequences.buildSymetricComplementary(newSequences.getSequence(), newSequences.getComplementary());
			String symetricSequence2 = NucleotidSequences.buildSymetricSequence(NucleotidSequences.getInversedSequence(newSequences.getComplementary()),NucleotidSequences.getInversedSequence(newSequences.getSequence()));
			String symetricComplementary2 = NucleotidSequences.buildSymetricComplementary(NucleotidSequences.getInversedSequence(newSequences.getComplementary()), NucleotidSequences.getInversedSequence(newSequences.getSequence()));
			
			NucleotidSequences sequences1 = new NucleotidSequences(symetricSequence1, symetricComplementary1);
			NucleotidSequences sequences2 = new NucleotidSequences(symetricSequence2, symetricComplementary2);
			if (isMissingParameters(sequences1, 0, sequences1.getDuplexLength() - 1)){
				return true;
			}
			if (isMissingParameters(sequences2, 0, sequences2.getDuplexLength() - 1)){
				return true;
			}
			if (newSequences.isTandemMismatchGGPenaltyNecessary(1)){
				if (this.collector.getPenalty("G/G_adjacent_AA_or_nonCanonicalPyrimidine") == null){
					OptionManagement.meltingLogger.log(Level.WARNING, "The penalty for G/G adjacent to AA or a non canonical base pair with pyrimidine is missing. Check the tandem mismatch parameters.");
					return true;
				}
				
			}
			
			else if (newSequences.isTandemMismatchDeltaPPenaltyNecessary(1)){
				if (this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA") == null){
					OptionManagement.meltingLogger.log(Level.WARNING, "The penalty for AG, GA or UU adjacent to UU, CU, CC or AA is missing. Check the tandem mismatch parameters.");
					return true;
				}
				
			}
		}
		else{
			StringBuffer closing = new StringBuffer();
			
			closing.append(newSequences.getSequence().charAt(0));
			closing.append("/");
			closing.append(newSequences.getComplementary().charAt(0));
			if (this.collector.getMismatchValue(newSequences.getSequence(1, newSequences.getDuplexLength() - 2), newSequences.getComplementary(1, newSequences.getDuplexLength() - 2), closing.toString()) == null){
				OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for " + newSequences.getSequence(1, newSequences.getDuplexLength() - 2) + "and" + newSequences.getComplementary(1, newSequences.getDuplexLength() - 2) + " is missing. Check the tandem mismatch parameters.");

				return true;
			}
		}
		
		return super.isMissingParameters(newSequences, 0, newSequences.getDuplexLength() - 1);
	}
}
