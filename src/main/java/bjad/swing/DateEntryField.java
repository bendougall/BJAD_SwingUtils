package bjad.swing;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.Document;

import bjad.swing.InvalidKeyEntryListener.InvalidatedReason;

/**
 * Field for entering a date value into and 
 * then show as a formatted string once 
 * focus is lost in the field.
 *
 * @author 
 *   Ben Dougall
 */
public class DateEntryField extends AbstractRestrictiveTextField
{
   private static final long serialVersionUID = -4673002871494096631L;
   
   private static final SimpleDateFormat YYYYMMDD_FORMAT = new SimpleDateFormat("yyyyMMdd");
   private static final SimpleDateFormat YYMMDD_FORMAT = new SimpleDateFormat("yyMMdd");
   
   private Document baseDoc;
   private NumericFieldDocument numDoc;
   
   /**
    * The datw formatter used to format the date entered in the
    * field to a more readable format. 
    */
   protected DateFormat displayFormat = new SimpleDateFormat("MMM d, yyyy");
   /**
    * The date entered in the field. 
    */
   protected Date enteredDate = null;
   
   /**
    * Default constructor, setting up the field without a 
    * prefilled date. 
    */
   public DateEntryField()
   {
      this(null);
   }
   
   /**
    * Constructor setting the date within the field. 
    * 
    * @param defaultDate
    *    The default to show in the field.
    */
   public DateEntryField(Date defaultDate)
   {
      setPlaceholderText("CCYYMMDD");
      baseDoc = getDocument();
      if (defaultDate != null)
      {
         enteredDate = defaultDate;
         updateDateDisplay();
      }      
      numDoc = new NumericFieldDocument(this, new BigDecimal(99_000_000L), false, false);
   }
   
   /**
    * Returns the date in the the field, or null if blank 
    * or an invalid date has been entered. 
    * 
    * @return  
    *    The date entered, which is validated if the field 
    *    currently has focus. 
    */
   public Date getEnteredDate()
   {
      // Field has focus? Trigger the validation and update
      // logic we do when focus is losted to ensure the 
      // date is properly set. 
      if (hasFocus())
      {
         onFocusLost();
      }
      return enteredDate;
   }
   
   /**
    * Sets the date value in the field
    * @param value
    *    The date to show in the field.
    */
   public void setEnteredDate(Date value)
   {
      enteredDate = value;
      updateDateDisplay();
   }
   
   /**
    * Throws an exception right away, as you should use the 
    * getEnteredDate method to get the date contained in the field.
    * 
    * @see javax.swing.text.JTextComponent#setText(java.lang.String)
    */
   @Override
   public String getText()
   {
      throw new IllegalArgumentException("Please use getEnteredDate() from the field.");
   }
   
   /**
    * Throws an exception right away, as you should use the 
    * setEnteredDate(Date) function to set the date in the field.
    */
   @Override
   public void setText(String val)
   {
      throw new IllegalArgumentException("Please use setEnteredDate(Date) to set the date in the field");
   }
   
   /**
    * Returns the date formatter used to display the date
    * once entered by the user.
    * 
    * @return
    *    The date formatter used to display the date to
    *    the user once the date is enetered and focus 
    *    is lost.
    */
   public DateFormat getDisplayFormat()
   {
      return this.displayFormat;
   }
   /**
    * Sets the format to display the date in the field with
    * once a date is entered and focus on lost within the 
    * field.
    * 
    * @param formatter
    *    The formatter to use to display the entered date 
    *    with. If null is passed, the existing formatter 
    *    will be used. 
    */
   public void setDisplayFormat(DateFormat formatter)
   {
      if (formatter != null)
      {
         displayFormat = formatter;
         if (enteredDate != null)
         {
            updateDateDisplay();
         }
      }
   }
   
   /**
    * When focus is lost on the field, the user entry 
    * will be validated. If a date is found, the entered
    * value is formatted into the readable format.
    */
   @Override
   protected void onFocusLost()
   {
      // Attempt to convert the entry into a 
      // date object. 
      try
      {
         switch (getTextContent().length())
         {
         case 0:
            enteredDate = null;
         case 6:
            enteredDate = YYMMDD_FORMAT.parse(getTextContent());
            break;
         case 8:
            enteredDate = YYYYMMDD_FORMAT.parse(getTextContent());
            break;
         default:
            fireInvalidEntryListeners(InvalidatedReason.INVALID_DATE, getTextContent() + " is an invalid date.");
            enteredDate = null;
            super.setText("");
         }
      }
      catch (ParseException ex)
      {
         fireInvalidEntryListeners(InvalidatedReason.INVALID_DATE, getTextContent() + " is an invalid date.");
         enteredDate = null;
         super.setText("");         
      }
      // Update the display in the field.
      updateDateDisplay();
   }
   
   /**
    * When focus is gained in the field, the document
    * supporting numeric entry only is applied, and 
    * the entered date is returned to the int key 
    * format.
    */
   @Override
   protected void onFocusGained()
   {
      if (enteredDate != null)
      {
         super.setText(YYYYMMDD_FORMAT.format(enteredDate));
      }
      setDocument(numDoc);
   }
   
   /**
    * Updates the field's contents by removing the 
    * character restricting document and then formatted
    * the entered date if one is found. 
    */
   private void updateDateDisplay()
   {
      setDocument(baseDoc);
      if (enteredDate != null)
      {
         super.setText(displayFormat.format(enteredDate));
      }
   }
}
