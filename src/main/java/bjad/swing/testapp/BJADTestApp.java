package bjad.swing.testapp;

import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import bjad.swing.DateTimeTextField;
import bjad.swing.NumericTextField;

/**
 * Quick Test UI Application for the
 * BJAD Library used for manual testing
 * of the fields on the screen.
 *
 * @author 
 *   Ben Dougall
 */
public class BJADTestApp extends JFrame
{
   private static final long serialVersionUID = -5205067524615288143L;

   public BJADTestApp()
   {
      super("BJAD UI Test App");
      setSize(800, 315);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setContentPane(createContentPane());
   }
   
   private JPanel createContentPane()
   {
      JPanel pane = new JPanel(null, true);
      JLabel lbl = new JLabel("Integer Field:");
      lbl.setName("IntFieldLabel");
      lbl.setBounds(10, 10, 200, 25);
      pane.add(lbl);
      
      NumericTextField numField = NumericTextField.newIntegerFieldNoLimits();
      numField.setName("IntField");
      numField.setBounds(220, 10, 400, 25);
      pane.add(numField);
      
      lbl = new JLabel("Decimal Field:");
      lbl.setName("DecFieldLabel");
      lbl.setBounds(10, 40, 200, 25);
      pane.add(lbl);
      
      numField = NumericTextField.newDecimalFieldNoLimits();
      numField.setName("DecField");
      numField.setBounds(220, 40, 400, 25);
      pane.add(numField);
      
      lbl = new JLabel("Money Field:");
      lbl.setName("MoneyFieldLabel");
      lbl.setBounds(10, 70, 200, 25);
      pane.add(lbl);
      
      numField = NumericTextField.newMoneyField();
      numField.setName("MoneyField");
      numField.setBounds(220, 70, 400, 25);
      pane.add(numField);
      
      lbl = new JLabel("Date Field:");
      lbl.setName("DateFieldLabel");
      lbl.setBounds(10, 100, 200, 25);
      pane.add(lbl);
      
      DateTimeTextField dtField = new DateTimeTextField(new Date());
      dtField.setName("dtField");
      dtField.setBounds(220, 100, 400, 25);
      pane.add(dtField);
      
      JPanel innerPane = new JPanel(null, true);
      innerPane.setName("InnerPane");
      innerPane.setBounds(10, 140, 760, 120);
      innerPane.setBorder(new TitledBorder("Inner Panel"));
      pane.add(innerPane);
      
      lbl = new JLabel("No Name Label");
      lbl.setBounds(10, 20, 200, 25);
      innerPane.add(lbl);
      
      JTextField txtField = new JTextField("No Name TextField");
      txtField.setBounds(220, 20, 400, 25);
      innerPane.add(txtField);
      
      lbl = new JLabel("Disabled Field Label");
      lbl.setBounds(10, 50, 200, 25);
      innerPane.add(lbl);
      
      txtField = new JTextField("Disabled TextField");
      txtField.setName("DisabledField");
      txtField.setEnabled(false);
      txtField.setBounds(220, 50, 400, 25);
      innerPane.add(txtField);
      
      lbl = new JLabel("Non-editable Field Label");
      lbl.setBounds(10, 80, 200, 25);
      innerPane.add(lbl);
      
      txtField = new JTextField("NonEditabled TextField");
      txtField.setName("NonEditableField");
      txtField.setEditable(false);
      txtField.setBounds(220, 80, 400, 25);
      innerPane.add(txtField);
      
      return pane;
   }
   
   public static void main(String[] args)
   {
      SwingUtilities.invokeLater(new Runnable()
      {         
         @Override
         public void run()
         {
            BJADTestApp app = new BJADTestApp();
            app.setVisible(true);
         }
      });
   }
}
