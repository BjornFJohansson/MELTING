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

package meltinggui.frames;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observer;
import java.util.Properties;
import javax.swing.*;

import meltinggui.MeltingObservable;

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
   * Sets up the GUI and displays the main MELTING frame.
   */
  public OuterFrame()
  {
    super("Melting v5.0");

    desktopPane = new JDesktopPane();

    meltingFrame = new MeltingFrame();
    meltingFrame.setVisible(true);
    desktopPane.add(meltingFrame);
    
    try {
      meltingFrame.setSelected(true);
    }
    catch (java.beans.PropertyVetoException exception) {}

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
   * Stores the screen positions of the frames to a file.
   */
  private void storeScreenPositions()
  {
    GuiScreenPositions screenPositions = new GuiScreenPositions(this,
                                                                meltingFrame,
                                                                errorFrame);
    try {
      FileOutputStream fileOutputStream =
                                 new FileOutputStream(screenPositionsFileName);
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
    GuiScreenPositions screenPositions = null;
    try {
      FileInputStream fileInputStream =
                                  new FileInputStream(screenPositionsFileName);
      ObjectInputStream objectInputStream =
                                  new ObjectInputStream(fileInputStream);
      screenPositions = (GuiScreenPositions) objectInputStream.readObject();
      objectInputStream.close();
      fileInputStream.close();
    }
    catch (ClassNotFoundException exception) {
      exception.printStackTrace();
      return;
    }
    catch (FileNotFoundException exception) {
      setDefaultScreenPositions();
      return;
    }
    catch (IOException exception) {
      exception.printStackTrace();
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
}

