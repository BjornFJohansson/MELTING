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
// Marine Dumousseau, Nicolas Le Novere
// EMBL-EBI, neurobiology computational group,             
// Cambridge, UK. e-mail: melting-forum@googlegroups.com

package meltinggui.frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLEditorKit;

import melting.Environment;
import melting.Helper;
import melting.configuration.OptionManagement;
import meltinggui.dialogs.ResultsPanel;
import meltinggui.graphs.LineGraph;
import meltinggui.widgets.SequenceNavigatorPanel;

/**
 * A frame with multiple tabs to display the melting results in different ways.
 * 
 * @author rodrigue
 *
 */
public class ResultFrame extends JFrame {

  /**
   * 
   */
  private static final String X_AXIS_LABEL = "Nucleotide (and position)";
  /**
   * 
   */
  private static final String X_AXIS_LABEL2 = "Position";
  /**
   * 
   */
  private static final String Y_AXIS_LABEL = "Temperature (degres C)";
  /**
   * 
   */
  private static final String LINE_GRAPH_TITLE_PREFIX = "Melting points with a window size of ";
  /**
   * 
   */
  JPanel resultPanel = new JPanel();
  /**
   * 
   */
  JTextPane informationArea = new JTextPane();  
  /**
   * 
   */
  JTextArea optionsTA = new JTextArea();
  /**
   * 
   */
  JPanel simpleResultPanel = new ResultsPanel();
  /**
   * 
   */
  SequenceNavigatorPanel sequenceNav;
  
  /**
   * 
   */
  public List<List<Environment>> results;
  
  /**
   * 
   */
  public int nbSequences = -1;
  
  /**
   * 
   */
  public boolean slidingWindow = false;
  /**
   * 
   */
  public String slidingWindowSize = "";
  
  /**
   * 
   */
  public ResultFrame(List<List<Environment>> results) { // TODO - pass in the original options ?
    this.results = results;
    
    if (results != null) {
      nbSequences = results.size();
      
      if (nbSequences > 0) {
        slidingWindow = results.get(0).size() > 1;
        slidingWindowSize = results.get(0).get(0).getOptions().get(OptionManagement.sliding_window);
      }
    }
    
    init();
  }
  
