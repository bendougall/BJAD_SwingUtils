package bjad.swing.positioner;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashSet;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 * Positioner helper that allows for a right click
 * on a field to bring up a dialog that will allow
 * the user to define the position and size of the 
 * fields provided to the helper. 
 *
 * @author 
 *   Ben Dougall
 */
public class BJADUIPositionerHelper
{
   private ComponentWrapper[] componentsToMove = null;
   
   /**
    * Constructor, setting the field to add the mouse listener to so that right 
    * click will bring up the positioner window. 
    * 
    * @param horizontal
    *    True to wire the horizontal positior window to appear on right click 
    *    of the controlToTrigger, or false to wire the vertical version of the 
    *    window.
    * @param controlToTrigger
    *    The field that will have the key listener that will adjust the 
    *    position and size of the fields passed
    * @param componentsToControl
    *    The fields to re-position and/or re-size.
    */
   private BJADUIPositionerHelper(final boolean horizontal, Component controlToTrigger, Component... componentsToControl)
   {
      if (controlToTrigger == null)
      {
         throw new IllegalArgumentException("ControlToTrigger cannot be null");
      }
      if (componentsToControl == null || componentsToControl.length == 0)
      {
         return;
      }
      
      // Add all the controls (and the components from any panel passed)
      LinkedHashSet<ComponentWrapper> components = new LinkedHashSet<>();
      for (Component c : componentsToControl)
      {
         // Add thd component to the list. As the control was in the primary 
         // control list, the preselection will be set to true.
         components.add(new ComponentWrapper(c, true));
         // If the component is a panel, add the panel's controls to the list. 
         if (c instanceof JPanel)
         {
            components = getContainedComponents(components, (JPanel)c);
         }
      }
      
      // Now that we have the list of unique controls, make the 
      // array from the set to apply to the table model
      componentsToMove = new ComponentWrapper[components.size()];
      componentsToMove = components.toArray(componentsToMove);
      
      // Make the window that will show when the control gets 
      // right clicked on. 
      final JDialog window = horizontal ? new PositionerWindow(componentsToMove) :
            new VerticalPositionerWindow(componentsToMove);
      
      // Add the right click listener to the control
      controlToTrigger.addMouseListener(new MouseAdapter()
      {
         @Override
         public void mouseClicked(MouseEvent e)
         {
            if (SwingUtilities.isRightMouseButton(e))
            {
               Container parentOfTrigger = getParentOfTriggerBounds(controlToTrigger);               
               Rectangle windowBounds = parentOfTrigger.getBounds();
                  
               window.setLocation((windowBounds.x + windowBounds.width), windowBounds.y);
               window.setVisible(true);
            }
         }
      });
      
      // Add the instructions for use to the console.
      ComponentWrapper w = new ComponentWrapper(controlToTrigger, false);
      
      StringBuilder sb = new StringBuilder();
      sb.append("Type=").append(w.getComponent().getClass().getSimpleName()).append(" :: ");
      
      sb.append(w.getNameOrText());
      sb.append(" :: ");
      
      String boundsText = w.originalPositionText();
      sb.append("Original Position=").append(boundsText); 
      
      // Output the instructions and the original positions of the fields.
      System.out.println(String.format(
            "%s: Control %s has been wired to show the UI positioner window. " + System.lineSeparator() +
            "Right click the control to show the positioner window.", 
            window.getClass().getSimpleName(),
            sb.toString()));
   }
   
   private Container getParentOfTriggerBounds(Component trigger)
   {
      Container superParentOfTrigger = trigger.getParent();
      for (;;)
      {
         Container parent = superParentOfTrigger.getParent();
         if (parent != null)
         {
            superParentOfTrigger = parent;
            if (
                  superParentOfTrigger instanceof JFrame || 
                  superParentOfTrigger instanceof JWindow ||
                  superParentOfTrigger instanceof JDialog)
            {
               break;
            }
         }
         else
         {
            break;
         }
      }
      return superParentOfTrigger;
   }
   
   /**
    * Gathers the controls from the panel passed, recursing into 
    * subpanels if found. 
    * 
    * @param componentSet
    *    The set of components being built.
    * @param parentPanel
    *    The panel to get the components from
    * @return
    *    The set with the panel's controls in it.
    */
   private LinkedHashSet<ComponentWrapper> getContainedComponents(LinkedHashSet<ComponentWrapper> componentSet, JPanel parentPanel)
   {
      for (Component c : parentPanel.getComponents())
      {
         componentSet.add(new ComponentWrapper(c, false));
         if (c instanceof JPanel)
         {
            componentSet = getContainedComponents(componentSet, (JPanel)c);
         }
      }
      return componentSet;
   }
   
