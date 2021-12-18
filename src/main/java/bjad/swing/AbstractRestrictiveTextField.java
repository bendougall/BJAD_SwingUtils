package bjad.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import bjad.swing.listener.InvalidEntryListener;
import bjad.swing.listener.InvalidEntryListener.InvalidatedReason;

/**
 * Base class for restrictive text fields, such as
 * the integer and decimal text fields contained
 * within this package.
 *
 * @author 
 *  Ben Dougall
 */
public abstract class AbstractRestrictiveTextField extends JTextField implements 
   FocusListener
{
   private static final long serialVersionUID = 1558940477904874871L;

   /**
    * The text to show in the field if the field is empty, but enabled 
    * and editable. 
    */
   protected String placeholderText = "";
   
   /**
    * The color to draw the placeholder text in.
    */
   protected Color placeholderColor = this.getDisabledTextColor().darker();
   
   /**
    * The font to draw the placeholder text in. 
    */
   protected Font placeholderFont = this.getFont().deriveFont(Font.ITALIC);
   
   /**
    * Property telling the field to either select all the text 
    * or to move the caret to the end the text in the field 
    * when focus is gained in the text box. 
    */
   protected boolean selectAllOnFocus = true;
   
   /**
    * Adds the invalid entry listener to the field so it will
    * be registered for the events. 
    * 
    * @param listener
    *    The listener to register
    */
   public void addInvalidEntryListener(InvalidEntryListener listener)
   {
      listenerList.add(InvalidEntryListener.class, listener);
   }
   
   /**
    * Removes the invalid entry listener from the field so it
    * will no longer be registered for events.
    * 
    * @param listener
    *    The listener to remove.
    */
   public void removeInvalidEntryListener(InvalidEntryListener listener)
   {
      listenerList.remove(InvalidEntryListener.class, listener);
   }
   
   /**
    * Returns the placeholder text, which will be shown to the user 
    * when the field is empty, but enabled and editable. 
    * 
    * @return
    *    The placeholder text currently in use by the field.
    */
   public String getPlaceHolderText()
   {
      return placeholderText;
   }
   
   /**
    * Sets the placeholder text to display in the field when its
    * empty, but enabled and editable. 
    * @param text
    *    The text to display.
    */
   public void setPlaceholderText(String text)
   {
      this.placeholderText = text;
   }
   
   /**
    * Returns the value of the AbstractRestrictiveTextField instance's 
    * placeholderColor property.
    *
    * @return 
    *   The value of placeholderColor
    */
   public Color getPlaceholderColor()
   {
      return this.placeholderColor;
   }

   /**
    * Sets the value of the AbstractRestrictiveTextField instance's 
    * placeholderColor property.
    *
    * @param placeholderColor 
    *   The value to set within the instance's 
    *   placeholderColor property
    */
   public void setPlaceholderColor(Color placeholderColor)
   {
      this.placeholderColor = placeholderColor;
   }

   /**
    * Returns the value of the AbstractRestrictiveTextField instance's 
    * placeholderFont property.
    *
    * @return 
    *   The value of placeholderFont
    */
   public Font getPlaceholderFont()
   {
      return this.placeholderFont;
   }

   /**
    * Sets the value of the AbstractRestrictiveTextField instance's 
    * placeholderFont property.
    *
    * @param placeholderFont 
    *   The value to set within the instance's 
    *   placeholderFont property
    */
   public void setPlaceholderFont(Font placeholderFont)
   {
      this.placeholderFont = placeholderFont;
   }
   
   /**
    * Sets whether the text in the field will be selected when focus
    * is gained in the text box (default), or if the caret position 
    * will be moved to the end of the text in the field. 
    * 
    * @param val
    *    True to select all text when focus is gained, false to move
    *    the caret to the end of the text content.
    */
   public void setSelectAllOnFocus(boolean val)
   {
      this.selectAllOnFocus = val;
   }

   /**
    * Paints the component on the screen first by letting the swing framework
    * do what it usually does, then paints the place holder text for the field
    * if the place holder text is present, and the field has no text, is 
    * enabled and editable. 
    * 
    * @param pG
    *    The graphics object from the swing framework to draw the 
    *    placeholder text with.
    */
   @Override
   protected void paintComponent(final Graphics pG) 
   {
      super.paintComponent(pG);

      boolean paintPlaceHolder = true;
      
      // There has to be placeholder text to draw
      if (placeholderText == null || placeholderText.trim().isEmpty())
      {
         paintPlaceHolder = false;
      }
      // The field must be enabled
      else if (!isEnabled())
      {
         paintPlaceHolder = false;
      }
      // The field must be editable
      else if (!isEditable())
      {
         paintPlaceHolder = false;
      }
      // The field must be empty with no input from the user.
      else if (getTextContent().length() > 0)
      {
         paintPlaceHolder = false;
      }

      // All conditions met? Draw the placeholder text within the 
      // text field. 
      if (paintPlaceHolder)
      {
         final Graphics2D g = (Graphics2D) pG;
         g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         g.setColor(placeholderColor);
         g.setFont(placeholderFont);
         
         g.drawString(
               placeholderText, 
               getInsets().left + 1, 
               pG.getFontMetrics().getMaxAscent() + getInsets().top + 1);
      }
   }
   
   /**
    * Retrieves the actual text content for the 
    * field, trimmed, without subclasses affecting  
    * the results.
    * 
    * @return
    *    The results of JTextField.getText();
    */
   protected String getTextContent()
   {
      return super.getText().trim();
   }
   
   /**
    * Notifies all listeners that have registered interest for
    * notification on this event type. The event instance is lazily
    * created using the parameters passed into the fire method. The
    * listener list is processed in a last-to-first manner.
    *
    * @param reason
    *    The reason the input was invalid
    * @param badEntry
    *    The text causing the bad entry.
    */
   void fireInvalidEntryListeners(InvalidatedReason reason, String badEntry)
   {
      // Guaranteed to return a non-null array
      Object[] listeners = listenerList.getListenerList();

      // Process the listeners last to first, notifying
      // those that are interested in this event
      for (int i = listeners.length - 2; i >= 0; i -= 2)
      {
         if (listeners[i] == InvalidEntryListener.class)
         {
            ((InvalidEntryListener) listeners[i + 1]).invalidEntryDetected(this, reason, badEntry);
         }
      }
   }
   
   /** 
    * Focus Listener implementation that will select all the text
    * in the field if the "select all on focus" property is true. 
    *  
    * @param e
    *    The focus event which will be for this text field.
    */
   @Override
   public void focusGained(FocusEvent e)
   {
      if (this.selectAllOnFocus)
      {
         this.selectAll();
      }
      else
      {
         this.setCaretPosition(this.getText().length());
      }
   }
   
   /** 
    * Focus Listener implementation that will trim the text 
    * in the field when focus is lost from the field.  
    *  
    * @param e
    *    The focus event which will be for this text field.
    */
   @Override
   public void focusLost(FocusEvent e)
   {
      this.setText(this.getText().trim());
   }
}
