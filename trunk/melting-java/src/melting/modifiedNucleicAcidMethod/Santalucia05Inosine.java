package melting.modifiedNucleicAcidMethod;

import java.util.HashMap;

import melting.Helper;
import melting.configuration.OptionManagement;

public class Santalucia05Inosine extends InosineNNMethod{

	/*Santalucia et al.(2005). Nucleic acids research 33 : 6258-6267*/
	
	public Santalucia05Inosine(){
		Helper.loadData("Santalucia2005inomn.xml", this.collector);
	}
	
	public boolean isApplicable(HashMap<String, String> options, int pos1,
			int pos2) {
		String hybridization = options.get(OptionManagement.hybridization);
		boolean isApplicable = super.isApplicable(options, pos1, pos2);
		
		if (hybridization.equals("dnadna") == false) {
			System.err.println("WARNING : The thermodynamic parameters for inosine base of" +
					"Santalucia (2005) are established for DNA sequences.");
			isApplicable = false;
		}
		
		return isApplicable;
	}
	
}
