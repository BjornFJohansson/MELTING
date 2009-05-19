package melting.cricksNNMethods;

import melting.Environment;
import melting.NucleotidSequences;
import melting.ThermoResult;

public class Santalucia96 extends GlobalInitiationNNMethod {

	/*SantaLucia et al.(1996). Biochemistry 35 : 3555-3562*/                    
	
	public static String defaultFileName = "Santalucia1998nn.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		boolean isApplicable = isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : The thermodynamic parameters of Santalucia et al (1996)" +
					"are established for DNA sequences ");
		}
		return isApplicable;
	}
	
	public ThermoResult calculateInitiationHybridation(Environment environment){
		
		environment.setResult(super.calculateInitiationHybridation(environment));
		NucleotidSequences withoutTerminalUnpairedNucleotides =  environment.getSequences().removeTerminalUnpairedNucleotides();
		
		if (withoutTerminalUnpairedNucleotides == null){
			return null;
		}
		double enthalpy = 0;
		double entropy = 0;
		
		if (withoutTerminalUnpairedNucleotides.isTerminal5AT()) {
			enthalpy += this.collector.getTerminal("5_T/A").getEnthalpy();
			entropy += this.collector.getTerminal("5_T/A").getEntropy();
		}
		
		environment.setResult(enthalpy, entropy);
		return environment.getResult();
	}
}