   /**
    * Wires the control to trigger with a mouse listener so a right click event
    * will show the horizontal positioner window to modify the location of the 
    * controls/panels passed to the function. 
    * 
    * @param controlToTrigger
    *    The field that will have the key listener that will adjust the 
    *    position and size of the fields passed
    * @param componentsToControl
    *    The fields to re-position and/or re-size.
    */
   public static void wireHorizontalPositionerDialog(Component controlToTrigger, Component... componentsToControl)
   {
      new BJADUIPositionerHelper(true, controlToTrigger, componentsToControl);
   }
   
   /**
    * Wires the control to trigger with a mouse listener so a right click event
    * will show the vertical positioner window to modify the location of the 
    * controls/panels passed to the function. 
    * 
    * @param controlToTrigger
    *    The field that will have the key listener that will adjust the 
    *    position and size of the fields passed
    * @param componentsToControl
    *    The fields to re-position and/or re-size.
    */
   public static void wireVerticalPositionerDialog(Component controlToTrigger, Component... componentsToControl)
   {
      new BJADUIPositionerHelper(false, controlToTrigger, componentsToControl);
   }
}

/**
 * Dialog to display with the positioning options within it, including
 * the positioning buttons and the overall control table. 
 *
 * @author 
 *   Ben Dougall
 */
class PositionerWindow extends JDialog implements ActionListener, ItemListener
{
   private static final long serialVersionUID = 4335206789540540660L;
 
   private JRadioButton locationRadioButton = new JRadioButton("Location");
   private JRadioButton sizeRadioButton = new JRadioButton("Size");
   private JButton upButton = new JButton("Up");
   private JButton rightButton = new JButton("Right");
   private JButton downButton = new JButton("Down");
   private JButton leftButton = new JButton("Left");
   
   private JTable table; 
   private ControlTableModel controlTableModel; 
   
   /**
    * Creates the dialog with the components the window is configured
    * to control. 
    * 
    * @param components
    *    The list of components to manage the position(s) of
    */
   public PositionerWindow(ComponentWrapper[] components)
   {
      super();
      setModal(false);
      setTitle("Positioner Helper");
   
      controlTableModel = new ControlTableModel(components);
      table = new JTable(controlTableModel);
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      table.setFillsViewportHeight(true);
      
      JPanel contentPane = new JPanel(new BorderLayout());
      contentPane.add(buildDPadPanel(), BorderLayout.WEST);
      
      JScrollPane sp = new JScrollPane(table);
      contentPane.add(sp);      
      
      contentPane.setBorder(new EmptyBorder(0, 0, 0, 5));
      setContentPane(contentPane);
      setSize(825, 310);
      
      resizeColumnWidth(table);
   }
   
   /**
    * Creates the instruction section of the dialog. 
    * @return
    *    The wrapped label control containing the 
    *    instructions for the panel.    
    */
   private JTextArea createInstructionLabel()
   {
      JTextArea wrappedLabel = new JTextArea();
      StringBuilder sb = new StringBuilder("Use arrow keys while a radio button is in focus as a shortcut for the buttons below.");
      sb.append(System.lineSeparator());
      sb.append(System.lineSeparator());
      sb.append("Or you can manually set the coordinates within the table to the right.");
      wrappedLabel.setText(sb.toString());
      
      // Turn on wrapping by default.
      wrappedLabel.setWrapStyleWord(true);
      wrappedLabel.setLineWrap(true);
      
      // Make the text area super class act like
      // like a label by making it transparent,
      // not editable, and not focusable.
      wrappedLabel.setOpaque(false);
      wrappedLabel.setEditable(false);
      wrappedLabel.setFocusable(false);
      
      // Make the text area super class look
      // like a label by stealing all the 
      // label properties from the UIManager
      wrappedLabel.setBackground(UIManager.getColor("Label.background"));
      wrappedLabel.setFont(UIManager.getFont("Label.font"));
      wrappedLabel.setBorder(new TitledBorder("Instructions"));
      
      return wrappedLabel;
   }
   
