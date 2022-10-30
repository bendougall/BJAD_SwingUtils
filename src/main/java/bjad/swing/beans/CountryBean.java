package bjad.swing.beans;

import java.util.Map;
import java.util.Objects;

/**
 * Bean representing countries that can be shown in the 
 * bjad.swing.CountryDropdown control. The properties 
 * of this bean support the fields in the ISO-3166-1
 * standard, as well as a mapping of languages and text
 * to display for international support.
 *
 * @author 
 *    Ben Dougall
 */
public class CountryBean
{
   /**
    * The alpha 2 code as per the ISO-3166-1 standard.
    */
   protected String alpha2Code;
   /**
    * The alpha 3 code as per the ISD-3166-1 standard.
    */
   protected String alpha3Code; 
   /**
    * The numeric code as per the ISO-3166-1 standard.
    */
   protected int numericCode; 
   /**
    * The english text to display from the country (which 
    * can be used as a default if the language needed is 
    * missing in the otherLanguageText mapping. 
    */
   protected String englishText; 
   /**
    * Mapping of non-english languages and the text to display  
    * for the country for that langauge. English text should
    * be found in the englishText property of the bean.
    */
   protected Map<String, String> otherLanguageText;
   
   /**
    * Property used to house how the text will be displayed
    * for the country bean if toString() is called. By
    * default, the english text safe impl will be used.
    */
   protected ICountryDisplay displayFormatter = ICountryDisplay.createEnglishDisplaySafeImpl();
   
   /**
    * Returns the value of the CountryBean instance's 
    * alpha2Code property.
    *
    * @return 
    *   The value of alpha2Code
    */
   public String getAlpha2Code()
   {
      return this.alpha2Code;
   }
   /**
    * Sets the value of the CountryBean instance's 
    * alpha2Code property.
    *
    * @param alpha2Code 
    *   The value to set within the instance's 
    *   alpha2Code property
    */
   public void setAlpha2Code(String alpha2Code)
   {
      this.alpha2Code = alpha2Code != null ? alpha2Code.trim().toUpperCase() : null;
   }
   /**
    * Returns the value of the CountryBean instance's 
    * alpha3Code property.
    *
    * @return 
    *   The value of alpha3Code
    */
   public String getAlpha3Code()
   {
      return this.alpha3Code;
   }
   /**
    * Sets the value of the CountryBean instance's 
    * alpha3Code property.
    *
    * @param alpha3Code 
    *   The value to set within the instance's 
    *   alpha3Code property
    */
   public void setAlpha3Code(String alpha3Code)
   {
      this.alpha3Code = alpha3Code != null ? alpha3Code.trim().toUpperCase() : null;
   }
   /**
    * Returns the value of the CountryBean instance's 
    * numericCode property.
    *
    * @return 
    *   The value of numericCode
    */
   public int getNumericCode()
   {
      return this.numericCode;
   }
   /**
    * Sets the value of the CountryBean instance's 
    * numericCode property.
    *
    * @param numericCode 
    *   The value to set within the instance's 
    *   numericCode property
    */
   public void setNumericCode(int numericCode)
   {
      this.numericCode = numericCode;
   }
   /**
    * Returns the value of the CountryBean instance's 
    * englishText property.
    *
    * @return 
    *   The value of englishText
    */
   public String getEnglishText()
   {
      return this.englishText;
   }
   /**
    * Sets the value of the CountryBean instance's 
    * englishText property.
    *
    * @param englishText 
    *   The value to set within the instance's 
    *   englishText property
    */
   public void setEnglishText(String englishText)
   {
      this.englishText = englishText != null ? englishText.trim() : null;
   }
   /**
    * Returns the value of the CountryBean instance's 
    * otherLanguageText property.
    *
    * @return 
    *   The value of otherLanguageText
    */
   public Map<String, String> getOtherLanguageText()
   {
      return this.otherLanguageText;
   }
   /**
    * Sets the value of the CountryBean instance's 
    * otherLanguageText property.
    *
    * @param otherLanguageText 
    *   The value to set within the instance's 
    *   otherLanguageText property
    */
   public void setOtherLanguageText(Map<String, String> otherLanguageText)
   {
      this.otherLanguageText = otherLanguageText;
   }
   /**
    * Returns the value of the CountryBean instance's 
    * displayFormatter property.
    *
    * @return 
    *   The value of displayFormatter
    */
   public ICountryDisplay getDisplayFormatter()
   {
      return this.displayFormatter;
   }
   /**
    * Sets the value of the CountryBean instance's 
    * displayFormatter property.
    *
    * @param displayFormatter 
    *   The value to set within the instance's 
    *   displayFormatter property
    */
   public void setDisplayFormatter(ICountryDisplay displayFormatter)
   {
      // Only set the formatter if its an actual implementation to
      // avoid potential null pointers.
      if (displayFormatter != null)
      {
         this.displayFormatter = displayFormatter;
      }
   }
   
   /**
    * Returns the text to display in a control for the bean.
    * @return  
    *    The text for the country coming from the displayFormatter
    *    implementation. Depending on the implementation, the return
    *    value could be null.
    */
   public String toString()
   {
      return this.displayFormatter.getTextToDisplayForCountry(this);
   }
   /**
    * Returns the hash code for the object based on the ISO-3166-1 
    * standard's alpha2, alpha3, and numeric codes. 
    * 
    * @return
    *    The hash code based on the ISO-3166-1 standard fields.
    */
   @Override
   public int hashCode()
   {
      return Objects.hash(alpha2Code, alpha3Code, numericCode);
   }
   
   /**
    * Returns true if the ISO-3166-1 fields in the beans match. Those 
    * fields being the alpha2, alpha3, and numeric codes.
    * 
    * @return
    *    True if the ISO-3166-1 fields between the beans match.
    */
   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      CountryBean other = (CountryBean) obj;
      return Objects.equals(this.alpha2Code, other.alpha2Code) && Objects.equals(this.alpha3Code, other.alpha3Code)
            && this.numericCode == other.numericCode;
   }
   
}
