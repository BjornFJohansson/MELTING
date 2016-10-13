package meltinggui.dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLEditorKit;

/**
 * @author rodrigue
 *
 */
public class ExpertOptionPanel extends JPanel implements ActionListener, DialogInterface {

  
  
  /**
   * A panel where the user can select all the thermodynamic parameters and methods.
   */
  ThermodynamicOptionPanel thermodynamicOptionPanel = new ThermodynamicOptionPanel();
  /**
   * A panel where the user can select all the melting temperature parameters.
   */
  MeltingTemperatureCorrectionOptionPanel temperatureOptionPanel = new MeltingTemperatureCorrectionOptionPanel();

  // TODO - info panel, agent panel
  
  /**
   * Create the panel.
   */
  public ExpertOptionPanel() {

    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);   
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "Expert options"));

    init();
    
    // creates a constraints object
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0; // column 0
    c.gridy = 0; // row 0
    c.weightx = 1; // resize so that all combo box have the same width
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridwidth = 2;

    // First column
    
    // TODO - insert an information panel
    c.insets = new Insets(10, 4, 10, 4); 
    JTextPane informationArea = new JTextPane();
    informationArea.setEditable(false);
    informationArea.setEditorKit(new HTMLEditorKit());
    informationArea.setText("<html>Long text explaning the expert optionsrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr<p>blabla"
        + "<p>an other paragraphe");
    JScrollPane scrollPane = new JScrollPane(informationArea);
    scrollPane.setMinimumSize(new Dimension(600, 120));
    add(scrollPane, c);
    // add(informationArea, c);
    
    // thermodynamic parameters and methods
    c.insets = new Insets(4, 4, 4, 4); // insets for all components
    c.gridy++;
    c.gridwidth = 1;
    c.gridheight = 2;    
    c.anchor = GridBagConstraints.NORTHEAST;
    add(thermodynamicOptionPanel, c);
    
    
    // empty JPanel to fill in the empty space
    c.gridwidth = 2;
    c.gridheight = 1;    
    c.gridy++;
    c.weighty = 2;
    c.fill = GridBagConstraints.BOTH;
    add(new JPanel(), c);

    
    // Second column
    c.gridx = 1; // column 1
    c.gridy = 1; // row 1 - as the text area is in row 0.
    c.weighty = 0;
    
    // melting temperature options
    c.anchor = GridBagConstraints.NORTH;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridwidth = 1;
    add(temperatureOptionPanel, c);
    

  }

  /** Listens to the radio buttons. */
  public void actionPerformed(ActionEvent e) {
    switch(e.getActionCommand()) {
    }
  }

  /**
   * Create all the combo boxes.
   * 
   */
  private void init() 
  {
    // TODO - create the info panel
    
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
      frame.setMinimumSize(new Dimension(600, 940));

      //Create and set up the content pane.
      JComponent newContentPane = new ExpertOptionPanel();
      newContentPane.setOpaque(true); //content panes must be opaque
      frame.setContentPane(newContentPane);

      //Display the window.
      frame.pack();
      frame.setVisible(true);
  }

  /**
   * Builds and displays a {@link ExpertOptionPanel} for debugging.
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
    
    String command = thermodynamicOptionPanel.getCommandLineFlags() + temperatureOptionPanel.getCommandLineFlags();
        
    return command;
  }
}
