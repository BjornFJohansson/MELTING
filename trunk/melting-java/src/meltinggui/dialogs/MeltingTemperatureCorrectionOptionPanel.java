package meltinggui.dialogs;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import meltinggui.widgets.ComboBoxOption;
import meltinggui.widgets.MeltingComboBox;

/**
 * @author rodrigue
 *
 */
public class MeltingTemperatureCorrectionOptionPanel extends JPanel implements DialogInterface {

  
  
  /**
   * A combo box where the user can select a specific ion correction.
   */
  MeltingComboBox ionCorrectionCB;
  /**
   * A combo box where the user can select a specific ion correction which gives a sodium 
   * equivalent concentration if other cations are present.
   */
  MeltingComboBox naEquivalentCB;
  /**
   * A combo box where the user can select a specific DMSO correction (DMSO is always in percent).
   */
  MeltingComboBox dmsoCorrectionCB;
  /**
   * A combo box where the user can select a specific formamide correction.
   */
  MeltingComboBox formamideCorrectionCB;

  /**
   * Create the panel.
   */
  public MeltingTemperatureCorrectionOptionPanel() {

    setLayout(new GridBagLayout());
    setBackground(Color.WHITE);
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "Melting temperature corrections"));

    initComboBoxes();
    
    int row = 0;
    
    // creates a constraints object
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2); // insets for all components
    c.gridx = 0; // column 0
    c.gridy = row; // row ?
    c.ipadx = 10; // increases components width by 10 pixels
    c.ipady = 0; // increases components height by 10 pixels
    c.weightx = 2; // resize so that all combo box have the same width
    c.anchor = GridBagConstraints.NORTHEAST;
    c.fill = GridBagConstraints.HORIZONTAL;

    add(ionCorrectionCB, c);
    
    c.anchor = GridBagConstraints.EAST;

    // Na equivalent correction
    row++;
    c.gridy = row; // row ?
    add(naEquivalentCB, c);
    
    // DMSO correction
    row++;
    c.gridy = row; // row ?
    add(dmsoCorrectionCB, c);
    
    // formamide correction
    row++;
    c.gridy = row; // row ?
    add(formamideCorrectionCB, c);

    
    // empty JPanel to fill in the empty space
    row++;
    c.gridy = row; // row 2
    c.weighty = 2;
    c.fill = GridBagConstraints.BOTH;
    JPanel emptyPanel = new JPanel();
    emptyPanel.setBackground(Color.WHITE);
    add(emptyPanel, c);
  }

  /**
   * Create all the combo boxes.
   * 
   */
  private void initComboBoxes() 
  {
    //  Set of melting temperature corrections:
    
    // Formamide correction
    ComboBoxOption[] options = new ComboBoxOption[3];
    options[0] = new ComboBoxOption("<html><u>RNA:</u>", "");
    options[1] = new ComboBoxOption("  Blake et al 1996 (default)", "-for bla96");
    options[2] = new ComboBoxOption("  Linear correction", "-for lincorr");
    
    formamideCorrectionCB = new MeltingComboBox(options);
    formamideCorrectionCB.setBackground(Color.WHITE);
    formamideCorrectionCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "Formamide correction"));
    formamideCorrectionCB.setToolTipText("Select a specific formamide correction.");

    // DMSO correction
    options = new ComboBoxOption[5];
    options[0] = new ComboBoxOption("<html><u>RNA:</u>", "");
    options[1] = new ComboBoxOption("  Von Ahsen et al 2001 (default)", "-DMSO ahs01");
    options[2] = new ComboBoxOption("  Musielski et al. 1981", "-DMSO mus81");
    options[3] = new ComboBoxOption("  Cullen et al. 1976", "-DMSO cul76");
    options[4] = new ComboBoxOption("  Escara et al. 1980", "-DMSO esc80");
    
    dmsoCorrectionCB = new MeltingComboBox(options);
    dmsoCorrectionCB.setBackground(Color.WHITE);
    dmsoCorrectionCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "DMSO correction"));
    dmsoCorrectionCB.setToolTipText("Select a specific DMSO correction (DMSO is always in percent).");
    
    // ion corrections
    int index = 0;
    options = new ComboBoxOption[32];
    options[index] = new ComboBoxOption("<html><u>Sodium corrections:</u>", "");
    options[++index] = new ComboBoxOption("<html>&nbsp;&nbsp;<u>RNA:</u>", "");
    options[++index] = new ComboBoxOption("    Von Ahsen et al. 2001", "-ion ahs01");
    options[++index] = new ComboBoxOption("    Frank-Kamenetskii et al 2001", "-ion kam71");
    options[++index] = new ComboBoxOption("    equation 19 from Owczarzy et al. 2004", "-ion owc1904");
    options[++index] = new ComboBoxOption("    equation 20 from Owczarzy et al. 2004", "-ion owc2004");
    options[++index] = new ComboBoxOption("    equation 21 from Owczarzy et al. 2004", "-ion owc2104");
    options[++index] = new ComboBoxOption("    equation 22 from Owczarzy et al. 2004 (default)", "-ion owc2204");
    options[++index] = new ComboBoxOption("    Santalucia et al. 1996", "-ion san96");
    options[++index] = new ComboBoxOption("    Santalucia et al. 1998, 2004", "-ion san04");
    options[++index] = new ComboBoxOption("    Schildkraut and Lifson 1965", "-ion schlif");
    options[++index] = new ComboBoxOption("    Tan et al. 2006", "-ion tanna06");
    options[++index] = new ComboBoxOption("    Wetmur 1991", "-ion wetdna91");
    
    options[++index] = new ComboBoxOption("<html>&nbsp;&nbsp;<u>RNA or 2-o methyl RNA:</u>", "");
    options[++index] = new ComboBoxOption("    Tan et al. 2007 (default)", "-ion tanna07");
    options[++index] = new ComboBoxOption("    Wetmur 1991", "-ion wetrna91");
    
    options[++index] = new ComboBoxOption("<html>&nbsp;&nbsp;<u>RNA/DNA:</u>", "");
    options[++index] = new ComboBoxOption("    Wetmur 1991 (default)", "-ion wetdnarna91");

    options[++index] = new ComboBoxOption("", "");
    options[++index] = new ComboBoxOption("<html><u>Magnesium corrections:</u>", "");
    options[++index] = new ComboBoxOption("<html>&nbsp;&nbsp;<u>DNA:</u>", "");
    options[++index] = new ComboBoxOption("    Owczarzy et al. 2008 (default)", "-ion owcmg08");
    options[++index] = new ComboBoxOption("    Tan et al. 2006", "-ion tanmg06");
    
    options[++index] = new ComboBoxOption("<html>&nbsp;&nbsp;<u>RNA or 2-o methyl RNA:</u>", "");
    options[++index] = new ComboBoxOption("    Tan et al. 2007", "-ion tanmg07");

    options[++index] = new ComboBoxOption("", "");
    options[++index] = new ComboBoxOption("<html><u>Mixed Na Mg corrections:</u>", "");
    options[++index] = new ComboBoxOption("<html>&nbsp;&nbsp;<u>DNA:</u>", "");
    options[++index] = new ComboBoxOption("    Owczarzy et al. 2008 (default)", "-ion owcmix08");
    options[++index] = new ComboBoxOption("    Tan et al. 2007", "-ion tanmix07");

    options[++index] = new ComboBoxOption("<html>&nbsp;&nbsp;<u>RNA or 2-o methyl RNA:</u>", "");
    options[++index] = new ComboBoxOption("    Tan et al. 2007", "-ion tanmix07"); // TODO - same name with the DNA option ??

    ionCorrectionCB = new MeltingComboBox(options);   
    ionCorrectionCB.setMaximumRowCount(25);
    ionCorrectionCB.setBackground(Color.WHITE);
    ionCorrectionCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "ion corrections"));
    ionCorrectionCB.setToolTipText("<html>Select a specific ion correction.<br>" 
        + "By default, the program use the algorithm from Owczarzy et al. 2008 : ratio = Mg^0.5 and monovalent = Na + Tris + K<br><br>"
        + "<ul><li>    if monovalent = 0, a magnesium correction is used.<br>"
        + "<li>    if ratio < 0.22, a sodium correction is used.<br>"
        + "<li>    if 0.22 <= ratio < 6, a mixed Na Mg correction is used.<br>"
        + "<li>    if ratio >= 6, a magnesium correction is used.");
   
    // Na equivalent concentration correction
    options = new ComboBoxOption[4];
    options[0] = new ComboBoxOption("<html><u>DNA:</u>", "");
    options[1] = new ComboBoxOption("  Von Ahsen et al 2001 (default)", "-naeq ahs01");
    options[2] = new ComboBoxOption("  Mitsuhashi et al. 1996", "-naeq mit96");
    options[3] = new ComboBoxOption("  Peyret 2000", "-naeq pey00");
    
    naEquivalentCB = new MeltingComboBox(options);   
    naEquivalentCB.setBackground(Color.WHITE);
    naEquivalentCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "Na equivalent concentration"));
    naEquivalentCB.setToolTipText("Select a specific ion correction which gives a sodium equivalent concentration if other cations are present.");
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
      JComponent newContentPane = new MeltingTemperatureCorrectionOptionPanel();
      newContentPane.setOpaque(true); //content panes must be opaque
      frame.setContentPane(newContentPane);

      //Display the window.
      frame.pack();
      frame.setVisible(true);
  }

  /**
   * Builds and displays a {@link MeltingTemperatureCorrectionOptionPanel} for debugging.
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
    
    String command = " ";
    
    if (formamideCorrectionCB.getValue().trim().length() > 0) {
      command += formamideCorrectionCB.getValue() + " ";
    }    
    if (dmsoCorrectionCB.getValue().trim().length() > 0) {
      command += dmsoCorrectionCB.getValue() + " ";
    }    
    if (ionCorrectionCB.getValue().trim().length() > 0) {
      command += ionCorrectionCB.getValue() + " ";
    }    
    if (naEquivalentCB.getValue().trim().length() > 0) {
      command += naEquivalentCB.getValue() + " ";
    }    
        
    return command;
  }
}