   /**
    * Builds left side of the dialog with the instructions and 
    * button "d-pad" controls.
    * @return
    *    The constructed panel.
    */
   private JPanel buildDPadPanel()
   {
      JPanel dPadPane = new JPanel(null, true);
      dPadPane.setPreferredSize(new Dimension(300, 500));
      
      JTextArea instructionBox = createInstructionLabel();
      instructionBox.setBounds(5, 5, 290, 110);      
      dPadPane.add(instructionBox);
      
      JPanel optionPane = new JPanel(new GridLayout(1, 2), true);
      optionPane.add(locationRadioButton);
      optionPane.add(sizeRadioButton);
      optionPane.setBounds(50, 120, 245, 30);
      
      // Make a wiring for the radio buttons so that the 
      // arrow keys will "press" the buttons in the d-pad.
      KeyListener arrowBinding = new KeyAdapter()
      {
         @Override
         public void keyPressed(KeyEvent e)
         {
            switch (e.getKeyCode())
            {
            case KeyEvent.VK_UP:
               upButton.doClick();
               e.consume();
               break;
            case KeyEvent.VK_LEFT:
               leftButton.doClick();
               e.consume();
               break;
            case KeyEvent.VK_DOWN:
               downButton.doClick();
               e.consume();
               break;
            case KeyEvent.VK_RIGHT:
               rightButton.doClick();
               e.consume();
               break;
            default:
                  break;                  
            }
         }
      };
      
      locationRadioButton.setSelected(true);
      
      locationRadioButton.addKeyListener(arrowBinding);
      locationRadioButton.addItemListener(this);
      locationRadioButton.setMnemonic('L');
      sizeRadioButton.addKeyListener(arrowBinding);
      sizeRadioButton.addItemListener(this);
      sizeRadioButton.setMnemonic('S');
      
      ButtonGroup bg = new ButtonGroup();
      bg.add(locationRadioButton);
      bg.add(sizeRadioButton);
      
      dPadPane.add(optionPane);
            
      JPanel buttonGridPane = new JPanel(new GridLayout(3, 3), true);
      buttonGridPane.add(new JLabel());
      buttonGridPane.add(upButton);      
      buttonGridPane.add(new JLabel());
      
      buttonGridPane.add(leftButton);
      buttonGridPane.add(new JLabel());
      buttonGridPane.add(rightButton);
      
      buttonGridPane.add(new JLabel());
      buttonGridPane.add(downButton);      
      buttonGridPane.add(new JLabel());
      
      buttonGridPane.setBounds(25, 160, 235, 90);
      dPadPane.add(buttonGridPane);
      
      upButton.addActionListener(this);
      upButton.setFocusable(false);
      rightButton.addActionListener(this);
      rightButton.setFocusable(false);
      downButton.addActionListener(this);
      downButton.setFocusable(false);
      leftButton.addActionListener(this);
      leftButton.setFocusable(false);
      
      return dPadPane;
   }
   
   /**
    * Quick column resizer that will size the 
    * columns in the table based on the contents 
    * in the table. 
    * 
    * @param table
    *    The table to size the columns in.
    */
   private void resizeColumnWidth(JTable table) 
   {
      final TableColumnModel columnModel = table.getColumnModel();
      for (int column = 0; column < table.getColumnCount(); column++) 
      {
          int width = 15; // Min width
          for (int row = 0; row < table.getRowCount(); row++) 
          {
              TableCellRenderer renderer = table.getCellRenderer(row, column);
              Component comp = table.prepareRenderer(renderer, row, column);
              width = Math.max(comp.getPreferredSize().width +1 , width);
          }
          columnModel.getColumn(column).setPreferredWidth(width);
      }
   }

   /**
    * Relabel the buttons to be directional if the location 
    * radio button is selected, or size based if the size
    * radio button is selected. 
    * 
    * @param e
    *    The radio button selection event.
    */
   @Override
   public void itemStateChanged(ItemEvent e)
   {
      if (locationRadioButton.isSelected())
      {
         upButton.setText("Up");
         downButton.setText("Down");
         leftButton.setText("Left");
         rightButton.setText("Right");
      }
      else if (sizeRadioButton.isSelected())
      {
         upButton.setText("Shorter");
         downButton.setText("Taller");
         leftButton.setText("Thinner");
         rightButton.setText("Wider");
      }
   }

