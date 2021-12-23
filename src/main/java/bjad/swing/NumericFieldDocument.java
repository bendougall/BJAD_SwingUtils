package bjad.swing;

import java.math.BigDecimal;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import bjad.swing.InvalidKeyEntryListener.InvalidatedReason;

/**
 * Numeric value document that will allow the integer 
 * and decimal text fields to filter out invalid
 * input from the user.
 *
 * @author 
 *  Ben Dougall
 */
class NumericFieldDocument extends AbstractBJADDocument
{
   private static final long serialVersionUID = -3409138035297440726L;
   
   private BigDecimal maximumValue;

   private boolean allowNegatives;
   private boolean allowDecimals;
   
   private int numberOfDecimalPlaces = -1;
   
   /** 
    * Applies new number of decimal place limit, or removes
    * if the value is less than 0 
    * 
    * @param decimalPlaces
    *    Maximum number of decimal places, or remove the 
    *    limit with a value less than 1.
    */
   public void setNumberOfDecimalPlaces(int decimalPlaces)
   {
      this.numberOfDecimalPlaces = decimalPlaces;
   }
   
   /**
    * Constructor, lays out the parameters for the document's filters.
    * 
    * @param owningField
    *    The field the document is owned by.
    * @param maximumValue
    *    The maximum value allowed within the field.
    * @param allowDecimals
    *    Flag to allow or disallow decimal values in the field.
    * @param allowNegatives
    *    Flag to allow or disallow negative values in the field.
    */
   public NumericFieldDocument(AbstractRestrictiveTextField owningField, BigDecimal maximumValue, boolean allowDecimals, boolean allowNegatives)
   {
      super(owningField);
      this.maximumValue = maximumValue;
      this.allowDecimals = allowDecimals;
      this.allowNegatives = allowNegatives;
   }

   /**
    * Verifys the content of the string passed to check that 
    * the input is valid based on the allow negatives and
    * allow decimals settings. 
    * 
    * @param newTextValue
    *    The text to verify
    * @return
    *    The value of the text with the modifications needed for 
    *    verification, or null if bad input is found.
    */
   private BigDecimal checkForDefaultValues(String newTextValue)
   {
      BigDecimal val = null;
      // If the negative sign is the only text in the field, 
      // treat it as minus one.
      if ("-".equals(newTextValue))         
      {
         if (!allowNegatives)
         {
            fireListenersOnOwningField(InvalidatedReason.NEGATIVE_VALUE, "Negative value not allowed.");
         }
         val = new BigDecimal("-1");
      }
      // if a decimal point is the only thing in the 
      // text field, treat the value as .1
      else if (newTextValue.endsWith("."))
      {
         if (allowDecimals)
         {         
            try
            {
               val = new BigDecimal(newTextValue + "1");
            }
            catch (NumberFormatException ex)
            {
               fireListenersOnOwningField(InvalidatedReason.NON_NUMERIC, "Invalid number.");
            }
         }
         else
         {
            fireListenersOnOwningField(InvalidatedReason.NON_INTEGER, "Decimal value not allowed");
         }
      }
      
      return val;
   }
   
   /**
    * Verifying the value in the field once all the invalid character 
    * input is found. 
    * 
    * @param valToCheck
    *    The value to check
    * @return
    *    The value within the field if the input is valid with the 
    *    rules in the field, or null if the input breaks the 
    *    validation rules. 
    */
   private BigDecimal verifyRangeInformation(BigDecimal valToCheck)
   {
      BigDecimal retValue = valToCheck;
          
      if (valToCheck != null)
      {
         // Invalid input due to negatives not being allowed.
         if (!allowNegatives && BigDecimal.ZERO.compareTo(valToCheck) == 1)
         {
            retValue = null;
            fireListenersOnOwningField(InvalidatedReason.NEGATIVE_VALUE, "Negative value not allowed.");
         }
         // Invalid input due to the amount being more than the max amount setting. 
         if (maximumValue != null && maximumValue.compareTo(valToCheck) == -1)
         {
            retValue = null;
            fireListenersOnOwningField(InvalidatedReason.MAXIMUM_VALUE_PASSED, "Maximum amount exceeded.");
         }
         
         // Check for invalid input due to the number of decimal places
         // of the value being more than the max allowed in the field. 
         if (allowDecimals && numberOfDecimalPlaces > 0)
         {
            String str = valToCheck.toPlainString();            
            int decimalPosition = str.indexOf('.') + 1;
            if (decimalPosition != 0 && !str.endsWith("."))
            {
               String decimalPortion = str.substring(decimalPosition);
               
               if (decimalPortion.length() > numberOfDecimalPlaces)
               {
                  retValue = null;
                  fireListenersOnOwningField(InvalidatedReason.TOO_MANY_DECIMALS, "Decimal Precision exceeded.");
               }
            }
         }
      }
      
      return retValue;
   }
   
   /**
    * Validates the content for the field's potential 
    * new value.
    * 
    * @param newTextValue
    *    The value to verify.
    * @return
    *    The value as a number, or null if the 
    *    text value is invalid.
    */
   public BigDecimal verifyContents(String newTextValue)
   {
      BigDecimal val = checkForDefaultValues(newTextValue);
      if (val == null)
      {
         try
         {
            val = new BigDecimal(newTextValue);
         }
         catch (Exception ex)
         {
            fireListenersOnOwningField(InvalidatedReason.NON_NUMERIC, "Non numeric value entered.");
         }
         if (!allowDecimals) 
         {
            try
            {
               val.toBigIntegerExact();
            }
            catch (Exception ex)
            {
               fireListenersOnOwningField(InvalidatedReason.NON_INTEGER, "Non-integer value entered.");
            }
         }
      }
      
      return verifyRangeInformation(val);
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
      String newTextValue = getFullText(offs, str, a);
      
      // Empty string, pass it up to the super class to place in the field.
      if (newTextValue.isEmpty())
      {
         super.insertString(offs, str, a);
      }
      
      // If the new string contains a decimal point but decimals
      // are not allowed, prevent the text entry.
      if (!allowDecimals && str.contains("."))
      {
         fireListenersOnOwningField(InvalidatedReason.NON_INTEGER, "Non-integer value entered.");
      }
      else
      {
         BigDecimal val = verifyContents(newTextValue);
         if (val != null)
         {
            super.insertString(offs, str, a);
         }
      }
   }
}
