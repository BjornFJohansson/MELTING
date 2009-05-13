package melting.modifiedNucleicAcidMethod;


import melting.Environment;

public class Santalucia05Inosine extends InosineNNMethod{

	/*Santalucia et al.(2005). Nucleic acids research 33 : 6258-6267*/
	
	public Santalucia05Inosin(){
		this.fileName = "Santalucia2005inomn.xml";
	}
	
	public boolean isApplicable(Environment environment, int pos1,
			int pos2) {

		boolean isApplicable = super.isApplicable(environment, pos1, pos2);
		
		if (environment.getHybridization().equals("dnadna") == false) {
			System.err.println("WARNING : The thermodynamic parameters for inosine base of" +
					"Santalucia (2005) are established for DNA sequences.");
			isApplicable = false;
		}
		
		return isApplicable;
	}
	
}
