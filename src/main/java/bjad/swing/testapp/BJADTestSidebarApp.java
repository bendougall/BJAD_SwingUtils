package bjad.swing.testapp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import bjad.swing.DateEntryField;
import bjad.swing.DateTimeTextField;
import bjad.swing.NumericTextField;
import bjad.swing.TextField;
import bjad.swing.WrappedLabel;
import bjad.swing.nav.AbstractBJADNavPanel;
import bjad.swing.nav.BJADModuleEntry;
import bjad.swing.nav.BJADNavModule;
import bjad.swing.nav.BJADSidebarNavContentPane;
import bjad.swing.nav.SidebarSectionBehaviour;
import bjad.swing.positioner.BJADConsolePositionHelper;
import bjad.swing.positioner.BJADUIHorizontalPositionerHelper;
import bjad.swing.positioner.BJADUIVerticalPositionerHelper;

/**
 * (Description)
 *
 *
 * @author 
 *   bendo
 */
public class BJADTestSidebarApp extends JFrame
{
   private static final long serialVersionUID = -6784328019471275079L;

   /**
    * Constructor, creating the window and the controls
    * and wiring the frame to close when the close button
    * is pressed. 
    */
   public BJADTestSidebarApp()
   {
      super("BJAD Sidebar Nav Test App");
      setSize(800, 440);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      
      List<BJADNavModule> modules = new ArrayList<BJADNavModule>();
      
      BJADNavModule module = new BJADNavModule();
      module.setDisplayName("Field Positioner");
      module.setOrdinial(1);
      
      BJADModuleEntry entry = new BJADModuleEntry();
      entry.setDisplayName("Demo");
      entry.setOrdinial(1);
      entry.setNavPanel(new PositionerDemoPanel());
      module.getEntries().add(entry);
      modules.add(module);
      
      module = new BJADNavModule();
      module.setDisplayName("Entry Field Demos");
      module.setOrdinial(0);
      
      entry = new BJADModuleEntry();
      entry.setDisplayName("TextField Demo");
      entry.setOrdinial(1);
      entry.setNavPanel(new TextFieldEntryPanel());
      module.getEntries().add(entry);
      
      entry = new BJADModuleEntry();
      entry.setDisplayName("NumField Demo");
      entry.setOrdinial(0);
      module.getEntries().add(entry);
      entry.setNavPanel(new NumericEntryPanel());
      
      entry = new BJADModuleEntry();
      entry.setDisplayName("DateField Demo");
      entry.setOrdinial(0);
      module.getEntries().add(entry);
      entry.setNavPanel(new DateEntryPanel());
      modules.add(module);
      
      setContentPane(new BJADSidebarNavContentPane(modules, SidebarSectionBehaviour.ALL_SECTIONS_ALWAYS_EXPANDED));
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
            BJADTestSidebarApp app = new BJADTestSidebarApp();
            app.setVisible(true);
         }
      });
   }
}

class TextFieldEntryPanel extends AbstractBJADNavPanel
{
   private static final long serialVersionUID = -5886406219517070874L;
   
   private TextField noRestrictionsField = new TextField();
   private TextField abcdeField = new TextField();
   private TextField maxLengthField = new TextField();
   
   public TextFieldEntryPanel()
   {
      super();
      setLayout(new BorderLayout());
      
      JPanel content = new JPanel(true);
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      JPanel pane = new JPanel(new BorderLayout(5,5));
      JLabel lbl = new JLabel("No Restrictions Field:");
      lbl.setPreferredSize(new Dimension(150, 35));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(noRestrictionsField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("'ABCDE' Only Field:");
      lbl.setPreferredSize(new Dimension(150, 35));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(abcdeField, BorderLayout.CENTER);
      abcdeField.setName("abcdeField");
      abcdeField.addAllowableCharacter('a');
      abcdeField.addAllowableCharactersFromString("bcdeABCDE");
      abcdeField.setPlaceholderText("Can only enter the following characters in this field: AaBbCcDdEe");
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("3 Characters only Field");
      lbl.setPreferredSize(new Dimension(150, 35));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(maxLengthField, BorderLayout.CENTER);
      maxLengthField.setName("maxLengthField");
      maxLengthField.setMaxLength(3);
      content.add(pane);
      
      this.add(content, BorderLayout.NORTH);
      this.add(new JLabel(""), BorderLayout.CENTER);
   }
   @Override
   public String getPanelTitle()
   {
      return "Text Field entry options";
   }

   @Override
   public JComponent getComponentForDefaultFocus()
   {      
      return noRestrictionsField;
   }

   @Override
   public void onPanelDisplay()
   {
   }

   @Override
   public boolean canPanelClose()
   {
      return true;
   }

   @Override
   public void onPanelClosed()
   {
   }  
}

class NumericEntryPanel extends AbstractBJADNavPanel
{
   private static final long serialVersionUID = -5886406219517070874L;
   