   /**
    * Reacts to the "dpad" buttons being pressed so any 
    * selected controls in the table will be re-positioned
    * or re-sized based on the button pressed.  
    */
   @Override
   public void actionPerformed(ActionEvent e)
   {
      boolean sizeMode = sizeRadioButton.isSelected();
      int columnToAdjust = -1;
      int adjustValue = 1;
      for (int i = 0; i < controlTableModel.getRowCount(); ++i)
      {
         if (controlTableModel.getValueAt(i, 0) == (Boolean.TRUE))
         {
            if (e.getSource().equals(upButton) || e.getSource().equals(downButton))
            {
               columnToAdjust = sizeMode ? 6 : 4;
               if (upButton.equals(e.getSource()))
               {
                  adjustValue = -1;
               }
            }
            else if (e.getSource().equals(leftButton) || e.getSource().equals(rightButton))
            {
               columnToAdjust = sizeMode ? 5 : 3;
               if (leftButton.equals(e.getSource()))
               {
                  adjustValue = -1;
               }
            }
            
            if (columnToAdjust > 2)
            {
               int existingValue = (int)controlTableModel.getValueAt(i, columnToAdjust);            
               controlTableModel.setValueAt((existingValue + adjustValue), i, columnToAdjust);
               controlTableModel.fireTableCellUpdated(i, columnToAdjust);
            }
         }         
      }      
   }
}

/**
 * The table model for the control table, setting up the 
 * ability to select the controls to position or size 
 * via the buttons and set the position and size via 
 * the x, y, width, and height columns.
 *
 * @author 
 *   Ben Dougall
 */
class ControlTableModel extends AbstractTableModel
{
   private static final long serialVersionUID = -8854009637774563020L;
   
   /**
    * The components to show in the table.
    */
   ComponentWrapper[] components;
   
   /**
    * Constructor, setting the controls to show in the table. 
    * @param components
    *    The controls to show in the table. 
    */
   public ControlTableModel(ComponentWrapper[] components)
   {
      this.components = components;
   }
   
   /**
    * Returns the row count (aka the number of components
    * to show in the table)
    * @return 
    *    The number of rows in the table. 
    */
   @Override
   public int getRowCount()
   {
      return components.length;
   }

   /**
    * Provides the column names in the table.
    * @param column
    *    The index for the column to get the name for.
    * @return
    *    The column name.
    */
   @Override 
   public String getColumnName(int column)
   {
      switch (column)
      {
      case 0: 
         return "  ";
      case 1:
         return "Type";
      case 2:
         return "Name/Text ";
      case 3:
         return "X";
      case 4: 
         return "Y";
      case 5:
         return "Width ";
      case 6:
         return "Height ";
      }
      return "";
   }
   
   /**
    * Returns the class for the data displayed 
    * in the column. 
    * @param column
    *    The column to get the display class for.
    * @return
    *    The class that will be displayed in the 
    *    table for the column passed.
    */
   @Override 
   public Class<?> getColumnClass(int column)
   {
      if (column == 0)
      {
         return Boolean.class;
      }
      else if (column == 1 || column == 2)
      {
         return String.class;
      }
      else
      {
         return Integer.class;
      }
   }
   
   /**
    * Returns 7, the number of columns in
    * the table.
    * @return
    *    The number of columns in the table (aka 7)
    */
   @Override
   public int getColumnCount()
   {
      return 7;
   }

   /**
    * Gets the value to display in the table for the 
    * row and column passed. 
    * @param rowIndex
    *    The row for the data
    * @param columnIndex
    *    The column to get the data for,
    * @return
    *    The data to display in the table for the 
    *    row and column passed.
    */
   @Override
   public Object getValueAt(int rowIndex, int columnIndex)
   {
      ComponentWrapper cw = components[rowIndex];
      switch (columnIndex)
      {
      case 0: 
         return cw.isSelected();
      case 1:
         return cw.getComponent().getClass().getSimpleName();
      case 2:
         return cw.getNameOrText();
      case 3:
         return cw.getComponent().getBounds().x;
      case 4: 
         return cw.getComponent().getBounds().y;
      case 5:
         return cw.getComponent().getBounds().width;
      case 6:
         return cw.getComponent().getBounds().height;
      }
      return "";
   }   
   
   /**
    * Returns true if the cell can be modified. 
    * @param rowIndex
    *    The row in the table to check.
    * @param columnIndex  
    *    The column in the table to check.
    * @return
    *    True if the cell at the row and column 
    *    passed is editable (any row and any column 
    *    apart from the type or name/text column)
    */
   @Override
   public boolean isCellEditable(int rowIndex, int columnIndex)
   {
      if (rowIndex < 0 && rowIndex >= components.length)
      {
         return false;
      }
      else if (columnIndex == 1 || columnIndex == 2)
      {
         return false;
      }
      return true;
   }
   
