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

package melting;

/**
 * This class extends the Thermodynamics class. It represents the computed thermodynamic results of Melting
 * It contains a Tm double and a salIndependentEntropy double as instance variables in addition to the instance variables
 * of the Thermodynamics class.
 */
public class ThermoResult extends Thermodynamics{
	
	// Instance variables
	
	/**
	 * double Tm : the computed melting temperature
	 */
	private double Tm;
	
	/**
	 * double saltIndependentEntropy : a computed entropy representing the entropy of a pattern in the duplex
	 * which is independent of the salt concentration. No salt correction will be applied to the entropy value.
	 */
	private double saltIndependentEntropy;
	
	// ThermoResult constructor
	
	/**
	 * creates a ThermoResult object initialised with the double enthalpy, double entropy and double Tm.
	 * @param double enthalpy : the enthalpy value
	 * @param double entropy : the entropy value
	 * @param double Tm : the melting temperature
	 */
	public ThermoResult(double enthalpy, double entropy, double Tm){
		super(enthalpy, entropy);
		this.Tm = Tm;
		this.saltIndependentEntropy = 0;
	}
	
	// public methods
	
	/**
	 * This method is called to get the Tm double of a ThermoResult object.
	 * @return the Tm double of a ThermoResult object.
	 */
	public double getTm() {
		return Tm;
	}
	
	/**
	 * This method is called to change the Tm double value of a ThermoResult object.
	 * @param double tm : the new melting temperature
	 */
	public void setTm(double tm) {
		Tm = tm;
	}
	
	/**
	 * This method is called to get the SaltIndependentEntropy double of a ThermoResult object.
	 * @return the SaltIndependentEntropy double of a ThermoResult object.
	 */
	public double getSaltIndependentEntropy() {
		return saltIndependentEntropy;
	}

	/**
	 * This method is called to change the SaltIndependentEntropy double value of a ThermoResult object.
	 * @param double SaltIndependentEntropy : the new salt independent concentration.
	 */
	public void setSaltIndependentEntropy(double saltIndependentEntropy) {
		this.saltIndependentEntropy = saltIndependentEntropy;
	}

	/**
	 * converts the energyValy double in cal/mol into an energy value in J/mol.
	 * @param double energyValue : a thermodynamic energy value in cal/mol (enthalpy or entropy)
	 * @return a double representing the energy value in J/mol.
	 */
	public double getEnergyValueInJ(double energyValue){
		return energyValue * 4.18;
	}

}