   private NumericTextField intField = NumericTextField.newIntegerFieldNoLimits();
   private NumericTextField decimalField = NumericTextField.newDecimalFieldNoLimits();
   private NumericTextField moneyField = NumericTextField.newMoneyField();
   
   public NumericEntryPanel()
   {
      super();
      setLayout(new BorderLayout());
      
      JPanel content = new JPanel(true);
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      JPanel pane = new JPanel(new BorderLayout(5,5));
      JLabel lbl = new JLabel("Integer Only Field:");
      lbl.setPreferredSize(new Dimension(150, 39));
      intField.setPlaceholderText("Integers only in this field.");
      pane.add(lbl, BorderLayout.WEST);
      pane.add(intField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Decimal Field:");
      lbl.setPreferredSize(new Dimension(150, 30));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(decimalField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Money Field");
      lbl.setPreferredSize(new Dimension(150, 30));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(moneyField, BorderLayout.CENTER);
      moneyField.setPlaceholderText("Decimal Field but with 2 decimal places maximum.");
      content.add(pane);
      
      this.add(content, BorderLayout.NORTH);
      this.add(new JLabel(""), BorderLayout.CENTER);
   }
   @Override
   public String getPanelTitle()
   {
      return "Numeric Entry Field Demo";
   }

   @Override
   public JComponent getComponentForDefaultFocus()
   {      
      return moneyField;
   }

   @Override
   public void onPanelDisplay()
   {
   }

   @Override
   public boolean canPanelClose()
   {
      boolean ret = !moneyField.isFieldEmpty();
      if (!ret)
      {
         JOptionPane.showMessageDialog(this, "Money field must cannot be empty prior to navigating to next screen.", "Validation", JOptionPane.WARNING_MESSAGE);
         moneyField.requestFocusInWindow();
      }
      return ret;
   }

   @Override
   public void onPanelClosed()
   {
   }   
}

class DateEntryPanel extends AbstractBJADNavPanel implements ActionListener
{
   private static final long serialVersionUID = -5886406219517070874L;
   private static final String[] FORMATS = new String[] 
         {
               "MMM d, yyyy",
               "yyyy-MM-dd",
               "yyyy/MM/dd",
               "EEE, MMM d, ''yy" 
         };
   private DateEntryField dateWithDefaultField = new DateEntryField(new Date(1641060941000L));
   private DateEntryField dateField = new DateEntryField();
   private JComboBox<String> firstFormatDropdown = new JComboBox<>(FORMATS);
   private JComboBox<String> secondFormatDropdown = new JComboBox<>(FORMATS);
   private DateTimeTextField dateTimeTextField = new DateTimeTextField(new Date());
   
   public DateEntryPanel()
   {
      super();
      setLayout(new BorderLayout());
      
      JPanel content = new JPanel(true);
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      
      JPanel pane = new JPanel(new BorderLayout(5,5));
      JLabel lbl = new JLabel("Date Field defaulting to 2022/01/01:");
      lbl.setPreferredSize(new Dimension(300, 30));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(dateWithDefaultField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Date format for the above field:");
      lbl.setPreferredSize(new Dimension(300, 20));
      lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN | Font.ITALIC));
      pane.add(lbl, BorderLayout.WEST);      
      firstFormatDropdown.addActionListener(this);
      firstFormatDropdown.setSelectedIndex(firstFormatDropdown.getItemCount()-1);
      pane.add(firstFormatDropdown, BorderLayout.CENTER);
      content.add(pane);  
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Date Field (no default):");
      lbl.setPreferredSize(new Dimension(300, 30));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(dateField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Date format for the above field:");
      lbl.setPreferredSize(new Dimension(300, 20));
      lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN | Font.ITALIC));
      pane.add(lbl, BorderLayout.WEST);      
      secondFormatDropdown.addActionListener(this);
      pane.add(secondFormatDropdown, BorderLayout.CENTER);
      content.add(pane);  
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("");
      lbl.setPreferredSize(new Dimension(300, 30));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(new JLabel(), BorderLayout.CENTER);
      content.add(pane);     
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Date Time Entry Field (use arrow keys to set):");
      lbl.setPreferredSize(new Dimension(300, 30));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(dateTimeTextField, BorderLayout.CENTER);
      content.add(pane);      
      
      this.add(content, BorderLayout.NORTH);
      this.add(new JLabel(""), BorderLayout.CENTER);
   }
   @Override
   public String getPanelTitle()
   {
      return "Date and Time Entry Field Demo";
   }

   @Override
   public JComponent getComponentForDefaultFocus()
   {      
      return dateTimeTextField;
   }

   @Override
   public void onPanelDisplay()
   {
   }

   @Override
   public boolean canPanelClose()
   {
      return true;
   }

