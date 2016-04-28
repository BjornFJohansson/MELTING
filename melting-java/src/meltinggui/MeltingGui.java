// This program is free software; you can redistribute it and/or modify it
// under the terms of the GNU General Public Licence as published by the Free
// Software Foundation; either verison 2 of the Licence, or (at your option)
// any later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public Licence for
// more details.  
//
// You should have received a copy of the GNU General Public Licence along with
// this program; if not, write to the Free Software Foundation, Inc., 
// 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
//
// Marine Dumousseau, Nicolas Lenovere
// EMBL-EBI, neurobiology computational group,             
// Cambridge, UK. e-mail: lenov@ebi.ac.uk, marine@ebi.ac.uk

package meltinggui;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import melting.BatchMain;
import melting.Environment;
import melting.configuration.OptionManagement;
import melting.configuration.RegisterMethods;
import melting.methodInterfaces.MeltingComputationMethod;
import meltinggui.frames.MeltingFrame;
import meltinggui.frames.OuterFrame;

/**
 * The Main class for the Melting GUI. 
 */
public class MeltingGui
  extends JFrame implements Observer
{
  /**
   * Frame holding all the other components.
   */
  private OuterFrame outerFrame;
 
  /**
   * The text that would be entered into the command line were the user
   * using the command line.
   */
  private String commandLineText = new String("");

  /**
   * The mandatory part of the command line text.
   */
  private String mandatoryCommandLineText = new String("");

  /**
   * The optional part of the command line text.
   */
  private String generalCommandLineText = new String("");

  
  /**
   * Sets up the GUI and displays the main MELTING frame.
   */
  public MeltingGui()
  {
    outerFrame = new OuterFrame();
    outerFrame.addObserver(this);
  }

  
  /**
   * Receives command line options and sends out the MELTING results.
   */
  public void update(Observable observable, Object message)
  {
    String[] argsOption;
    melting.ThermoResult results;

    if (message instanceof ArgsMessage)
    {
      ArgsMessage argsMessage = (ArgsMessage) message;
      switch (argsMessage.getArgumentType()) {
        case MANDATORY :
          mandatoryCommandLineText = argsMessage.getCommandLineText();
          break;

        case GENERAL :
          generalCommandLineText = argsMessage.getCommandLineText();
          break;

        default:
          // Add appropriate message of exasperation here.
          break;
      }
    }

    commandLineText = mandatoryCommandLineText + generalCommandLineText; 
    argsOption = commandLineText.trim().split(" +");
    outerFrame.clearErrors();
    
    
	//results = getMeltingResults(argsOption);
    // The following does the same essentially, but keeps track of the environment and calculMethod, which are 
    // used for printing later.
    
    
    // TODO - For some reason the selected algorithm is not NearestNeighborMode. 
    // Therefore the entrophy and entalphy are not shown. 
    // These instead are shown when melting.jar is executed...
    // Don't know why this happens as the code should be the same.. 
    
	OptionManagement manager = new OptionManagement();
	Environment environment = manager.createEnvironment(argsOption);
	RegisterMethods register = new RegisterMethods();
	MeltingComputationMethod calculMethod = register
			.getMeltingComputationMethod(environment.getOptions());
	results = calculMethod.computesThermodynamics();
	environment.setResult(results);
	
	results = calculMethod.getRegister()
			.computeOtherMeltingCorrections(environment);
	
	environment.setResult(results);
	
	File sequenceFile = outerFrame.getSequenceFile();
	if(sequenceFile != null) {
	    OutputStream os = null; 
	    try {
	      String outputFileName = sequenceFile.getAbsolutePath()+".results.csv";
			os = new FileOutputStream(outputFileName);
			BatchMain.displaysMeltingResults(os, environment, environment.getResult(), calculMethod);
			outerFrame.setStatusPanelText("Results written to "+sequenceFile.getName()+".results.csv");
			displayFileinNewWindow(outputFileName);
		} catch (FileNotFoundException e) {
			outerFrame.setStatusPanelText("File " + sequenceFile.getAbsolutePath()+".results.csv" + " could not be written");
		} catch (IOException e) {
			outerFrame.setStatusPanelText("Error writing file " + sequenceFile.getAbsolutePath()+".results.csv");
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
	// TODO - if we have a sequence file, don't display the normal results !! Only the sequence file results.
	
    outerFrame.displayMeltingResults(results);
    
    
    
    
    
  }

  /**
   * Gets results from the MELTING program.
   * 
   * @param argsOption Command line arguments for MELTING.
   * @return The results from MELTING.
   */
  private melting.ThermoResult getMeltingResults(String[] argsOption)
  {
    melting.ThermoResult results = new melting.ThermoResult(0.0, 0.0, 0.0);
    MeltingCalculator calculator = new MeltingCalculator(argsOption);
    try {
      results = calculator.fetchMeltingResults();
    }
    catch (RuntimeException exception) {
      outerFrame.logException(exception);
    }

    return results;
  }
  
  /**
   * Shows the content of a given file in a new windows.
   * 
   * @param outputFileName the path of the file to show.
   */
  private void displayFileinNewWindow(String outputFileName) 
  {
    // TODO - add a parameter to tell if we display the whole file or only a short preview (250 to 500 characters maximum)
    
    if ((outputFileName == null) || (!new File(outputFileName).exists())) {
      System.out.println("Output file '" + outputFileName + "' does not seem to exist !");
      return;
    }
    
    try {
      System.out.println("Trying to read '" + outputFileName + "'.");
      String outputFileContent = readFile(outputFileName, Charset.forName("UTF-8"));
      JFrame outputFileJFrame = new JFrame("Results saved on file : " + outputFileName);
      JTextArea outputFileTA = new JTextArea(outputFileContent);
      outputFileTA.setEditable(false);
      
      outputFileJFrame.add(new JScrollPane(outputFileTA));
      outputFileJFrame.setMinimumSize(new Dimension(800, 600));
      outputFileJFrame.setVisible(true);
      
    } catch (IOException e) {
      System.out.println("There was a problem opening the ouput file: '" + e.getMessage() + "'");
    } 
    
  }

  /**
   * Reads the content of a file, using the provided encoding, into a String.
   * 
   * @param path the path of the file to be read
   * @param encoding the encoding to be used to read the file
   * @return the content of the file
   * @throws IOException if an IO error happen.
   */
  static String readFile(String path, Charset encoding) 
      throws IOException 
  {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, encoding);
  }

  
  /**
   * Starts up the new frame, and creates an instance of MELTING on it.
   * @param args - Arguments from the command line.
   */
  public static void main(String args[])
  {
    // Get the Nimbus look and feel, if supported (Java SE 6 or later).  
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info :
        UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    }
    catch (ClassNotFoundException exception) {
      Logger.getLogger(MeltingFrame.class.getName()).log(Level.SEVERE,
                                                         null,
                                                         exception);
    } 
    catch (InstantiationException exception) {
      Logger.getLogger(MeltingFrame.class.getName()).log(Level.SEVERE,
                                                         null,
                                                         exception);
    } 
    catch (IllegalAccessException exception) {
      Logger.getLogger(MeltingFrame.class.getName()).log(Level.SEVERE,
                                                         null,
                                                         exception);
    } 
    catch (UnsupportedLookAndFeelException exception) {
      Logger.getLogger(MeltingFrame.class.getName()).log(Level.SEVERE,
                                                         null,
                                                         exception);
    } 

    java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run()
                    {
                      new MeltingGui();
                    }
                  });
  }
}
