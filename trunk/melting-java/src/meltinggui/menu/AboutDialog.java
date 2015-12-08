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

import javax.swing.*;

import meltinggui.MeltingGui;
import meltinggui.frames.OuterFrame;

import java.awt.*;
import java.awt.event.*;

/**
 * Shows the generic about dialog giving details of the current version
 * and copyright assignments.  This is just a thin shell around the 
 * MeltingTitlePanel which actually holds the relevant information and
 * which is also used on the welcome screen.
 * @author dallepep
 */
public class AboutDialog extends JDialog {
	private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new about dialog.
     * 
     * @param application The BamQC application.
     */
    public AboutDialog(OuterFrame application) {
    	super(application);
        setTitle("About Melting...");  
        Container cont = getContentPane();
        cont.setLayout(new BorderLayout());
        
        add(new MeltingTitlePanel(),BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        
        JButton closeButton = new JButton("Close");
        getRootPane().setDefaultButton(closeButton);
        closeButton.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent arg0) {
                setVisible(false);
                dispose();
            }
        });
        buttonPanel.add(closeButton);
        
        cont.add(buttonPanel,BorderLayout.SOUTH);
        
        setSize(650,230);
        setLocationRelativeTo(application);
        setResizable(false);
        setVisible(true);
    }
    
}
