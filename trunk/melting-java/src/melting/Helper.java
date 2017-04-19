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

package melting;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import htsjdk.samtools.reference.FastaSequenceFile;
import htsjdk.samtools.reference.ReferenceSequence;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterMethods;
import melting.exceptions.NoExistingMethodException;
import melting.methodInterfaces.MeltingComputationMethod;
import melting.methodInterfaces.SodiumEquivalentMethod;

/**
 * This class contains useful public static methods.
 */
public class Helper {
	
	/**
	 * Checks if the user wants to use another thermodynamic parameters file with the used model.
	 * The new file name is specified in the methodName, preceded by the model name and ":".
	 * 
	 * @param  methodName : the method or model option entered by the user.
	 * @return true if the methodName String contains ":". (a new file name has been entered by the user)
	 * Ex : san04 => the model name (no specified file name, the default file name of the san04 model is used)
	 * Ex : san04:file.xml => the model name followed by a new file name. (the san04 model will be used with the 
	 * thermodynamic parameters of the new file file.xml)
	 */
	public static boolean useOtherDataFile(String methodName){
		if (methodName.contains(":")){
			return true;
		}
		return false;
	}
	
	/**
	 * Extracts the new file name to the method or model option entered by the user.
	 * 
	 * @param  methodName : the method or model option entered by the user.
	 * @return String new file name (or pathway + new file name) containing the thermodynamic parameters.
	 */
	public static String extractsOptionFileName(String methodName){
		return methodName.split(":")[1];
	}
	
	/**
	 * Extracts the method or model name to the method or model option entered by the user.
	 * 
	 * @param  methodName : the method or model option entered by the user.
	 * @return String method or model name.
	 */
	public static String extractsOptionMethodName(String methodName){
		return methodName.split(":")[0];
	}
	
	/**
	 * Computes a sodium equivalent concentration from the different ion concentrations of the Environment object.
	 * 
	 * @param environment the melting {@link Environment}
	 * @return double sodium equivalent concentration.
	 */
	public static double computesNaEquivalent(Environment environment){
		double NaEq = environment.getNa() + environment.getK() + environment.getTris() / 2;
		
		if (environment.getMg() > 0){
			RegisterMethods setNaEqMethod = new RegisterMethods();
			SodiumEquivalentMethod method = setNaEqMethod.getNaEqMethod(environment.getOptions());
			if (method == null){
				throw new NoExistingMethodException("\n There is no implemented method to compute the Na equivalent concentration. Check the option " + OptionManagement.NaEquivalentMethod);
			}
			
			NaEq = method.computeSodiumEquivalent(environment.getNa(), environment.getMg(), environment.getK(), environment.getTris(),environment.getDNTP());
		}
		
		return NaEq;
	}

