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
// Marine Dumousseau, Nicolas Lenovere
// EMBL-EBI, neurobiology computational group,             
// Cambridge, UK. e-mail: lenov@ebi.ac.uk, marine@ebi.ac.uk

package meltinggui.widgets;

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
   * Array of combo box options for the combo box.
   */
  private static final JFileChooser comboBoxOptions = new JFileChooser(); 
  
  private JTextField fileNameTextField;
  
  // TODO - create a panel with a textfield and button launching the jfileChooser - return the file name as value

  /**
   * Fills in the combo box options, and sets up the combo box model.
   * @param suppliedOptions The combo box options supplied parameters to the
   * constructor.
   */
  public MeltingFileChooser(ComboBoxOption[] suppliedOptions)
  {
    // TODO - create a panel with a textfield and button launching the jfileChooser - return the file name as value

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
   * @param newValue
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
}

