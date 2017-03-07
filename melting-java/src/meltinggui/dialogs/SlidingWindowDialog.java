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

import java.awt.Color;

import meltinggui.widgets.InputField;
import meltinggui.widgets.MeltingTextField;

/**
 * A dialog for specifying the sliding window.
 * 
 * @author rodrigue
 */
public class SlidingWindowDialog extends InputField<MeltingTextField>
  implements DialogInterface
{

  /**
   * Sets up the dialog.
   */
  public SlidingWindowDialog()
  {
    super("Sliding window: ", MeltingTextField.class);
    setBackground(new Color(0, 0, 0, 0));
  }

  /**
   * Gets the command-line flags.
   * @return The command-line flags the user would have used to specify the
   *         given sliding window if they had been using the command-line.
   */
  @Override
  public String getCommandLineFlags()
  {
    String value = getValue().trim();
    
    if (value.length() > 0) {
      String commandLineFlags = " -w " + getValue();
      return commandLineFlags;
    }
    
    return "";
  }
}

