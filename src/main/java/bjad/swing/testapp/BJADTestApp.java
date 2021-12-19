package bjad.swing.testapp;

import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import bjad.swing.BJADFieldPositionHelper;
import bjad.swing.DateTimeTextField;
import bjad.swing.NumericTextField;
import bjad.swing.TextField;

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

   /**
    * Constructor, creating the window and the controls
    * and wiring the frame to close when the close button
    * is pressed. 
    */
   public BJADTestApp()
   {
      super("BJAD UI Test App");
      setSize(700, 410);
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
      numField.setBounds(220, 10, 450, 26);
      numField.setPlaceholderFont(numField.getPlaceholderFont().deriveFont(14.0f));
      numField.setPlaceholderText("Arrow keys to position, CTRL+Arrow keys to size.");
      pane.add(numField);
      
      // Wire the int field show the user can use the arrow keys to position 
      // the label and the field on the screen, and use CTRL+Arrow Keys to 
      // resize the label and text field. 
      new BJADFieldPositionHelper(numField, lbl, numField);
      
      lbl = new JLabel("Decimal Field:");
      lbl.setName("DecFieldLabel");
      lbl.setBounds(10, 40, 200, 25);
      pane.add(lbl);
      
      numField = NumericTextField.newDecimalFieldNoLimits();
      numField.setName("DecField");
      numField.setBounds(220, 40, 450, 25);
      pane.add(numField);
      
      lbl = new JLabel("Money Field:");
      lbl.setName("MoneyFieldLabel");
      lbl.setBounds(10, 70, 200, 25);
      pane.add(lbl);
      
      numField = NumericTextField.newMoneyField();
      numField.setName("MoneyField");
      numField.setPlaceholderText("Decimal Field but with 2 decimal places maximum.");
      numField.setBounds(220, 70, 450, 25);
      pane.add(numField);
      
      lbl = new JLabel("Date Field:");
      lbl.setName("DateFieldLabel");
      lbl.setBounds(10, 100, 200, 25);
      pane.add(lbl);
      
      DateTimeTextField dtField = new DateTimeTextField(new Date());
      dtField.setName("dtField");
      dtField.setBounds(220, 100, 450, 25);
      pane.add(dtField);
      
      lbl = new JLabel("'A-E' Field:");
      lbl.setName("AbcdeFieldLabel");
      lbl.setBounds(10, 130, 200, 25);
      pane.add(lbl);
      
      TextField abcdeField = new TextField();
      abcdeField.setName("abcdeField");
      abcdeField.addAllowableCharacter('a');
      abcdeField.addAllowableCharactersFromString("bcdeABCDE");
      abcdeField.setBounds(220, 130, 450, 25);
      abcdeField.setPlaceholderText("Only characters AAbBcCdDeE are allowed");
      pane.add(abcdeField);
      
      JPanel innerPane = new JPanel(null, true);
      innerPane.setName("InnerPane");
      innerPane.setBounds(10, 170, 660, 120);
      innerPane.setBorder(new TitledBorder("Inner Panel"));
      pane.add(innerPane);
      
      lbl = new JLabel("No Name Label");
      lbl.setBounds(10, 20, 200, 25);
      innerPane.add(lbl);
      
      TextField txtField = new TextField("No Name TextField");
      txtField.setBounds(220, 20, 425, 25);
      innerPane.add(txtField);
      
      lbl = new JLabel("Disabled Field Label");
      lbl.setBounds(10, 50, 200, 25);
      innerPane.add(lbl);
      
      txtField = new TextField("Disabled TextField");
      txtField.setName("DisabledField");
      txtField.setEnabled(false);
      txtField.setBounds(220, 50, 425, 25);
      innerPane.add(txtField);
      
      lbl = new JLabel("Non-editable Field Label");
      lbl.setBounds(10, 80, 200, 25);
      innerPane.add(lbl);
      
      txtField = new TextField("NonEditable TextField");
      txtField.setName("NonEditableField");
      txtField.setEditable(false);
      txtField.setBounds(220, 80, 425, 25);
      innerPane.add(txtField);
      
      JPanel instructions = new JPanel(null, true);
      instructions.setBounds(10, 300, 660, 50);
      instructions.setBorder(new TitledBorder("Instructions"));
      pane.add(instructions);
      
      lbl = new JLabel("Use the arrow keys while focused in the int field to move its label and the field itself.");
      lbl.setBounds(10, 15, 640, 30);
      instructions.add(lbl);
      
      return pane;
   }
   
   /**
    * Launching point of the application, showing the 
    * frame to the user. 
    * 
    * @param args
    *    Command line arguments are not used for this 
    *    application. 
    */
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
