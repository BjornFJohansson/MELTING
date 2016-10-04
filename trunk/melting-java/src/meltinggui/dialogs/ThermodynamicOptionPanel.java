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

import meltinggui.widgets.ComboBoxOption;
import meltinggui.widgets.MeltingComboBox;

/**
 * @author rodrigue
 *
 */
public class ThermodynamicOptionPanel extends JPanel implements ActionListener, DialogInterface {

  
  
  /**
   * A combo box where the user can select a specific approximative formula.
   */
  MeltingComboBox approximativeFormulaCB;
  /**
   * A combo box where the user can select a specific nearest neighbor model.
   */
  MeltingComboBox nearestNeighborModelCB;
  /**
   * A combo box where the user can select a specific nearest neighbor model for single mismatch(es) in the sequences.
   */
  MeltingComboBox nnmSingleMismatchCB;
  /**
   * A combo box where the user can select a specific nearest neighbor model for GU base pairs in RNA sequences.
   */
  MeltingComboBox nnmGuBasedPairInRnaCB;
  /**
   * A combo box where the user can select a specific nearest neighbor model for tandem mismatches.
   */
  MeltingComboBox nnmTandemMismatchCB;
  /**
   * A combo box where the user can select a specific nearest neighbor model for internal loop.
   */
  MeltingComboBox nnmInternalLoopCB;
  /**
   * A combo box where the user can select a specific nearest neighbor model for single dangling end.
   */
  MeltingComboBox nnmSingleDanglingEndCB;
  /**
   * A combo box where the user can select a specific nearest neighbor model for second dangling end.
   */
  MeltingComboBox nnmSecondDanglingEndCB;
  /**
   * A combo box where the user can select a specific nearest neighbor model for long dangling end.
   */
  MeltingComboBox nnmLongDanglingEndCB;
  /**
   * A combo box where the user can select a specific nearest neighbor model for single bulge loop.
   */
  MeltingComboBox nnmSingleBulgeLoopCB;
  /**
   * A combo box where the user can select a specific nearest neighbor model for long bulge loop.
   */
  MeltingComboBox nnmLongBulgeLoopCB;
  /**
   * A combo box where the user can select a specific nearest neighbor model for inosine base.
   */
  MeltingComboBox nnmInosineBaseCB;
  /**
   * A combo box where the user can select a specific nearest neighbor model for locked nucleic acid.
   */
  MeltingComboBox nnmLockedNucleicAcidCB;

  
  /**
   * Create the panel.
   */
  public ThermodynamicOptionPanel() {

    setLayout(new GridBagLayout());
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "Thermodynamic parameters and methods"));
    setBackground(Color.WHITE);
    
    initComboBoxes();
    
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

    // Approximative formula
    add(approximativeFormulaCB, c);
    
    c.anchor = GridBagConstraints.EAST;

    // nearest neighbor model
    row++;
    c.gridy = row; // row ?
    add(nearestNeighborModelCB, c);
    
    // nearest neighbor model for single mismatch
    row++;
    c.gridy = row; // row ?
    add(nnmSingleMismatchCB, c);
    
    // formamide correction for GU base pairs in RNA sequences
    row++;
    c.gridy = row; // row ?
    add(nnmGuBasedPairInRnaCB, c);

    // nearest neighbor model for tandem mismatches
    row++;
    c.gridy = row; // row ?
    add(nnmTandemMismatchCB, c);

    // nearest neighbor model for internal loop
    row++;
    c.gridy = row; // row ?
    add(nnmInternalLoopCB, c);

    // nearest neighbor model for single dangling end
    row++;
    c.gridy = row; // row ?
    add(nnmSingleDanglingEndCB, c);

    // nearest neighbor model for second dangling end
    row++;
    c.gridy = row; // row ?
    add(nnmSecondDanglingEndCB, c);

    // nearest neighbor model for long dangling end
    row++;
    c.gridy = row; // row ?
    add(nnmLongDanglingEndCB, c);

    // nearest neighbor model for single bulge loop
    row++;
    c.gridy = row; // row ?
    add(nnmSingleBulgeLoopCB, c);

    // nearest neighbor model for long bulge loop
    row++;
    c.gridy = row; // row ?
    add(nnmLongBulgeLoopCB, c);

    // nearest neighbor model for inosine base
    row++;
    c.gridy = row; // row ?
    add(nnmInosineBaseCB, c);

    // nearest neighbor model for DNA sequences containing locked nucleic acid (Al, Tl, Cl or Gl)
    row++;
    c.gridy = row; // row ?
    add(nnmLockedNucleicAcidCB, c);

    
    // empty JPanel to fill in the empty space
    row++;
    c.gridy = row; // row 2
    c.weighty = 2;
    c.fill = GridBagConstraints.BOTH;
    JPanel emptyPanel = new JPanel();
    emptyPanel.setBackground(Color.WHITE);
    add(emptyPanel, c);
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
  private void initComboBoxes() 
  {
    // Set of thermodynamic parameters and methods:

    ComboBoxOption[] options = null;

    // approximative formula
    int index = 0;
    options = new ComboBoxOption[12];
    options[index] = new ComboBoxOption("<html><u>DNA:</u>", "");
    options[++index] = new ComboBoxOption("  Von Ahsen et al. 2001", "-am ahs01");
    options[++index] = new ComboBoxOption("  Marmur 1962, Chester et al. 1993", "-am che93");
    options[++index] = new ComboBoxOption("  Von Ahsen et al. 2001, Marmur 1962, Chester et al. 1993", "-am che93corr");
    options[++index] = new ComboBoxOption("  Marmur-Schildkraut-Doty formula", "-am schdot");
    options[++index] = new ComboBoxOption("  Owen et al. 1969", "-am owe69");
    options[++index] = new ComboBoxOption("  Santalucia et al. 1998", "-am san98");
    options[++index] = new ComboBoxOption("  Wetmur 1991 (default)", "-am wetdna91");

    options[++index] = new ComboBoxOption("<html><u>RNA:</u>", "");
    options[++index] = new ComboBoxOption("  Wetmur 1991", "-am wetrna91");

    options[++index] = new ComboBoxOption("<html><u>DNA/RNA:</u>", "");
    options[++index] = new ComboBoxOption("  Wetmur 1991", "-am wetdnarna91");

    approximativeFormulaCB = new MeltingComboBox(options);   
    approximativeFormulaCB.setMaximumRowCount(20);
    approximativeFormulaCB.setBackground(Color.WHITE);
    approximativeFormulaCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "approximative formula"));
    approximativeFormulaCB.setToolTipText("<html>Select a specific approximative formula.<br>"
        + "The approximative mode is used for oligonucleotides longer than 60 bases (the default threshold value), "
        + "<br>otherwise the nearest neighbor model is used."); 

    // nearest neighbor model
    index = 0;
    options = new ComboBoxOption[14];
    options[index] = new ComboBoxOption("<html><u>DNA:</u>", "");
    options[++index] = new ComboBoxOption("  Allawi and Santalucia 1997 (default)", "-nn all97");
    options[++index] = new ComboBoxOption("  Breslauer et al. 1986", "-nn bre86");
    options[++index] = new ComboBoxOption("  Hicks and Santalucia 2004", "-nn san04");
    options[++index] = new ComboBoxOption("  Santalucia et al. 1998", "-nn san98");
    options[++index] = new ComboBoxOption("  Sugimoto et al. 1996", "-nn sug96");
    options[++index] = new ComboBoxOption("  Tanaka et al. 2004", "-nn tan04");

    options[++index] = new ComboBoxOption("<html><u>RNA:</u>", "");
    options[++index] = new ComboBoxOption("  Wetmur 1991", "-nn wetrna91");
    options[++index] = new ComboBoxOption("  Xia et al. 1998 (default)", "-nn fre86");

    options[++index] = new ComboBoxOption("<html><u>DNA/RNA:</u>", "");
    options[++index] = new ComboBoxOption("  Sugimoto et al. 1995", "-nn sug95");

    options[++index] = new ComboBoxOption("<html><u>mRNA/RNA:</u>", "");
    options[++index] = new ComboBoxOption("  Kierzek et al. 2006", "-nn tur06");

    nearestNeighborModelCB = new MeltingComboBox(options);
    nearestNeighborModelCB.setMaximumRowCount(25);
    nearestNeighborModelCB.setBackground(Color.WHITE);
    nearestNeighborModelCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "nearest neighbor model"));
    nearestNeighborModelCB.setToolTipText("<html>Select a specific nearest neighbor model.<br>"
        + "The approximative mode is used for oligonucleotides longer than 60 bases (the default threshold value), "
        + "<br>otherwise the nearest neighbor model is used."); 
    
    // nearest neighbor model for single mismatch(es) in the sequences
    index = 0;
    options = new ComboBoxOption[8];
    options[index] = new ComboBoxOption("<html><u>DNA:</u>", "");
    options[++index] = new ComboBoxOption("  Allawi, Santalucia and Peyret 1997, 1998 and 1999", "-sinMM allsanpey");

    options[++index] = new ComboBoxOption("<html><u>DNA/RNA:</u>", "");
    options[++index] = new ComboBoxOption("  Watkins et al. 2011", "-sinMM wat10");

    options[++index] = new ComboBoxOption("<html><u>RNA:</u>", "");
    options[++index] = new ComboBoxOption("  Lu et al. 2006", "-sinMM tur06");
    options[++index] = new ComboBoxOption("  Davis et al. 2007 (default)", "-sinMM zno07");
    options[++index] = new ComboBoxOption("  Davis et al. 2008", "-sinMM zno08");

    nnmSingleMismatchCB = new MeltingComboBox(options);   
    nnmSingleMismatchCB.setBackground(Color.WHITE);
    nnmSingleMismatchCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for single mismatch(es)"));
    nnmSingleMismatchCB.setToolTipText("<html>Select a specific nearest neighbor model <br>for single mismatch(es) in the sequences.");
    
    // nearest neighbor model for GU base pairs in RNA sequences
    options = new ComboBoxOption[2];
    options[0] = new ComboBoxOption("  Turner et al. 1999", "-GU tur99");
    options[1] = new ComboBoxOption("  Serra et al. 2012 (default)", "-GU ser12");
    
    nnmGuBasedPairInRnaCB = new MeltingComboBox(options);
    nnmGuBasedPairInRnaCB.setBackground(Color.WHITE);
    nnmGuBasedPairInRnaCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for GU base pairs in RNA"));
    nnmGuBasedPairInRnaCB.setToolTipText("Select a specific formamide correction.");

    // nearest neighbor model for tandem mismatches
    options = new ComboBoxOption[4];
    options[0] = new ComboBoxOption("<html><u>DNA:</u>", "");
    options[1] = new ComboBoxOption("  Allawi, Santalucia and Peyret 1997, 1998 and 1999", "-tanMM allsanpey");
    options[2] = new ComboBoxOption("<html><u>RNA:</u>", "");
    options[3] = new ComboBoxOption("  Mathews et al. 1999", "-tanMM tur99");
    
    nnmTandemMismatchCB = new MeltingComboBox(options);
    nnmTandemMismatchCB.setBackground(Color.WHITE);
    nnmTandemMismatchCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for tandem mismatches"));
    nnmTandemMismatchCB.setToolTipText("<html>Select a specific nearest neighbor model <br>for tandem mismatches in the sequences.");

    // nearest neighbor model for internal loop
    options = new ComboBoxOption[5];
    options[0] = new ComboBoxOption("<html><u>DNA:</u>", "");
    options[1] = new ComboBoxOption("  Hicks and Santalucia 2004", "-intLP allsanpey");
    options[2] = new ComboBoxOption("<html><u>RNA:</u>", "");
    options[3] = new ComboBoxOption("  Lu et al. 2006 (default)", "-intLP tur06");
    options[4] = new ComboBoxOption("  Badhwarr et al. 2007, only for 1x2 loop", "-intLP zno07");
    
    nnmInternalLoopCB = new MeltingComboBox(options);
    nnmInternalLoopCB.setBackground(Color.WHITE);
    nnmInternalLoopCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for internal loop"));
    nnmInternalLoopCB.setToolTipText("<html>Select a specific nearest neighbor model <br>for internal loop in the sequences.");

    // nearest neighbor model for single dangling end
    options = new ComboBoxOption[6];
    options[0] = new ComboBoxOption("<html><u>DNA:</u>", "");
    options[1] = new ComboBoxOption("  Bommarito et al. 2000 (default)", "-sinDE bom00");
    options[2] = new ComboBoxOption("  Ohmichi et al. 2002, only for polyA dangling ends", "-sinDE sugdna02");
    options[3] = new ComboBoxOption("<html><u>RNA:</u>", "");
    options[4] = new ComboBoxOption("  Ohmichi et al. 2002, only for polyA dangling ends", "-sinDE sugrna02");
    options[5] = new ComboBoxOption("  Miller et al. 2008 (default)", "-sinDE ser08");
    
    nnmSingleDanglingEndCB = new MeltingComboBox(options);
    nnmSingleDanglingEndCB.setBackground(Color.WHITE);
    nnmSingleDanglingEndCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for single dangling end"));
    nnmSingleDanglingEndCB.setToolTipText("<html>Select a specific nearest neighbor model <br>for single dangling end(s) in the sequences.");

    // nearest neighbor model for second dangling end
    options = new ComboBoxOption[6];
    options[0] = new ComboBoxOption("<html><u>DNA:</u>", "");
    options[1] = new ComboBoxOption("  Ohmichi et al. 2002, only for polyA dangling ends", "-secDE sugdna02");
    options[2] = new ComboBoxOption("<html><u>RNA:</u>", "");
    options[3] = new ComboBoxOption("  Ohmichi et al. 2002, only for polyA dangling ends", "-secDE sugrna02");
    options[4] = new ComboBoxOption("  O'toole et al. 2005", "-secDE ser05");
    options[5] = new ComboBoxOption("  O'toole et al. 2006 (default)", "-secDE ser06");
    
    nnmSecondDanglingEndCB = new MeltingComboBox(options);
    nnmSecondDanglingEndCB.setBackground(Color.WHITE);
    nnmSecondDanglingEndCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for second dangling end"));
    nnmSecondDanglingEndCB.setToolTipText("<html>Select a specific nearest neighbor model <br>for second dangling end(s) in the sequences.");

    // nearest neighbor model for long dangling end
    options = new ComboBoxOption[4];
    options[0] = new ComboBoxOption("<html><u>DNA:</u>", "");
    options[1] = new ComboBoxOption("  Ohmichi et al. 2002, only for polyA dangling ends", "-lonDE sugdna02");
    options[2] = new ComboBoxOption("<html><u>RNA:</u>", "");
    options[3] = new ComboBoxOption("  Ohmichi et al. 2002, only for polyA dangling ends", "-lonDE sugrna02");
    
    nnmLongDanglingEndCB = new MeltingComboBox(options);
    nnmLongDanglingEndCB.setBackground(Color.WHITE);
    nnmLongDanglingEndCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for long dangling end"));
    nnmLongDanglingEndCB.setToolTipText("<html>Select a specific nearest neighbor model <br>for long dangling end(s) in the sequences (self complementary sequences).");

    // nearest neighbor model for single bulge loop
    options = new ComboBoxOption[6];
    options[0] = new ComboBoxOption("<html><u>DNA:</u>", "");
    options[1] = new ComboBoxOption("  Hicks and Santalucia 2004", "-sinBU san04");
    options[2] = new ComboBoxOption("  Tanaka et al. 2004 (default)", "-sinBU tan04");
    options[3] = new ComboBoxOption("<html><u>RNA:</u>", "");
    options[4] = new ComboBoxOption("  Blose et al. 2007", "-sinBU ser07");
    options[5] = new ComboBoxOption("  Lu et al. 1999 and 2006 (default)", "-sinBU tur06");
    
    nnmSingleBulgeLoopCB = new MeltingComboBox(options);
    nnmSingleBulgeLoopCB.setBackground(Color.WHITE);
    nnmSingleBulgeLoopCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for single bulge loop"));
    nnmSingleBulgeLoopCB.setToolTipText("<html>Select a specific nearest neighbor model <br>for single bulge loop(s) in the sequences.");

    // nearest neighbor model for long bulge loop
    options = new ComboBoxOption[4];
    options[0] = new ComboBoxOption("<html><u>DNA:</u>", "");
    options[1] = new ComboBoxOption("  Hicks and Santalucia 2004", "-lonBU san04");
    options[2] = new ComboBoxOption("<html><u>RNA:</u>", "");
    options[3] = new ComboBoxOption("  Mathews et al. 1999 and Lu et al 2006", "-lonBU tur06");
    
    nnmLongBulgeLoopCB = new MeltingComboBox(options);
    nnmLongBulgeLoopCB.setBackground(Color.WHITE);
    nnmLongBulgeLoopCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for long bulge loop"));
    nnmLongBulgeLoopCB.setToolTipText("<html>Select a specific nearest neighbor model <br>for long bulge loop(s) in the sequences.");

    // nearest neighbor model for inosine base
    options = new ComboBoxOption[4];
    options[0] = new ComboBoxOption("<html><u>DNA:</u>", "");
    options[1] = new ComboBoxOption("  Watkins and Santalucia 2005", "-ino san05");
    options[2] = new ComboBoxOption("<html><u>RNA:</u>", "");
    options[3] = new ComboBoxOption("  Wright et al. 2007", "-ino zno07");
    
    nnmInosineBaseCB = new MeltingComboBox(options);
    nnmInosineBaseCB.setBackground(Color.WHITE);
    nnmInosineBaseCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for inosine base"));
    nnmInosineBaseCB.setToolTipText("<html>Select a specific nearest neighbor model <br>for inosine base (I) in the sequences.");

    // nearest neighbor model for locked nucleic acid
    options = new ComboBoxOption[2];
    options[0] = new ComboBoxOption("  McTigue et al. 2004", "-lck mct04");
    options[1] = new ComboBoxOption("  Owczarzy et al. 2011", "-lck owc11");
    
    nnmLockedNucleicAcidCB = new MeltingComboBox(options);
    nnmLockedNucleicAcidCB.setBackground(Color.WHITE);
    nnmLockedNucleicAcidCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for locked nucleic acid"));
    nnmLockedNucleicAcidCB.setToolTipText("<html>Select a specific nearest neighbor model <br>for DNA sequences containing locked nucleic acid (Al, Tl, Cl or Gl).");

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
      JComponent newContentPane = new ThermodynamicOptionPanel();
      newContentPane.setOpaque(true); //content panes must be opaque
      frame.setContentPane(newContentPane);

      //Display the window.
      frame.pack();
      frame.setVisible(true);
  }

  /**
   * Builds and displays a {@link ThermodynamicOptionPanel} for debugging.
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
    
    String command = "" + nnmGuBasedPairInRnaCB.getValue() + " " + nnmSingleMismatchCB.getValue();
    
        
    return command;
  }
}
