package melting.cricksNNMethods;

import melting.Environment;

public class Tanaka04 extends DecomposedInitiationNNMethod {

	/*Tanaka Fumiaki et al (2004). Biochemistry 43 : 7143-7150 */
	
	public Tanaka04(){
		this.fileName = "Tanaka2004nn.xml";
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