  /**
   * Computes the entropy, enthalpy and melting temperature for the given {@link Environment} 
   * and returns the results in the given environment.
   * 
   * @param environment {@link Environment} that contain the options (entered by the user or us) that determine the
   *             sequence, hybridization type and other features in order to compute the results.
   * @return The {@link Environment} containing the results of the Melting computation.
   */
  public static Environment computeMeltingResults(Environment environment)
  {
    NumberFormat format = NumberFormat.getInstance();
    format.setMaximumFractionDigits(2);
  
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
   * Computes the entropies, enthalpies and melting temperatures for the given {@link Environment} 
   * and returns the results as a {@link List} of environments.
   * 
   * @param environment {@link Environment} that contain the options (entered by the user or us) that determine the
   *             sequence, hybridization type and other features in order to compute the results.
   * @return The {@link Environment}s containing the results of the Melting computations.
   * @throws IllegalArgumentException if the environment does not defined a sequence '-S' and a window '-w'
   */
  @SuppressWarnings("unchecked")
  public static List<Environment> computeMeltingResultsWithWindow(Environment environment)
  {
    ArrayList<Environment> results = new ArrayList<Environment>();
    HashMap<String, String> options = (HashMap<String, String>) environment.getOptions().clone();

    if (! options.containsKey(OptionManagement.sequence)) {
      throw new IllegalArgumentException("You need to define the sequence option ('" + OptionManagement.sequence + "')");
    }
    if (! options.containsKey(OptionManagement.sliding_window)) {
      throw new IllegalArgumentException("You need to define the sliding window option (" + OptionManagement.sliding_window + ")");
    }
    
    String sequence = options.get(OptionManagement.sequence);
    String complementSeq = options.get(OptionManagement.complementarySequence);
    int slidingWindow = Integer.parseInt(environment.getOptions().get(OptionManagement.sliding_window));
    boolean getComplement = false;
    
    if ((complementSeq != null) && (complementSeq.trim().length() == 0)) {
      getComplement = true;
    }

    int sequenceLength = sequence.length();

    // removing unnecessary option
    options.remove(OptionManagement.complementarySequence);
    
    if (sequenceLength <= slidingWindow) {

      options.put(OptionManagement.sequence, sequence);
      
      if (getComplement) {
        options.put(OptionManagement.complementarySequence, complementSeq);
      }
      
      HashMap<String, String> newOptions = (HashMap<String, String>) options.clone();
      // create the complementary sequence if needed
      OptionManagement.hasRequiredOptions(newOptions);
      
      results.add(computeMeltingResults(new Environment(newOptions)));
    }
    else 
    {
      int max = sequenceLength - slidingWindow + 1;

      for (int i = 0; i < max; i++) 
      {
        String subSeq = sequence.substring(i, i + slidingWindow);
        options.put(OptionManagement.sequence, subSeq);

        if (getComplement) {
          options.put(OptionManagement.complementarySequence, complementSeq.substring(i, i + slidingWindow));
        }

        HashMap<String, String> newOptions = (HashMap<String, String>) options.clone();
        // create the complementary sequence if needed
        OptionManagement.hasRequiredOptions(newOptions);
        
        results.add(computeMeltingResults(new Environment(newOptions)));
      }
    }
    
    return results;
  }
  
  /**
   * Calculates melting temperatures for each sequence present in the input list
   * and return the results in a list of {@link Environment}.
   * 
   * @param sequences input list where to get the sequences
   * @param complement_sequences input list where to get the complementary sequences if given by the user (can be null)
   * @param optionsOrigin user input arguments to run melting
   * 
   */
  @SuppressWarnings("unchecked")
  public static List<Environment> runMeltingOnSequenceList(List<String> sequences, List<String> complement_sequences,
      HashMap<String, String> optionsOrigin) 
  {
    ArrayList<Environment> results = new ArrayList<Environment>();
    HashMap<String, String> options = (HashMap<String, String>) optionsOrigin.clone();
    boolean getComplement = false;
    
    if ((complement_sequences != null) && (complement_sequences.size() == sequences.size())) {
      getComplement = true;
    }
  
    // removing options (if present) that we want to replace and are not necessary
    options.remove(OptionManagement.complementarySequence);
  
    int i = 0;
    for (String seq : sequences) 
    {      
      options.put(OptionManagement.sequence, seq);
      
      if (getComplement)
      {
        // complementary sequence is present
        String cseq = complement_sequences.get(i);
        
        options.put(OptionManagement.complementarySequence, cseq);
      } 
  
      HashMap<String, String> newOptions = (HashMap<String, String>) options.clone();
      // create the complementary sequence if needed
      OptionManagement.hasRequiredOptions(newOptions);

      Environment result = new Environment(newOptions);
      computeMeltingResults(result);
      
      results.add(result);
  
      i++;
    }
    
    return results;
  }

  /**
   * Computes the entropies, enthalpies and melting temperatures for the given options. 
   * 
   * <p>We expect to find in the options a valid input file and a sliding window. If not
   * present, an {@link IllegalArgumentException} will be thrown.</p>
   * 
   * @param optionsOrigin user input arguments to run melting
   * @return a {@link List} of {@link List} of {@link Environment} that contain the computed entropies, enthalpies and melting temperatures for each sequences.
   * @throws IllegalArgumentException if the windows option '-w' is not present, if the input file is not defined
   * or if the input file(s) does not exits or is not readable.
   */
  @SuppressWarnings("unchecked")
  public static List<List<Environment>> computeMeltingOnFastaFileWithWindows(HashMap<String, String> optionsOrigin) {
    
    List<List<Environment>> results = new ArrayList<List<Environment>>();
    HashMap<String, String> options = (HashMap<String, String>) optionsOrigin.clone();
    
    String inputFileName = options.get(OptionManagement.inputFile);
    String inputComplementFileName = options.get(OptionManagement.inputComplementFile);
    String slidingWindowsStr = options.get(OptionManagement.sliding_window);
    boolean readComplement = false;
    
    if (inputFileName == null || inputFileName.trim().length() == 0) {
      
      throw new IllegalArgumentException("You need to define the input file '" + OptionManagement.inputFile + "' option.");
    }
    else if (! (new File(inputFileName).exists())) {
      OptionManagement.logInfo("The input file '" + new File(inputFileName).getAbsolutePath() + "' does not exists or is not readable.");
      throw new IllegalArgumentException("You need to define a valid input file '" + OptionManagement.inputFile + "' option.");
    }
    
    if ((inputComplementFileName != null) && (inputComplementFileName.trim().length() == 0)) {
      if (! (new File(inputComplementFileName).exists())) {
        OptionManagement.logInfo("The complement file '" + new File(inputComplementFileName).getAbsolutePath() + "' does not exists or is not readable.");
        throw new IllegalArgumentException("You need to define a valid complement input file '" + OptionManagement.inputComplementFile + "' option.");
      }
      readComplement = true;
    }
    if ((slidingWindowsStr == null) || (slidingWindowsStr.trim().length() == 0)) {
      throw new IllegalArgumentException("You need to define the sliding windows '" + OptionManagement.sliding_window + "' option.");
    }
    
    OptionManagement.logInfo("Input file = " + inputFileName);
  
    // removing unnecessary options
    options.remove(OptionManagement.inputFile);
    options.remove(OptionManagement.inputComplementFile);
    
    FastaSequenceFile fastaFile = new FastaSequenceFile(new File(inputFileName), true); // TODO - create a way to deal with different input files
    FastaSequenceFile fastaComplementFile = null;
    
    if (readComplement) {
      fastaComplementFile = new FastaSequenceFile(new File(inputComplementFileName), true);
    }
  
    // do a for loop on the input sequence(s)
    ReferenceSequence sequence = null;    
    while ((sequence = fastaFile.nextSequence()) != null) {
      
      String sequenceStr = sequence.getBaseString();
      options.put(OptionManagement.sequence, sequenceStr);
      
      // System.out.println("DEBUG - computeMeltingOnFastaFile - " + sequence.getName() + "\n" + sequenceStr);
      
      if (readComplement) {
        ReferenceSequence complementarySequence = fastaComplementFile.nextSequence();    
        
        if (complementarySequence == null) {
          fastaFile.close();
          if (readComplement) {
            fastaComplementFile.close();
          }
          throw new IllegalArgumentException("The input file ('" + OptionManagement.inputFile + "' option) and the complement input file ('" +
              OptionManagement.inputComplementFile + "' option) need to have the same size.");
        }
        
        options.put(OptionManagement.complementarySequence, sequenceStr);
      }
      
      results.add(computeMeltingResultsWithWindow(new Environment((HashMap<String, String>) options.clone())));
    }
  
    fastaFile.close();
    if (readComplement) {
      fastaComplementFile.close();
    }
    
    return results;
  }

  /**
   * Computes the entropies, enthalpies and melting temperatures for the given options. 
   * 
   * <p>We expect to find in the options a valid fasta input file, if not
   * present, an {@link IllegalArgumentException} will be thrown.</p>
   * 
   * @param optionsOrigin user input arguments to run melting
   * @return a {@link List} of {@link Environment} that contain the computed entropies, enthalpies and melting temperatures for each sequences.
   * @throws IllegalArgumentException if the input file is not defined or if the input file(s) does not exits or is not readable.
   */
  @SuppressWarnings("unchecked")
  public static List<Environment> computeMeltingOnFastaFile(HashMap<String, String> optionsOrigin) {
    
    ArrayList<Environment> results = new ArrayList<Environment>();
    HashMap<String, String> options = (HashMap<String, String>) optionsOrigin.clone();
    
    String inputFileName = options.get(OptionManagement.inputFile);
    String inputComplementFileName = options.get(OptionManagement.inputComplementFile);
    boolean readComplement = false;
    
    if (inputFileName == null || inputFileName.trim().length() == 0) {
      
      throw new IllegalArgumentException("You need to define the input file '" + OptionManagement.inputFile + "' option.");
    }
    else if (! (new File(inputFileName).exists())) {
      OptionManagement.logInfo("The input file '" + new File(inputFileName).getAbsolutePath() + "' does not exists or is not readable.");
      throw new IllegalArgumentException("You need to define a valid input file '" + OptionManagement.inputFile + "' option.");
    }
    
    if ((inputComplementFileName != null) && (inputComplementFileName.trim().length() == 0)) {
      if (! (new File(inputComplementFileName).exists())) {
        OptionManagement.logInfo("The complement file '" + new File(inputComplementFileName).getAbsolutePath() + "' does not exists or is not readable.");
        throw new IllegalArgumentException("You need to define a valid complement input file '" + OptionManagement.inputComplementFile + "' option.");
      }
      readComplement = true;
    }
    
    OptionManagement.logInfo("Input file = " + inputFileName);
  
    // removing unnecessary options
    options.remove(OptionManagement.inputFile);
    options.remove(OptionManagement.inputComplementFile);
    
    FastaSequenceFile fastaFile = new FastaSequenceFile(new File(inputFileName), true);
    FastaSequenceFile fastaComplementFile = null;
    
    if (readComplement) {
      fastaComplementFile = new FastaSequenceFile(new File(inputComplementFileName), true);
    }
  
    // do a for loop on the input sequence(s)
    ReferenceSequence sequence = null;    
    while ((sequence = fastaFile.nextSequence()) != null) {
      
      String sequenceStr = sequence.getBaseString();
      options.put(OptionManagement.sequence, sequenceStr);
      
      // System.out.println("DEBUG - computeMeltingOnFastaFile - " + sequence.getName() + "\n" + sequenceStr);
      
      if (readComplement) {
        ReferenceSequence complementarySequence = fastaComplementFile.nextSequence();    
        
        if (complementarySequence == null) {
          fastaFile.close();
          if (readComplement) {
            fastaComplementFile.close();
          }
          throw new IllegalArgumentException("The input file ('" + OptionManagement.inputFile + "' option) and the complement input file ('" +
              OptionManagement.inputComplementFile + "' option) need to have the same size.");
        }
        
        options.put(OptionManagement.complementarySequence, sequenceStr);
      }
      
      HashMap<String, String> newOptions = (HashMap<String, String>) options.clone();
      // create the complementary sequence if needed
      OptionManagement.hasRequiredOptions(newOptions);

      results.add(computeMeltingResults(new Environment(newOptions)));
    }
  
    fastaFile.close();
    if (readComplement) {
      fastaComplementFile.close();
    }
    
    return results;
  }
	

  /**
   * Computes the entropies, enthalpies and melting temperatures for the given options. 
   * 
   * <p>We expect to find in the options a valid input file, if not
   * present, an {@link IllegalArgumentException} will be thrown.</p>
   * 
   * @param optionsOrigin user input arguments to run melting
   * @return a {@link List} of {@link Environment} that contain the computed entropies, enthalpies and melting temperatures for each sequences.
   * @throws IllegalArgumentException if the input file is not defined or if the input file(s) does not exits or is not readable.
   */
  @SuppressWarnings("unchecked")
  public static List<Environment> computeMeltingOnsimpleFile(HashMap<String, String> optionsOrigin) {
    
    ArrayList<Environment> results = new ArrayList<Environment>();
    HashMap<String, String> options = (HashMap<String, String>) optionsOrigin.clone();
    
    String inputFileName = options.get(OptionManagement.inputFile);
    String inputComplementFileName = options.get(OptionManagement.inputComplementFile);
    boolean readComplement = false;
    
    if (inputFileName == null || inputFileName.trim().length() == 0) {
      
      throw new IllegalArgumentException("You need to define the input file '" + OptionManagement.inputFile + "' option.");
    }
    else if (! (new File(inputFileName).exists())) {
      OptionManagement.logInfo("The input file '" + new File(inputFileName).getAbsolutePath() + "' does not exists or is not readable.");
      throw new IllegalArgumentException("You need to define a valid input file '" + OptionManagement.inputFile + "' option.");
    }
    
    if ((inputComplementFileName != null) && (inputComplementFileName.trim().length() == 0)) {
      if (! (new File(inputComplementFileName).exists())) {
        OptionManagement.logInfo("The complement file '" + new File(inputComplementFileName).getAbsolutePath() + "' does not exists or is not readable.");
        throw new IllegalArgumentException("You need to define a valid complement input file '" + OptionManagement.inputComplementFile + "' option.");
      }
      readComplement = true;
    }
    
    OptionManagement.logInfo("Input file = " + inputFileName);
  
    // removing unnecessary options
    options.remove(OptionManagement.inputFile);
    options.remove(OptionManagement.inputComplementFile);
    
    FastaSequenceFile fastaFile = new FastaSequenceFile(new File(inputFileName), true);
    FastaSequenceFile fastaComplementFile = null;
    
    if (readComplement) {
      fastaComplementFile = new FastaSequenceFile(new File(inputComplementFileName), true);
    }
  
    // do a for loop on the input sequence(s)
    ReferenceSequence sequence = null;    
    while ((sequence = fastaFile.nextSequence()) != null) {
      
      String sequenceStr = sequence.getBaseString();
      options.put(OptionManagement.sequence, sequenceStr);
      
      // System.out.println("DEBUG - computeMeltingOnFastaFile - " + sequence.getName() + "\n" + sequenceStr);
      
      if (readComplement) {
        ReferenceSequence complementarySequence = fastaComplementFile.nextSequence();    
        
        if (complementarySequence == null) {
          fastaFile.close();
          if (readComplement) {
            fastaComplementFile.close();
          }
          throw new IllegalArgumentException("The input file ('" + OptionManagement.inputFile + "' option) and the complement input file ('" +
              OptionManagement.inputComplementFile + "' option) need to have the same size.");
        }
        
        options.put(OptionManagement.complementarySequence, sequenceStr);
      }
      
      results.add(computeMeltingResults(new Environment((HashMap<String, String>) options.clone())));
    }
  
    fastaFile.close();
    if (readComplement) {
      fastaComplementFile.close();
    }
    
    return results;
  }
    

}
