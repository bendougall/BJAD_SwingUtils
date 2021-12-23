package bjad.swing.testapp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bjad.swing.DateTimeTextField;
import bjad.swing.NumericTextField;
import bjad.swing.TextField;
import bjad.swing.nav.AbstractBJADNavPanel;
import bjad.swing.nav.BJADModuleEntry;
import bjad.swing.nav.BJADNavModule;
import bjad.swing.nav.BJADSidebarNavContentPane;

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
      module.setDisplayName("Module 2");
      module.setOrdinial(1);
      
      BJADModuleEntry entry = new BJADModuleEntry();
      entry.setDisplayName("Entry 2-2");
      entry.setOrdinial(1);
      module.getEntries().add(entry);
      entry = new BJADModuleEntry();
      entry.setDisplayName("Entry 2-1");
      entry.setOrdinial(0);
      module.getEntries().add(entry);
      modules.add(module);
      
      module = new BJADNavModule();
      module.setDisplayName("TextField Demos");
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
      
      setContentPane(new BJADSidebarNavContentPane(modules));
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
      lbl.setPreferredSize(new Dimension(150, 30));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(noRestrictionsField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("'ABCDE' Only Field:");
      lbl.setPreferredSize(new Dimension(150, 30));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(abcdeField, BorderLayout.CENTER);
      abcdeField.setName("abcdeField");
      abcdeField.addAllowableCharacter('a');
      abcdeField.addAllowableCharactersFromString("bcdeABCDE");
      abcdeField.setPlaceholderText("Can only enter the following characters in this field: AaBbCcDdEe");
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("3 Characters only Field");
      lbl.setPreferredSize(new Dimension(150, 30));
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
      lbl.setPreferredSize(new Dimension(150, 30));
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

class DateEntryPanel extends AbstractBJADNavPanel
{
   private static final long serialVersionUID = -5886406219517070874L;
   
   private DateTimeTextField dateTimeTextField = new DateTimeTextField(new Date());
   
   public DateEntryPanel()
   {
      super();
      setLayout(new BorderLayout());
      
      JPanel content = new JPanel(true);
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      JPanel pane = new JPanel(new BorderLayout(5,5));
      JLabel lbl = new JLabel("Date Time Entry Field (use arrow keys to set):");
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
}
