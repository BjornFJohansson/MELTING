package melting.singleMismatchMethods;

public class Znosco07mm extends ZnoscoMethod {

	/*REF: Brent M Znosko et al (2007). Biochemistry 46: 13425-13436.*/
	
	public static String defaultFileName = "Znosco2007mm.xml";
	
	@Override
	public void initializeFileName(String methodName){
		super.initializeFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}

}
