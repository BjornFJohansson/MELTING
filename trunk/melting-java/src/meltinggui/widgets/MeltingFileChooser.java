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

package meltinggui.widgets;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A panel where the user can select a file.  It implements
 * {@link InputWidgetInterface} so it has a
 * method that returns a string value corresponding to particular command-line
 * parameters.
 * 
 * @author rodrigue
 */
public class MeltingFileChooser extends JPanel
  implements InputWidgetInterface
{
  /**
   * file browser so that the user can select a file easily.
   */
  private static final JFileChooser fileChooser = new JFileChooser(); 
  
  /**
   * 
   */
  private JTextField fileNameTextField;
  /**
   * 
   */
  private JButton fileChooserButton = new JButton("...");
  
  /**
   * 
   */
  public MeltingFileChooser()
  {
    super();
    setLayout(new GridBagLayout());

    // creates a constraints object
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(2, 2, 2, 2); // insets for all components
    c.gridx = 0; // column 0
    c.gridy = 0; // row 0
    c.ipadx = 10; // increases components width by 10 pixels
    c.ipady = 0; // increases components height by 10 pixels
    c.weightx = 2;
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    
    fileNameTextField = new JTextField(15);
    fileNameTextField.setMinimumSize(new Dimension(50, 10));
    add(fileNameTextField, c);
    
    fileChooserButton.setAction(new AbstractAction() {
      
      @Override
      public void actionPerformed(ActionEvent e) {

        int result = fileChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
          setValue(fileChooser.getSelectedFile().getAbsolutePath());
        }
      }
    });
    fileChooserButton.setToolTipText("Clicking on this button will open a file browser where you will be able to easily select the file.");
    fileChooserButton.setText("...");
    
    c.gridx = 1; // column 1
    c.weightx = 0;
    c.fill = GridBagConstraints.NONE;
    add(fileChooserButton, c);
  }


  /**
   * Gets the command-line text corresponding to the selected file.
   * 
   * @return The command-line text.
   */
  @Override
  public String getValue()
  {
    return fileNameTextField.getText();
  }

  /**
   * Sets the file name in the JTextField
   * 
   * @param newValue the new value
   */
  @Override
  public void setValue(String newValue) {
    fileNameTextField.setText(newValue);
  }

  /**
   * Does nothing.
   */
  @Override
  public void selectAll() {
    
  }
  
  /**
   * Sets the tooltip value on the text field.
   * 
   * @param tooltip the tooltip
   */
  public void setTextFieldToolTip(String tooltip) {
    fileNameTextField.setToolTipText(tooltip);
  }
}

