package melting.cricksNNMethods;

import melting.Environment;

public class Freier86 extends CricksNNMethod {
	
	/*Freier et al (1986) Proc Natl Acad Sci USA 83: 9373-9377 */
	
	public Freier86() {
		this.fileName = "Freier1986nn.xml";	
		}
	
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		boolean isApplicable = isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("rnarna") == false){
			if (environment.getHybridization().equals("mrnarna") == false){
				isApplicable = false;
			}
			System.out.println("WARNING : It is possible to use the thermodynamic parameters of Freier et al. (1986)" +
					"for 2_O methyl RNA dulexes but these parameters are originally established for RNA/RNA sequences.");
		}
		return isApplicable;
	}

}
