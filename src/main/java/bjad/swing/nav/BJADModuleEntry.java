package bjad.swing.nav;

import javax.swing.Icon;

/**
 * Bean defining the applications that are contained 
 * within a BJADNavModule, including the name to 
 * display, an icon to display if desired, the ordinal
 * of the entry within the module, and the 
 * AbstractBJADNavPanel to display.
 *
 * @author 
 *   Ben Dougall
 */
public class BJADModuleEntry implements Comparable<BJADModuleEntry>
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
    * The Panel to display when the entry is selected.
    */
   protected AbstractBJADNavPanel navPanel = null;
   
   
   /**
    * Returns the value of the BJADModuleEntry instance's 
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
    * Sets the value of the BJADModuleEntry instance's 
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
    * Returns the value of the BJADModuleEntry instance's 
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
    * Sets the value of the BJADModuleEntry instance's 
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
    * Returns the value of the BJADModuleEntry instance's 
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
    * Sets the value of the BJADModuleEntry instance's 
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
    * Returns the value of the BJADModuleEntry instance's 
    * navPanel property.
    *
    * @return 
    *   The value of navPanel
    */
   public AbstractBJADNavPanel getNavPanel()
   {
      return this.navPanel;
   }

   /**
    * Sets the value of the BJADModuleEntry instance's 
    * navPanel property.
    *
    * @param navPanel 
    *   The value to set within the instance's 
    *   navPanel property
    */
   public void setNavPanel(AbstractBJADNavPanel navPanel)
   {
      this.navPanel = navPanel;
   }

   /**
    * Implementation of the Comparable interface, returning the 
    * compare result between the ordinal, or if the ordinal is
    * the same, comparsion of the display name for alphabetical
    * order. 
    * 
    * @param o
    *    The BJADModuleEntry to compare against.
    */
   @Override
   public int compareTo(BJADModuleEntry o)
   {
      int result = Integer.compare(this.ordinial, o.ordinial);
      if (result == 0)
      {
         result = this.displayName.compareToIgnoreCase(o.displayName);
      }
      return result;
   }
}
