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

/**
 * An option on a combo box used to select additional MELTING options.
 * 
 * @author John Gowers
 */
public class ComboBoxOption
{
  /**
   * The text displayed on the combo box option itself.
   */
  private String optionText;

  /**
   * The text that would be written to the command line were that option to be
   * selected.
   */
  private String commandLineText;

  /**
   * The tooltip text for the option
   */
  private String tooltip;
  
  /**
   * Constructor setting the text to be displayed in the combo box and the text
   * that would be written to the command line.
   */
  public ComboBoxOption(String optionText,
                        String commandLineText)
  {
    setOptionText(optionText);
    setCommandLineText(commandLineText);
  }

  /**
   * Constructor setting the text to be displayed in the combo box and the text
   * that would be written to the command line.
   */
  public ComboBoxOption(String optionText, String commandLineText, String tooltip)
  {
    setOptionText(optionText);
    setCommandLineText(commandLineText);
    setTooltip(tooltip);
  }

  /**
   * Sets the option text.
   * 
   * @param optionText Text displayed on the combo box option.
   */
  public void setOptionText(String optionText)
  {
    this.optionText = optionText;
  }

  /**
   * Gets the option text.
   * 
   * @return The text displayed on the combo box option.
   */
  public String getOptionText()
  {
    return optionText;
  }

  /**
   * Sets the command-line text.
   * 
   * @param commandLineText Text that would be written to the command line.
   */
  public void setCommandLineText(String commandLineText)
  {
    this.commandLineText = commandLineText;
  }

  /**
   * Gets the command-line text.
   * 
   * @return The text that would be written to the command line.
   */
  public String getCommandLineText()
  {
    return commandLineText;
  }

  /**
   * Copies this combo box option to another combo box option.
   * 
   * @return The new combo box option.
   */
  public ComboBoxOption copy()
  {
    ComboBoxOption newComboBoxOption = 
            new ComboBoxOption(getOptionText(), getCommandLineText());
    return newComboBoxOption;
  }

  /**
   * Returns the tooltip text.
   * 
   * @return the tooltip
   */
  public String getTooltip() {
    return tooltip;
  }

  /**
   * Sets the tooltip text for the option.
   * 
   * @param tooltip the tooltip to set
   */
  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }
  
  
}
    

