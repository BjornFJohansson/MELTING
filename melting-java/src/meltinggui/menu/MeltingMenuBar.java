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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;



import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import meltinggui.frames.OuterFrame;
import meltinggui.menu.AboutDialog;
import meltinggui.menu.LicenseDialog;

/**
 * @author dallepep
 */
public class MeltingMenuBar extends JMenuBar implements ActionListener {

	private static final long serialVersionUID = -1301056504996459340L;
	/** The main application */
	private OuterFrame application;
	

	private JMenu fileMenu;
	private JMenuItem fileConfigOpen;
	private JMenuItem fileOpen;
	private JMenuItem fileClose;
	private JMenuItem fileExit;
	
	private JMenu helpMenu;
	private JMenuItem helpContents;
	private JMenuItem helpLicense;
	private JMenuItem helpAbout;
	
	
	
	
	public MeltingMenuBar (OuterFrame outerFrame) {
		this.application = outerFrame;
		
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
				
		fileConfigOpen = new JMenuItem("Open Configuration File...");
		fileConfigOpen.setMnemonic(KeyEvent.VK_O);
		fileConfigOpen.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileConfigOpen.setActionCommand("open_config");
		fileConfigOpen.addActionListener(this);
		//TODO Currently disabled
		//fileMenu.add(fileConfigOpen);
			
		fileOpen = new JMenuItem("Open Sequence File...");
		fileOpen.setMnemonic(KeyEvent.VK_S);
		fileOpen.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileOpen.setActionCommand("open");
		fileOpen.addActionListener(this);
		fileMenu.add(fileOpen);
		
		fileMenu.addSeparator();
		
		fileClose = new JMenuItem("Close");
		fileClose.setMnemonic(KeyEvent.VK_C);
		fileClose.setAccelerator(KeyStroke.getKeyStroke('C', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileClose.setActionCommand("close");
		fileClose.addActionListener(this);
		fileClose.setEnabled(false);
		fileMenu.add(fileClose);
		
		fileMenu.addSeparator();
		
		fileExit = new JMenuItem("Exit");
		fileExit.setMnemonic(KeyEvent.VK_Q);
		fileExit.setAccelerator(KeyStroke.getKeyStroke('Q', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileExit.setActionCommand("exit");
		fileExit.addActionListener(this);
		fileMenu.add(fileExit);
		
		add(fileMenu);
			
		
		helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		
//		helpContents = new JMenuItem("Contents...");
//		helpContents.setMnemonic(KeyEvent.VK_T);
//		helpContents.setAccelerator(KeyStroke.getKeyStroke('T', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
//		helpContents.setActionCommand("help_contents");
//		helpContents.addActionListener(this);
//		helpMenu.add(helpContents);
		
		helpLicense = new JMenuItem("License");
		helpLicense.setActionCommand("help_license");
		helpLicense.setMnemonic(KeyEvent.VK_L);
		helpLicense.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		helpLicense.addActionListener(this);
		helpMenu.add(helpLicense);
		
		helpAbout = new JMenuItem("About Melting");
		helpAbout.setMnemonic(KeyEvent.VK_A);
		helpAbout.setAccelerator(KeyStroke.getKeyStroke('A', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		helpAbout.setActionCommand("about");
		helpAbout.addActionListener(this);
		helpMenu.add(helpAbout);
		
		add(helpMenu);
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		String action = ae.getActionCommand();
		
		if (action.equals("exit")) {
			application.dispose();
		}
		else if (action.equals("open_config")) {
			if(application.openConfigFile()) {
//				fileSave.setEnabled(true);
			}
		}
		else if (action.equals("open")) {
			if(application.openFile()) {
				fileClose.setEnabled(true);
//				fileSave.setEnabled(true);
			}
		}
//		else if (action.equals("save")) {
//			application.saveReport();
//		}
		else if (action.equals("close")) {
			if(application.closeFile()) {
//				fileSave.setEnabled(false);
				fileClose.setEnabled(false);		
			}
		}
//		else if (action.equals("help_contents")) {
//			try {
//				new HelpDialog(application,new File(URLDecoder.decode(ClassLoader.getSystemResource("Help").getFile(),"UTF-8")));
//			} 
//			catch (UnsupportedEncodingException e1) {
//				e1.printStackTrace();
//			}
//		}
		else if (action.equals("help_license")) {
			try {
				new LicenseDialog(application);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (action.equals("about")) {
			new AboutDialog(application);
		}
		else {
			JOptionPane.showMessageDialog(application, "Unknown menu command "+action, "Unknown command", JOptionPane.ERROR_MESSAGE);
		}
	}
	
}
