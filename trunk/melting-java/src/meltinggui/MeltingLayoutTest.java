package meltinggui;

import java.awt.*;
import javax.swing.*;

public class MeltingLayoutTest extends JFrame
{
  private JPanel mainPanel = new JPanel();
  private JPanel sequencePanel = new JPanel();
  private JPanel ocPanel = new JPanel();
  
  public MeltingLayoutTest()
  {
    mainPanel.setLayout(new GridLayout(2, 1));
    sequencePanel.setLayout(new MeltingLayout(sequencePanel, 2, 2));

    sequencePanel.add(new JLabel("Sequence (-S):"), MeltingLayout.LABEL_GROUP);
    sequencePanel.add(new JTextField(15), MeltingLayout.INPUT_GROUP);
    sequencePanel.add(new JLabel("Complementary sequence (-C):"),
                      MeltingLayout.LABEL_GROUP);
    sequencePanel.add(new JTextField(15), MeltingLayout.INPUT_GROUP);
    sequencePanel.add(new JLabel("Complementary sequence (-C):"),
                      MeltingLayout.LABEL_GROUP);
    sequencePanel.add(new JTextField(15), MeltingLayout.INPUT_GROUP);

    sequencePanel.setBackground(Color.RED);


    ocPanel.setLayout(new MeltingLayout(ocPanel, 2, 2));

    ocPanel.add(new JLabel("Oligomer concentration (-P):"), 
                MeltingLayout.LABEL_GROUP);
    ocPanel.add(new JTextField(20), MeltingLayout.INPUT_GROUP);
    ocPanel.add(new JLabel("mol/L"), MeltingLayout.INPUT_GROUP);

    mainPanel.add(sequencePanel);
    mainPanel.add(ocPanel);
    
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(Box.createRigidArea(new Dimension(532, 12)),
                    BorderLayout.NORTH);
    contentPane.add(Box.createRigidArea(new Dimension(0, 12)),
                    BorderLayout.SOUTH);
    contentPane.add(Box.createRigidArea(new Dimension(12, 0)),
                    BorderLayout.WEST);
    contentPane.add(Box.createRigidArea(new Dimension(12, 0)),
                    BorderLayout.EAST);
    contentPane.add(mainPanel, BorderLayout.CENTER);

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    pack();
  }

  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run()
                    {
                      new MeltingLayoutTest().setVisible(true);
                    }
    });
  }
}
