package bjad.swing;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import bjad.swing.InvalidKeyEntryListener.InvalidatedReason;

/**
 * Document that contains a list of characters to validate the 
 * insert into a text field if. If the allowable characters are 
 * null or empty, any input is allowed. 
 *
 * @author 
 *   Ben Dougall
 */
class AllowableCharacterDocument extends AbstractBJADDocument
{
   private static final long serialVersionUID = -2895129417418051824L;
   
   protected int maxLength = -1;
   
   /** 
    * LinkedHashSet of characters allowed to be in the 
    * field. Leave blank if any character is to be allowed.  
    */
   protected Set<Character> allowedCharacters = new LinkedHashSet<Character>();
   
   /**
    * Constructor, setting the field the document is owned
    * by so we can fire invalid entry events if needed. 
    * 
    * @param field
    *    The owning field to fire listeners against.
    */
   public AllowableCharacterDocument(AbstractRestrictiveTextField field)
   {
      super(field);
   }

   /**
    * Returns the set of allowable characters in the field. 
    * @return
    *    The set of allowable characters in the field.
    */
   public Set<Character> getAllowableCharacters()
   {
      return allowedCharacters;
   }
   
   /**
    * Overrides the super class's insert string so it will 
    * filter out bad characters within the text field if 
    * entered by the user by typing or by copying and 
    * pasting from the clipboard.
    * 
    * @param offs 
    *    the starting offset >= 0
    * @param str 
    *    the string to insert; does nothing with null/empty strings
    * @param a 
    *    the attributes for the inserted content
    */
   @Override
   public void insertString(int offs, String str, AttributeSet a) throws BadLocationException 
   {
      // Make sure the first character of the string being added is not
      // a space if its being added to the begining of the text field.
      if (offs == 0)
      {
         StringBuilder sb = new StringBuilder(str);
         while (sb.charAt(0) == ' ' || sb.charAt(0) == '\t')
         {
            sb.deleteCharAt(0);
         }
         str = sb.toString();
      }
      
      // Check to see if the maximum length property of the field
      // is set, and if so, see if the new text with the existing
      // text will exceed that length. 
      // 
      // If so, stop the insert, unless the field was empty, in
      // which, insert the substring of the text being entered to
      // match the max length.
      if (maxLength > 0)
      {         
         if (getFullText(offs, str, a).length() > maxLength)
         {
            boolean finished = true;
            // Add the substring of the text being entered if the 
            // field has no content. 
            if (getFullText(offs, str, a).trim().isEmpty())
            {
               str = str.substring(0, maxLength);
               finished = false;
            }
            // Notify the error occurred. 
            fireListenersOnOwningField(InvalidatedReason.MAX_LENGTH_EXCEEDED, "MAx length of " + maxLength + " exceeded.");
            
            // Field had text, nothing will be done, so exit the method. 
            if (finished)
            {
               return;
            }
         }
      }
      
      // If there are restrictions on the field, check to 
      // see if the characters in the new string passed to 
      // to the function are in the restricted list.
      if (!(allowedCharacters == null || allowedCharacters.isEmpty()))
      {
         char[] newCharacters = str.toCharArray();
         for (Character c : newCharacters)
         {
            // Character in the new string not found in the list? 
            // exit the function without updating the text field.
            if (!(allowedCharacters.contains(c)))
            {               
               fireListenersOnOwningField(InvalidatedReason.INVALID_CHARACTER, "Character \'" + c + "\' not allowed in field.");
               return;
            }
         }
      }
      // Validation complete, insert the string into the text field.
      super.insertString(offs, str, a);
   }
}