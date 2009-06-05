package melting.wobbleNNMethod;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;

public class Znosco07Inosine extends InosineNNMethod {

	/*Brent M Znosko et al. (2005). Biochemistry 46 : 4625-4634 */
	
	public static String defaultFileName = "Znosco2007inomn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public ThermoResult calculateThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
		NucleotidSequences inosine = new NucleotidSequences(sequences.getSequence(pos1, pos2, "rna"), sequences.getComplementary(pos1, pos2, "rna"));

		OptionManagement.meltingLogger.log(Level.FINE, "The thermodynamic parameters for inosine are from Znosco et al. (2007) : ");
		
		result = super.calculateThermodynamics(inosine, 0, inosine.getDuplexLength() - 1, result);
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		int numberIU = inosine.calculateNumberOfTerminal('I', 'U');
		
		if ((pos1 == 0 || pos2 == inosine.getDuplexLength() - 1) && numberIU > 0) {
			Thermodynamics terminaIU = this.collector.getTerminal("per_I/U");
			OptionManagement.meltingLogger.log(Level.FINE, numberIU + " x terminal IU : enthalpy = " + terminaIU.getEnthalpy() + "  entropy = " + terminaIU.getEntropy());

			enthalpy += numberIU * terminaIU.getEnthalpy();
			entropy += numberIU * terminaIU.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		return result;
	}
	
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		
		
		if (environment.getHybridization().equals("rnarna") == false) {
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters for inosine base of" +
					"Znosco (2007) are established for RNA sequences.");
			
			environment.modifieSequences(environment.getSequences().getSequence(pos1, pos2, "rna"), environment.getSequences().getSequence(pos1, pos2, "rna"));

		}
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		for (int i = pos1; i < pos2; i++){
			if (environment.getSequences().isBasePairEqualsTo('I', 'U', i) == false){
				isApplicable = false;
				OptionManagement.meltingLogger.log(Level.WARNING, " The thermodynamic parameters of Znosco" +
						"(2007) are only established for IU base pairs.");
				break;
			}
		}
		return isApplicable;
	}
	
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences inosine = new NucleotidSequences(sequences.getSequence(pos1, pos2), sequences.getComplementary(pos1, pos2));
		int numberIU = inosine.calculateNumberOfTerminal('I', 'U');
		
		if ((pos1 == 0 || pos2 == inosine.getDuplexLength() - 1) && numberIU > 0){
			if (this.collector.getTerminal("per_I/U") == null){
				return true;
			}
		}
		return false;
	}
	
}
