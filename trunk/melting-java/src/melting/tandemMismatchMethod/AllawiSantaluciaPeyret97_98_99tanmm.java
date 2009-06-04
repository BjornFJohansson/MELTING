package melting.tandemMismatchMethod;


import java.util.HashMap;
import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.calculMethodInterfaces.PartialCalculMethod;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterCalculMethod;

public class AllawiSantaluciaPeyret97_98_99tanmm extends PartialCalcul{

	/*REF: Allawi and SantaLucia (1997). Biochemistry 36: 10581-10594. 
	REF: Allawi and SantaLucia (1998). Biochemistry 37: 2170-2179.
	REF: Allawi and SantaLucia (1998). Nuc Acids Res 26: 2694-2701. 
	REF: Allawi and SantaLucia (1998). Biochemistry 37: 9435-9444.
	REF: Peyret et al. (1999). Biochemistry 38: 3468-3477*/
	
	public static String defaultFileName = "AllawiSantaluciaPeyret1997_1998_1999tanmm.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		OptionManagement.meltingLogger.log(Level.INFO, "The tandem mismatch parameters are from Allawi, Santalucia and Peyret (1997, 1998, 1999)");

		NucleotidSequences newSequences = new NucleotidSequences(sequences.getSequence(pos1, pos2, "dna"), sequences.getComplementary(pos1, pos2, "dna"));
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		Thermodynamics mismatchValue;
		for (int i = 0; i < newSequences.getDuplexLength(); i++){
			mismatchValue = this.collector.getMismatchvalue(newSequences.getSequenceNNPair(i), newSequences.getComplementaryNNPair(i));
			
			OptionManagement.meltingLogger.log(Level.INFO, sequences.getSequenceNNPair(pos1 + i) + "/" + sequences.getComplementaryNNPair(pos1 + i) + " : enthalpy = " + mismatchValue.getEnthalpy() + "  entropy = " + mismatchValue);

			enthalpy += mismatchValue.getEnthalpy();
			entropy += mismatchValue.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
				
		if (environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The tandem mismatch parameters of " +
					"Allawi, Santalucia and Peyret are originally established " +
					"for DNA duplexes.");
		}
		return super.isApplicable(environment, pos1, pos2);
	}

	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {

		for (int i = pos1; i <= pos2; i++){
			if (this.collector.getMismatchvalue(sequences.getSequenceNNPair(i), sequences.getComplementaryNNPair(i)) == null){
				return true;
			}
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}
	
	@Override
	public void loadData(HashMap<String, String> options) {
		super.loadData(options);
		
		String singleMismatchName = options.get(OptionManagement.singleMismatchMethod);
		RegisterCalculMethod register = new RegisterCalculMethod();
		PartialCalculMethod singleMismatch = register.getPartialCalculMethod(OptionManagement.singleMismatchMethod, singleMismatchName);
		String fileSingleMismatch = singleMismatch.getDataFileName(singleMismatchName);
		
		
		loadFile(fileSingleMismatch, this.collector);
	}
	
}
