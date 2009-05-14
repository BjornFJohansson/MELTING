package melting.cricksNNMethods;

import melting.Environment;

public class Breslauer86 extends GlobalInitiationNNMethod{

	/* (1986). Proc Natl Acad Sci USA 83 : 3746-3750 */
	
	public static String defaultFileName = "Breslauer1986nn.xml";
	
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
			System.out.println("WARNING : The thermodynamic parameters of Breslauer et al (1986)" +
					"are established for DNA sequences ");
		}
		return isApplicable;
	}
}
