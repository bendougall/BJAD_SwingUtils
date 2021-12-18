package bjad.swing;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import bjad.swing.listener.InvalidEntryListener.InvalidatedReason;

/**
 * Standard text field with the option to restrict
 * characters if need be. 
 *
 * @author 
 *   Ben Dougall
 */
public class TextField extends AbstractRestrictiveTextField
{
   private static final long serialVersionUID = 5000442719460052411L; 
   
   /** 
    * The document that will apply the edit checks to 
    * make sure only allowable characters are entered
    * into the field if characters are being filtered. 
    */
   protected AllowableCharacterDocument doc; 
   
   /** 
    * Default constructor, setting the document to a blank
    * Allowable character document. 
    */
   public TextField()
   {
      this("");
   }
   
   /**
    * Constructor, setting the initial text to show in the 
    * field. 
    * 
    * @param text
    *    The text to show in the field.
    */
   public TextField(String text)
   {
      doc = new AllowableCharacterDocument(this);
      setDocument(doc);
      
      this.setText(text);
   }
   
   /**
    * Adds an allowable character to the text field's
    * allowable character set. 
    * 
    * @param c
    *    The character to allow.
    */
   public void addAllowableCharacter(Character c)
   {
      doc.getAllowableCharacters().add(c);
   }
   
   /**
    * Adds the characters in the string provided to the 
    * allowable character set for the field. 
    * @param str
    *    The string containing the characters to add
    *    to the allowable character set for the field.
    */
   public void addAllowableCharactersFromString(String str)
   {
      if (str != null)
      {
         char[] charactersToAdd = str.toCharArray();
         for (Character c : charactersToAdd)
         {
            addAllowableCharacter(c);
         }
      }
   }
   
   /**
    * Removes an allowable character from the text field's
    * allowable character set. 
    * 
    * @param c
    *    The character to no longer allow.
    */
   public void removeAllowableCharacter(Character c)
   {
      doc.getAllowableCharacters().remove(c);
   }
   
   /**
    * Removes the characters in the string provided from the 
    * allowable character set for the field. 
    * @param str
    *    The string containing the characters to remove
    *    from the allowable character set for the field.
    */
   public void removeAllowableCharactersFromString(String str)
   {
      if (str != null)
      {
         char[] charactersToAdd = str.toCharArray();
         for (Character c : charactersToAdd)
         {
            removeAllowableCharacter(c);
         }
      }
   }
}

/**
 * Document that contains a list of characters to validate the 
 * insert into a text field if. If the allowable characters are 
 * null or empty, any input is allowed. 
 *
 * @author 
 *   Ben Dougall
 */
class AllowableCharacterDocument extends PlainDocument
{
   private static final long serialVersionUID = -2895129417418051824L;
   
   /** 
    * The field owning this document which will have 
    * its InvalidEntryListeners fired if an invalid
    * character is entered into the field. 
    */
   protected AbstractRestrictiveTextField owningField; 
   
   /** 
    * LinkedHashSet of characters allowed to be in the 
    * field. Leave blank if any character is to be allowed.  
    */
   protected Set<Character> allowedCharacters = new LinkedHashSet<Character>();
   
   /**
    * Constructor, setting the field the document is owned
    * by so we can fire invalid entry events if needed. 
    * 
    * @param owningField
    *    The field that will be using this document to 
    *    filter any invalid input. 
    */
   public AllowableCharacterDocument(AbstractRestrictiveTextField owningField)
   {
      this.owningField = owningField;
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
               StringBuilder sb = new StringBuilder(owningField.getTextContent());
               sb.insert(offs, str);            
               String newTextValue = sb.toString();
               
               owningField.fireInvalidEntryListeners(InvalidatedReason.CHARACTER_NOT_ALLOWED, "\'" + c+ "\' within " + newTextValue);
               return;
            }
         }
      }
      // Validation complete, insert the string into the text field.
      super.insertString(offs, str, a);
   }
}
