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

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;



/**
 * The Class StatusPanel shows the interactive bar at the bottom
 * of the main application screen.
 * @author dallepep
 */
public class StatusPanel extends JPanel {

	private static final long serialVersionUID = -7979299860162515406L;
	/** The textLabel. */
	private JLabel textLabel = new JLabel(" ",JLabel.LEFT);
	
	private JLabel progressLabel = new JLabel(" ", JLabel.RIGHT);
	
	
	/**
	 * Instantiates a new status panel.
	 */
	public StatusPanel() {
		setLayout(new BorderLayout());
		add(textLabel,BorderLayout.WEST);
		add(progressLabel,BorderLayout.EAST);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}
	
	/**
	 * Sets the file name.
	 * 
	 * @param text the new file name
	 */
	public void setText(String text) {
		textLabel.setText(text);
	}
	
}
