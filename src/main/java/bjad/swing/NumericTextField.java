package bjad.swing;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Text field for swing applications that will
 * allow for users to enter only Integer values
 * into the field. 
 *
 * @author 
 *  Ben Dougall
 */
public class NumericTextField extends AbstractRestrictiveTextField
{
   private static final long serialVersionUID = 548220705280538015L;
   
   /**
    * The document object used to control the values the user can
    * enter into the field. 
    */
   NumericFieldDocument numDoc;
   
   /**
    * Custom constructor that defines the minimum and maximum 
    * parameters for the field.
    * 
    * @param allowDecimals
    *    Flag to allow decimals in the field 
    * @param allowNegatives 
    *    Flag to allow negatives in the field.
    * @param maximumValue
    *    The maximum value allowed within the text field.
    */
   private NumericTextField(boolean allowDecimals, boolean allowNegatives, BigDecimal maximumValue)
   {      
      numDoc = new NumericFieldDocument(
            maximumValue, 
            this, 
            allowDecimals,
            allowNegatives);
      
      addCaretListener(null);
      setDocument(numDoc);
   }
   
   /** 
    * Applies new number of decimal place limit, or removes
    * if the value is less than 0 
    * 
    * @param decimalPlaces
    *    Maximum number of decimal places, or remove the 
    *    limit with a value less than 1.
    */   
   public void setMaximumDecimalPlaces(int decimalPlaces)
   {
      numDoc.setNumberOfDecimalPlaces(decimalPlaces);
   }
   
   /**
    * Sets the value for the field to display
    * 
    * @param val
    *    The value to display in the field.
    */
   public void setValue(Number val)
   {
      super.setText(val.toString());
   }
   
   /**
    * Gathers the value from the text field, or null
    * if the text is an invalid number.
    * 
    * @return
    *    the value from the text field, or null
    *    if the text is an invalid number.
    */
   public BigDecimal getDecimalValue()
   {
      return numDoc.verifyContents(getTextContent());
   }
   
   /**
    * Gathers the value from the text field, or null
    * if the text is an invalid number.
    * 
    * @return
    *    the value from the text field, or null
    *    if the text is an invalid number.
    */
   public BigInteger getIntegerValue()
   {
      BigDecimal val = getDecimalValue();
      if (val != null)
      {
         return val.toBigInteger();
      }
      return null;         
   }
   
   /**
    * Throws an exception right away, as you should use the 
    * setValue method to apply values to the field.
    * 
    * @see javax.swing.text.JTextComponent#setText(java.lang.String)
    */
   @Override
   public void setText(String text) throws IllegalArgumentException
   {
      throw new IllegalArgumentException("Please use setValue method to apply values to the field");
   }
   
   /**
    * Throws an exception right away, as you should use the 
    * getIntegerValue or getDecimalValue method to the field content.
    * 
    * @see javax.swing.text.JTextComponent#setText(java.lang.String)
    */
   public String getText()
   {
      throw new IllegalArgumentException("Please use getIntegerValue or getDecimalValue method to get the value from the field.");
   }
   /**
    * Creates a new integer text field which will be customized
    * to allow negative values and not have a maximum limit 
    * on the field.
    * 
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newIntegerFieldNoLimits()
   {
      return newIntegerField(true, null);
   }
   
   /**
    * Creates a new integer text field which will be customized
    * to allow negative values and not have a maximum limit 
    * on the field.
    * 
    * @param maximumValue
    *    The maximum value allowed for the field. Null for no limit.   
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newIntegerFieldWithLimit(Number maximumValue)
   {
      return newIntegerField(true, maximumValue);
   }
   
   /**
    * Creates a new integer text field which will be customized
    * to disallow negative values and not have a maximum limit 
    * on the field.
    * 
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newIntegerFieldNoNegatives()
   {
      return newIntegerField(false, null);
   }
   
   /**
    * Creates a new integer text field which will be customized
    * to either allow or disallow negative values and set a 
    * maximum value for the field.
    * 
    * @param allowNegatives
    *    True to allow negative values
    * @param maximumValue
    *    The maximum value allowed for the field. Null for no limit.
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newIntegerField(boolean allowNegatives, Number maximumValue)
   {      
      BigDecimal maxVal = maximumValue == null ? null : new BigDecimal(maximumValue.toString());
      return new NumericTextField(false, allowNegatives, maxVal);
   }
   
   /**
    * Creates a new decimal text field which will be customized
    * to allow negative values and not have a maximum limit 
    * on the field.
    * 
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newDecimalFieldNoLimits()
   {
      return newDecimalField(true, null);
   }
   
   /**
    * Creates a new decimal text field which will be customized
    * to allow negative values and not have a maximum limit 
    * on the field.
    * 
    * @param maximumValue
    *    The maximum value allowed for the field. Null for no limit.   
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newDecimalFieldWithLimit(Number maximumValue)
   {
      return newDecimalField(true, maximumValue);
   }
   
   /**
    * Creates a new decimal text field which will be customized
    * to disallow negative values and not have a maximum limit 
    * on the field.
    * 
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newDecimalFieldNoNegatives()
   {
      return newDecimalField(false, null);
   }
   
   /**
    * Creates a numeric decimal field that has no upper limit, and 
    * a decimal place limit of 2.
    * 
    * @return
    *    The constructed and configured numeric text field.
    */
   public static NumericTextField newMoneyField()
   {
      return newMoneyField(null);
   }
   
   /**
    * Creates a numeric decimal field with an upper limit, and 
    * a decimal place limit of 2.
    * 
    * @param maximumValue
    *    The upper limit for the text field.
    * @return
    *    The constructed and configured numeric text field.
    */
   public static NumericTextField newMoneyField(Number maximumValue)
   {
      NumericTextField field = newDecimalField(false, maximumValue);
      field.setMaximumDecimalPlaces(2);
      
      return field;
   }
   /**
    * Creates a new decimal text field which will be customized
    * to either allow or disallow negative values and set a 
    * maximum value for the field.
    * 
    * @param allowNegatives
    *    True to allow negative values
    * @param maximumValue
    *    The maximum value allowed for the field. Null for no limit.
    * @return
    *    The constructed numeric text field with the customized settings.
    */
   public static NumericTextField newDecimalField(boolean allowNegatives, Number maximumValue)
   {      
      BigDecimal maxVal = maximumValue == null ? null : new BigDecimal(maximumValue.toString());
      return new NumericTextField(true, allowNegatives, maxVal);
   }
}
