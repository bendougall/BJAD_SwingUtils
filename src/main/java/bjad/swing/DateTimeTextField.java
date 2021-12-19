package bjad.swing;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.SwingUtilities;

/**
 * A date/time entry field that can be used within 
 * swing applications. 
 *
 * @author 
 *  Ben Dougall
 */
public class DateTimeTextField extends AbstractRestrictiveTextField implements KeyListener, FocusListener
{
   private static final long            serialVersionUID    = 7905086211679479493L;

   private static final SimpleDateFormat FIELD_DATE_FORMAT   = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
   private static final Point[]         DATE_SECTION_RANGES = { 
         new Point(0, 4), 
         new Point(5, 7), 
         new Point(8, 10), 
         new Point(11, 13), 
         new Point(14, 16), 
         new Point(17, 19) 
      };

   private Date                         selectedDate        = new Date();
   
   /**
    * Creates the field, setting the default 
    * value for the field.
    *
    * @param defaultValue
    *    The default date to show in the field.
    */
   public DateTimeTextField(Date defaultValue)
   {
      super();
      
      this.selectedDate = defaultValue;
      
      Color c = this.getBackground();
      
      this.addKeyListener(this);
      this.setBounds(10, 32, 200, 26);
      this.setEditable(false);
      this.setBackground(c);
      this.addFocusListener(this);
      this.setSelectionColor(Color.yellow);
      this.setSelectedTextColor(Color.black);
      this.setText(FIELD_DATE_FORMAT.format(this.selectedDate));
   }
   
   @Override
   public void keyPressed(KeyEvent keyEvent)
   {
     switch (keyEvent.getKeyCode())
     {
     case KeyEvent.VK_LEFT: 
     case KeyEvent.VK_RIGHT: 
       shiftFocus(keyEvent.getKeyCode(), this.getCaretPosition());
       break;
     case KeyEvent.VK_UP: 
     case KeyEvent.VK_DOWN: 
       updateSelectedDate(keyEvent.getKeyCode(), this.getCaretPosition());
       break;
     }
   }
   
   @Override
   public void focusGained(FocusEvent focusEvent)
   {
     setSelectedTextForPoint(DATE_SECTION_RANGES[findFocusedSection(this.getCaretPosition())]);
   }
   
   public void focusLost(FocusEvent arg0) {}
   
   public void keyReleased(KeyEvent arg0) {}
   
   public void keyTyped(KeyEvent arg0) {}
   

   private void setSelectedTextForPoint(final Point selectedRange)
   {
      final DateTimeTextField me = this;
      SwingUtilities.invokeLater(new Runnable()
      {         
         @Override
         public void run()
         {
           me.select(selectedRange.x, selectedRange.y);
         }
      });
   }

   private int findFocusedSection(int caretPosition)
   {
      for (int index = 0; index < DATE_SECTION_RANGES.length; index++)
      {
         if ((caretPosition >= DATE_SECTION_RANGES[index].x) && (caretPosition <= DATE_SECTION_RANGES[index].y))
         {
            return index;
         }
      }
      return 0;
   }

   private void shiftFocus(int keyCode, int caretPosition)
   {
      int index = 0;
      switch (keyCode)
      {
      case KeyEvent.VK_LEFT:
         index = findFocusedSection(caretPosition);
         if (index == 0)
         {
            setSelectedTextForPoint(DATE_SECTION_RANGES[(DATE_SECTION_RANGES.length - 1)]);
         }
         else
         {
            setSelectedTextForPoint(DATE_SECTION_RANGES[(index - 1)]);
         }
         break;
      case KeyEvent.VK_RIGHT:
         index = findFocusedSection(caretPosition);
         if (index == DATE_SECTION_RANGES.length - 1)
         {
            setSelectedTextForPoint(DATE_SECTION_RANGES[0]);
         }
         else
         {
            setSelectedTextForPoint(DATE_SECTION_RANGES[(index + 1)]);
         }
         break;
      }
   }

   private void updateSelectedDate(Date newSelectedDate)
   {
      this.selectedDate = newSelectedDate;
      int selectedIndex = findFocusedSection(this.getCaretPosition());
      this.setText(FIELD_DATE_FORMAT.format(this.selectedDate));
      setSelectedTextForPoint(DATE_SECTION_RANGES[selectedIndex]);
   }
   
   private void updateSelectedDate(int keyCode, int caretPosition)
   {
      int direction = keyCode == KeyEvent.VK_UP ? 1 : -1;
      Calendar c = Calendar.getInstance();
      c.setTime(this.selectedDate);

      int index = findFocusedSection(caretPosition);
      switch (index)
      {
      case 0:
         c.add(Calendar.YEAR, direction);
         break;
      case 1:
         c.add(Calendar.MONTH, direction);
         break;
      case 2:
         c.add(Calendar.DATE, direction);
         break;
      case 3:
         c.add(Calendar.HOUR_OF_DAY, direction);
         break;
      case 4:
         c.add(Calendar.MINUTE, direction);
         break;
      case 5:
         c.add(Calendar.HOUR_OF_DAY, direction * 12);
      }
      updateSelectedDate(c.getTime());
   }
}
