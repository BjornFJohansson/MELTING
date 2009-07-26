/* This program is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the 
 * License, or (at your option) any later version
                                
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
 * Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, 
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA                                                                  

 *       Marine Dumousseau and Nicolas Lenovere                                                   
 *       EMBL-EBI, neurobiology computational group,                          
*       Cambridge, UK. e-mail: lenov@ebi.ac.uk, marine@ebi.ac.uk        */

package melting.approximativeMethods;

import java.util.HashMap;
import java.util.logging.Level;

import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.exceptions.NoExistingMethodException;

/**
 * This class represents the model che93. It extends the ApproximativeMode class
 * 
 * Marmur J, Doty P, "Determination of the base composition of 
 * deoxyribonucleic acid from its thermal denaturation temperature", 
 * 1962, Journal of molecular biology, 5, 109-118.
 * 
 * Chester N, Marshak DR, "dimethyl sulfoxide-mediated primer Tm reduction : 
 * a method for analyzing the role of renaturation temperature in the polymerase 
 * chain reaction", 1993, Analytical Biochemistry, 209, 284-290.
 */
public class MarmurChester62_93 extends ApproximativeMode{
	
	// Instance variables
	
	/**
	 * double parameter. This parameter changes if we use the model che93corr.
	 */
	private double parameter;
	
	/**
	 * temperature formula
	 */
	private String temperatureEquation = "Tm = 69.3 + 0.41 * PercentGC - parameter / duplexLength.";
	
	// public methods
	
	@Override
	public ThermoResult computesThermodynamics() {
		double Tm = super.computesThermodynamics().getTm();
		
		Tm = 69.3 + 0.41 * this.environment.getSequences().computesPercentGC() - parameter / (double)this.environment.getSequences().getDuplexLength();
		
		this.environment.setResult(Tm);
		
		OptionManagement.meltingLogger.log(Level.FINE, " from Marmur et al. (1962) and Chester et al (1993)");
		OptionManagement.meltingLogger.log(Level.FINE, temperatureEquation);
		OptionManagement.meltingLogger.log(Level.FINE, "Where parameter = " + parameter);

		return this.environment.getResult();
	}

	@Override
	public boolean isApplicable() {
		boolean isApplicable = super.isApplicable();
		
		if (environment.getSequences().computesPercentMismatching() != 0){
			isApplicable = false;
		}
		
		if (this.environment.getHybridization().equals("dnadna") == false){
			OptionManagement.meltingLogger.log(Level.WARNING, "the formula of Marmur, Doty, Chester " +
					"and Marshak is originally established for DNA duplexes.");
		}
		if (this.environment.getNa() != 0.0 || this.environment.getMg() != 0.0015 || this.environment.getTris() != 0.01 || this.environment.getK() != 0.05){
			isApplicable = false;
		   OptionManagement.meltingLogger.log(Level.WARNING,"the formula of Marmur, Doty, Chester " +
			"and Marshak is originally established at a given ionic strength : " +
			"Na = 0 M, Mg = 0.0015 M, Tris = 0.01 M and k = 0.05 M");
		}
		
		return isApplicable;
	}
	
	@Override
	public void setUpVariables(HashMap<String, String> options) {
		String method = options.get(OptionManagement.approximativeMode);
		
		super.setUpVariables(options);
		if (method.equals("che93corr")){
			parameter = 535;
		}
		else if (method.equals("che93")){
			parameter = 650;
		}
		else {
			throw new NoExistingMethodException("The two possible methods for Marmur and Chester, 1962-1993 are MarmurChester62_93_corr and MarmurChester62_93." +
					"The formula is the same but one factor value change : 535 or 650.");
		}
	}
	
	// protected method
	
	@Override
	protected boolean isNaEqPossible(){
		return false;
	}

}
