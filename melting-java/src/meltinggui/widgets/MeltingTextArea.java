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

import javax.swing.JTextArea;

/**
 * A text area for use with the MELTING program.  Implements
 * {@link InputWidgetInterface}.  
 * @author John Gowers
 */
public class MeltingTextArea extends JTextArea
                              implements InputWidgetInterface
{
  /**
   * Creates a new MELTING text area with the default number of 4 rows and
   * 32 columns.
   */
  public MeltingTextArea()
  {
    this(4, 32);
  }

  /**
   * Creates a new MELTING text area with a given number of columns.
   * @param   rows      The number of rows in the text area.
   * @param   columns   The number of columns in the text area.
   */
  public MeltingTextArea(int rows, int columns)
  {
    super(rows, columns);
    setLineWrap(true);
  }

  /**
   * Sets the text in the area.
   * @param newValue The new text for the area.
   */
  @Override
  public void setValue(String newValue)
  {
    setText(newValue);
  }

  /**
   * Returns the text from the area.
   * @return The text from the area.
   */
  @Override
  public String getValue()
  {
    return getText();
  }

  /**
   * Selects all the text in the area.
   */
  @Override
  public void selectAll()
  {
    super.selectAll();
  }


  /**
   * Override the <code>getMinimumSize</code> method to return the preferred 
   * size - these text areas should never be any smaller than their preferred
   * size.
   */
  @Override
  public Dimension getMinimumSize()
  {
    return getPreferredSize();
  }
}
