// This program is free software; you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the Free
// Software Foundation; either version 2 of the License, or (at your option)
// any later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
// more details.  
//
// You should have received a copy of the GNU General Public License along with
// this program; if not, write to the Free Software Foundation, Inc., 
// 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
//
// Marine Dumousseau, Nicolas Le Novere
// EMBL-EBI, neurobiology computational group,             
// Cambridge, UK. e-mail: melting-forum@googlegroups.com

package meltinggui.frames;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;

import meltinggui.ArgsMessage;
import meltinggui.MeltingObservable;
import meltinggui.dialogs.ExpertOptionPanel;
import meltinggui.dialogs.GenericConcentrationTextFieldDialog;
import meltinggui.dialogs.HybridizationDialog;
import meltinggui.dialogs.OligomerConcentrationDialog;
import meltinggui.dialogs.ResultsPanel;
import meltinggui.dialogs.SequenceInputPanel;
import meltinggui.dialogs.SlidingWindowDialog;

/**
 * The frame that opens when the user runs the GUI.
 * 
 * @author John Gowers
 */
public class MeltingFrame extends JInternalFrame
{
  
  /** 
   * The outer frame containing this frame;
   */
  OuterFrame outerFrame = null;
  
  /** 
   * Creates a new frame.
   */
  public MeltingFrame() {
    super("Melting Input parameters", true, false, true, true);
    initializeWidgets();
  }
  
  /**
   * Creates the new frame.
   */
  public MeltingFrame(OuterFrame outerFrame) {
	this();
    this.outerFrame = outerFrame;
  }
  
  /**
   * Observable used to send messages back to all observers.
   */
  private MeltingObservable observable = new MeltingObservable();

  /**
   * Adds an observer to the melting frame observable.
   * @param observer The new observer.
   */
  public void addObserver(Observer observer)
  {
    observable.addObserver(observer);
  }

  /**
   * The text that would be entered into the command line were the user using
   * the command line.  Only mandatory options (-C -S -E -P -H) are included.
   */
  private String commandLineText;

  // Widgets to put on to the frame.  Declarations indented to show structure.
  /**
   * 
   */
  private JPanel mainPanel = new JPanel();
  /**
   * 
   */
  private JPanel sequencesPanel = new JPanel();
  // private SequenceDialog sequenceDialog = new SequenceDialog();  
  /**
   * 
   */
  private SequenceInputPanel sequenceInputDialog = new SequenceInputPanel();
  /**
   * 
   */
  private SlidingWindowDialog slidingWindowDialog = new SlidingWindowDialog();
  /**
   * 
   */
  private JPanel hybridizationPanel = new JPanel();
  /**
   * 
   */
  private HybridizationDialog hybridizationDialog = new HybridizationDialog();
  /**
   * 
   */
  private OligomerConcentrationDialog oligomerConcentrationDialog = new OligomerConcentrationDialog();
  /**
   * 
   */
  private GenericConcentrationTextFieldDialog sodiumConcentrationDialog = new GenericConcentrationTextFieldDialog("Salt (Na)      ", "Na=");
  /**
   * 
   */
  private GenericConcentrationTextFieldDialog potassiumConcentrationDialog = new GenericConcentrationTextFieldDialog("Potassium (K)  ", "K=");
  /**
   * 
   */
  private GenericConcentrationTextFieldDialog magnesiumConcentrationDialog = new GenericConcentrationTextFieldDialog("Magnessium (Mg)", "Mg=");
  /**
   * 
   */
  private GenericConcentrationTextFieldDialog trisConcentrationDialog = new GenericConcentrationTextFieldDialog("Tris           ", "Tris=");
  /**
   * 
   */
  private JPanel buttonsPanel = new JPanel();
  /**
   * 
   */
  private JButton getThermodynamicsButton = new JButton("Get Thermodynamics");
  /**
   * 
   */
  private JToggleButton moreOptionsButton = new JToggleButton("Expert Options...");
  /**
   * 
   */
  private ExpertOptionPanel expertOptionPanel = new ExpertOptionPanel();
  /**
   * 
   */
  private JScrollPane expertOptionJSP;
  /**
   * 
   */
  private JPanel commandLinePanel = new JPanel();
  /**
   * 
   */
  private JTextArea commandLineTextArea = new JTextArea(" -S  -H  -P  -E");
  /**
   * 
   */
  private JPanel resultsPanelPanel = new JPanel();
  /**
   * 
   */
  private ResultsPanel resultsPanel = new ResultsPanel();

  /**
   * Sets up the widgets and puts them on the frame.
   */
  private void initializeWidgets()
  {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    GridBagConstraints constraints = new GridBagConstraints();

    getRootPane().setDefaultButton(getThermodynamicsButton);
    getThermodynamicsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event)
            {
              sendMeltingArgs();
            }
    });
