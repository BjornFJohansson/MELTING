package melting.singleBulgeMethod;


import melting.Environment;

public class Serra07SingleBulgeLoop extends GlobalSingleBulgeLoopMethod{

	/*Martin J Serra et al (2007). Biochemistry 46 : 15123-15135 */
	
	public Serra07SingleBulgeLoop(){
		this.fileName = "Serra07bulge.xml";
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("rnarna") == false){
			System.out.println("WARNING : the single bulge loop parameters of " +
					"Serra et al. (2007) are originally established " +
					"for RNA sequences.");
			
			isApplicable = false;
		}
	
		return isApplicable;
	}

}
