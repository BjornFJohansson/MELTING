package melting.singleBulgeMethod;

import java.util.HashMap;

import melting.configuration.OptionManagement;

public class Tanaka04SingleBulgeLoop extends GlobalSingleBulgeLoopMethod{

	/*Tanaka Fumiaki et al (2004). Biochemistry 43 : 7143-7150*/
	
	public Tanaka04SingleBulgeLoop(){
		loadData("Tanaka2004Bulge.xml", this.collector);
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = super.isApplicable(options, pos1, pos2);
		
		if (hybridization.equals("dnadna") == false){
			System.out.println("WARNING : the single bulge loop parameters of " +
					"Tanaka (2004) are originally established " +
					"for DNA sequences.");
			
			isApplicable = false;
		}
	
		return isApplicable;
	}

}
