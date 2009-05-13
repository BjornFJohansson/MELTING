package melting.cricksNNMethods;

import melting.Environment;

public class Sugimoto96 extends CricksNNMethod {
	
	/*Sugimoto et al. (1996). Nuc Acids Res 24 : 4501-4505*/ 
	
	public Sugimoto96(){
		this.fileName = "Sugimoto1996nn.xml";
	}
	
	public boolean isApplicable(Environment environment, int pos1, int pos2) {
		boolean isApplicable = isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : The thermodynamic parameters of Sugimoto et al (1996)" +
					"are established for DNA sequences ");
		}
		return isApplicable;
	}
}
