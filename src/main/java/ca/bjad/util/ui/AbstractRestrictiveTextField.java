package ca.bjad.util.ui;

import javax.swing.JTextField;
import ca.bjad.util.ui.listener.InvalidEntryListener;
import ca.bjad.util.ui.listener.InvalidEntryListener.InvalidatedReason;

/**
 * Base class for restrictive text fields, such as
 * the integer and decimal text fields contained
 * within this package.
 *
 * @author 
 *  Ben Dougall
 */
public abstract class AbstractRestrictiveTextField extends JTextField
{
   private static final long serialVersionUID = 1558940477904874871L;

   /**
    * Retrieves the actual text content for the 
    * field, without subclasses affecting the 
    * results.
    * 
    * @return
    *    The results of JTextField.getText();
    */
   String getTextContent()
   {
      return super.getText();
   }
   
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
}
