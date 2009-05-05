package melting.singleMismatchMethods;

import melting.Helper;

public class Znosco07mm extends ZnoscoMethod {

	/*REF: Brent M Znosko et al (2007). Biochemistry 46: 13425-13436.*/
	
	public Znosco07mm() {
		Helper.loadData("Znosco2007mm.xml", this.collector);
	}

}
