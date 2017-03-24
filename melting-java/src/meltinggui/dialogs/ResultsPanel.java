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

package meltinggui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import melting.ThermoResult;
import melting.nearestNeighborModel.NearestNeighborMode;

/**
 * A panel to display the melting temperature, enthalpy and entropy results. 
 *  
 * @author John Gowers
 * @author rodrigue
 */
public class ResultsPanel extends JPanel
{
  /**
   * 
   */
  private JLabel enthalpyLabel = new JLabel("Enthalpy (J/mol): ");
  /**
   * 
   */
  private JTextField enthalpyOutputField = new JTextField();  
  /**
   * 
   */
  private JLabel entropyLabel = new JLabel("Entropy (J.mol-1.K-1): ");
  /**
   * 
   */
  private JTextField entropyOutputField = new JTextField();  
  /**
   * 
   */
  private JLabel tmLabel = new JLabel("Melting temperature (degrees C): ");
  /**
   * 
   */
  private JTextField tmOutputField = new JTextField();  
  

  /**
   * Create and set up the panel.
   */
  public ResultsPanel()
  {
    disableEnthalpyEntropy();
    
    setLayout(new GridBagLayout());

    // creates a constraints object
    GridBagConstraints c = new GridBagConstraints();
    int row = 0;
    c.gridx = 0; // column 0
    c.gridy = row; // row 0
    c.weightx = 1; // resize so that all combo box have the same width
    c.anchor = GridBagConstraints.LINE_START;
    c.fill = GridBagConstraints.NONE;
    c.gridwidth = 1;

    // enthalpy
    c.weightx = 0;
    add(enthalpyLabel, c);
    c.gridx = 1;
    c.weightx = 1;
    c.fill = GridBagConstraints.HORIZONTAL;
    add(enthalpyOutputField, c);

    // entropy
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 0;
    c.fill = GridBagConstraints.NONE;
    add(entropyLabel, c);
    c.gridx = 1;
    c.weightx = 1;
    c.fill = GridBagConstraints.HORIZONTAL;
    add(entropyOutputField, c);

    // melting temperature
    c.gridx = 0;
    c.gridy = 2;
    c.weightx = 0;
    c.fill = GridBagConstraints.NONE;
    add(tmLabel, c);
    c.gridx = 1;
    c.weightx = 1;
    c.fill = GridBagConstraints.HORIZONTAL;
    add(tmOutputField, c);
    
  }

  /**
   * Disables the enthalpy and entropy output fields.
   */
  public final void disableEnthalpyEntropy()
  {
    enthalpyOutputField.setVisible(false);
    enthalpyLabel.setVisible(false);
    entropyOutputField.setVisible(false);
    entropyLabel.setVisible(false);
  }

  /**
   * Enables the enthalpy and entropy output fields.
   */
  public final void enableEnthalpyEntropy()
  {
    enthalpyOutputField.setVisible(true);
    enthalpyLabel.setVisible(true);
    entropyOutputField.setVisible(true);
    entropyLabel.setVisible(true);
  }

  /**
   * Displays the results from the MELTING program.
   * 
   * @param results The {@link ThermoResult <code>ThermoResult</code>} 
   *                containing the results.
   */
  public void displayMeltingResults(melting.ThermoResult results)
  {
    double tm = results.getTm();
    tmOutputField.setText(String.format("%.2f", tm));

    if (results.getCalculMethod() instanceof NearestNeighborMode) {
      double enthalpy = results.getEnthalpy();
      double entropy = results.getEntropy();

      enableEnthalpyEntropy();

      enthalpyOutputField.setText(String.format("%.0f", enthalpy));
      entropyOutputField.setText(String.format("%.2f", entropy));
    } else {
      disableEnthalpyEntropy();

      enthalpyOutputField.setText("");
    }
  }
}

