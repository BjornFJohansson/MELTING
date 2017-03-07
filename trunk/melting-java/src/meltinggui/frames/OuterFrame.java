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

package meltinggui.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observer;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import meltinggui.dialogs.StatusPanel;
import meltinggui.menu.MeltingMenuBar;

/**
 * Main {@link JFrame} containing all of the Melting options component. 
 * 
 * @author John Gowers
 * @author rodrigue
 *
 */
public class OuterFrame
  extends JFrame
{
  /**
   * Desktop for putting the different frames on.
   */
  private JDesktopPane desktopPane;

  /**
   * Instance of the principle MELTING frame.
   */
  private MeltingFrame meltingFrame;

  /**
   * Error frame for displaying errors.
   */
  private ErrorFrame errorFrame;

  /**
   * File name for storing frame screen positions.
   */
  private static final String screenPositionsFileName =
                                                    "gui-screen-positions.ser";

  /**
   * Directory name to hold the file in.
   */
  private static final String meltingDir = ".melting";

  /**
   * The full path to the screen positions file.
   */
  private String screenPositionsPath;

  /** 
   * The MenuBar for Melting
   */
  private MeltingMenuBar menu = null;
  
  /** 
   * The last used directory for reading a configuration file.
   */
  private File lastUsedDir = null;
  
  
  /** This is the small strip at the bottom of the main display */
  private StatusPanel statusPanel;
  
  /**
   * The sequence passed as a file.
   */
  private File sequenceFile = null;

  
  /**
   * Sets up the GUI and displays the main MELTING frame.
   */
  public OuterFrame()
  {
    super("Melting v5.0");

    // Set up the path for storing screen positions.
    String userHome = System.getProperty("user.home");
    String slash = System.getProperty("file.separator");
    String screenPositionsDirectory = userHome + slash +
                                      meltingDir;
    new File(screenPositionsDirectory).mkdir();
    screenPositionsPath = screenPositionsDirectory + slash +
                          screenPositionsFileName;

    desktopPane = new JDesktopPane();
    
    // set the menu bar
    menu = new MeltingMenuBar(this);
	setJMenuBar(menu);
	
	desktopPane.setLayout(new BorderLayout());
	
	statusPanel = new StatusPanel();
	statusPanel.setVisible(true);
	desktopPane.add(statusPanel, BorderLayout.SOUTH);
	

    meltingFrame = new MeltingFrame(this);
    meltingFrame.setVisible(true);
    //desktopPane.add(meltingFrame, BorderLayout.NORTH);
    desktopPane.add(meltingFrame);
    
    try {
      meltingFrame.setSelected(true);
    }
    catch (java.beans.PropertyVetoException exception) {}

    // Not sure this is loaded
    errorFrame = new ErrorFrame();
    desktopPane.add(errorFrame);

    restoreScreenPositions();

    setContentPane(desktopPane);
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    
    
    addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent windowEvent)
                    {
                      storeScreenPositions();
                      System.exit(0);
                    }
    });

    setVisible(true);
  }

  /**
   * 
   * @return ??
   */
  public boolean openConfigFile() {
	return false;
  }
  
  /**
   * Opens a {@link JFileChooser} so that the user can select a sequence file and
   * sets the file name into the {@code meltingFrame}.
   * 
   * @return true
   */
  public boolean openFile() {
    JFileChooser chooser;

    if (lastUsedDir == null) {
      chooser = new JFileChooser();
    }
    else {
      chooser = new JFileChooser(lastUsedDir);
    }
    chooser.setMultiSelectionEnabled(false);
    //		chooser.addChoosableFileFilter(bff);
    //		chooser.setFileFilter(bff);
    int result = chooser.showOpenDialog(this);
    if (result == JFileChooser.CANCEL_OPTION) return false;

    File filename = chooser.getSelectedFile();
    meltingFrame.setSequence(filename);
    return true;
  }
  
  
  /**
   * 
   * @return true
   */
  public boolean closeFile() {
	  meltingFrame.cleanSequence();
	  return true;
  }
  
  /**
   * Sets the status panel text
   * 
   * @param str the text to set
   */
  public void setStatusPanelText(String str) {
	  statusPanel.setText(str);
  }
  
  
  /**
   * Adds an observer to the melting frame.
   * @param observer The observer.
   */
  public void addObserver(Observer observer)
  {
    meltingFrame.addObserver(observer);
  }

  /**
   * Displays MELTING results on the MELTING frame.
   * @param results The results from MELTING.
   */
  public void displayMeltingResults(melting.ThermoResult results)
  {
    meltingFrame.displayMeltingResults(results);
  }

  /**
   * Logs an exception on to the error frame.  
   * @param exception The exception.
   */
  public void logException(Exception exception)
  {
    errorFrame.setVisible(true);
    errorFrame.logException(exception);
  }

  /**
   * Returns the sequence file.
   * 
   * @return the sequence file.
   */
  public File getSequenceFile() {
	  return sequenceFile;
  }
  
  /**
   * Set the sequence file.
   * 
   * @param file the {@link File} that contain the sequence(s)
   */
  public void setSequenceFile(File file) {
	  sequenceFile = file;
  }
  
  
  /**
   * Stores the screen positions of the frames to a file.
   */
  private void storeScreenPositions()
  {
    GuiScreenPositions screenPositions = new GuiScreenPositions(this,
                                                                meltingFrame,
                                                                errorFrame);
    try {
      FileOutputStream fileOutputStream =
                                 new FileOutputStream(screenPositionsPath);
      ObjectOutputStream objectOutputStream =
                                 new ObjectOutputStream(fileOutputStream);
      objectOutputStream.writeObject(screenPositions);
      objectOutputStream.close();
      fileOutputStream.close();
    }
    catch(IOException exception)
    {
      exception.printStackTrace();
    }
  }

  /**
   * Sets up the screen positions of the frames.  The screen positions are read
   * from a file, if available, and otherwise set from a default method.
   */
  public void restoreScreenPositions()
  {
    GuiScreenPositions screenPositions;
    try {
      FileInputStream fileInputStream =
                                  new FileInputStream(screenPositionsPath);
      ObjectInputStream objectInputStream =
                                  new ObjectInputStream(fileInputStream);
      screenPositions = (GuiScreenPositions) objectInputStream.readObject();
      objectInputStream.close();
      fileInputStream.close();
    }
    catch (ClassNotFoundException exception) {
      return;
    }
    catch (FileNotFoundException exception) {
      setDefaultScreenPositions();
      return;
    }
    catch (IOException exception) {
      return;
    }

    screenPositions.setScreenPositions(this, meltingFrame, errorFrame);
  }

  /**
   * Sets up the default screen positions if none were found in a file.
   */
  private void setDefaultScreenPositions()
  {
    // Set up the frame so it is indented 100 pixels from each edge of the 
    // screen.
    int inset = 100;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(inset,
              inset,
              screenSize.width - (inset * 2),
              screenSize.height - (inset * 2));
  }

  /**
   * 
   */
  public void clearErrors() {
    errorFrame.clearErrors();
  }

}

