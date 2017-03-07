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

import htsjdk.samtools.reference.FastaSequenceFile;
import htsjdk.samtools.reference.ReferenceSequence;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import melting.configuration.OptionManagement;
import melting.configuration.RegisterMethods;
import melting.methodInterfaces.MeltingComputationMethod;
import melting.nearestNeighborModel.NearestNeighborMode;

/**
 * The Melting main class which contains the public static void main(String[] args) method.
 */
public class Main {

	// private static methods
	
  /**
   * Compute the entropy, enthalpy and the melting temperature and display the results. 
   * 
   * @param args : contains the options entered by the user.
   * @param optionManager : the OptionManagement which allows to manage
   * the different options entered by the user.
   */
  private static ThermoResult runMelting(String [] args, OptionManagement optionManager){
    try {
      ThermoResult results = getMeltingResults(args, optionManager).getResult();
      displaysMeltingResults(results);
      return results;

    } catch (Exception e) {
      OptionManagement.logError(e.getMessage());
      return null;
    }
  }

  /**
   * Compute the entropy, enthalpy and the melting temperature and display the results.
   * 
   * @param initArgs : contains the options entered by the user.
   * @param optionManager : the OptionManagement which allows to manage
   * the different options entered by the user.
   * @param baseString : the input sequence
   */
  private static void runMeltingSequenceFromInputFile(String[] initArgs,
      OptionManagement optionManager, String baseString) 
  {
    int inputFileIndex = -1;
    String[] args = Arrays.copyOf(initArgs, initArgs.length);
    
    for (int i = 0; i < args.length; i++)
    {
      if (args[i].equals(OptionManagement.inputFile))
      {
        inputFileIndex = i;
        break;
      }
    }
    
    if (inputFileIndex != -1) 
    {
      args[inputFileIndex] = OptionManagement.sequence;
      args[inputFileIndex + 1] = baseString;
    }
    
    runMelting(args, optionManager);
  }


  /**
   * Compute the entropy, enthalpy and melting temperature, and return 
   * these results.
   * 
   * @param args options (entered by the user) that determine the
   *             sequence, hybridization type and other features of the
   *             environment.
   * @param optionManager the {@link OptionManagement} which
   *                      allows the program to manage the different
   *                      options entered by the user.  
   * @return The {@link Environment} containing the results of the Melting computation.
   */
  public static Environment getMeltingResults(String[] args,
      OptionManagement optionManager)
  {
    NumberFormat format = NumberFormat.getInstance();
    format.setMaximumFractionDigits(2);

    // Set up the environment from the supplied arguments and get the 
    // results.
    Environment environment = optionManager.createEnvironment(args);
    RegisterMethods register = new RegisterMethods();
    MeltingComputationMethod calculMethod = register.getMeltingComputationMethod(environment.getOptions());
    ThermoResult results = calculMethod.computesThermodynamics();
    results.setCalculMethod(calculMethod);
    environment.setResult(results);

    // Apply corrections to the results.
    results = calculMethod.getRegister().computeOtherMeltingCorrections(environment);
    environment.setResult(results);
    return environment;
  }

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
	
  // public static main method

  
  // TODO - do a second method that take an array of sequences and return an array of ThermoResult ?
  // TODO - support fasta and fastq ? Use the classes FastaSequenceFile and FastqReader
  // TODO - separate the calculations from a set of arguments from the display of the result, so that we can re-use the same API for the command line and the GUI
  

  
  /**
   * @param args : contains the options entered by the user.
   */
  public static void main(String[] args) {

    OptionManagement optionManager = new OptionManagement();
    Environment env = optionManager.createEnvironment(args);
    
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
      
      // TODO - check if an input file was given instead of a single sequence !
      // In this case, save one file per sequence or just add everything to the same output, adding comments when we change sequence.
      ArrayList<String> inputSequences = new ArrayList<String>();
      
      if (env.getOptions().containsKey(OptionManagement.inputFile)) {
        String inputFileName = env.getOptions().get(OptionManagement.inputFile);
        
        System.out.println("Input file = " + inputFileName);
        
        FastaSequenceFile fastaFile = new FastaSequenceFile(new File(inputFileName), true);

        // do a for loop on the input sequence(s)
        ReferenceSequence sequence = null;
        while ((sequence = fastaFile.nextSequence()) != null) {
          System.out.println(sequence.getName() + "\n" + sequence.getBaseString());
          
          runMeltingSequenceFromInputFile(args, optionManager, sequence.getBaseString());
        }
        return;
      }
      else 
      {
        // TODO - add the sequence + complementary if available ?
      }


      
      if (env.getOptions().get(OptionManagement.sliding_window) != null) 
      {

        int slidingWindow = Integer.parseInt(env.getOptions().get(OptionManagement.sliding_window));
        String sequence = env.getSequences().getSequence(); // TODO - slide on the complementary sequence as well if provided
        int sequenceLength = sequence.length();

        // System.out.println("Sliding windows was used: " + slidingWindow);

        // generating the list of sequences 
        ArrayList<String> sequences = new ArrayList<String>();

        if (sequenceLength <= slidingWindow) {
          sequences.add(sequence);
        }
        else 
        {
          int max = sequenceLength - slidingWindow;

          for (int i = 0; i < max; i++) 
          {
            String subSeq = sequence.substring(i, i + slidingWindow);
            sequences.add(subSeq);
          }
        }

        // System.out.println("sliced sequences : " + sequences);

        // launch melting on each sequences
        List<Environment> results = runMeltingOnSequenceList(sequences, null, args, optionManager);

        if (results.size() > 0) 
        {
          writeColumnHeaders(results.get(0).getResult().getCalculMethod());
        }

        for (Environment result : results)
        {
          writeMeltingResults(result.getSequences().getSequence(), result.getSequences().getComplementary(), result.getResult());
        }

      }
      else 
      {
        runMelting(args, optionManager);
      }
    }
  }
  
  /**
   * Calculates melting temperatures for each sequence present in the input list
   * and return the results in a list of {@link ThermoResult}.
   * 
   * @param sequences input list where to get the sequences
   * @param complement_sequences input list where to get the complementary sequences if given by the user (can be null)
   * @param defaultArgs user input arguments to run melting
   * 
   */
  public static List<Environment> runMeltingOnSequenceList(List<String> sequences, List<String> complement_sequences,
      String[] defaultArgs, OptionManagement optionManager) 
  {
    ArrayList<Environment> results = new ArrayList<Environment>();
    boolean getComplement = false;
    
    if ((complement_sequences != null) && (complement_sequences.size() == sequences.size())) {
      getComplement = true;
    }
    String[] newArgs = (getComplement) ? new String[4] : new String[2];
    
    int i = 0;
    for (String seq : sequences) 
    {
      if (!getComplement)
      {
        // sequence is alone, prefix with -S
        newArgs[0] = "-S";
        newArgs[1] = seq;
      }
      else
      {
        // complementary sequence is present
        String cseq = complement_sequences.get(i);

        newArgs[0] = "-S";
        newArgs[1] = seq;
        newArgs[2] = "-C";
        newArgs[3] = cseq;
      } 

      String[] completeArgs = new String[defaultArgs.length + newArgs.length];
      int j = 0;

      for (String arg : defaultArgs) {
        completeArgs[j++] = arg;
      }
      for (String arg : newArgs) {
        completeArgs[j++] = arg;
      }

      Environment result = getMeltingResults(completeArgs, optionManager);
      results.add(result);

      i++;
    }
    
    return results;
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
