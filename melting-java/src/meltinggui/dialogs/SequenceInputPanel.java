package meltinggui.dialogs;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author rodrigue
 *
 */
public class SequenceInputPanel extends JPanel implements ActionListener {

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
   * 
   */
  SequenceFileDialog sequenceFileDialog;
  
  /**
   * Create the panel.
   */
  public SequenceInputPanel() {

    setLayout(new GridLayout(0, 1)); // TODO - do a GridBagLayout

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
    
    add(radioPanel);
    
    sequenceDialog = new SequenceDialog();
    add(sequenceDialog);
    
    sequenceFileDialog = new SequenceFileDialog();
    sequenceFileDialog.setVisible(false);
    // TODO - set tooltips sequenceFileDialog.get
    add(sequenceFileDialog);
  }

  /** Listens to the radio buttons. */
  public void actionPerformed(ActionEvent e) {
    switch(e.getActionCommand()) {
      case DIRECT_INPUT: {
        System.out.println(DIRECT_INPUT);
        sequenceDialog.setVisible(true);
        sequenceFileDialog.setVisible(false);
        break;
      }
      case MELTING_FILE_INPUT: {
          System.out.println(MELTING_FILE_INPUT);
          sequenceDialog.setVisible(false);
          sequenceFileDialog.setVisible(true);
        break;
      }
      case FASTA_FILE_INPUT: {
        System.out.println(FASTA_FILE_INPUT);
        sequenceDialog.setVisible(false);
        sequenceFileDialog.setVisible(false);
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
}
