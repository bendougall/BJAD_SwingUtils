package bjad.swing.beans;

/**
 * Interface used to define how the CountryBean
 * will be shown the user in the bjad.swing.CountryDropdown
 * control.
 *
 * @author 
 *   Ben Dougall
 */
public interface ICountryDisplay
{
   /**
    * Returns the text to display in the control for country 
    * bean passed.
    * 
    * @param bean
    *    The country bean to display.
    * @return
    *    The text to display within the control.
    */
   public String getTextToDisplayForCountry(CountryBean bean);
   
   /**
    * Creates an impl of the interface that will safely return the text
    * to display for the bean passed, first looking to send back the english
    * text, but if that is missing, the alpha3code will be returned, and if that
    * is missing, the alpha2code will be returned. If all properties are missing, 
    * "Unknown" will be returned.
    *  
    * @return
    *    The impl that will return a string to display for the country
    */
   public static ICountryDisplay createEnglishDisplaySafeImpl()
   {
      return new ICountryDisplay()
      {         
         @Override
         public String getTextToDisplayForCountry(CountryBean bean)
         {
            String retVal = "Unknown";
            if (bean != null)
            {
               if (bean.getEnglishText() != null && !bean.getEnglishText().isEmpty())
               {
                  retVal = bean.getEnglishText();
               }
               else if (bean.getAlpha3Code() != null && !bean.getAlpha3Code().isEmpty())
               {
                  retVal = bean.getAlpha3Code();
               }
               else if (bean.getAlpha2Code() != null && !bean.getAlpha2Code().isEmpty())
               {
                  retVal = bean.getAlpha2Code();
               }
            }
            return retVal;
         }
      };
   }
   
   /**
    * Returns a impl that will return the english text for the 
    * country, or null if the english text is not set. 
    *  
    * @return
    *    The impl returning the english text in the bean without 
    *    caring for null checks.
    */
   public static ICountryDisplay createEnglishDisplayImpl()
   {
      return new ICountryDisplay()
      {         
         @Override
         public String getTextToDisplayForCountry(CountryBean bean)
         {
            return bean.getEnglishText();
         }
      };
   }
   
   /**
    * Returns a impl that will return the alpha 2 code for the 
    * country, or null if the code is not set. 
    *  
    * @return
    *    The impl returning the alpha 2 code in the bean without 
    *    caring for null checks.
    */
   public static ICountryDisplay createAlpha2DisplayImpl()
   {
      return new ICountryDisplay()
      {         
         @Override
         public String getTextToDisplayForCountry(CountryBean bean)
         {
            return bean.getAlpha2Code();
         }
      };
   }
   
   /**
    * Returns a impl that will return the alpha 3 code for the 
    * country, or null if the code is not set. 
    *  
    * @return
    *    The impl returning the alpha 3 code in the bean without 
    *    caring for null checks.
    */
   public static ICountryDisplay createAlpha3DisplayImpl()
   {
      return new ICountryDisplay()
      {         
         @Override
         public String getTextToDisplayForCountry(CountryBean bean)
         {
            return bean.getAlpha3Code();
         }
      };
   }
   
   /**
    * Returns a impl that will return the text for the country using 
    * the language code passed. If the text for the language passed cannot
    * be found, the result of createEnglishDisplaySafeImpl()'s implementation 
    * will be returned.
    *  
    * @param language
    *    The language code to get the text of the country for.
    * @return
    *    The impl returning the text for the language code passed, or if the 
    *    text cannot be found for that language code, the result of 
    *    createEnglishDisplaySafeImpl()'s implementation will be returned.
    */
   public static ICountryDisplay createNonEnglishDisplayImpl(final String language)
   {
      return new ICountryDisplay()
      {         
         private String languageCode = language;
         
         @Override
         public String getTextToDisplayForCountry(CountryBean bean)
         {
            String retVal = null;
            
            if (bean.getOtherLanguageText() != null)
            {
               retVal = bean.getOtherLanguageText().get(this.languageCode);
            }
            if (retVal == null)
            {
               retVal = createEnglishDisplaySafeImpl().getTextToDisplayForCountry(bean);
            }
            return retVal;
         }
      };
   }
}
