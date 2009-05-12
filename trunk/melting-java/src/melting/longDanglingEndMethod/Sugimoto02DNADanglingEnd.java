package melting.longDanglingEndMethod;

import melting.Environment;


public class Sugimoto02DNADanglingEnd extends SugimotoLongDanglingEndMethod {

	public Sugimoto02DNADanglingEnd(){
		loadData("Sugimoto2002longdde.xml", this.collector);
	}
	
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("dnadna")){
			isApplicable = false;
			System.out.println("WARNING : the used thermodynamic parameters for long dangling end of Sugimoto et al." +
					"(2002) are established for DNA sequences.");
		}
		
		return isApplicable;
	}
}
