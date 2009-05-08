package melting.cricksNNMethods;

import melting.Environment;

public class Breslauer86 extends GlobalInitiationNNMethod{

	/* (1986). Proc Natl Acad Sci USA 83 : 3746-3750 */
	
	public Breslauer86() {
		super("Breslauer1986nn.xml");
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
