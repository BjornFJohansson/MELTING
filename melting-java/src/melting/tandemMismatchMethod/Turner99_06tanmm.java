package melting.tandemMismatchMethod;


import melting.Environment;
import melting.Helper;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

public class Turner99_06tanmm extends PartialCalcul{

	/*REF: Douglas M Turner et al (2006). Nucleic Acids Research 34: 4912-4924.
	REF: Douglas M Turner et al (1999). J.Mol.Biol.  288: 911_940 */
	
	public static String defaultFileName = "Turner1999_2006tanmm.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}

	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		StringBuffer closing = new StringBuffer();
	
		if (sequences.isSymetric(pos1, pos2)){
			closing.append(sequences.getSequence(pos1, pos2).charAt(0));
			closing.append("/");
			closing.append(sequences.getComplementary(pos1, pos2).charAt(0));
			
			enthalpy += this.collector.getMismatchValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2), closing.toString()).getEnthalpy();
			entropy += this.collector.getMismatchValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2), closing.toString()).getEntropy();
		}
		else {
			String symetricSequence1 = sequences.buildSymetricSequence(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
			String symetricSequence2 = sequences.buildSymetricComplementary(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
			String symetricComplementary1 = sequences.buildSymetricSequence(NucleotidSequences.getInversedSequence(sequences.getComplementary(pos1, pos2)),NucleotidSequences.getInversedSequence(sequences.getSequence(pos1, pos2)));
			String symetricComplementary2 = sequences.buildSymetricComplementary(NucleotidSequences.getInversedSequence(sequences.getComplementary(pos1, pos2)), NucleotidSequences.getInversedSequence(sequences.getSequence(pos1, pos2)));
			
			NucleotidSequences sequences1 = new NucleotidSequences(symetricSequence1, symetricComplementary1);
			NucleotidSequences sequences2 = new NucleotidSequences(symetricSequence2, symetricComplementary2);

			ThermoResult result1 = new ThermoResult(0,0,0);
			ThermoResult result2 = new ThermoResult(0,0,0);
			
			result1 = calculateThermodynamics(sequences1, 0, 3, result1);
			result2 = calculateThermodynamics(sequences2, 0, 3, result2);
			
			enthalpy += (result1.getEnthalpy() + result2.getEnthalpy()) / 2;
			entropy += (result1.getEntropy() + result2.getEntropy()) / 2;
			
			if (sequences.isBasePairEqualsTo('G', 'G', pos1 + 1) || sequences.isBasePairEqualsTo('G', 'G', pos1 + 2)){
				if ((sequences.isBasePairEqualsTo('G', 'G', pos1 + 1) && sequences.isBasePairEqualsTo('A','A', pos1 + 2)) || (sequences.isBasePairEqualsTo('G', 'G', pos1 + 2) && sequences.isBasePairEqualsTo('A','A', pos1 + 1))){
					enthalpy += this.collector.getPenalty("G/G_adjacent_AA_or_nonCanonicalPyrimidine").getEnthalpy();
					entropy += this.collector.getPenalty("G/G_adjacent_AA_or_nonCanonicalPyrimidine").getEntropy();
				}
				else if (Helper.isPyrimidine(sequences.getSequence().charAt(1)) || (Helper.isPyrimidine(sequences.getSequence().charAt(2))) || (Helper.isPyrimidine(sequences.getComplementary().charAt(1))) || (Helper.isPyrimidine(sequences.getComplementary().charAt(2)))){
					enthalpy += this.collector.getPenalty("G/G_adjacent_AA_or_nonCanonicalPyrimidine").getEnthalpy();
					entropy += this.collector.getPenalty("G/G_adjacent_AA_or_nonCanonicalPyrimidine").getEntropy();
				}
			}
			
			else if (sequences.isBasePairEqualsTo('A', 'G', pos1 + 1) || sequences.isBasePairEqualsTo('A', 'G', pos1 + 2)){
				if (sequences.isBasePairEqualsTo('C', 'U', pos1 + 1) || sequences.isBasePairEqualsTo('C', 'U', pos1 + 2)){
					enthalpy += this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA").getEnthalpy();
					entropy += this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA").getEntropy();
				}
				else if (sequences.isBasePairEqualsTo('C', 'C', pos1 + 1) || sequences.isBasePairEqualsTo('C', 'C', pos1 + 2)){
					enthalpy += this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA").getEnthalpy();
					entropy += this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA").getEntropy();
				}
			}
			else if ((sequences.isBasePairEqualsTo('U', 'U', pos1 + 1) && sequences.isBasePairEqualsTo('A', 'A', pos1 + 2)) || (sequences.isBasePairEqualsTo('U', 'U', pos1 + 2) && sequences.isBasePairEqualsTo('A', 'A', pos1 + 1))){
				enthalpy += this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA").getEnthalpy();
				entropy += this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA").getEntropy();
			}
		}
	
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("rnarna") == false){
			System.out.println("WARNING : the tandem mismatch parameters of " +
					"Turner (1999-2006) are originally established " +
					"for RNA sequences.");
			
			isApplicable = false;
		}
		
		return isApplicable;
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
		StringBuffer closing = new StringBuffer();
		
		closing.append(sequences.getSequence(pos1, pos2).charAt(0));
		closing.append("/");
		closing.append(sequences.getComplementary(pos1, pos2).charAt(0));
		
		if (this.collector.getMismatchValue(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2), closing.toString()) == null){
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
				if (sequences.isBasePairEqualsTo('C', 'U', pos1 + 1) || sequences.isBasePairEqualsTo('C', 'U', pos1 + 2)){
					needPenaltyAG = true;
				}
				else if (sequences.isBasePairEqualsTo('C', 'C', pos1 + 1) || sequences.isBasePairEqualsTo('C', 'C', pos1 + 2)){
					needPenaltyAG = true;
				}
			}
			else if ((sequences.isBasePairEqualsTo('U', 'U', pos1 + 1) && sequences.isBasePairEqualsTo('A', 'A', pos1 + 2)) || (sequences.isBasePairEqualsTo('U', 'U', pos1 + 2) && sequences.isBasePairEqualsTo('A', 'A', pos1 + 1))){
				needPenaltyAG = true;
			}
				
			if (needPenaltyGG && this.collector.getPenalty("G/G_adjacent_AA_or_nonCanonicalPyrimidine") == null){
				return true;
			}
			else if (needPenaltyAG && this.collector.getPenalty("AG_GA_UU_adjacent_UU_CU_CC_AA") == null){
				return true;
			}
		}
		
		return super.isMissingParameters(sequences, pos1, pos2);
	}
}
