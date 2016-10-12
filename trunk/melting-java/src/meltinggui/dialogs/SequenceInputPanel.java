package meltinggui.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author rodrigue
 *
 */
public class SequenceInputPanel extends JPanel implements ActionListener, DialogInterface {

  /**
   * 
   */
  private static final String FASTA_FILE_INPUT = "fasta file";
  /**
   * 
   */
  private static final String MELTING_FILE_INPUT = "melting file";
  /**
   * 
   */
  private static final String DIRECT_INPUT = "direct input";
  
  /**
   * 
   */
  ButtonGroup inputTypeButtonGroup;
  /**
   * 
   */
  JRadioButton directInputRadioButton;
  /**
   * 
   */
  JRadioButton meltingFileRadioButton;
  /**
   * 
   */
  JRadioButton fastaFileRadioButton;
  
  /**
   * A panel that contain two text area so that the user can input a sequence and optionally a complement sequence.
   */
  SequenceDialog sequenceDialog;
  
  /**
   * A panel that contain two text filed so that the user can input a sequence file path and optionally a complement sequence file path.
   */
  SequenceFileDialog sequenceFileDialog;

  /**
   * A panel that contain two text filed so that the user can input a sequence file path and optionally a complement sequence file path.
   */
  SequenceFileDialog fastaSequenceFileDialog;

  /**
   * Create the panel.
   */
  public SequenceInputPanel() {

    setLayout(new GridBagLayout());
    setMinimumSize(new Dimension(537, 240));
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "Sequence(s) Input"));

    directInputRadioButton = new JRadioButton(DIRECT_INPUT);
    directInputRadioButton.setSelected(true);
    directInputRadioButton.addActionListener(this);
    meltingFileRadioButton = new JRadioButton(MELTING_FILE_INPUT);
    meltingFileRadioButton.addActionListener(this);
    fastaFileRadioButton = new JRadioButton(FASTA_FILE_INPUT);
    fastaFileRadioButton.addActionListener(this);
    
    //Group the radio buttons.
    ButtonGroup inputTypeButtonGroup = new ButtonGroup();
    inputTypeButtonGroup.add(directInputRadioButton);
    inputTypeButtonGroup.add(meltingFileRadioButton);
    inputTypeButtonGroup.add(fastaFileRadioButton);
    
    //Put the radio buttons in a column in a panel.
    JPanel radioPanel = new JPanel(new GridLayout(1, 0));
    radioPanel.add(directInputRadioButton);
    radioPanel.add(meltingFileRadioButton);
    radioPanel.add(fastaFileRadioButton);
    
    // creates a constraints object
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2); // insets for all components
    c.gridx = 0; // column 0
    c.gridy = 0; // row 0
    c.ipadx = 10; // increases components width by 10 pixels
    c.ipady = 0; // increases components height by 10 pixels
    c.weightx = 0; // No resize if the weight is zero (default value)
    c.anchor = GridBagConstraints.NORTH;
    c.fill = GridBagConstraints.NONE;

    add(radioPanel, c);
    
    c.gridy = 1; // row 1
    c.weightx = 2;
    c.weighty = 6;
    c.fill = GridBagConstraints.BOTH;
    sequenceDialog = new SequenceDialog();
    add(sequenceDialog, c);
    
    c.gridy = 1; // row 1
    c.weightx = 2;
    c.weighty = 0;
    c.fill = GridBagConstraints.HORIZONTAL;
    sequenceFileDialog = new SequenceFileDialog();
    sequenceFileDialog.setVisible(false);
    // setting tooltips for the file text fields
    sequenceFileDialog.getWidget(0).setTextFieldToolTip("Tooltip test");
    sequenceFileDialog.getWidget(1).setTextFieldToolTip("Tooltip test for the complement file");
    add(sequenceFileDialog, c);
    
    c.gridy = 1; // row 1
    c.weightx = 2;
    fastaSequenceFileDialog = new SequenceFileDialog();
    fastaSequenceFileDialog.setVisible(false);
    // setting tooltips for the file text fields
    fastaSequenceFileDialog.getWidget(0).setTextFieldToolTip("Tooltip test");
    fastaSequenceFileDialog.getWidget(1).setTextFieldToolTip("Tooltip test for the complement file");
    add(fastaSequenceFileDialog, c);
    
    // empty JPanel to fill in the empty space
    c.gridy = 2; // row 2
    c.weighty = 2;
    c.fill = GridBagConstraints.BOTH;
    add(new JPanel(), c);
  }

  /** Listens to the radio buttons. */
  public void actionPerformed(ActionEvent e) {
    switch(e.getActionCommand()) {
      case DIRECT_INPUT: {
        System.out.println(DIRECT_INPUT);
        sequenceDialog.setVisible(true);
        sequenceFileDialog.setVisible(false);
        fastaSequenceFileDialog.setVisible(false);
        this.repaint();
        break;
      }
      case MELTING_FILE_INPUT: {
          System.out.println(MELTING_FILE_INPUT);
          sequenceDialog.setVisible(false);
          sequenceFileDialog.setVisible(true);
          fastaSequenceFileDialog.setVisible(false);
          this.repaint();
        break;
      }
      case FASTA_FILE_INPUT: {
        System.out.println(FASTA_FILE_INPUT);
        sequenceDialog.setVisible(false);
        sequenceFileDialog.setVisible(false);
        fastaSequenceFileDialog.setVisible(true);
        this.repaint();
        break;
      }
    }
  }

  
  /**
   * Create the GUI and show it.  For thread safety,
   * this method should be invoked from the
   * event-dispatching thread.
   */
  private static void createAndShowGUI() {
      //Create and set up the window.
      JFrame frame = new JFrame("TestFrame");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      //Create and set up the content pane.
      JComponent newContentPane = new SequenceInputPanel();
      newContentPane.setOpaque(true); //content panes must be opaque
      frame.setContentPane(newContentPane);

      //Display the window.
      frame.pack();
      frame.setVisible(true);
  }

  /**
   * Builds and displays a {@link SequenceInputPanel} for debugging.
   * 
   * @param args no arguments expected
   */
  public static void main(String[] args) {
      //Schedule a job for the event-dispatching thread:
      //creating and showing this application's GUI.
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
          public void run() {
              createAndShowGUI();
          }
      });
  }

  @Override
  public String getCommandLineFlags() {
    
    String command = "";
    
    if (sequenceDialog.isVisible()) 
    {
      command = sequenceDialog.getCommandLineFlags();
    }
    else if (sequenceFileDialog.isVisible()) 
    {
      command = sequenceFileDialog.getCommandLineFlags();
    }
    else if (fastaSequenceFileDialog.isVisible()) 
    {
      command = fastaSequenceFileDialog.getCommandLineFlags();
    }
        
    return command;
  }
}