   /**
    * Sets the value for the row and column passed.
    * @param aValue
    *    The value to set
    * @param rowIndex
    *    The row to change the value for.
    * @param columnIndex
    *    The column the data change is coming from.
    */
   @Override
   public void setValueAt(Object aValue, int rowIndex, int columnIndex)
   {
      ComponentWrapper cw = components[rowIndex];
      Rectangle r = cw.getComponent().getBounds();
      boolean reposition = false;
      
      switch (columnIndex)
      {
      case 0: 
         cw.setSelected((Boolean)aValue);
         break;
      case 3:
         int value = r.x;
         try 
         {
            value = Integer.parseInt(aValue.toString());
            r.x = value;
            reposition = true; 
         }
         catch (Exception ex)
         {
            
         }
         break;
      case 4: 
         value = r.y;
         try 
         {
            value = Integer.parseInt(aValue.toString());
            r.y = value;
            reposition = true; 
         }
         catch (Exception ex)
         {
            
         }
         break;
      case 5:
         value = r.width;
         try 
         {
            value = Integer.parseInt(aValue.toString());
            r.width = value;
            reposition = true; 
         }
         catch (Exception ex)
         {
            
         }
         break;
      case 6:
         value = r.height;
         try 
         {
            value = Integer.parseInt(aValue.toString());
            r.height = value;
            reposition = true; 
         }
         catch (Exception ex)
         {
            
         }
         break;
      }
      if (reposition)
      {
         cw.getComponent().setBounds(r);
      }
   }
}
/**
 * Wrapper for component in the position helper
 * that contains the original position for the 
 * component. 
 *
 * @author 
 *   Ben Dougall
 */
class ComponentWrapper
{
   /**
    * The component being moved. 
    */
   private Component component;
   /**
    * The original position of the component before all the moves. 
    */
   private Rectangle originalBounds; 
   /**
    * Flag storing if the control should be moved via the 
    * UI's positioning controls.
    */
   private boolean selected = false;
   
   /**
    * Constructor, setting the component and the original 
    * position of the component. 
    * 
    * @param c
    *    The component that will be held by the wrapper.
    * @param preSelected
    *    True if the initial display in the table will have
    *    the control selected or not.
    */
   public ComponentWrapper(Component c, boolean preSelected)
   {
      this.component = c;
      this.originalBounds = component.getBounds();
      this.selected = preSelected;
   }
   
   /**
    * Provides the position string for the original
    * position of the component. 
    * 
    * @return  
    *    The string outlining the original position 
    *    of the component in the wrapper.
    */
   public String originalPositionText()
   {
      return getBoundsText(this.originalBounds);
   }
   
   /**
    * Provides the position string for the current
    * position of the component. 
    * 
    * @return  
    *    The string outlining the current position 
    *    of the component in the wrapper.
    */
   public String currentPositionText()
   {
      return getBoundsText(this.component.getBounds());
   }
   /**
    * Returns if the control is selected in the table 
    * or not. 
    * @return
    *    True if the control is checked off in the 
    *    control table. 
    */
   public boolean isSelected()
   {
      return this.selected;
   }
   
   /**
    * Sets the "selected" flag so the positioning
    * done by the buttons will only affect the 
    * checked off controls.
    * @param value
    *    True to have the field checked off in the 
    *    table, false otherwise.
    */
   public void setSelected(boolean value)
   {
      this.selected = value;
   }
   
   /**
    * Provides the string for the Rectangle object 
    * passed, which will outline the x1, y1, x2, y2,
    * width, and height of the rectangle. 
    * 
    * @param r
    *    The rectangle to create the string for.
    * @return
    *    The string with the rectangle and size information.
    */
   private String getBoundsText(Rectangle r)
   {
      return String.format("(%s, %s, %s, %s) Size: (%s, %s)", 
            r.x,
            r.y,
            r.x + r.width,
            r.y + r.height,
            r.width,
            r.height);
   }
   
   /**
    * Provides the name of the component in the wrapper, or if the 
    * field is not named, the text returned by the fields "getText()"
    * method (if has one). 
    * 
    * No Name and No Text will return "<Nameless/Textless>"
    * @return
    *    The name of the component, or the text from the component,
    *    or the default text if name and text could not be found.
    */
   public String getNameOrText()
   {
      StringBuilder sb = new StringBuilder();
      
      boolean nameOrTextFound = false;
      
      // Get the name of the field if it has a name.
      if (component.getName() != null && !component.getName().trim().isEmpty())
      {
         sb.append("Name=\"").append(component.getName()).append('\"');
         nameOrTextFound = true;
      }
      // Get the text from the field if it has a getText method. 
      else
      {
         try
         {
            String textResult = component.getClass().getMethod("getText").invoke(component).toString();
            if (textResult != null && !textResult.trim().isEmpty())
            {
               sb.append("Text=\"").append(textResult).append('\"');
               nameOrTextFound = true;
            }
         }
         catch (Exception ex)
         {            
         }
      }
      // No name, no text, return default text.
      if (!nameOrTextFound)
      {
         sb.append("<Nameless/Textless>");
      }
      
      return sb.toString();
   }
   
