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
   * Create the panel.
   */
  public ThermodynamicOptionPanel() {

    setLayout(new GridBagLayout());
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "Thermodynamic parameters and methods"));

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

    add(approximativeFormulaCB, c);
    
    c.anchor = GridBagConstraints.EAST;

    // Na equivalent correction
    row++;
    c.gridy = row; // row ?
    add(nearestNeighborModelCB, c);
    
    // DMSO correction
    row++;
    c.gridy = row; // row ?
    add(nnmSingleMismatchCB, c);
    
    // formamide correction
    row++;
    c.gridy = row; // row ?
    add(nnmGuBasedPairInRnaCB, c);

    
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
  private void initComboBoxes() 
  {
    // Set of thermodynamic parameters and methods:

    ComboBoxOption[] options = null;

    // approximative formula
    int index = 0;
    options = new ComboBoxOption[12];
    options[index] = new ComboBoxOption("DNA:", "");
    options[++index] = new ComboBoxOption("  Von Ahsen et al. 2001", "-am ahs01");
    options[++index] = new ComboBoxOption("  Marmur 1962, Chester et al. 1993", "-am che93");
    options[++index] = new ComboBoxOption("  Von Ahsen et al. 2001, Marmur 1962, Chester et al. 1993", "-am che93corr");
    options[++index] = new ComboBoxOption("  Marmur-Schildkraut-Doty formula", "-am schdot");
    options[++index] = new ComboBoxOption("  Owen et al. 1969", "-am owe69");
    options[++index] = new ComboBoxOption("  Santalucia et al. 1998", "-am san98");
    options[++index] = new ComboBoxOption("  Wetmur 1991 (default)", "-am wetdna91");

    options[++index] = new ComboBoxOption("RNA:", "");
    options[++index] = new ComboBoxOption("  Wetmur 1991", "-am wetrna91");

    options[++index] = new ComboBoxOption("<html><u>DNA/RNA:</u>", "");
    options[++index] = new ComboBoxOption("  Wetmur 1991", "-am wetdnarna91");

    approximativeFormulaCB = new MeltingComboBox(options);   
    approximativeFormulaCB.setMaximumRowCount(20);
    approximativeFormulaCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "approximative formula"));
    approximativeFormulaCB.setToolTipText("Select a specific approximative formula."); 

    // nearest neighbor model
    index = 0;
    options = new ComboBoxOption[14];
    options[index] = new ComboBoxOption("DNA:", "");
    options[++index] = new ComboBoxOption("  Allawi and Santalucia 1997 (default)", "-nn all97");
    options[++index] = new ComboBoxOption("  Breslauer et al. 1986", "-nn bre86");
    options[++index] = new ComboBoxOption("  Hicks and Santalucia 2004", "-nn san04");
    options[++index] = new ComboBoxOption("  Santalucia et al. 1998", "-nn san98");
    options[++index] = new ComboBoxOption("  Sugimoto et al. 1996", "-nn sug96");
    options[++index] = new ComboBoxOption("  Tanaka et al. 2004", "-nn tan04");

    options[++index] = new ComboBoxOption("RNA:", "");
    options[++index] = new ComboBoxOption("  Wetmur 1991", "-nn wetrna91");
    options[++index] = new ComboBoxOption("  Xia et al. 1998 (default)", "-nn fre86");

    options[++index] = new ComboBoxOption("<html><u>DNA/RNA:</u>", "");
    options[++index] = new ComboBoxOption("  Sugimoto et al. 1995", "-nn sug95");

    options[++index] = new ComboBoxOption("<html><u>mRNA/RNA:</u>", "");
    options[++index] = new ComboBoxOption("  Kierzek et al. 2006", "-nn tur06");

    nearestNeighborModelCB = new MeltingComboBox(options);   
    nearestNeighborModelCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "nearest neighbor model"));
    nearestNeighborModelCB.setToolTipText("Forces to use a specific nearest neighbor model.");
    
    // nearest neighbor model for single mismatch(es) in the sequences
    index = 0;
    options = new ComboBoxOption[8];
    options[index] = new ComboBoxOption("DNA:", "");
    options[++index] = new ComboBoxOption("  Allawi, Santalucia and Peyret 1997, 1998 and 1999", "-sinMM allsanpey");

    options[++index] = new ComboBoxOption("<html><u>DNA/RNA:</u>", "");
    options[++index] = new ComboBoxOption("  Watkins et al. 2011", "-sinMM wat10");

    options[++index] = new ComboBoxOption("RNA:", "");
    options[++index] = new ComboBoxOption("  Lu et al. 2006", "-sinMM tur06");
    options[++index] = new ComboBoxOption("  Davis et al. 2007 (default)", "-sinMM zno07");
    options[++index] = new ComboBoxOption("  Davis et al. 2008", "-sinMM zno08");

    nnmSingleMismatchCB = new MeltingComboBox(options);   
    nnmSingleMismatchCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for single mismatch(es)"));
    nnmSingleMismatchCB.setToolTipText("Forces to use a specific nearest neighbor model for single mismatch(es) in the sequences.");
    
    // nearest neighbor model for GU base pairs in RNA sequences
    options = new ComboBoxOption[2];
    options[0] = new ComboBoxOption("  Turner et al. 1999", "-GU tur99");
    options[1] = new ComboBoxOption("  Serra et al. 2012 (default)", "-GU ser12");
    
    nnmGuBasedPairInRnaCB = new MeltingComboBox(options);
    nnmGuBasedPairInRnaCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for GU base pairs in RNA"));
    nnmGuBasedPairInRnaCB.setToolTipText("Forces to use a specific formamide correction.");

    // nearest neighbor model for tandem mismatches
    options = new ComboBoxOption[4];
    options[0] = new ComboBoxOption("DNA:", "");
    options[1] = new ComboBoxOption("  Allawi, Santalucia and Peyret 1997, 1998 and 1999", "-tanMM allsanpey");
    options[2] = new ComboBoxOption("RNA:", "");
    options[3] = new ComboBoxOption("  Mathews et al. 1999", "-tanMM tur99");
    
    nnmTandemMismatchCB = new MeltingComboBox(options);
    nnmTandemMismatchCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for tandem mismatches"));
    nnmTandemMismatchCB.setToolTipText("Forces to use a specific nearest neighbor model for tandem mismatches in the sequences.");

    // nearest neighbor model for internal loop
    options = new ComboBoxOption[5];
    options[0] = new ComboBoxOption("DNA:", "");
    options[1] = new ComboBoxOption("  Hicks and Santalucia 2004", "-intLP allsanpey");
    options[2] = new ComboBoxOption("RNA:", "");
    options[3] = new ComboBoxOption("  Lu et al. 2006 (default)", "-intLP tur06");
    options[4] = new ComboBoxOption("  Badhwarr et al. 2007, only for 1x2 loop", "-intLP zno07");
    
    nnmInternalLoopCB = new MeltingComboBox(options);
    nnmInternalLoopCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for internal loop"));
    nnmInternalLoopCB.setToolTipText("Forces to use a specific nearest neighbor model for internal loop in the sequences.");

    // nearest neighbor model for single dangling end
    options = new ComboBoxOption[6];
    options[0] = new ComboBoxOption("DNA:", "");
    options[1] = new ComboBoxOption("  Bommarito et al. 2000 (default)", "-sinDE bom00");
    options[2] = new ComboBoxOption("  Ohmichi et al. 2002, only for polyA dangling ends", "-sinDE sugdna02");
    options[3] = new ComboBoxOption("RNA:", "");
    options[4] = new ComboBoxOption("  Ohmichi et al. 2002, only for polyA dangling ends", "-sinDE sugrna02");
    options[5] = new ComboBoxOption("  Miller et al. 2008 (default)", "-sinDE ser08");
    
    nnmSingleDanglingEndCB = new MeltingComboBox(options);
    nnmSingleDanglingEndCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for single dangling end"));
    nnmSingleDanglingEndCB.setToolTipText("Forces to use a specific nearest neighbor model for single dangling end(s) in the sequences.");

    // nearest neighbor model for second dangling end
    options = new ComboBoxOption[6];
    options[0] = new ComboBoxOption("DNA:", "");
    options[1] = new ComboBoxOption("  Ohmichi et al. 2002, only for polyA dangling ends", "-secDE sugdna02");
    options[2] = new ComboBoxOption("RNA:", "");
    options[3] = new ComboBoxOption("  Ohmichi et al. 2002, only for polyA dangling ends", "-secDE sugrna02");
    options[4] = new ComboBoxOption("  O'toole et al. 2005", "-secDE ser05");
    options[5] = new ComboBoxOption("  O'toole et al. 2006 (default)", "-secDE ser06");
    
    nnmSingleDanglingEndCB = new MeltingComboBox(options);
    nnmSingleDanglingEndCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "NN model for second dangling end"));
    nnmSingleDanglingEndCB.setToolTipText("Forces to use a specific nearest neighbor model for second dangling end(s) in the sequences.");

    /*
       // TODO - other combo boxes !!
x     nearest neighbor model for  tandem mismatches
x     nearest neighbor model for internal loop
x     nearest neighbor model for single dangling end
x     neighbor model for second dangling end
     neighbor model for long dangling end
     nearest neighbor model for single bulge loop
     neighbor model for long bulge loop
     nearest neighbor model for inosine base
     nearest neighbor model for DNA sequences containing locked nucleic acid (Al, Tl, Cl or Gl)
-lonDE [name]
    Forces to use a specific nearest neighbor model for long dangling end(s) in the sequences (self complementary sequences). You can use one of the following :
        (DNA)
            sugdna02 (from Ohmichi et al. 2002, only for polyA dangling ends)  (by default)
        (RNA)
            sugrna02 (from Ohmichi et al. 2002, only for polyA dangling ends)  (by default)

    To change the file containing the thermodynamic parameters for long dangling end computation, the same syntax as the one for the -nn option is used. 

-sinBU [name]
    Forces to use a specific nearest neighbor model for single bulge loop(s) in the sequences. You can use one of the following :
        (DNA)
            san04 (from Hicks and Santalucia 2004)
            tan04 (from Tanaka et al. 2004)  (by default)
        (RNA)
            ser07 (from Blose et al. 2007)
            tur06 (from Lu et al. 1999 and 2006)  (by default)

    To change the file containing the thermodynamic parameters for single bulge loop computation, the same syntax as the one for the -nn option is used. 

-lonBU [name]
    Forces to use a specific nearest neighbor model for long bulge loop(s) in the sequences. You can use one of the following :
        (DNA) 
            san04 (from Hicks and Santalucia 2004)  (by default)
        (RNA)
            tur06 (from Mathews et al. 1999 and Lu et al 2006)  (by default)
            
    To change the file containing the thermodynamic parameters for long bulge loop computation, the same syntax as the one for the -nn option is used. 

-ino [name]
    Forces to use a specific nearest neighbor model for inosine base (I) in the sequences. You can use one of the following :
        (DNA)
            san05 (from Watkins and Santalucia 2005)  (by default)
        (RNA)
            zno07 (from Wright et al. 2007)  (by default)
            
    To change the file containing the thermodynamic parameters for inosine base computation, the same syntax as the one for the -nn option is used. 

-lck [name]
    Forces to use a specific nearest neighbor model for DNA sequences containing locked nucleic acid (Al, Tl, Cl or Gl). You can use mct04 (from McTigue et al. 2004) or
    owc11 (from Owczarzy et al. 2011, by default).
    To change the file containing the thermodynamic parameters for locked acid nucleic computation, the same syntax as the one for the -nn option is used.

     */


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
