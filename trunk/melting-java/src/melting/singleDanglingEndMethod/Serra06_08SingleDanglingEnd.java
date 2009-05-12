package melting.singleDanglingEndMethod;


import melting.Environment;

public class Serra06_08SingleDanglingEnd extends SingleDanglingEndMethod {

	/*REF: Martin J Serra et al. (2006). Nucleic Acids research 34: 3338-3344
	REF: Martin J Serra et al. (2008). Nucleic Acids research 36: 5652-5659 */
	
	public Serra06_08SingleDanglingEnd(){
		loadData("Serra2006_2008de.xml",this.collector);
	}
	
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {
		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("rnarna") == false) {
			System.err.println("The thermodynamic parameters for dangling ends" +
					"of Serra et al. (2006 - 2008) are established for RNA sequences.");
			isApplicable = false;
		}
		return isApplicable;
	}
}