   /**
    * Returns the component the wrapper contains.
    * @return
    *    The wrapped component.
    */
   public Component getComponent()
   {
      return this.component;
   }
}

/**
 * Dialog to display with the positioning options within it, including
 * the positioning buttons and the overall control table. 
 *
 * @author 
 *   Ben Dougall
 */
class VerticalPositionerWindow extends JDialog implements ActionListener
{
   private static final long serialVersionUID = 4335206789540540660L;
   private static final int WINDOW_WIDTH = 560;
   private static final String INSTRUCTION_TEXT = 
         "While the focus is on the pixel field, use arrow keys to adjust position, or CTRL+arrow keys to adjust the size of the checked elements in the table below." +
          System.lineSeparator() + System.lineSeparator() + 
          "You can also change the position and size manually by modifying the values in the table.";
        
 
   private JButton upButton = new JButton("Up");
   private JButton rightButton = new JButton("Right");
   private JButton downButton = new JButton("Down");
   private JButton leftButton = new JButton("Left");
   private JButton thinButton = new JButton("Thin");
   private JButton wideButton = new JButton("Wide");
   private JButton tallButton = new JButton("Tall");
   private JButton shortButton = new JButton("Short");
   private JTextField factorField = new JTextField("1");
   private JCheckBox consoleCheckbox = new JCheckBox("Send changes to console");
   private JCheckBox alwaysOnTopCheckbox = new JCheckBox("Always on top?");
   
   private JTable table; 
   private ControlTableModel controlTableModel; 
   
   /**
    * Creates the dialog with the components the window is configured
    * to control. 
    * 
    * @param components
    *    The list of components to manage the position(s) of
    */
   public VerticalPositionerWindow(ComponentWrapper[] components)
   {
      super();
      setModal(false);
      setTitle("Positioner Helper");
   
      controlTableModel = new ControlTableModel(components);
      
      JPanel contentPane = new JPanel(new BorderLayout(0, 5));
      JPanel dPanel = buildDPadPanel();
      dPanel.setPreferredSize(new Dimension(WINDOW_WIDTH-10, 205));
      dPanel.setMinimumSize(new Dimension(WINDOW_WIDTH-10, 205));
      contentPane.add(dPanel, BorderLayout.NORTH);
      
      JPanel tableAreaPanel = createTableArea();
      contentPane.add(tableAreaPanel, BorderLayout.CENTER);     
      
      contentPane.setBorder(new EmptyBorder(0, 0, 0, 5));
      setContentPane(contentPane);
      setSize(WINDOW_WIDTH, 500);
      setAlwaysOnTop(true);
      
      resizeColumnWidth(table);
      
      addComponentListener(new ComponentAdapter()
      {
         @Override
         public void componentHidden(ComponentEvent e)
         {
            if (!consoleCheckbox.isSelected())
            {
               return;
            }
            
            StringBuilder selSB = new StringBuilder();
            StringBuilder unSelSB = new StringBuilder();
            for (int i = 0; i != controlTableModel.getRowCount(); ++i)
            {
               StringBuilder sb = new StringBuilder();
               sb.append("      Type=").append(controlTableModel.components[i].getComponent().getClass().getSimpleName()).append(" :: ");
               
               sb.append(controlTableModel.components[i].getNameOrText());
               sb.append(" :: ");
               
               String boundsText = controlTableModel.components[i].currentPositionText();
               sb.append("Position = ").append(boundsText);
               
               sb.append(System.lineSeparator());
               
               if (controlTableModel.getValueAt(i, 0) == (Boolean.TRUE))
               {
                  selSB.append(sb.toString());
               }
               else 
               {
                  unSelSB.append(sb.toString());
               }
            }
                        
            System.out.println("- Positioner Window Closed -");
            System.out.println("---  Start of selected control informaton  ---");
            System.out.print(selSB.toString());
            System.out.println("---   End of selected control informaton   ---");
            System.out.println("--- Start of unselected control informaton ---");
            System.out.print(unSelSB.toString());
            System.out.println("---  End of unselected control informaton  ---");
            System.out.println("- Positioner Output complete -");
         }
      });
   }
   
