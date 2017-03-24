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

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.html.HTMLEditorKit;

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
  public ResultFrame() {
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
    
    // simple Result panel for one result - Will be hidden if the window option as been selected. 
    c.gridy = ++row; // row 3
    resultPanel.add(simpleResultPanel, c);
    
    
    // TODO - complex result panel - Will be hidden if the window option as not been selected. LineGaph ( + result as an array?) 
    
  }

  /**
   * @param args program arguments
   */
  public static void main(String[] args) {
    // 

    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        int size = 100;
        LineGraph lineGraph = LineGraph.randomLineGraph(size, "Melting temperatures");

        ResultFrame f = new ResultFrame();

        // creates a constraints object
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; // column 0
        c.gridy = 5; // row 1
        c.weightx = 1; // resize so that all combo box have the same width
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;

        f.resultPanel.add(lineGraph, c);

        
        
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
}
