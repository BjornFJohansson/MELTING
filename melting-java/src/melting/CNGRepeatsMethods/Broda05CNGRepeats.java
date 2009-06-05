package melting.CNGRepeatsMethods;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Broda05CNGRepeats extends PartialCalcul {

	/*REF: Broda et al (2005). Biochemistry 44: 10873-10882.*/
	
	
	public static String defaultFileName = "Broda2005CNG.xml";

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
		
		OptionManagement.meltingLogger.log(Level.FINE, "CNG motifs method : from Broda et al. (2005). \n");
		
		int repeats = (pos2 - pos1 + 1) / 3;
		Thermodynamics CNGValue = this.collector.getCNGvalue(Integer.toString(repeats), sequences.getSequence(pos1, pos1 + 2,"rna"), sequences.getComplementary(pos1, pos1 + 2, "rna"));
		double enthalpy = result.getEnthalpy() + CNGValue.getEnthalpy();
		double entropy = result.getEntropy() + CNGValue.getEntropy();			
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		OptionManagement.meltingLogger.log(Level.FINE, "motif (" + sequences.getSequence(pos1, pos1 + 2) + ")" + repeats + " : " + "enthalpy = " + CNGValue.getEnthalpy() + "  entropy = " + CNGValue.getEntropy());
		
		return result;
	}
	
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		
		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for CNG repeats of Broda et al." +
					"(2005) is only established for RNA sequences.");
			
			environment.modifieSequences(environment.getSequences().getSequence(pos1, pos2, "rna"), environment.getSequences().getSequence(pos1, pos2, "rna"));
		}

		return super.isApplicable(environment, pos1, pos2);
	}
	
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int repeats = sequences.getDuplexLength() / 3;
		
		if (this.collector.getCNGvalue(Integer.toString(repeats), sequences.getSequence(pos1, pos1 + 2), sequences.getComplementary(pos1, pos1 + 2)) == null){
			return true;			
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}

}
