package melting.cricksNNMethods;

import melting.Environment;

public class Tanaka04 extends DecomposedInitiationNNMethod {

	/*Tanaka Fumiaki et al (2004). Biochemistry 43 : 7143-7150 */
	
	public static String defaultFileName = "Tanaka2004nn.xml";
	
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
			System.out.println("WARNING : The thermodynamic parameters of Tanaka (2004)" +
					"are established for DNA sequences ");
		}
		return isApplicable;
	}
}
