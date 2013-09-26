package meltinggui.tmp;

import java.awt.FlowLayout;
import javax.swing.*;

public class LayoutTest
{
  private JFrame frame = new JFrame();

  private JPanel outerPanel = new JPanel();

  private JLabel nw = new JLabel("I am at the north west.");

  private JLabel ne = new JLabel("I am at the north east.");

  private JLabel sw = new JLabel("I am at the south west.");

  private JLabel se = new JLabel("I am at the south east.");

  public LayoutTest()
  {
    GroupLayout layout = new GroupLayout(outerPanel);
    layout.setHorizontalGroup(
      layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup()
          .addComponent(nw)
          .addComponent(sw))
        .addGroup(layout.createParallelGroup()
          .addComponent(ne)
          .addComponent(se))
    );
    layout.setVerticalGroup(
      layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup()
          .addComponent(nw)
          .addComponent(ne))
        .addGroup(layout.createParallelGroup()
          .addComponent(sw)
          .addComponent(se))
    );
    outerPanel.setLayout(layout);

    frame.add(outerPanel);
    frame.pack();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run()
                    {
                      new LayoutTest();
                    }
    });
  }
}

