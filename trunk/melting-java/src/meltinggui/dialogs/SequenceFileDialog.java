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

package meltinggui.dialogs;

import meltinggui.widgets.*;

/**
 * Dialog for specifying the sequence file and complementary sequence file.
 * 
 * @author rodrigue
 */
public class SequenceFileDialog extends InputFieldArray<MeltingFileChooser>
                            implements DialogInterface
{
  /**
   * Creates the dialog.
   */
  public SequenceFileDialog()
  {
    super(new String[] {"Sequence file: ",
                        "Complementary sequence file: "},
        MeltingFileChooser.class);
  }

  /**
   * Gets the sequence file path.
   * 
   * @return the sequence file path.
   */
  public String getSequenceFilePath()
  {
    return getValue(0);
  }

  /**
   * Gets the complementary sequence file path.
   * 
   * @return the complementary sequence file path.
   */
  public String getComplementaryFilePath()
  {
    return getValue(1);
  }

  /**
   * Gets the command-line flags corresponding to the chosen sequence file and
   * complementary sequence file.
   * 
   * @return The command-line flags the user would have to type in to specify
   *         the same sequence files.
   */
  @Override
  public String getCommandLineFlags()
  {
    String commandLineFlags = " -I ";

    // Remove all white space from the sequences.
    String sequenceText = getSequenceFilePath();
    String complementaryText = getComplementaryFilePath();

    commandLineFlags = commandLineFlags.concat(sequenceText);
    
    // Only give the flag for the complementary sequence file if the user has
    // entered one.
    if (getComplementaryFilePath().trim().length() > 0) {
      commandLineFlags += " -IC ";
      commandLineFlags += complementaryText;
    }

    return commandLineFlags;
  }
  
  /**
   * Set the sequence file path externally.
   * 
   * @param sequenceFilePath the sequence file path
   */
  public void setSequence(String sequenceFilePath) {
	  this.setValue(0, sequenceFilePath);
  }

  /**
   * Set the complement sequence file path  externally.
   * 
   * @param complementFilePath the complement file path to be added
   */
  public void setComplement(String complementFilePath) {
	  this.setValue(1, complementFilePath);
  }
  
  /**
   * Clear the text for the two text areas.
   */
  public void clearText() { 
	  setSequence("");
	  setComplement("");
  }
}

