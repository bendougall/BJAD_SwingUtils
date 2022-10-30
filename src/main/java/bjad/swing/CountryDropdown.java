package bjad.swing;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.function.Consumer;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import bjad.swing.beans.CountryBean;
import bjad.swing.beans.ICountryDisplay;

/**
 * Dropdown used to display countries to the user. 
 *
 * @author 
 *   Ben Dougall
 */
public class CountryDropdown extends JComboBox<CountryBean>
{
   private static final long serialVersionUID = -6227001906185492195L;
   protected ICountryDisplay displayFormatter = ICountryDisplay.createEnglishDisplaySafeImpl();
   
   /**
    * Default constructor, creating an empty dropdown.
    */
   public CountryDropdown()
   {
      super();
      setEditable(true);
   }

   /**
    * Constructor setting the model to use for the dropdown
    * @param aModel
    *    The model the dropdown will use to show items.
    */
   public CountryDropdown(ComboBoxModel<CountryBean> aModel)
   {
      super(aModel);
      setEditable(true);
   }

   /**
    * Constructor, setting the array items to display.
    * @param countries
    *    the countries to display.
    */
   public CountryDropdown(CountryBean[] countries)
   {
      super(countries);
      setEditable(true);
   }

   /**
    * @param countries
    *    The collection of countries to add. 
    */
   public CountryDropdown(Collection<CountryBean> countries)
   {
      this();
      if (countries != null)
      {
         Iterator<CountryBean> iter = countries.iterator();
         iter.forEachRemaining(new Consumer<CountryBean>()
         {
            @Override
            public void accept(CountryBean t)
            {
               addItem(t);               
            }
         });
      }
   }

   /**
    * Returns the selected country bean from the dropdown if 
    * a selection is made, or null if no selection is found.
    * 
    * @return
    *    The selected country bean, or null if no selection has 
    *    been made. 
    */
   public CountryBean getSelectedCountry()
   {
      int index = getSelectedIndex();
      if (index > -1)
      {
         return getItemAt(index);
      }
      return null;
   }
   
   /**
    * Returns the selected countries from the dropdowm, or null
    * if no selections are found. 
    * 
    * @return
    *    The array of selected countries.
    */
   public CountryBean[] getSelectedCountries()
   {
      Object selectedObject = super.getSelectedItem();
      if (selectedObject != null && selectedObject instanceof CountryBean)
      {
         CountryBean result[] = new CountryBean[1];
         result[0] = (CountryBean)selectedObject;
         return result;
      }
      else 
      {
         return new CountryBean[0];
      }
   }
   
   /**
    * Sets the selected country in the dropdown if its found.
    * @param country
    *    The country to select.
    */
   public void setSelectedCountry(CountryBean country)
   {
      super.setSelectedItem(country);
   }   
   
   /**
    * Sets the selected country in the dropdown by finding
    * the Country that matches the text passed (see
    * bjad.swing.CountryDropdown.findCountryByText(String))
    * 
    * @see 
    *    #findCountryByText(String)
    * @param text
    *    The text to find in the country beans in the dropdown. 
    *    Selection will be set to null if the text cannot be
    *    matched.
    */
   public void setSelectedCountryByText(String text)
   {
      super.setSelectedIndex(findCountryByText(text));
   }
   
   /**
    * Sets the display formatter for the dropdown so the 
    * countries display can be customed to the needs of the user. 
    * 
    * @param formatter
    *    The formatter to use. If null, no change will be made.
    */
   public void setDisplayFormatter(ICountryDisplay formatter)
   {
      if (formatter != null)
      {
         this.displayFormatter = formatter;
         int selectedIndex = getSelectedIndex();
         setSelectedIndex(-1);
         
         for (int index = 0; index != getItemCount(); ++index)
         {
            getItemAt(index).setDisplayFormatter(formatter);
         }
         repaint();
         setSelectedIndex(selectedIndex);
      }
   }
   
