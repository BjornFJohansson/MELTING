package melting.CNGRepeatsMethods;


import melting.Environment;
import melting.NucleotidSequences;
import melting.PartialCalcul;
import melting.ThermoResult;

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
		
		int repeats = sequences.getDuplexLength() / 3;
		double enthalpy = result.getEnthalpy() + this.collector.getCNGvalue(Integer.toString(repeats), sequences.getSequence(pos1, pos1 + 2), sequences.getComplementary(pos1, pos1 + 2)).getEnthalpy();
		double entropy = result.getEntropy() + this.collector.getCNGvalue(Integer.toString(repeats), sequences.getSequence(pos1, pos1 + 2), sequences.getComplementary(pos1, pos1 + 2)).getEntropy();			
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("rnarna") == false){
			isApplicable = false;
			System.out.println("WARNING : the thermodynamic parameters for CNG repeats of Broda et al." +
					"(2005) is only established for RNA sequences.");
		}

		return isApplicable;
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
