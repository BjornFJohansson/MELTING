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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import meltinggui.graphs.LineGraph;

/**
 * A frame with multiple tabs that could be used to display the melting results in different ways. 
 * 
 * <p>Not used at the moment but keeping it just in case, as an example.</p>
 * 
 * @author rodrigue
 *
 */
public class TabResultFrame extends JFrame {

  /**
   * 
   */
  private JTabbedPane tabbedPane = new JTabbedPane();
  /**
   * 
   */
  JPanel overviewPanel = new JPanel();
  /**
   * 
   */
  JTextArea overviewTextArea = new JTextArea();
  /**
   * 
   */
  JPanel htmlPanel = new JPanel();
  /**
   * 
   */
  JTextPane htmlTextPane = new JTextPane();
  /**
   * 
   */
  JPanel csvPanel = new JPanel();
  /**
   * 
   */
  JPanel graphPanel = new JPanel();
  
  
  
  /**
   * 
   */
  public TabResultFrame() {
    init();
  }
  
  /**
   * 
   */
  private void init() {
    
    // TODO - 1 tab overview, 1 tab html?, csv?, 1 optional tab for graph

    overviewPanel.add(overviewTextArea);
    overviewTextArea.setText("This is a test");
    
    htmlPanel.add(htmlTextPane);
    
    tabbedPane.addTab("Overview", null, overviewPanel, "Overview of the results");
    tabbedPane.addTab("HTML", null, htmlPanel, "Overview of the results in HTML");
    tabbedPane.addTab("CSV", null, csvPanel, "Overview of the results in csv");
    
    // TODO - add the graph tab only if needed
    tabbedPane.addTab("Graph", null, graphPanel, "Results as a graph");
    
    
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

        TabResultFrame f = new TabResultFrame();

        f.graphPanel.add(lineGraph);

        f.setSize(900, 650);
        f.getContentPane().add(f.tabbedPane);

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
