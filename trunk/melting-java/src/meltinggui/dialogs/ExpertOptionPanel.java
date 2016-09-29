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
public class ExpertOptionPanel extends JPanel implements ActionListener, DialogInterface {

  
  
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
  public ExpertOptionPanel() {

    setLayout(new GridBagLayout());
    setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "Expert options"));

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
    //  Set of melting temperature corrections:
    
    // Formamide correction
    ComboBoxOption[] options = new ComboBoxOption[3];
    options[0] = new ComboBoxOption("DNA:", "");
    options[1] = new ComboBoxOption("  Blake et al 1996 (default)", "-for bla96");
    options[2] = new ComboBoxOption("  Linear correction", "-for lincorr");
    
    formamideCorrectionCB = new MeltingComboBox(options); // TODO - add the JLabel or put a Border
    formamideCorrectionCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "Formamide correction"));
    formamideCorrectionCB.setToolTipText("Forces to use a specific formamide correction.");

    // DMSO correction
    options = new ComboBoxOption[5];
    options[0] = new ComboBoxOption("DNA:", "");
    options[1] = new ComboBoxOption("  Von Ahsen et al 2001 (default)", "-DMSO ahs01");
    options[2] = new ComboBoxOption("  Musielski et al. 1981", "-DMSO mus81");
    options[3] = new ComboBoxOption("  Cullen et al. 1976", "-DMSO cul76");
    options[4] = new ComboBoxOption("  Escara et al. 1980", "-DMSO esc80");
    
    dmsoCorrectionCB = new MeltingComboBox(options);   
    dmsoCorrectionCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "DMSO correction"));
    dmsoCorrectionCB.setToolTipText("Forces to use a specific DMSO correction (DMSO is always in percent).");
    
    // ion corrections
    int index = 0;
    options = new ComboBoxOption[30];
    options[index] = new ComboBoxOption("Sodium corrections:", "");
    options[++index] = new ComboBoxOption("  DNA:", "");
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
    
    options[++index] = new ComboBoxOption("  RNA or 2-o methyl RNA:", "");
    options[++index] = new ComboBoxOption("    Tan et al. 2007 (default)", "-ion tanna07");
    options[++index] = new ComboBoxOption("    Wetmur 1991", "-ion wetrna91");
    
    options[++index] = new ComboBoxOption("  RNA/DNA:", "");
    options[++index] = new ComboBoxOption("    Wetmur 1991 (default)", "-ion wetdnarna91");

    options[++index] = new ComboBoxOption("Magnesium corrections:", "");
    options[++index] = new ComboBoxOption("  DNA:", "");
    options[++index] = new ComboBoxOption("    Owczarzy et al. 2008 (default)", "-ion owcmg08");
    options[++index] = new ComboBoxOption("    Tan et al. 2006", "-ion tanmg06");
    
    options[++index] = new ComboBoxOption("  RNA or 2-o methyl RNA:", "");
    options[++index] = new ComboBoxOption("    Tan et al. 2007", "-ion tanmg07");

    options[++index] = new ComboBoxOption("Mixed Na Mg corrections:", "");
    options[++index] = new ComboBoxOption("  DNA:", "");
    options[++index] = new ComboBoxOption("    Owczarzy et al. 2008 (default)", "-ion owcmix08");
    options[++index] = new ComboBoxOption("    Tan et al. 2007", "-ion tanmix07");

    options[++index] = new ComboBoxOption("  RNA or 2-o methyl RNA:", "");
    options[++index] = new ComboBoxOption("    Tan et al. 2007", "-ion tanmix07"); // TODO - same name with the DNA option ??

    ionCorrectionCB = new MeltingComboBox(options);   
    ionCorrectionCB.setMaximumRowCount(20);
    ionCorrectionCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "ion corrections"));
    ionCorrectionCB.setToolTipText("<html>Forces to use a specific ion correction.<br>" 
        + "By default, the program use the algorithm from Owczarzy et al. 2008 : ratio = Mg^0.5 and monovalent = Na + Tris + K<br><br>"
        + "<ul><li>    if monovalent = 0, a magnesium correction is used.<br>"
        + "<li>    if ratio < 0.22, a sodium correction is used.<br>"
        + "<li>    if 0.22 <= ratio < 6, a mixed Na Mg correction is used.<br>"
        + "<li>    if ratio >= 6, a magnesium correction is used.");
   
    // Na equivalent concentration correction
    options = new ComboBoxOption[4];
    options[0] = new ComboBoxOption("DNA:", "");
    options[1] = new ComboBoxOption("  Von Ahsen et al 2001 (default)", "-naeq ahs01");
    options[2] = new ComboBoxOption("  Mitsuhashi et al. 1996", "-naeq mit96");
    options[3] = new ComboBoxOption("  Peyret 2000", "-naeq pey00");
    
    naEquivalentCB = new MeltingComboBox(options);   
    naEquivalentCB.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "Na equivalent concentration"));
    naEquivalentCB.setToolTipText("Forces to use a specific ion correction which gives a sodium equivalent concentration if other cations are present.");

    // Set of thermodynamic parameters and methods : 
    /*
     *  Set of thermodynamic parameters and methods : 
By default, the approximative mode is used for oligonucleotides longer than 60 bases (the default threshold value), otherwise the nearest 
neighbor model is used. 

-am [optional name]
    Forces to use a specific approximative formula. You can use one of the following : 
        (DNA)
            ahs01 (from von Ahsen et al. 2001)
            che93 (from Marmur 1962, Chester et al. 1993)
            che93corr (from von Ahsen et al. 2001, Marmur 1962, Chester et al. 1993)
            schdot (Marmur-Schildkraut-Doty formula)
            owe69 (from Owen et al. 1969)
            san98 (from Santalucia et al. 1998)
            wetdna91 (from Wetmur 1991)  (by default)
        (RNA)
            wetrna91 (from Wetmur 1991)  (by default) 
        (DNA/RNA)
            wetdnarna91 (from Wetmur 1991)  (by default)
    If there is no formula name after the option -am, we will compute the melting temperature with the default approximative formula. 

-nn [optional name]
    Forces to use a specific nearest neighbor model. You can use one of the following : 
        (DNA)
            all97 (from Allawi and Santalucia 1997) (by default)
            bre86 (from Breslauer et al. 1986)
            san04 (from Hicks and Santalucia 2004)  
            san96 (from Santalucia et al. 1996)
            sug96 (from Sugimoto et al 1996)
            tan04 (from Tanaka et al. 2004)
        (RNA)
            fre86 (from Freier al. 1986)
            xia98 (from Xia et al. 1998)  (by default)
        (DNA/RNA)
            sug95 (from Sugimoto et al. 1995)  (by default)
        (mRNA/RNA)
            tur06 (from Kierzek et al. 2006)  (by default)

    If there is no formula name after the option -nn, we will compute the melting temperature with the default nearest neighbor model. 
    Each nearest neighbor model uses a specific xml file containing the thermodynamic values. If you want to use another file, write the file name or the file pathway preceded by ':' (-nn [optionalname:optionalfile]).
        Ex: -nn tan04:fileName if you want to use the nearest neighbor model from Tanaka et al. 2004 with the thermodynamic parameters in the file fileName. 
        Ex: -nn :fileName if you want to use the default nearest neighbor model with the thermodynamic parameters in the file fileName. 

-sinMM [name]
    Forces to use a specific nearest neighbor model for single mismatch(es) in the sequences. You can use one of the following :
        (DNA)
            allsanpey (from Allawi, Santalucia and Peyret 1997, 1998 and 1999)  (by default)
        (DNA/RNA)
            wat10 (from Watkins et al. 2011) (by default)
        (RNA)
            tur06 (from Lu et al. 2006)
            zno07 (from Davis et al. 2007)  (by default)
            zno08 (from Davis et al. 2008)

    To change the file containing the thermodynamic parameters for single mismatch computation, the same syntax as the one for the -nn option is used.

-GU [name]
    Forces to use a specific nearest neighbor model for GU base pairs in RNA sequences. You can use one of the following :
           tur99 (from Turner et al. 1999).
           ser12 (from Serra et al. 2012) (by default)
    To change the file containing the thermodynamic parameters for GU base pairs computation, the same syntax as the one for the -nn option is used.

-tanMM [name]
    Forces to use a specific nearest neighbor model for tandem mismatches in the sequences. You can use one of the following :
        (DNA)
            allsanpey (from Allawi, Santalucia and Peyret 1997, 1998 and 1999)  (by default)
        (RNA)
            tur99 (from Mathews et al. 1999)  (by default)

    To change the file containing the thermodynamic parameters for tandem mismatches computation, the same syntax as the one for the -nn option is used. 

-intLP [name]
    Forces to use a specific nearest neighbor model for internal loop in the sequences. You can use one of the following :
        (DNA)
            san04 (from Hicks and Santalucia 2004)  (by default)
        (RNA)
            tur06 (from Lu et al. 2006)  (by default)
            zno07 (from Badhwarr et al. 2007, only for 1x2 loop)

    To change the file containing the thermodynamic parameters for internal loop computation, the same syntax as the one for the -nn option is used.

-sinDE [name]
    Forces to use a specific nearest neighbor model for single dangling end(s) in the sequences. You can use one of the following :
        (DNA)
            bom00 (from Bommarito et al. 2000)  (by default)
            sugdna02 (from Ohmichi et al. 2002, only for polyA dangling ends)
        (RNA)
            sugrna02 (from Ohmichi et al. 2002, only for polyA dangling ends)
            ser08 (from Miller et al. 2008)  (by default)

    To change the file containing the thermodynamic parameters for single dangling end computation, the same syntax as the one for the -nn option is used. 

-secDE [name]
    Forces to use a specific nearest neighbor model for second dangling end(s) in the sequences. You can use one of the following :
        (DNA)
            sugdna02 (from Ohmichi et al. 2002, only for polyA dangling ends)  (by default)
        (RNA)
            sugrna02 (from Ohmichi et al. 2002, only for polyA dangling ends)
            ser05 (from O'toole et al. 2005)
            ser06 (from O'toole et al. 2006)  (by default)

    To change the file containing the thermodynamic parameters for second dangling end computation, the same syntax as the one for the -nn option is used. 

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

-CNG [name]
    Forces to use a specific nearest neighbor model for RNA sequences composed of CNG repeats (G(CNG)xC where N is a single N/N mismatch). You can only use bro05 (from Magdalena et al. 2005).
    To change the file containing the thermodynamic parameters for RNA sequences composed of CNG repeats computation, the same syntax as the one for the -nn option is used. 

-ino [name]
    Forces to use a specific nearest neighbor model for inosine base (I) in the sequences. You can use one of the following :
        (DNA)
            san05 (from Watkins and Santalucia 2005)  (by default)
        (RNA)
            zno07 (from Wright et al. 2007)  (by default)
            
    To change the file containing the thermodynamic parameters for inosine base computation, the same syntax as the one for the -nn option is used. 

-ha [name]
    Forces to use a specific nearest neighbor model for hydroxyadenine base (A*) in DNA sequences. You can only use sug01 (from Kawakami et al. 2001).
    To change the file containing the thermodynamic parameters for hydroxyadenine base computation, the same syntax as the one for the -nn option is used. 

-azo [name]
    Forces to use a specific nearest neighbor model for DNA sequences containing azobenzene (cis : X_C or trans : X_T). You can only use asa05 (from Asanuma et al. 2005).
    To change the file containing the thermodynamic parameters for azobenzene computation, the same syntax as the one for the -nn option is used.

-lck [name]
    Forces to use a specific nearest neighbor model for DNA sequences containing locked nucleic acid (Al, Tl, Cl or Gl). You can use mct04 (from McTigue et al. 2004) or
    owc11 (from Owczarzy et al. 2011, by default).
    To change the file containing the thermodynamic parameters for locked acid nucleic computation, the same syntax as the one for the -nn option is used.

-tanLck [name]
    Forces to use a specific nearest neighbor model for DNA sequences containing consecutive locked nucleic acid (Al, Tl, Cl or Gl). You can only use owc11 (from Owczarzy et al. 2011, by default).
    To change the file containing the thermodynamic parameters for locked acid nucleic computation, the same syntax as the one for the -nn option is used.

-sinMMLck [name]
    Forces to use a specific nearest neighbor model for DNA sequences containing consecutive locked nucleic acid with one single mismatch (Al, Tl, Cl or Gl).
    You can use only owc11 (from Owczarzy et al. 2011, by default).
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
    
    String command = "" + formamideCorrectionCB.getValue() + " " + dmsoCorrectionCB.getValue();
    
        
    return command;
  }
}
