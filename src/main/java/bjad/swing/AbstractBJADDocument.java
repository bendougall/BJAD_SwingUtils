package bjad.swing;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import bjad.swing.InvalidKeyEntryListener.InvalidatedReason;

/**
 * Abstract document class for all the sub document
 * implementations to inherit from. 
 *
 *
 * @author 
 *   Ben Dougall
 */
public class AbstractBJADDocument extends PlainDocument
{
   private static final long serialVersionUID = -8223066353506548022L;
   
   protected AbstractRestrictiveTextField owningField;
   
   /**
    * Constructor, setting the field that owns the document. 
    * @param owningField
    *    The owning field for the document.
    */
   public AbstractBJADDocument(AbstractRestrictiveTextField owningField)
   {
      this.owningField = owningField;
   }
   
   /**
    * Gets the full text content from the owning field and the 
    * and the text that about to be entered into the field. 
    * 
    * @param offs 
    *    the starting offset >= 0
    * @param str 
    *    the string to insert; does nothing with null/empty strings
    * @param a 
    *    the attributes for the inserted content
    * @return
    *    The full text of the field if the text being entered is 
    *    acceptable based on the document's rules.  
    * @throws BadLocationException
    *    Any exceptions for the document framework will be thrown
    */
   public String getFullText(int offs, String str, AttributeSet a) throws BadLocationException
   {
      return getText(0, offs) + str + getText(offs, getLength()-offs);
   }
   
   /**
    * Fires the invalid key event detected listener on the owning field.
    * @param keyEventBlockedReason
    *    The reason the key event was invalid.
    * @param message
    *    The message associated to the key event being blocked.
    */
   protected void fireListenersOnOwningField(InvalidatedReason keyEventBlockedReason, String message)
   {
      owningField.fireInvalidEntryListeners(keyEventBlockedReason, message);
   }
}
