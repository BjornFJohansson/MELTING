// This program is free software; you can redistribute it and/or modify it
// under the terms of the GNU General Public Licence as published by the Free
// Software Foundation; either version 2 of the Licence, or (at your option)
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

import meltinggui.widgets.*;

/**
 * 
 *
 */
public class HybridizationDialog extends InputField<HybridizationWidget>
                                 implements DialogInterface
{
  /**
   * Set up the dialog.
   */
  public HybridizationDialog()
  {
    super("Hybridization type: ", HybridizationWidget.class);
  }

  /**
   * Returns the command-line flags corresponding to the user's choice of 
   * hybridization type.
   * @return The command-line flags.
   */
  @Override
  public String getCommandLineFlags()
  {
    String commandLineFlags = " -H " + getValue();
    return commandLineFlags;
  }
}

