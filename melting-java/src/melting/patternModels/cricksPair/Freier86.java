/* This program is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the 
 * License, or (at your option) any later version
                                
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General
 * Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, 
 * write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA                                                                  

 *       Marine Dumousseau and Nicolas Le Novere                                                   
 *       EMBL-EBI, neurobiology computational group,                          
 *       Cambridge, UK. e-mail: melting-forum@googlegroups.com        */

package melting.patternModels.cricksPair;


import melting.Environment;
import melting.ThermoResult;
import melting.configuration.OptionManagement;
import melting.sequences.NucleotidSequences;
import melting.methodInterfaces.NamedMethod;

/**
 * This class represents the nearest neighbor model fre86. It extends CricksNNMethod.
 * 
 * Freier et al (1986) Proc Natl Acad Sci USA 83: 9373-9377
 */
public class Freier86 extends CricksNNMethod
  implements NamedMethod
{
	
	// Instance variables
	
	/**
	 * String defaultFileName : default name for the xml file containing the thermodynamic parameters for each Crick's pair
	 */
	public static String defaultFileName = "Freier1986nn.xml";

  /**
   * Full name of the method.
   */
  private static String methodName = "Freier et al. (1986)";
	
	// PatternComputationMethod interface implementation

	@Override
	public boolean isApplicable(Environment environment, int pos1, int pos2) {

		if (environment.getHybridization().equals("rnarna") == false){

			OptionManagement.logWarning("\n The model of Freier et al. (1986)" +
			"is established for RNA sequences.");	
		}

		return super.isApplicable(environment, pos1, pos2);
	}
	
	@Override
	public ThermoResult computeThermodynamics(NucleotidSequences sequences,
			int pos1, int pos2, ThermoResult result) {
    OptionManagement.logMethodName(methodName);
    OptionManagement.logFileName(this.fileName);

		NucleotidSequences newSequences = sequences.getEquivalentSequences("rna");
		
		return super.computeThermodynamics(newSequences, pos1, pos2, result);
	}
	
	@Override
	public boolean isMissingParameters(NucleotidSequences sequences, int pos1,
			int pos2) {
		NucleotidSequences newSequences = sequences.getEquivalentSequences("rna");
		
		return super.isMissingParameters(newSequences, pos1, pos2);
	}
	
	@Override
	public void initialiseFileName(String methodName){
		super.initialiseFileName(methodName);
		
		if (this.fileName == null){
			this.fileName = defaultFileName;
		}
	}

  /**
   * Gets the full name of the method.
   * @return The full name of the method.
   */
  @Override
  public String getName()
  {
    return methodName;
  }
}
