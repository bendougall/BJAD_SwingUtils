package bjad.swing.nav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Icon;

/**
 * Bean defining the modules that are contained 
 * within an application using the BJAD Nav framework,
 * including the name to display, an icon to display 
 * if desired, the ordinal of the module within the list, 
 * and the List of BJADModuleEntries to display.
 *
 * @author 
 *   Ben Dougall
 */
public class BJADNavModule implements Comparable<BJADNavModule>
{
   /**
    * The display name for the application/panel to display
    */
   protected String displayName = "";
   /**
    * The ordinal for the display order within the module entry list.
    */
   protected int ordinial = -1;
   /**
    * The icon to display for the panel/application.
    */
   protected Icon icon = null;
   /**
    * The list of module entries to display within the module.
    */
   protected List<BJADModuleEntry> entries = new ArrayList<>();
   
   /**
    * Returns the value of the BJADNavModule instance's 
    * displayName property.
    *
    * @return 
    *   The value of displayName
    */
   public String getDisplayName()
   {
      return this.displayName;
   }

   /**
    * Sets the value of the BJADNavModule instance's 
    * displayName property.
    *
    * @param displayName 
    *   The value to set within the instance's 
    *   displayName property
    */
   public void setDisplayName(String displayName)
   {
      this.displayName = displayName;
   }

   /**
    * Returns the value of the BJADNavModule instance's 
    * ordinial property.
    *
    * @return 
    *   The value of ordinial
    */
   public int getOrdinial()
   {
      return this.ordinial;
   }

   /**
    * Sets the value of the BJADNavModule instance's 
    * ordinial property.
    *
    * @param ordinial 
    *   The value to set within the instance's 
    *   ordinial property
    */
   public void setOrdinial(int ordinial)
   {
      this.ordinial = ordinial;
   }

   /**
    * Returns the value of the BJADNavModule instance's 
    * icon property.
    *
    * @return 
    *   The value of icon
    */
   public Icon getIcon()
   {
      return this.icon;
   }

   /**
    * Sets the value of the BJADNavModule instance's 
    * icon property.
    *
    * @param icon 
    *   The value to set within the instance's 
    *   icon property
    */
   public void setIcon(Icon icon)
   {
      this.icon = icon;
   }

   /**
    * Returns the value of the BJADNavModule instance's 
    * entries property.
    *
    * @return 
    *   The value of entries
    */
   public List<BJADModuleEntry> getEntries()
   {
      if (this.entries == null)
      {
         this.entries = new ArrayList<>();
      }
      return this.entries;
   }

   /**
    * Sets the value of the BJADNavModule instance's 
    * entries property.
    *
    * @param entries 
    *   The value to set within the instance's 
    *   entries property
    */
   public void setEntries(List<BJADModuleEntry> entries)
   {
      this.entries = entries;
   }

   /**
    * Implementation of the Comparable interface, returning the 
    * compare result between the ordinal, or if the ordinal is
    * the same, comparsion of the display name for alphabetical
    * order. 
    * 
    * @param o
    *    The BJADNavModule to compare against.
    */
   @Override
   public int compareTo(BJADNavModule o)
   {
      int result = Integer.compare(this.ordinial, o.ordinial);
      if (result == 0)
      {
         result = this.displayName.compareToIgnoreCase(o.displayName);
      }
      return result;
   }
   
   /**
    * Sorts the modules and their entries based on the ordinals (or name 
    * if the ordinal matches) for display within the application.
    * @param modulesToSort
    *    The modules that will be displayed containing all their 
    *    entries that will be sorted as well. 
    * @return
    *    The sorted modules within their sorted entries. 
    */
   public static List<BJADNavModule> sortModules(List<BJADNavModule> modulesToSort)
   {
      Collections.sort(modulesToSort);
      for (BJADNavModule module : modulesToSort)
      {
         Collections.sort(module.getEntries());
      }
      return modulesToSort;
   }
}
