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
package meltinggui.menu;

import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import meltinggui.frames.OuterFrame;


/**
 * The Class LicenseDialog shows a text representation of the GPL
 * @author dallepep
 */
public class LicenseDialog extends JDialog {

	private static final long serialVersionUID = -8023870968821351252L;
	
	/** The html pane. */
	public JEditorPane htmlPane;

	/**
	 * Instantiates a new license dialog.
	 * 
	 * @param application the a
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public LicenseDialog (OuterFrame application) throws IOException {
		super(application);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Melting License...");
		htmlPane = new JEditorPane(new File("LICENSE.txt").toURI().toURL());
		htmlPane.setEditable(false);
		htmlPane.setFont(new Font("Monospaced",Font.PLAIN,12));
		setContentPane(new JScrollPane(htmlPane));
		setSize(560,500);
		setLocationRelativeTo(application);
		setVisible(true);
	}
}

