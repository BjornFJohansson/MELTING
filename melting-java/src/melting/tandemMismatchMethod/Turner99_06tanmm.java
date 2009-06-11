package melting.tandemMismatchMethod;


import java.util.logging.Level;

import melting.Environment;
import melting.Helper;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Turner99_06tanmm extends PartialCalcul{

	/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924.
	REF: Douglas M Turner et al (1999). J.Mol.Biol.  288: 911_940 */
	
	public static String defaultFileName = "Turner1999_2006tanmm.xml";
	private static String enthalpyFormula = "delta H(5'PXYS/3'QWZT) = (H(5'PXWQ/3'QWXP) + H(5'TZYS/3'SYZT)) / 2 + penalty1( = H(GG pair adjacent to an AA or any non canonical pair with a pyrimidine) + penalty2 (= H(AG or GA pair adjacent to UC,CU or CC pairs or with a UU pair adjacent to a AA.))";

	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}

	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		OptionManagement.meltingLogger.log(Level.FINE, "The tandem mismatch parameters for symmetric sequences are from Turner et al. (1999, 2006). If the sequences are not symmetric, we use this formula : " + enthalpyFormula + "(entropy formula is similar.)");

		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		StringBuffer closing = new StringBuffer();
	
		if (sequences.isSymetric(pos1, pos2)){
			closing.append(newSequences.getSequence().charAt(0));
			closing.append("/");
			closing.append(newSequences.getComplementary().charAt(0));
			
			Thermodynamics mismatchValue = this.collector.getMismatchValue(newSequences.getSequence(), newSequences.getComplementary(), closing.toString());
			
			OptionManagement.meltingLogger.log(Level.FINE, "symmetric tandem mismatches " + sequences.getSequence(pos1, pos2) + "/" + sequences.getComplementary(pos1, pos2) + " : enthalpy = " + mismatchValue.getEnthalpy() + "  entropy = " + mismatchValue.getEntropy());
			
			enthalpy += mismatchValue.getEnthalpy();
			entropy += mismatchValue.getEntropy();
		}
		else {
			String symetricSequence1 = NucleotidSequences.buildSymetricSequence(newSequences.getSequence(), newSequences.getComplementary());
			String symetricSequence2 = NucleotidSequences.buildSymetricComplementary(newSequences.getSequence(), newSequences.getComplementary());
			String symetricComplementary1 = NucleotidSequences.buildSymetricSequence(NucleotidSequences.getInversedSequence(newSequences.getComplementary()),NucleotidSequences.getInversedSequence(newSequences.getSequence()));
			String symetricComplementary2 = NucleotidSequences.buildSymetricComplementary(NucleotidSequences.getInversedSequence(newSequences.getComplementary()), NucleotidSequences.getInversedSequence(newSequences.getSequence()));
			
			NucleotidSequences sequences1 = new NucleotidSequences(symetricSequence1, symetricComplementary1);
			NucleotidSequences sequences2 = new NucleotidSequences(symetricSequence2, symetricComplementary2);

			OptionManagement.meltingLogger.log(Level.FINE, "asymmetric tandem mismatches (other formula used): ");

			ThermoResult result1 = new ThermoResult(0,0,0);
			ThermoResult result2 = new ThermoResult(0,0,0);
			
			result1 = calculateThermodynamics(sequences1, 0, 3, result1);
			result2 = calculateThermodynamics(sequences2, 0, 3, result2);
			
			OptionManagement.meltingLogger.log(Level.FINE, symetricSequence1 + "/" + symetricComplementary1 + " : enthalpy = " + result1.getEnthalpy() + "  entropy = " + result1.getEntropy());
			OptionManagement.meltingLogger.log(Level.FINE, symetricSequence2 + "/" + symetricComplementary2 + " : enthalpy = " + result2.getEnthalpy() + "  entropy = " + result2.getEntropy());

			enthalpy += (result1.getEnthalpy() + result2.getEnthalpy()) / 2;
			entropy += (result1.getEntropy() + result2.getEntropy()) / 2;
			
			boolean isPenaltyNecessary = false;

			if (sequences.isBasePairEqualsTo('G', 'G', pos1 + 1) || sequences.isBasePairEqualsTo('G', 'G', pos1 + 2)){
				Thermodynamics penalty1 = this.collector.getPenalty("G/G_adjacent_AA_or_nonCanonicalPyrimidine");
				
				if ((sequences.isBasePairEqualsTo('G', 'G', pos1 + 1) && sequences.isBasePairEqualsTo('A','A', pos1 + 2)) || (sequences.isBasePairEqualsTo('G', 'G', pos1 + 2) && sequences.isBasePairEqualsTo('A','A', pos1 + 1))){
					enthalpy += penalty1.getEnthalpy();
					entropy += penalty1.getEntropy();
					
					isPenaltyNecessary = true;
				}
				else if (Helper.isPyrimidine(sequences.getSequence().charAt(1)) || (Helper.isPyrimidine(sequences.getSequence().charAt(2))) || (Helper.isPyrimidine(sequences.getComplementary().charAt(1))) || (Helper.isPyrimidine(sequences.getComplementary().charAt(2)))){
					enthalpy += penalty1.getEnthalpy();
					entropy += penalty1.getEntropy();
					
					isPenaltyNecessary = true;
				}
				
				if (isPenaltyNecessary){
					OptionManagement.meltingLogger.log(Level.FINE, "penalty1 : enthalpy = " + penalty1.getEnthalpy() + "  entropy = " + penalty1.getEntropy());
					isPenaltyNecessary = false;
				}
			}
			
			else if (sequences.isBasePairEqualsTo('A', 'G', pos1 + 1) || sequences.isBasePairEqualsTo('A', 'G', pos1 + 2)){
				Thermodynamics penalty2 = this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA");
				if (sequences.isBasePairEqualsTo('C', 'U', pos1 + 1) || sequences.isBasePairEqualsTo('C', 'U', pos1 + 2)){
					enthalpy += penalty2.getEnthalpy();
					entropy += penalty2.getEntropy();
					isPenaltyNecessary = true;
				}
				else if (sequences.isBasePairEqualsTo('C', 'C', pos1 + 1) || sequences.isBasePairEqualsTo('C', 'C', pos1 + 2)){
					enthalpy += penalty2.getEnthalpy();
					entropy += penalty2.getEntropy();
					isPenaltyNecessary = true;
				}
				
			}
			else if ((sequences.isBasePairEqualsTo('U', 'U', pos1 + 1) && sequences.isBasePairEqualsTo('A', 'A', pos1 + 2)) || (sequences.isBasePairEqualsTo('U', 'U', pos1 + 2) && sequences.isBasePairEqualsTo('A', 'A', pos1 + 1))){
				Thermodynamics penalty = this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA");
				enthalpy += penalty.getEnthalpy();
				entropy += penalty.getEntropy();
				isPenaltyNecessary = true;
			}
			if (isPenaltyNecessary){
				Thermodynamics penalty = this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA");

				OptionManagement.meltingLogger.log(Level.FINE, "penalty2 : enthalpy = " + penalty.getEnthalpy() + "  entropy = " + penalty.getEntropy());
				isPenaltyNecessary = false;
			}
		}
	
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {

		if (environment.getHybridization().equals("rnarna") == false){
			
			OptionManagement.meltingLogger.log(Level.WARNING, "the tandem mismatch parameters of " +
					"Turner (1999-2006) are originally established " +
					"for RNA sequences.");
		}
		
		return super.isApplicable(environment, pos1, pos2);
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		StringBuffer closing = new StringBuffer();
		
		closing.append(newSequences.getSequence().charAt(0));
		closing.append("/");
		closing.append(newSequences.getComplementary().charAt(0));
		
		if (this.collector.getMismatchValue(newSequences.getSequence(), newSequences.getComplementary(), closing.toString()) == null){
			return true;
		}
		if (sequences.isSymetric(pos1, pos2) == false){
			boolean needPenaltyGG = false;
			boolean needPenaltyAG = false;
			
			if (sequences.isBasePairEqualsTo('G', 'G', pos1 + 1) || sequences.isBasePairEqualsTo('G', 'G', pos1 + 2)){
				if ((sequences.isBasePairEqualsTo('G', 'G', pos1 + 1) && sequences.isBasePairEqualsTo('A','A', pos1 + 2)) || (sequences.isBasePairEqualsTo('G', 'G', pos1 + 2) && sequences.isBasePairEqualsTo('A','A', pos1 + 1))){
					needPenaltyGG = true;
				}
				else if (Helper.isPyrimidine(sequences.getSequence().charAt(1)) || (Helper.isPyrimidine(sequences.getSequence().charAt(2))) || (Helper.isPyrimidine(sequences.getComplementary().charAt(1))) || (Helper.isPyrimidine(sequences.getComplementary().charAt(2)))){
					needPenaltyGG = true;
				}
			}
			
			else if (sequences.isBasePairEqualsTo('A', 'G', pos1 + 1) || sequences.isBasePairEqualsTo('A', 'G', pos1 + 2)){
				if (newSequences.isBasePairEqualsTo('C', 'U', 1) || newSequences.isBasePairEqualsTo('C', 'U', 2)){
					needPenaltyAG = true;
				}
				else if (sequences.isBasePairEqualsTo('C', 'C', pos1 + 1) || sequences.isBasePairEqualsTo('C', 'C', pos1 + 2)){
					needPenaltyAG = true;
				}
			}
			else if ((newSequences.isBasePairEqualsTo('U', 'U', 1) && sequences.isBasePairEqualsTo('A', 'A', pos1 + 2)) || (newSequences.isBasePairEqualsTo('U', 'U', 2) && sequences.isBasePairEqualsTo('A', 'A', pos1 + 1))){
				needPenaltyAG = true;
			}
				
			if (needPenaltyGG && this.collector.getPenalty("G/G_adjacent_AA_or_nonCanonicalPyrimidine") == null){
				return true;
			}
			else if (needPenaltyAG && this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA") == null){
				return true;
			}
		}
		
		return super.isMissingParameters(newSequences, 0, newSequences.getDuplexLength() - 1);
	}
}
