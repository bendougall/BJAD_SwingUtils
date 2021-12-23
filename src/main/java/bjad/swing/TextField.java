package bjad.swing;

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
    * The maximum length of the text that can be entered into
    * the field. -1 means no set limit. 
    */
   protected int maxLength = -1;
   
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
      super();
      
      doc = new AllowableCharacterDocument(this);
      addCaretListener(null);
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
   
   /**
    * Returns the value of the AbstractRestrictiveTextField instance's 
    * maxLength property.
    *
    * @return 
    *   The value of maxLength
    */
   public int getMaxLength()
   {
      return doc.maxLength;
   }

   /**
    * 
    */
   /**
    * Sets the value of the AbstractRestrictiveTextField instance's 
    * maxLength property.
    *
    * @param maxLength 
    *   The value to set within the instance's 
    *   maxLength property
    */
   public void setMaxLength(int maxLength)
   {
      doc.maxLength = maxLength;
   }
}

