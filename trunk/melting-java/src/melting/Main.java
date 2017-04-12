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
 *       Cambridge, UK.
 *       Babraham Institute, Cambridge, UK. 
 *       e-mail: melting-forum@googlegroups.com
 */

package melting;

import java.text.NumberFormat;
import java.util.List;

import melting.configuration.OptionManagement;
import melting.methodInterfaces.MeltingComputationMethod;
import melting.nearestNeighborModel.NearestNeighborMode;

/**
 * The Melting main class which contains the public static void main(String[] args) method.
 */
public class Main {

  // TODO - support fastq and other formats ? Use the classes FastqReader, SamReaderFactory.makeDefault(), ...
  // see http://plindenbaum.blogspot.co.uk/2010/04/readingwriting-sambam-file-with-picard.html
  // see https://samtools.github.io/htsjdk/javadoc/htsjdk/htsjdk/samtools/example/ExampleSamUsage.html
  

  
	// private static methods
	
  /**
   * Displays the results of Melting : the computed enthalpy and entropy (in cal/mol and J/mol), and the computed 
   * melting temperature (in degrees).
   * 
   * @param results : the ThermoResult containing the computed enthalpy, entropy and
   * melting temperature
   * 
   */
  private static void displaysMeltingResults(ThermoResult results)
  {
    NumberFormat format = NumberFormat.getInstance(); 
    format.setMaximumFractionDigits(2);
    MeltingComputationMethod calculMethod = results.getCalculMethod();

    double enthalpy = results.getEnthalpy();
    double entropy = results.getEntropy();

    OptionManagement.logInfo("\n The MELTING results are : ");
    if (calculMethod instanceof NearestNeighborMode){
      OptionManagement.logInfo("Enthalpy : " + format.format(enthalpy) + " cal/mol ( " + format.format(results.getEnergyValueInJ(enthalpy)) + " J /mol)");
      OptionManagement.logInfo("Entropy : " + format.format(entropy) + " cal/mol-K ( " + format.format(results.getEnergyValueInJ(entropy)) + " J /mol-K)");
    }
    OptionManagement.logInfo("Melting temperature : " + format.format(results.getTm()) + " degrees C.\n");
  }
	
  
  /**
   * @param args : contains the options entered by the user.
   */
  public static void main(String[] args) {

    OptionManagement optionManager = new OptionManagement();
    
    if (args.length == 0){
      optionManager.initialiseLogger();
      optionManager.readMeltingHelp();
    }
    else if (optionManager.isMeltingInformationOption(args)){
      try {
        optionManager.readOptions(args);

      } catch (Exception e) {
        OptionManagement.logError(e.getMessage());
      }
    }
    else {
      // The options look like they are good
      Environment env = optionManager.createEnvironment(args);

      // TODO - In the case of a sliding windows on a set of input sequences, save one file per sequence or just add everything to the same output, adding comments when we change sequence?
      
      if (env.getOptions().containsKey(OptionManagement.inputFile)) {

        if (env.getOptions().get(OptionManagement.sliding_window) == null)  
        {
          List<Environment> results = Helper.computeMeltingOnFastaFile(env.getOptions());
          int i = 1;
        
          for (Environment result : results) {
            if (i == 1) {
              writeColumnHeaders(result.getResult().getCalculMethod());
            }
            writeMeltingResults(result.getSequences().getSequence(), result.getSequences().getComplementary(), result.getResult());
            
            i++;
          }
        }
        else 
        {
          List<List<Environment>> results = Helper.computeMeltingOnFastaFileWithWindows(env.getOptions());
          int i = 1;
          
          for (List<Environment> resultforOneSequence : results) 
          {
            int j = 1;
            // 
            System.out.println("\n\n Processing sequence number " + i + ".\n\n" + resultforOneSequence);
            
            for (Environment result : resultforOneSequence)
            {
              if (j == 1) {
                writeColumnHeaders(result.getResult().getCalculMethod());
              }

              writeMeltingResults(result.getSequences().getSequence(), result.getSequences().getComplementary(), result.getResult());
              j++;
            }
          }          
        }
      }
      else if (env.getOptions().containsKey(OptionManagement.oldInputFile)) 
      {
        // TODO - create methods for the old input file
        
      }
      else 
      {
        // we have a simple sequence as input
      
        if (env.getOptions().get(OptionManagement.sliding_window) == null) 
        {
          Environment result = Helper.computeMeltingResults(env);
          
          displaysMeltingResults(result.getResult());
        }
        else
        {
          List<Environment> results = Helper.computeMeltingResultsWithWindow(env);
          
          int i = 1;
          
          for (Environment result : results) {
            if (i == 1) {
              writeColumnHeaders(result.getResult().getCalculMethod());
            }
            writeMeltingResults(result.getSequences().getSequence(), result.getSequences().getComplementary(), result.getResult());
            
            i++;
          }
        }
      }
    }
  }

  
  /**
   * Writes the melting results as a single line, with each field separated by a tabulation.
   * 
   * @param sequence the input sequence
   * @param csequence the complementary sequence used
   * @param results the melting result to be written
   */
  public static void writeMeltingResults(String sequence, String csequence,
      ThermoResult results)
  {
    NumberFormat format = NumberFormat.getInstance();
    format.setMaximumFractionDigits(2);

    double enthalpy = results.getEnthalpy();
    double entropy = results.getEntropy();

    if (results.getCalculMethod() instanceof NearestNeighborMode) {
      OptionManagement.logInfo(sequence
          + "\t"
          + csequence
          + "\t"
          + format.format(results.getEnergyValueInJ(enthalpy))
          + "\t"
          + format.format(results.getEnergyValueInJ(entropy))
          + "\t"
          + format.format(results.getTm()));
    }
    else {
      OptionManagement.logInfo(sequence + "\t"
          + csequence + "\t"
          + format.format(results.getTm()));
    }
  }

  /**
   * Writes the column headers for the melting results given the {@link MeltingComputationMethod}.
   * 
   * @param calculMethod the melting method used
   */
  private static void writeColumnHeaders(MeltingComputationMethod calculMethod) 
  {
    if (calculMethod instanceof NearestNeighborMode) {
      OptionManagement.logInfo("Sequence\tComplementary\tDeltaH\tDeltaS\t'Tm (deg C)'");
    } else {
      OptionManagement.logInfo("Sequence\tComplementary\tTm (deg C)");
    }
  }

  
  
}
