package melting.longDanglingEndMethod;

import melting.Environment;

public class Sugimoto02RNADanglingEnd extends SugimotoLongDanglingEndMethod {

	public Sugimoto02RNADanglingEnd(){
		loadData("Sugimoto2002longrde.xml", this.collector);
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
