package melting.singleMismatchMethods;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class AllawiSantaluciaPeyret97_98_99mm extends PartialCalcul{

	/*REF: Allawi and SantaLucia (1997). Biochemistry 36: 10581-10594. 
	REF: Allawi and SantaLucia (1998). Biochemistry 37: 2170-2179.
	REF: Allawi and SantaLucia (1998). Nuc Acids Res 26: 2694-2701. 
	REF: Allawi and SantaLucia (1998). Biochemistry 37: 9435-9444.
	REF: Peyret et al. (1999). Biochemistry 38: 3468-3477*/
	

	public static String defaultFileName = "AllawiSantaluciaPeyret1997_1998_1999mm.xml";
	
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

		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));
		OptionManagement.meltingLogger.log(Level.FINE, "\n The thermodynamic parameters for single mismatches are from Allawi, Santalucia and Peyret. (1997, 1998, 1999) : ");
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		Thermodynamics mismatchValue;
		for (int i = 0; i < newSequences.getDuplexLength() - 1; i++){
			mismatchValue =  collector.getMismatchvalue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i));
			
			OptionManagement.meltingLogger.log(Level.FINE, newSequences.getSequenceNNPair(i) + "/" + newSequences.getComplementaryNNPair(i) + " : enthalpy = " + mismatchValue.getEnthalpy() + "  entropy = " + mismatchValue.getEntropy());

			enthalpy += mismatchValue.getEnthalpy();
			entropy += mismatchValue.getEntropy();		
		}

		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	@Override
	public boolean isApplicable(Environment environment, int pos1, int pos2) {

		if (environment.getHybridization().equals("dnadna") == false){
				
			OptionManagement.meltingLogger.log(Level.WARNING, "The single mismatch parameters of " +
					"Allawi, Santalucia and Peyret are originally established " +
					"for DNA duplexes.");
		}
		
		return super.isApplicable(environment, pos1, pos2);
	}

	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		
			NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));

		for (int i = 0; i < newSequences.getDuplexLength() - 1; i++){
			if (this.collector.getMismatchvalue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i)) == null){
				return true;
			}
		}
		return super.isMissingParameters(newSequences, 0, newSequences.getDuplexLength() - 1);
	}
	
}