   /**
    * Creates the instruction section of the dialog. 
    * @return
    *    The wrapped label control containing the 
    *    instructions for the panel.    
    */
   private JTextArea createInstructionLabel()
   {
      JTextArea wrappedLabel = new JTextArea();
      wrappedLabel.setText(INSTRUCTION_TEXT);
      wrappedLabel.setFont(wrappedLabel.getFont().deriveFont(Font.PLAIN));
      
      // Turn on wrapping by default.
      wrappedLabel.setWrapStyleWord(true);
      wrappedLabel.setLineWrap(true);
      
      // Make the text area super class act like
      // like a label by making it transparent,
      // not editable, and not focusable.
      wrappedLabel.setOpaque(false);
      wrappedLabel.setEditable(false);
      wrappedLabel.setFocusable(false);
      
      // Make the text area super class look
      // like a label by stealing all the 
      // label properties from the UIManager
      wrappedLabel.setBackground(UIManager.getColor("Label.background"));
      wrappedLabel.setFont(UIManager.getFont("Label.font"));
      wrappedLabel.setBorder(new TitledBorder("Instructions"));
      
      return wrappedLabel;
   }
   
   /**
    * Builds left side of the dialog with the instructions and 
    * button "d-pad" controls.
    * @return
    *    The constructed panel.
    */
   private JPanel buildDPadPanel()
   {
      JPanel dPadPane = new JPanel(null, true);
      dPadPane.setPreferredSize(new Dimension(WINDOW_WIDTH-10, 500));
      
      JTextArea instructionBox = createInstructionLabel();
      
      JPanel instructionPanel = new JPanel(new BorderLayout());
      dPadPane.add(instructionPanel);
      instructionPanel.setBounds(0, 1, WINDOW_WIDTH-30, 90);
      
      instructionPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
      instructionPanel.add(instructionBox, BorderLayout.CENTER);
      
      // Make a wiring for the radio buttons so that the 
      // arrow keys will "press" the buttons in the d-pad.
      KeyListener arrowBinding = new KeyAdapter()
      {
         @Override
         public void keyPressed(KeyEvent e)
         {
            switch (e.getKeyCode())
            {
            case KeyEvent.VK_UP:
               if (e.isControlDown())
               {
                  shortButton.doClick();
               }
               else
               {
                  upButton.doClick();
               }
               e.consume();
               break;
            case KeyEvent.VK_LEFT:
               if (e.isControlDown())
               {
                  thinButton.doClick();
               }
               else
               {
                  leftButton.doClick();
               }
               e.consume();
               break;
            case KeyEvent.VK_DOWN:
               if (e.isControlDown())
               {
                  tallButton.doClick();
               }
               else
               {
                  downButton.doClick();
               }
               e.consume();
               break;
            case KeyEvent.VK_RIGHT:
               if (e.isControlDown())
               {
                  wideButton.doClick();
               }
               else
               {
                  rightButton.doClick();
               }
               e.consume();
               break;
            default:
                  break;                  
            }
         }
      };
      
      factorField.addKeyListener(arrowBinding);
      
      JPanel optionPane = new JPanel(new BorderLayout(5, 5), true);
      JPanel factorPanel = new JPanel(new  BorderLayout(5, 5));
      JLabel lbl = new JLabel("Pixels to move by:");
      lbl.setBorder(new EmptyBorder(0, 0, 0, 5));
      factorPanel.add(lbl, BorderLayout.WEST);
      factorPanel.add(factorField, BorderLayout.CENTER);
      
      optionPane.add(factorPanel, BorderLayout.CENTER);
      
      JPanel boxesPanel = new JPanel(new FlowLayout(SwingConstants.LEFT, 5, 0), true);
      consoleCheckbox.setSelected(true);
      boxesPanel.add(consoleCheckbox);
            
      alwaysOnTopCheckbox.addActionListener(this);
      alwaysOnTopCheckbox.setSelected(true);      
      boxesPanel.add(alwaysOnTopCheckbox);
      
      optionPane.add(boxesPanel, BorderLayout.EAST);
      
      optionPane.setBounds(5, 105, WINDOW_WIDTH-30, 25);
      
      dPadPane.add(optionPane);
            
      JPanel buttonGridPane = new JPanel(new GridLayout(2, 4), true);
      buttonGridPane.add(upButton);    
      buttonGridPane.add(downButton);  
      buttonGridPane.add(leftButton);
      buttonGridPane.add(rightButton);
      buttonGridPane.add(shortButton);    
      buttonGridPane.add(tallButton);  
      buttonGridPane.add(thinButton);
      buttonGridPane.add(wideButton);
      
      buttonGridPane.setBounds(5, 140, WINDOW_WIDTH-30, 60);
      dPadPane.add(buttonGridPane);
      
      JButton[] btns = new JButton[] {
            upButton, downButton, leftButton, rightButton,
            shortButton, tallButton, thinButton, wideButton
      };
      for (JButton btn : btns)
      {
         btn.addActionListener(this);
         btn.setFocusable(false);
      }
      
      return dPadPane;
   }
   
