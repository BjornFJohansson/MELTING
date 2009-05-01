package melting.approximativeMethods;

import java.util.HashMap;

import melting.ThermoResult;
import melting.configuration.OptionManagement;

public class Santalucia98 extends ApproximativeMode{
	
	/* Santalucia J Jr, "A unified view of polymer, dumbbel, and 
	 * oligonucleotide DNA nearest-neighbor thermodynamics", Proc
	 * Nacl Acad Sci USA 1998, 95, 1460-1465.
	 * */

	public ThermoResult CalculateThermodynamics() {
		ThermoResult result = super.CalculateThermodynamics();
		
		this.Tm = 77.1 + 11.7 * Math.log10(this.Na) + 0.41 * this.percentGC - 528 / this.duplexLength;
		
		
		result.setTm(this.Tm);
		return result;
	}

	public boolean isApplicable() {
boolean isApplicable = super.isApplicable();
		
		if (this.hybridization.equals("dnadna") == false){
			isApplicable = false;
			System.out.println("WARNING : the polymer Santalucia equation" +
					"was originally established for DNA duplexes.");
		}
		
		return isApplicable;
	}
	
	public void setUpVariable(HashMap<String, String> options) {
		super.setUpVariable(options);
	}

}
