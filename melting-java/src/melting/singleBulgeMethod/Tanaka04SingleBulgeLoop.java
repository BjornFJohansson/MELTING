package melting.singleBulgeMethod;


import melting.Environment;

public class Tanaka04SingleBulgeLoop extends GlobalSingleBulgeLoopMethod{

	/*Tanaka Fumiaki et al (2004). Biochemistry 43 : 7143-7150*/
	
	public Tanaka04SingleBulgeLoop(){
		loadData("Tanaka2004Bulge.xml", this.collector);
	}

	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("dnadna") == false){
			System.out.println("WARNING : the single bulge loop parameters of " +
					"Tanaka (2004) are originally established " +
					"for DNA sequences.");
			
			isApplicable = false;
		}
	
		return isApplicable;
	}

}
