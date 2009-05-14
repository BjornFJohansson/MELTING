package melting.longDanglingEndMethod;

import melting.Environment;

public class Sugimoto02RNADanglingEnd extends SugimotoLongDanglingEndMethod {

/*REF: Sugimoto et al. (2002). J. Am. Chem. Soc. 124: 10367-10372 */
	
	public static String defaultFileName = "Sugimoto2002longrde.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}
	
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("rnarna")){
			isApplicable = false;
			System.out.println("WARNING : the used thermodynamic parameters for long dangling end of Sugimoto et al." +
					"(2002) are established for RNA sequences.");
		}
		
		return isApplicable;
	}
}