   @Override
   public void onPanelClosed()
   {
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
      if (e.getSource().equals(firstFormatDropdown))
      {
         if (firstFormatDropdown.getSelectedIndex() > -1)
         {
            dateWithDefaultField.setDisplayFormat(new SimpleDateFormat(firstFormatDropdown.getSelectedItem().toString()));
         }
         else
         {
            firstFormatDropdown.setSelectedIndex(0);
         }
      }
      if (e.getSource().equals(secondFormatDropdown))
      {
         if (secondFormatDropdown.getSelectedIndex() > -1)
         {
            dateField.setDisplayFormat(new SimpleDateFormat(secondFormatDropdown.getSelectedItem().toString()));
         }
         else
         {
            secondFormatDropdown.setSelectedIndex(0);
         }
      }
   }  
}

class PositionerDemoPanel extends AbstractBJADNavPanel implements ComponentListener
{
   private static final long serialVersionUID = -4087361006062121042L;
   
   private WrappedLabel firstLabel;
   private WrappedLabel instructionLabel;   
   private TextField textfield = new TextField();
   private boolean firstPositioning = true;
   
   public PositionerDemoPanel()
   {
      super();
      setLayout(null);
      
      firstLabel = new WrappedLabel("This display demos the field positioner helper.");
      firstLabel.setBounds(10, 10, 400, 25);
      firstLabel.setName("TitleLabel");
      this.add(firstLabel);
      
      StringBuilder instructionsSb = new StringBuilder("When focus is on the text field, you can do the ");
      instructionsSb.append("activities below and the console output will show the control's position ");
      instructionsSb.append("size each time an arrow key event occurs.");
      instructionsSb.append(System.lineSeparator());
      instructionsSb.append("  - Arrow Keys to position the field left/right or up/down.");      
      instructionsSb.append(System.lineSeparator());
      instructionsSb.append("  - CTRL + Arrow Keys to resize the field wider/thinner or taller/shorter");
      instructionsSb.append(System.lineSeparator());
      instructionsSb.append(System.lineSeparator());
      instructionsSb.append("");
      instructionLabel = new WrappedLabel(instructionsSb.toString());
      instructionLabel.setBounds(10, 40, 400, 200);
      instructionLabel.setName("InstructionLabel");
      this.add(instructionLabel);
      
      JPanel innerPane = new JPanel(null, true);
      innerPane.setName("InnerPane");
      innerPane.setBounds(10, 130, 612, 60);
      innerPane.setBorder(new TitledBorder("Inner Panel"));
      this.add(innerPane);
      
      JLabel innerLabel = new JLabel("Label");
      innerLabel.setName("InnerLabel");
      innerLabel.setBounds(10, 23, 50, 25);
      innerPane.add(innerLabel);
      
      TextField innerTextField = new TextField();
      innerTextField.setPlaceholderText("Text Field");
      innerTextField.setName("InnerTextField");
      innerTextField.setBounds(70, 23, 150, 25);
      innerPane.add(innerTextField);
      
      JCheckBox innerCheckbox = new JCheckBox("Checkbox");
      innerCheckbox.setName("InnerCheckbox");
      innerCheckbox.setBounds(230, 23, 100, 25);
      innerPane.add(innerCheckbox);
      
      JButton innerButton = new JButton("Button");
      innerButton.setName("InnerButton");
      innerButton.setBounds(340, 23, 100, 25);
      innerPane.add(innerButton);
      
      textfield.setBounds(10, 260, 400, 25);
      textfield.setName("TextField");
      textfield.addAllowableCharacter('¿');
      this.add(textfield);
      
      new BJADConsolePositionHelper(textfield, textfield);
      this.addComponentListener(this);
      
      new BJADUIHorizontalPositionerHelper(textfield, firstLabel, instructionLabel, innerPane, textfield);
      
      new BJADUIVerticalPositionerHelper(textfield, firstLabel, instructionLabel, innerPane, textfield);
   }
   
   @Override
   public String getPanelTitle()
   {
      return "BJAD Field Positioning Helper Demo";
   }

   @Override
   public JComponent getComponentForDefaultFocus()
   {
      // TODO Auto-generated method stub
      return textfield;
   }

   @Override
   public void onPanelDisplay()
   {
   }

   @Override
   public boolean canPanelClose()
   {
      return true;
   }

   @Override
   public void onPanelClosed()
   {  
   }

   @Override
   public void componentResized(ComponentEvent e)
   {
      int labelWidth = this.getWidth() - firstLabel.getX() - firstLabel.getX();
      
      firstLabel.setBounds(firstLabel.getX(), firstLabel.getY(), labelWidth, firstLabel.getHeight());
      
      labelWidth = this.getWidth() - instructionLabel.getX() - instructionLabel.getX();
      instructionLabel.setBounds(instructionLabel.getX(), instructionLabel.getY(), labelWidth, instructionLabel.getHeight());
      
      if (firstPositioning)
      {
         textfield.setBounds(textfield.getX(), textfield.getY(), labelWidth, textfield.getHeight());
         firstPositioning = false;
      }
   }

   @Override
   public void componentMoved(ComponentEvent e)
   {  
   }

   @Override
   public void componentShown(ComponentEvent e)
   {  
   }

   @Override
   public void componentHidden(ComponentEvent e)
   {      
   }
   
}
