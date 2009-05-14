package melting.cricksNNMethods;

import melting.Environment;

public class AllawiSantalucia97 extends DecomposedInitiationNNMethod {

	/*Allawi and SantaLucia (1997). Biochemistry 36 : 10581-10594 */
	
	public static String defaultFileName = "AllawiSantalucia1997nn.xml";

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
			System.out.println("WARNING : The thermodynamic parameters of Allawi and Santalucia (1997)" +
					"are established for DNA sequences ");
		}
		return isApplicable;
	}
	
}
