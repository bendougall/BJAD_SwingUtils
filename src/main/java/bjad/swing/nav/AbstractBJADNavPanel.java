package bjad.swing.nav;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Panel for all the navigation application options to
 * extend so they can be used in the BJAD framework.
 *
 * @author 
 *   Ben Dougall
 */
public abstract class AbstractBJADNavPanel extends JPanel
{
   private static final long serialVersionUID = -9068792570074621596L;

   /** 
    * Provides the title to show when the panel is in use.
    * @return
    *    The title for the application within the panel
    */
   public abstract String getPanelTitle(); 
   
   /**
    * Provides the component to focus on when the panel is 
    * displayed within the application.
    * @return
    *    The component to place focus in when the panel is 
    *    displayed to the user. Null is allowed, but 
    *    frowned upon.
    */
   public abstract JComponent getComponentForDefaultFocus();
   
   /**
    * Contains the logic to execute when the panel is 
    * displayed within the application.
    */
   public abstract void onPanelDisplay();
   
   /**
    * Defines if the application should be closed/switched
    * out within the application framework. 
    * @return
    *    True if the switch can happen, to cancel it. 
    */
   public abstract boolean canPanelClose();
   
   /**
    * Contains the logic to execute when the panel is 
    * closed/switched out within the application.
    */
   public abstract void onPanelClosed();
}
