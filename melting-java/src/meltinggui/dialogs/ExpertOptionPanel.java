package meltinggui.dialogs;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "Expert options"));

    init();
    
    int row = 0;
    
    // creates a constraints object
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(4, 4, 4, 4); // insets for all components
    c.gridx = 0; // column 0
    c.gridy = row; // row ?
    c.ipadx = 10; // increases components width by 10 pixels
    c.ipady = 0; // increases components height by 10 pixels
    c.weightx = 2; // resize so that all combo box have the same width
    c.anchor = GridBagConstraints.NORTHEAST;
    c.fill = GridBagConstraints.HORIZONTAL;

    // thermodynamic parameters and methods
    add(thermodynamicOptionPanel, c);
    
    // melting temperature options
    c.anchor = GridBagConstraints.NORTH;
    c.gridy = row; // row ?
    c.gridx = 1;
    add(temperatureOptionPanel, c);
    
    
    // empty JPanel to fill in the empty space
    row++;
    c.gridy = row; // row 2
    c.weighty = 2;
    c.fill = GridBagConstraints.BOTH;
    add(new JPanel(), c);
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
    
    String command = "" + thermodynamicOptionPanel.getCommandLineFlags() 
    + " " + temperatureOptionPanel.getCommandLineFlags();
    
        
    return command;
  }
}
