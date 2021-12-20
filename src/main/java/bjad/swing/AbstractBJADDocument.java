package bjad.swing;

import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;

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
    * Constructor, setting the field that document is 
    * owned by.
    * 
    * @param owningField
    *    The text field owning the document.
    */
   public AbstractBJADDocument(AbstractRestrictiveTextField owningField)
   {
      if (owningField == null)
      {
         throw new IllegalArgumentException("Owning Field cannot be null.");
      }
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
    */
   public String getFullText(int offs, String str, AttributeSet a) 
   {
      // Start if the text that is in the field already
      StringBuilder sb = new StringBuilder(owningField.getTextContent());
      // Insert the new text in the position its about to be added to the field.
      sb.insert(offs, str);
      
      return sb.toString();
   }
}
