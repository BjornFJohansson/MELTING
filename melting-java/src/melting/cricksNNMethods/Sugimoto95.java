package melting.cricksNNMethods;


import java.util.logging.Level;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;
import melting.Thermodynamics;
import melting.configuration.OptionManagement;
import melting.exceptions.MethodNotApplicableException;

public class Sugimoto95 extends CricksNNMethod {
	
	/*Sugimoto et al. (1995). Biochemistry 34 : 11211-11216*/ 
	
	public static String defaultFileName = "Sugimoto1995nn.xml";

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
		OptionManagement.meltingLogger.log(Level.FINE, "The thermodynamic parameters for the watson crick base pairs are from Sugimoto et al (1995).");
		
		double enthalpy = result.getEnthalpy();
		double entropy = result.getEntropy();
		
		Thermodynamics NNValue;
		for (int i = pos1; i <= pos2 - 1; i++){
			NNValue = this.collector.getNNvalue("d" + sequences.getSequenceNNPair(i), "r" + sequences.getComplementaryNNPair(i));
			
			OptionManagement.meltingLogger.log(Level.FINE, "d"+ sequences.getSequenceNNPair(i) + "/" + "r" + sequences.getComplementaryNNPair(i) + " : enthalpy = " + NNValue.getEnthalpy() + "  entropy = " + NNValue.getEntropy());

			enthalpy += NNValue.getEnthalpy();
			entropy += NNValue.getEntropy();
		}
		
		result.setEnthalpy(enthalpy);
		result.setEntropy(entropy);
		
		return result;
	}
	
	@Override
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		boolean isApplicable = true;
		if (environment.getHybridization().equals("dnarna") == false && environment.getHybridization().equals("rnadna") == false){
			isApplicable = false;
			OptionManagement.meltingLogger.log(Level.WARNING, "The thermodynamic parameters of Sugimoto et al. (1995)" +
					"are established for hybrid DNA/RNA sequences.");
		}
		
		isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.isSelfComplementarity()){
			throw new MethodNotApplicableException ( "\n The thermodynamic parameters of Sugimoto et al. (1995)" +
					"are established for hybrid DNA/RNA sequences and they can't be self complementary sequence.");
		}
		return isApplicable;
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		boolean isMissing = false;
		for (int i = pos1; i <= pos2 - 1; i++){

			if (this.collector.getNNvalue("d" + sequences.getSequenceNNPair(i), "r" + sequences.getComplementaryNNPair(i)) == null){
				isMissing = true;
			}
		}
		return isMissing;
	}
}
