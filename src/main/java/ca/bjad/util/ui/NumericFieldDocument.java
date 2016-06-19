package ca.bjad.util.ui;

import java.math.BigDecimal;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import ca.bjad.util.ui.listener.InvalidEntryListener.InvalidatedReason;

/**
 * Numeric value document that will allow the integer 
 * and decimal text fields to filter out invalid
 * input from the user.
 *
 * @author 
 *  Ben Dougall
 */
class NumericFieldDocument extends PlainDocument
{
   private static final long serialVersionUID = -3409138035297440726L;
   
   private BigDecimal maximumValue;
   private AbstractRestrictiveTextField owningField;

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
    * @param maximumValue
    *    The maximum value allowed within the field.
    * @param owningField
    *    The field that owns this document implementation.
    * @param allowDecimals
    *    Flag to allow or disallow decimal values in the field.
    * @param allowNegatives
    *    Flag to allow or disallow negative values in the field.
    */
   public NumericFieldDocument(BigDecimal maximumValue, AbstractRestrictiveTextField owningField, boolean allowDecimals, boolean allowNegatives)
   {
      super();
      this.maximumValue = maximumValue;
      this.owningField = owningField;
      this.allowDecimals = allowDecimals;
      this.allowNegatives = allowNegatives;
   }

   private BigDecimal checkForDefaultValues(String newTextValue)
   {
      BigDecimal val = null;
      // If the negative sign is the only text in the field, 
      // treat it as minus one.
      if ("-".equals(newTextValue))         
      {
         if (!allowNegatives)
         {
            owningField.fireInvalidEntryListeners(InvalidatedReason.NEGATIVE_VALUE, newTextValue);
         }
         val = new BigDecimal("-1");
      }
      // if a decimal point is the only thing in the 
      // text field, treat the value as .1
      else if (newTextValue.endsWith("."))
      {
         if (allowDecimals)
         {            
            val = new BigDecimal(newTextValue + "1");
         }
         else
         {
            owningField.fireInvalidEntryListeners(InvalidatedReason.NON_INTEGER, newTextValue);
         }
      }
      
      return val;
   }
   
   private BigDecimal verifyRangeInformation(BigDecimal valToCheck)
   {
      BigDecimal retValue = valToCheck;
          
      if (valToCheck != null)
      {
         if (!allowNegatives && BigDecimal.ZERO.compareTo(valToCheck) == 1)
         {
            retValue = null;
            owningField.fireInvalidEntryListeners(InvalidatedReason.NEGATIVE_VALUE, valToCheck.toPlainString());
         }
         
         if (maximumValue != null && maximumValue.compareTo(valToCheck) == -1)
         {
            retValue = null;
            owningField.fireInvalidEntryListeners(InvalidatedReason.MAXIMUM_VALUE_PASSED, valToCheck.toPlainString());
         }
         
         if (allowDecimals && numberOfDecimalPlaces > 0)
         {
            String str = valToCheck.toPlainString();            
            int decimalPosition = str.indexOf('.') + 1;
            if (decimalPosition != 0 && !str.endsWith("."))
            {
               String decimalPortion = str.substring(decimalPosition);
               System.out.println(decimalPortion);
               
               if (decimalPortion.length() > numberOfDecimalPlaces)
               {
                  retValue = null;
                  owningField.fireInvalidEntryListeners(InvalidatedReason.TOO_MANY_DECIMALS, str);
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
            owningField.fireInvalidEntryListeners(InvalidatedReason.NON_NUMERIC, newTextValue);
         }
         if (!allowDecimals) 
         {
            try
            {
               val.toBigIntegerExact();
            }
            catch (Exception ex)
            {
               owningField.fireInvalidEntryListeners(InvalidatedReason.NON_INTEGER, newTextValue);
            }
         }
      }
      
      return verifyRangeInformation(val);
   }
   
   @Override
   public void insertString(int offs, String str, AttributeSet a) throws BadLocationException 
   {
      StringBuilder sb = new StringBuilder(owningField.getTextContent());
      sb.insert(offs, str);            
      String newTextValue = sb.toString();
      
      // Empty string, pass it up to the super class to place in the 
      // field.
      if (newTextValue.isEmpty())
      {
         super.insertString(offs, str, a);
      }
      
      BigDecimal val = verifyContents(newTextValue);
      if (val != null)
      {
         super.insertString(offs, str, a);
      }
   }
}
