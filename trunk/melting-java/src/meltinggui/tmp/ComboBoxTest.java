package meltinggui.tmp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import meltinggui.dialogs.ComboBoxDialog;
import meltinggui.widgets.ComboBoxOption;

class ComboBoxTest
{
  private JFrame frame = new JFrame();

  private JPanel panel = new JPanel();

  private ComboBoxDialog comboBoxDialog;

  private JButton button;

  public ComboBoxTest()
  {
    comboBoxDialog =
          new ComboBoxDialog("Type of methylation: ",
                             new ComboBoxOption[] {
                               new ComboBoxOption("Unmethylated", "C"),
                               new ComboBoxOption("Methylated", "mC"),
                               new ComboBoxOption("Hydroxymethylated", "hmC")
                             },
                             "-M");
    
    button = new JButton("Click me for command line text.");
    button.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent event)
         {
           JOptionPane.showMessageDialog(frame, 
                                         comboBoxDialog.getCommandLineFlags());
         }
    });

    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

    panel.add(comboBoxDialog);
    panel.add(button);
    
    frame.add(panel);

    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    frame.pack();

    frame.setVisible(true);
  }

  public static void main(String[] args)
  {
    java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run()
                    {
                      new ComboBoxTest();
                    }
    });
  }
}