   private JPanel createTableArea()
   {
      table = new JTable(controlTableModel);
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      table.setFillsViewportHeight(true);
      
      JPanel tableArea = new JPanel(new BorderLayout(0, 0), true);
      tableArea.setBorder(new TitledBorder("Controls to move:"));
      tableArea.add(new JScrollPane(table), BorderLayout.CENTER);
      return tableArea;
   }
   /**
    * Quick column resizer that will size the 
    * columns in the table based on the contents 
    * in the table. 
    * 
    * @param table
    *    The table to size the columns in.
    */
   private void resizeColumnWidth(JTable table) 
   {
      final TableColumnModel columnModel = table.getColumnModel();
      for (int column = 0; column < table.getColumnCount(); column++) 
      {
          int width = 15; // Min width
          for (int row = 0; row < table.getRowCount(); row++) 
          {
              TableCellRenderer renderer = table.getCellRenderer(row, column);
              Component comp = table.prepareRenderer(renderer, row, column);
              width = Math.max(comp.getPreferredSize().width +1 , width);
          }
          columnModel.getColumn(column).setPreferredWidth(width);
      }
   }

   /**
    * Reacts to the "dpad" buttons being pressed so any 
    * selected controls in the table will be re-positioned
    * or re-sized based on the button pressed.  
    */
   @Override
   public void actionPerformed(ActionEvent e)
   {
      try
      {
         if (e.getSource().equals(alwaysOnTopCheckbox))
         {
            this.setAlwaysOnTop(alwaysOnTopCheckbox.isSelected());
            return;
         }
         
         boolean sizeMode = 
               e.getSource().equals(shortButton) ||
               e.getSource().equals(tallButton) ||
               e.getSource().equals(thinButton) ||
               e.getSource().equals(wideButton);
         
         if (consoleCheckbox.isSelected())
         {
            System.out.println("--- Start ---");
         }
         for (int i = 0; i < controlTableModel.getRowCount(); ++i)
         {         
            int columnToAdjust = -1;
            int adjustValue = 1;
            try
            {
               adjustValue = Integer.parseInt(factorField.getText());         
            }
            catch (Exception ex)
            {
               adjustValue = 1;
               factorField.setText("1");
            }
            if (controlTableModel.getValueAt(i, 0) == (Boolean.TRUE))
            {
               if (
                     e.getSource().equals(upButton) || 
                     e.getSource().equals(downButton) || 
                     e.getSource().equals(shortButton) || 
                     e.getSource().equals(tallButton))
               {
                  columnToAdjust = sizeMode ? 6 : 4;
                  if (upButton.equals(e.getSource()) || shortButton.equals(e.getSource()))
                  {
                     adjustValue *= -1;
                  }
               }
               else if (
                     e.getSource().equals(leftButton) || 
                     e.getSource().equals(rightButton) ||
                     e.getSource().equals(thinButton) ||
                     e.getSource().equals(wideButton))
               {
                  columnToAdjust = sizeMode ? 5 : 3;
                  if (leftButton.equals(e.getSource()) || thinButton.equals(e.getSource()))
                  {
                     adjustValue *= -1;
                  }
               }
               
               if (columnToAdjust > 2)
               {
                  int existingValue = (int)controlTableModel.getValueAt(i, columnToAdjust);            
                  controlTableModel.setValueAt((existingValue + adjustValue), i, columnToAdjust);
                  controlTableModel.fireTableCellUpdated(i, columnToAdjust);
                  
                  if (consoleCheckbox.isSelected())
                  {                  
                     StringBuilder sb = new StringBuilder();
                     sb.append("  Type=").append(controlTableModel.components[i].getComponent().getClass().getSimpleName()).append(" :: ");
                     
                     sb.append(controlTableModel.components[i].getNameOrText());
                     sb.append(" :: ");
                     
                     String boundsText = controlTableModel.components[i].currentPositionText();
                     sb.append("Position = ").append(boundsText);
                     
                     System.out.println(sb.toString());
                  }
               }
            }         
         }   
         if (consoleCheckbox.isSelected())
         {
            System.out.println("--- End ---");
         }
      }
      catch (Exception ex)
      {
         // Don't crash the system if something goes wrong.
         ex.printStackTrace();
      }
   }
}