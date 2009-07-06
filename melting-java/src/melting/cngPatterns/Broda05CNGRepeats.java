
/*REF: Broda et al (2005). Biochemistry 44: 10873-10882.*/

package melting.cngPatterns;

import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Broda05CNGRepeats extends PartialCalcul {	
	
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
		
		OptionManagement.meltingLogger.log(Level.FINE, "\n CNG motifs model : from Broda et al. (2005). \n");
		OptionManagement.meltingLogger.log(Level.FINE, "\n File name : " + this.fileName);

		int repeats = (pos2 - pos1 - 1) / 3;
		Thermodynamics CNGValue = this.collector.getCNGvalue(Integer.toString(repeats), sequences.getSequence(pos1 + 1, pos1 + 3,"rna"));
		double enthalpy = result.getEnthalpy() + CNGValue.getEnthalpy();
		double entropy = result.getEntropy() + CNGValue.getEntropy();			
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		OptionManagement.meltingLogger.log(Level.FINE, "motif (" + sequences.getSequence(pos1 + 1, pos1 + 3) + ")" + repeats + " : " + "enthalpy = " + CNGValue.getEnthalpy() + "  entropy = " + CNGValue.getEntropy());
		
		return result;
	}
	
	
	@Override
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		if (environment.getHybridization().equals("rnarna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The nearest neighbor model for CNG repeats of Broda et al." +
					"(2005) is only established for RNA sequences.");
		}
		
		if (environment.getSequences().getSequence().charAt(pos1) != 'G' && environment.getSequences().getSequence().charAt(pos2) != 'C'){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for CNG repeats of Broda et al." +
			"(2005) are only established for CNG RNA sequences. The sequence must begin with a G/C base pair and end with a C/Gbase pair.");
		}
		
		if (environment.isSelfComplementarity() == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for CNG repeats of Broda et al." +
			"(2005) are only established for self complementary RNA sequences.");
			
			isApplicable = false;
		}

		return isApplicable;
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		int repeats = (sequences.getDuplexLength() - 2) / 3;
		if (this.collector.getCNGvalue(Integer.toString(repeats), sequences.getSequence(pos1 + 1, pos1 + 3, "rna")) == null){
			return true;			
		}
		return super.isMissingParameters(sequences, pos1, pos2);
	}

}