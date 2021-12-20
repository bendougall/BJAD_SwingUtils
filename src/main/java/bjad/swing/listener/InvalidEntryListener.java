package bjad.swing.listener;

import java.util.EventListener;

import bjad.swing.AbstractRestrictiveTextField;

/**
 * Listener interface which will be triggered if invalid
 * text or values are entered into one of the customzied
 * UI elements contained in the library.
 *
 * @author 
 *  Ben Dougall
 */
public interface InvalidEntryListener extends EventListener
{
   /**
    * The various reasons text may be invalid 
    * within the text field.
    *
    * @author 
    *  Ben Dougall
    */
   public enum InvalidatedReason
   {
      /** Non-numeric text in number fields */
      NON_NUMERIC,
      /** Non-integer text in number fields */
      NON_INTEGER,
      /** Negative value entered into a non-negative number field */
      NEGATIVE_VALUE,
      /** Value entered surpasses the defined maximum limit of the field */
      MAXIMUM_VALUE_PASSED,
      /** Characters that are marked invalid */
      INVALID_CHARACTER,
      /** Too many decimal places in the field */
      TOO_MANY_DECIMALS,
      /** Maximum length of field is exceeded */
      MAX_LENGTH_EXCEEDED
   }
   
   /**
    * The method that will be fired when invalid entry is detected within 
    * a restrictive text field implementation.
    * 
    * @param field
    *    The field the invalid text was entered into.
    * @param reason
    *    The reason the text was invalid.
    * @param input
    *    The invalid text.
    */
   public void invalidEntryDetected(AbstractRestrictiveTextField field, InvalidatedReason reason, String input);
}