  /**
   * 
   */
  private void init() {
    
    getContentPane().add(resultPanel);
    
    resultPanel.setLayout(new GridBagLayout());
    resultPanel.setBackground(Color.WHITE);       

    // creates a constraints object
    GridBagConstraints c = new GridBagConstraints();
    int row = 0;
    c.gridx = 0; // column 0
    c.gridy = row; // row 0
    c.weightx = 1; // resize so that all combo box have the same width
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridwidth = 2;

    // First column
    
    // an information/help panel
    c.insets = new Insets(10, 4, 10, 4); 
    
    informationArea.setEditable(false);
    informationArea.setEditorKit(new HTMLEditorKit());
    informationArea.setText("<html>Long text explaning the result panel rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr<p>blabla"
        + "<p>an other paragraphe");
    JScrollPane scrollPane = new JScrollPane(informationArea);
    scrollPane.setMinimumSize(new Dimension(600, 120));
    scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1, true), "Help"));
    // resultPanel.add(informationArea, c);
    resultPanel.add(scrollPane, c);
    
    // options used text area
    c.gridy = ++row; // row 1
    optionsTA.setText("Long text showing the program arguments/options rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
    resultPanel.add(optionsTA, c);
    
    // slider to move between sequences. Will be hidden if there is only one sequence.
    c.gridy = ++row; // row 2
    sequenceNav = new SequenceNavigatorPanel(this);
    resultPanel.add(sequenceNav, c);
    
    if (nbSequences <= 1) {
      sequenceNav.setVisible(false);
    }
    
    // simple Result panel for one result - Will be hidden if the window option as been selected. 
    c.gridy = ++row; // row 3
    resultPanel.add(simpleResultPanel, c);
    
    if (slidingWindow) {
      simpleResultPanel.setVisible(false);
    
    
      // complex result panel - Will be hidden or not present if the window option has not been selected. LineGaph ( + result as an array?) 
      c.gridy = ++row; // row 4
      LineGraph lg = LineGraph.createLineGraph(results.get(0).size(), results.get(0), X_AXIS_LABEL, X_AXIS_LABEL2, Y_AXIS_LABEL, LINE_GRAPH_TITLE_PREFIX + " '" + slidingWindowSize + "'");
      resultPanel.add(lg, c);
      
    }
    
    // TODO - add an empty panel to fill the remaining space if needed
    c.gridy = ++row; // row 5-6
  }
  
  

  /**
   * Returns the number of sequences in this {@link ResultFrame}.
   * 
   * @return the number of sequences in this {@link ResultFrame}.
   */
  public int getNbSequences() {
    return nbSequences;
  }

  /**
   * @param args program arguments
   */
  public static void main(String[] args) {
    // 

    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {


        String[] singleSequenceArgs = {"-E", "Na=0.3", "-P", "0.03", "-H", "dnadna", "-nnpath", "/home/rodrigue/src/melting-java/Data", "-S", "AATGCTTGTATAGTTGA"};
        String[] singleSequenceWithWindowArgs = {"-E", "Na=0.3", "-P", "0.03", "-H", "dnadna", "-w", "4", "-nnpath", "/home/rodrigue/src/melting-java/Data", 
            "-S", "AATGCTTGTATAGTTGAAATGCTTGTATAGTTGAAATGCTTGTATAGTTGAAATGCTTGTATAGTTGAAATGCTTGTATAGTTGAAATGCTTGTATAGTTGAAATGCTTGTATAGTTGAAATGCTTGTATAGTTGA"};
        String[] fastaFileMeltingArgs = {"-E", "Na=0.3", "-P", "0.03", "-H", "dnadna", "-nnpath", "/home/rodrigue/src/melting-java/Data", 
            "-I", "/home/rodrigue/src/melting-java/testDataNico/fasta-example.txt", "-IC", "/home/rodrigue/src/melting-java/testDataNico/fasta-example-complement.txt"};
        String[] fastaFileMeltingWithWArgs = {"-E", "Na=0.3", "-P", "0.03", "-H", "dnadna", "-w", "4", "-nnpath", "/home/rodrigue/src/melting-java/Data", 
            "-I", "/home/rodrigue/src/melting-java/testDataNico/fasta-example.txt", "-IC", "/home/rodrigue/src/melting-java/testDataNico/fasta-example-complement.txt"};

        OptionManagement manager = new OptionManagement();
        manager.initialiseLogger();
        
        // single sequence, one melting computation
        Environment env = manager.createEnvironment(singleSequenceArgs);        
        Environment result = Helper.computeMeltingResults(env);        
        List<List<Environment>> results = new ArrayList<List<Environment>>();
        results.add(new ArrayList<Environment>());
        results.get(0).add(result);
        
        // single sequence, several melting computation
        env = manager.createEnvironment(singleSequenceWithWindowArgs);        
        List<Environment> resultSW = Helper.computeMeltingResultsWithWindow(env);        
        results = new ArrayList<List<Environment>>();
        results.add(resultSW);
        
        // several sequences, one melting computation
        env = manager.createEnvironment(fastaFileMeltingArgs);        
        List<Environment> resultFastaFile = Helper.computeMeltingOnFastaFile(env.getOptions());
        results = new ArrayList<List<Environment>>();
        
        for (Environment resultSeq : resultFastaFile) {
          List<Environment> resultsSingle = new ArrayList<Environment>();
          resultsSingle.add(resultSeq);
          
          results.add(resultsSingle);          
        }
        
        
        ResultFrame f = new ResultFrame(results);


        f.setSize(900, 650);

        WindowListener windowCloser = new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            System.exit(0);
          }
        };

        f.addWindowListener(windowCloser);
        f.setVisible(true);
      }
    });

  }

  /**
   * Updates the GUI, displaying the results for the sequence at the given index.
   * 
   * @param sequenceIndex the sequence index
   */
  public void updateSequence(int sequenceIndex) {
    // TODO - update the GUI, displaying the results for the sequence at the given index.  
    
  }
}
