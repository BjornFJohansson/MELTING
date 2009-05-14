package melting.singleDanglingEndMethod;


import melting.Environment;

public class Bommarito00SingleDanglingEnd extends SingleDanglingEndMethod {

	/*REF: Bommarito et al. (2000). Nuc Acids Res 28: 1929-1934 */
	
	public static String defaultFileName = "Bommarito2000de.xml";
	
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
		if (environment.getHybridization().equals("dnadna") == false) {
			System.err.println("The thermodynamic parameters for dangling ends" +
					"of Bommarito (2000) are established for DNA sequences.");
			isApplicable = false;
		}
		return isApplicable;
	}
	
}