//    commandLineTextArea.setBackground(new Color(0, 0, 0, 0));
//    commandLineTextArea.setEditable(false);
//    commandLineTextArea.setLineWrap(true);
//    commandLinePanel.setLayout(new BorderLayout(30, 30));

    mainPanel.setLayout(new GridBagLayout());
    sequencesPanel.setLayout(new GridLayout(1, 1));
    
    hybridizationPanel.setLayout(new GridLayout(1, 1));
    buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
    
    sequencesPanel.add(sequenceInputDialog);
    hybridizationPanel.add(hybridizationDialog);
    buttonsPanel.add(getThermodynamicsButton, BorderLayout.WEST);
    buttonsPanel.add(Box.createHorizontalGlue());
    buttonsPanel.add(moreOptionsButton, BorderLayout.EAST);

    moreOptionsButton.setAction(new AbstractAction("Expert Options...") {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        expertOptionJSP.setVisible(!expertOptionJSP.isVisible());
        pack();
      }
    });

    int row = 0;
    constraints = getGridBagRow(row++);
    constraints.weighty = 0;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    mainPanel.add(sequencesPanel, constraints);
    mainPanel.add(slidingWindowDialog, getGridBagRow(row++));
    mainPanel.add(hybridizationPanel, getGridBagRow(row++));
    mainPanel.add(oligomerConcentrationDialog, getGridBagRow(row++));
    mainPanel.add(sodiumConcentrationDialog, getGridBagRow(row++));
    mainPanel.add(potassiumConcentrationDialog, getGridBagRow(row++));
    mainPanel.add(magnesiumConcentrationDialog, getGridBagRow(row++));
    mainPanel.add(trisConcentrationDialog, getGridBagRow(row++));
    mainPanel.add(buttonsPanel, getGridBagRow(row++));
    constraints = getGridBagRow(row++);
    constraints.weighty = 1.0;
