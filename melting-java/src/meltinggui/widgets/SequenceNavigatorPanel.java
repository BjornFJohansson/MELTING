package meltinggui.widgets;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import meltinggui.frames.ResultFrame;

/**
 * 
 * 
 * @author rodrigue
 *
 */
public class SequenceNavigatorPanel extends JPanel {

  /**
   * Indicates the index of the selected sequence.
   */
  private int currentIndex = 0;
  /**
   * Total number of sequences.
   */
  private int nbSequence = 100;
  
  /**
   * 
   */
  private JButton firstButton = new JButton("<<");
  /**
   * 
   */
  private JButton prevButton = new JButton("<");
  /**
   * 
   */
  private JButton nextButton = new JButton(">");
  /**
   * 
   */
  private JButton lastButton = new JButton(">>");
  /**
   * 
   */
  private JButton randomButton = new JButton("Random");
  /**
   * 
   */
  private JLabel sequenceIndex = new JLabel("1 / " + nbSequence);
  
  /**
   * 
   */
  private ResultFrame resultFrame;
  
  /**
   * Tests the SequenceNavigatorPanel layout.
   * 
   * @param args program arguments. None expected
   */
  public static void main(String[] args) {

  //Create and set up the window.
    JFrame frame = new JFrame("TestFrame");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setMinimumSize(new Dimension(600, 100));

    //Create and set up the content pane.
    JComponent newContentPane = new SequenceNavigatorPanel(new ResultFrame(null));
    newContentPane.setOpaque(true); //content panes must be opaque
    frame.setContentPane(newContentPane);

    //Display the window.
    frame.pack();
    frame.setVisible(true);

  }
  
  /**
   * Creates a new {@link SequenceNavigatorPanel} instance.
   * 
   */
  public SequenceNavigatorPanel(ResultFrame resultFrame) {
   
    this.resultFrame = resultFrame;
    
    add(firstButton);
    
    add(prevButton);

    add(nextButton);

    add(lastButton);

    // TODO - insert empty space?
    
    add(randomButton);
    
    setNumberOfSequences(resultFrame.getNbSequences());
    
    // left label
    add(sequenceIndex);
    
    initButtonAction();
  }

  
  /**
   * Sets the number of sequences
   * 
   * @param n the number of sequences
   */
  public void setNumberOfSequences(int n) {
    nbSequence = n;
    updateSequenceIndexLabel();
  }
  
  /**
   * Updates the sequence index label.
   */
  public void updateSequenceIndexLabel() {
    sequenceIndex.setText((currentIndex + 1) + " / " + nbSequence);
  }
  
  /**
   * Updates the sequence index label and the result frame.
   */
  public void updateSequenceIndex() {
    updateSequenceIndexLabel();
    
    // telling the ResultFrame to update
    resultFrame.updateSequence(currentIndex);
  }
  
  /**
   * 
   */
  private void initButtonAction() {

    firstButton.addActionListener(new AbstractAction() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        currentIndex = 0;
        updateSequenceIndex();
      }
    });
   
    prevButton.addActionListener(new AbstractAction() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        if (currentIndex != 0) {
          currentIndex--;
          updateSequenceIndex();          
        }
      }
    });

    nextButton.addActionListener(new AbstractAction() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        if (currentIndex < nbSequence -1) {
          currentIndex++;
          updateSequenceIndex();          
        }
      }
    });

    lastButton.addActionListener(new AbstractAction() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        currentIndex = nbSequence - 1;
        updateSequenceIndex();          
      }
    });
    
    randomButton.addActionListener(new AbstractAction() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        Random rand = new Random();

        int oldIndex = currentIndex;
        currentIndex = rand.nextInt(nbSequence);
        
        if (oldIndex != currentIndex) {
          updateSequenceIndex();
        }
      }
    });

  }


}
