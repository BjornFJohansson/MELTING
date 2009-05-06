package melting.singleBulgeMethod;

import java.util.HashMap;

import melting.Helper;
import melting.configuration.OptionManagement;

public class Serra07SingleBulgeLoop extends GlobalSingleBulgeLoopMethod{

	/*Martin J Serra et al (2007). Biochemistry 46 : 15123-15135 */
	
	public Serra07SingleBulgeLoop(){
		Helper.loadData("Serra07bulge.xml", this.collector);
	}

	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = super.isApplicable(options, pos1, pos2);
		
		if (hybridization.equals("rnarna") == false){
			System.out.println("WARNING : the single bulge loop parameters of " +
					"Serra et al. (2007) are originally established " +
					"for RNA sequences.");
			
			isApplicable = false;
		}
	
		return isApplicable;
	}

}