   /**
    * Find the index for the country that matches the text
    * passed by comparing the fields in the following order:
    * <ol>
    * <li>The result of toString()</li>
    * <li>The ISO 3166-1 alpha 3 code</li>
    * <li>The ISO 3166-1 alpha 2 code</li>
    * <li>The english text</li>
    * <li>The non english text values</li>
    * </ol>
    * If the text cannot be matched to a country, -1 
    * will be returned.
    * 
    * @param text
    *    The text to match against the countries in the 
    *    dropdown.
    * @return
    *    The index of the first matching country (See 
    *    description for search order), or -1 if the text 
    *    could not be matched.
    */
   public int findCountryByText(String text)
   {
      int retVal = -1;
      if (text != null && !text.trim().isEmpty())
      {
         for (int index = 0; index != getItemCount(); ++index)
         {
            CountryBean bean = getItemAt(index);
            String val = bean.toString();
            if (val != null && val.equalsIgnoreCase(text))
            {
               retVal = index;
               break;
            }            
            val = bean.getAlpha3Code();
            if (val != null && val.equalsIgnoreCase(text))
            {
               retVal = index;
               break;
            }
            val = bean.getAlpha2Code();
            if (val != null && val.equalsIgnoreCase(text))
            {
               retVal = index;
               break;
            }
            val = bean.getEnglishText();
            if (val != null && val.equalsIgnoreCase(text))
            {
               retVal = index;
               break;
            }
            if (bean.getOtherLanguageText() != null)
            {
               for (String nonEnglishText : bean.getOtherLanguageText().values())
               {
                  if (nonEnglishText.equalsIgnoreCase(text))
                  {
                     retVal = index;
                     break;
                  }
               }
            }
         }
      }
      return retVal;
   }
   
   /**
    * Returns the selected objects in the dropdown, or null
    * if nothing is selected. 
    * 
    * @deprecated
    *    Should use getSelectedCountries() instead.
    * @return
    *    The selected objects in the dropdown.
    */
   @Override
   @Deprecated
   public Object[] getSelectedObjects()
   {
      return super.getSelectedObjects();
   }
   /**
    * Returns the selected object in the dropdown, or null
    * if nothing is selected. 
    * 
    * @deprecated
    *    Should use getSelectedCountry() instead.
    * @return
    *    The selected object in the dropdown.
    */
   @Override
   @Deprecated
   public Object getSelectedItem()
   {
      return super.getSelectedItem();
   }
   
   /**
    * Creates an instance of the dropdown with the list of ISO 3166-1 country
    * information stored within the library added as the items.
    * 
    * @return
    *    The dropdown with the pre-packaged countries added to it.
    */
   public static CountryDropdown createDropdownFromPackagedISO3166List()
   {
      List<CountryBean> countries = new LinkedList<>();
      try (Scanner s = new Scanner(ClassLoader.getSystemResourceAsStream("Country_ISO-3166-1_tabbed.txt")) )
      {
         while (s.hasNextLine())
         {
            String line = s.nextLine();
            StringTokenizer st = new StringTokenizer(line, "\t");
                        
            String eng = null;
            String a2c = null;
            String a3c = null;
            String num = null;
            
            eng = st.nextToken();
            if (st.hasMoreTokens())
            {
               a2c = st.nextToken();
            }
            if (st.hasMoreTokens())
            {
               a3c = st.nextToken();
            }
            if (st.hasMoreTokens())
            {
               num = st.nextToken();
            }
            
            if (num != null)
            {
               CountryBean bean = new CountryBean();
               bean.setEnglishText(eng);
               bean.setAlpha2Code(a2c);
               bean.setAlpha3Code(a3c);
               try
               {
                  bean.setNumericCode(Integer.parseInt(num));
               }
               catch (Exception ex)
               {
                  ; // do nothing, num is the least important field
               }
               countries.add(bean);
            }
         }
      }
      catch (Exception ex)
      {
         ; // do nothing
      }
      
      return new CountryDropdown(countries);
   }
}