//    mainPanel.add(commandLinePanel, constraints); // resultsPanelPanel.setLayout(new GridLayout(1, 2));
//    mainPanel.add(resultsPanelPanel, getGridBagRow(row++));
    JTextPane usageNotice = new JTextPane();
    usageNotice.setText("Remark: for more options (more computation methods, melting4 interface, batch mode, ...), see commandline-based scripts in this package.");
    constraints = getGridBagRow(row++);
    constraints.weighty = 0;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    mainPanel.add(usageNotice, constraints);

    // empty JPanel to fill in the empty space
    mainPanel.add(new JPanel(), getGridBagRow(row++));

    
    Container contentPane = getContentPane();
    contentPane.add(Box.createRigidArea(new Dimension(532, 12)),
                    BorderLayout.NORTH);
    contentPane.add(Box.createRigidArea(new Dimension(0, 12)),
                    BorderLayout.SOUTH);
    contentPane.add(Box.createRigidArea(new Dimension(12, 0)),
                    BorderLayout.WEST);
    contentPane.add(Box.createRigidArea(new Dimension(12, 0)),
                    BorderLayout.EAST);
    contentPane.add(mainPanel, BorderLayout.CENTER);

    // expert panel
    constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.BOTH;
    constraints.insets = new Insets(10, 4, 10, 4); 
    constraints.gridx = 1;
    constraints.gridy = 0;
    constraints.gridheight = 11;
    constraints.weightx = 10;
    constraints.weighty = 1;
    expertOptionJSP = new JScrollPane(expertOptionPanel);
    expertOptionJSP.setMinimumSize(new Dimension(500, 600));
    expertOptionJSP.setPreferredSize(new Dimension(800, 600));
    mainPanel.add(expertOptionJSP, constraints);
    
    
    pack();
  }

  /**
   * Factory for making standardized <code>GridBagConstraints</code> for use
   * with <code>GridBagLayout</code>.  These constraints are designed so they:
   *  - always fill up all available space
   *  - are always in the first column of the layout.
   * @param row the row of the layout for the new constraints.
   * @return The new <code>GridBagConstraints</code>
   */
  private GridBagConstraints getGridBagRow(int row)
  {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.BOTH;
    constraints.insets = new Insets(4, 4, 4, 4); // insets for all components
    constraints.gridx = 0;
    constraints.gridy = row;
    constraints.weightx = 0.5;
    constraints.weighty = 0.0;

    return constraints;
  }



  /**
   * Set the sequence in the sequence dialogue.
   * 
   * @param sequence the file containing the sequences
   */
  public void setSequence(File sequence) {  // TODO - do we still need this method ?
	  String[] sequenceStr = null;	  
	  sequenceStr = generateString(sequence);
	  if(sequenceStr != null) {
		  outerFrame.setSequenceFile(sequence);
//		  sequenceDialog.setSequence(sequenceStr[0]);
//		  sequenceDialog.setComplement(sequenceStr[1]);
//		  sequenceDialog.disableAll();
	  }
  }
  
  /** 
   * Remove the current file containing the sequences.
   */
  public void cleanSequence() { // TODO - do we still need this method ?
	  outerFrame.setSequenceFile(null);
//	  sequenceInputDialog.clearText();
//	  sequenceInputDialog.enableAll();
  }
  
  /**
   * Extract the sequences from the file and store them in one string.
   * 
   * @param sequence the file containing the sequences
   * @return a String containing the sequences imported from the file
   */
  private String[] generateString(File sequence) {
	    InputStream is = null;
	    BufferedReader bis = null;
	    String[] sequencesStr = new String[2];
		try {
	        is = new FileInputStream(sequence);
			bis = new BufferedReader(new InputStreamReader(is));
			StringBuilder sequenceBuilder = new StringBuilder();
			StringBuilder complementBuilder = new StringBuilder();
			String line;
			while((line = bis.readLine()) != null) {
				if(!line.startsWith("#") || !line.startsWith(" ")) {
					// TODO Check that this is the right separator between sequence and complement.
					int separator = line.indexOf("/");
					if(separator != -1) {
						sequenceBuilder.append(line.substring(0, separator).trim()).append("\n");
						complementBuilder.append(line.substring(separator+1).trim()).append("\n");
					} else {
						sequenceBuilder.append(line.trim()).append("\n");
					}
				}
			}
			if(sequenceBuilder.length() == 0) {
				outerFrame.setStatusPanelText("File " + sequence.getAbsolutePath() + " does not contain data");
				return null;
			}
			sequencesStr[0] = sequenceBuilder.toString();
			sequencesStr[1] = complementBuilder.toString();
			return sequencesStr;
		} catch (FileNotFoundException e) {
			outerFrame.setStatusPanelText("File " + sequence.getAbsolutePath() + " could not be opened");
			return null;
		} catch (IOException e) {
			outerFrame.setStatusPanelText("Error reading file " + sequence.getAbsolutePath());
			return null;
		} finally {
			if (is != null) {
				try {
					bis.close();
					is.close();
				} catch (IOException e) {
				}
			}
		}
    } 
  
  /**
   * Sets the command-line text in the command-line text area.
   */
  private void setCommandLineText()
  {
    commandLineText = sequenceInputDialog.getCommandLineFlags() + hybridizationDialog.getCommandLineFlags() +
        oligomerConcentrationDialog.getCommandLineFlags() + slidingWindowDialog.getCommandLineFlags();
    
    // retrieve and construct the ion -E option
    String concentration = null;
    
    if (sodiumConcentrationDialog.isValidNumber()) {
      concentration = sodiumConcentrationDialog.getCommandLineFlags();
    }
    if (potassiumConcentrationDialog.isValidNumber()) {
      if (concentration != null) {
        concentration += ":";
      }
      concentration += potassiumConcentrationDialog.getCommandLineFlags();
    }
    if (magnesiumConcentrationDialog.isValidNumber()) {
      if (concentration != null) {
        concentration += ":";
      }
      concentration += magnesiumConcentrationDialog.getCommandLineFlags();
    }
    if (trisConcentrationDialog.isValidNumber()) {
      if (concentration != null) {
        concentration += ":";
      }
      concentration += trisConcentrationDialog.getCommandLineFlags();
    }
    
    // add the expertPanel options if shown
    if (expertOptionPanel.isVisible()) {
      commandLineText += expertOptionPanel.getCommandLineFlags();   

      // TODO - add the cations from the expert panel to the 'concentration' variable, once it is developed.
    }
    
    commandLineText += " -E " + concentration + " ";

    commandLineTextArea.setText(commandLineText);
  }
  
  /**
   * Calculates and displays the MELTING results.
   */
  public void sendMeltingArgs()
  {
    setCommandLineText();
    
    System.out.println("Here are the options selected by the user:\n" + commandLineText + "\n");
    
    // TODO - do a bit of validation of the different parameters/arguments
    
    // TODO - main method to calculate the melting temperatures
    
    
    ArgsMessage message = new ArgsMessage(ArgsMessage.ArgumentType.MANDATORY,
                                          commandLineText);

    observable.setChanged();
    observable.notifyObservers(message);
  }

  /**
   * Displays the MELTING results.
   * @param results The results from the MELTING program.
   */
  public void displayMeltingResults(melting.ThermoResult results)
  {
 // TODO - if the option for a sliding window is given, generate the list of sequences and launch a set of jobs
    resultsPanel.displayMeltingResults(results);
  }
}
