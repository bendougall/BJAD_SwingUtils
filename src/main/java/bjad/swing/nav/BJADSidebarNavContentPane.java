package bjad.swing.nav;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import bjad.swing.LinkButton;

/**
 * The main content pane to use if using the BJAD 
 * Nav Framework to have a sidebar of the modules
 * and their entries for the user to select from.
 *
 * @author 
 *   Ben Dougall
 */
public class BJADSidebarNavContentPane extends JPanel implements ActionListener
{
   private static final long serialVersionUID = -7096129837581113056L;

   protected List<BJADNavModule> modules; 
   protected BJADModuleEntry moduleEntryInFocus = null;
   protected JPanel panelShowing = null;
   protected JPanel currentModulePanelShowing = null;
   
   /**
    * Constructor, setting the list of modules that will
    * be displayed within the sidebar of the overall content pane.
    * 
    * @param modules
    *    The modules to display in the sidebar of the content pane.
    */
   public BJADSidebarNavContentPane(List<BJADNavModule> modules)
   {
      super(new BorderLayout(), true);
      if (modules == null)
      {
         throw new IllegalArgumentException("Modules passed to " + this.getClass().getSimpleName() + " cannot be null");
      }
      // Set the module list for the sidebar to the sorted list of modules passed.
      this.modules = BJADNavModule.sortModules(modules);
      
      this.add(createSidebar(), BorderLayout.WEST);
      this.add(createDefaultMainPanel(), BorderLayout.CENTER);
   }
   
   /**
    * Creates the sidebar panel for the content pane by adding
    * in the modules and their entries and wiring them 
    * up to display when selected. 
    * 
    * @return
    *    The constructed sidebar panel.
    */
   private JPanel createSidebar()
   {
      JPanel sidebarPanel = new JPanel(new BorderLayout(), true);      
      
      // Add in the navigation title label to the top of the 
      // sidebar panel.
      addInNavigationTitle(sidebarPanel);
      
      // Add in each of the modules to the sidebar
      JPanel moduleListingPanel = new JPanel(true);
      moduleListingPanel.setLayout(new BoxLayout(moduleListingPanel, BoxLayout.Y_AXIS));
      int moduleIndex = 0;
      for (BJADNavModule module : this.modules)
      {         
         // Create the panel with all the module options in it. 
         // If this is the first module, keep it's options visible
         // to the user, other hide it. 
         final JPanel moduleOptions = createModuleOptionsPanel(module);
         if (moduleIndex == 0)
         {
            currentModulePanelShowing = moduleOptions;
         }
         else
         {
            moduleOptions.setVisible(false);
         }
         moduleIndex++;         

         // Add in the module's parent link button so the user 
         // can make the module options visible for each module
         // one at a time.
         LinkButtonWithNavModule moduleTitle = new LinkButtonWithNavModule(module, moduleOptions);
         moduleTitle.setFocusable(false);
         moduleListingPanel.add(moduleTitle);
         
         // Add a listener to the module title text so it
         // will display it's modules options and hide the 
         // previously selected options.
         moduleTitle.addActionListener(this);
         
         // Add to the sidebar as a whole. 
         moduleListingPanel.add(moduleOptions);
      }
      
      // Add the completed module area to the sidebar.
      sidebarPanel.add(moduleListingPanel, BorderLayout.CENTER);
      
      // Set the size of the sidebar panel so its padded.
      sidebarPanel.setPreferredSize(new Dimension(150, 800));
      
      sidebarPanel.setBorder(new LineBorder(Color.darkGray));
      
      return sidebarPanel;
   }
   
   private void addInNavigationTitle(JPanel sidebarPanel)
   {
      JPanel navigationPanel = new JPanel(true);
      navigationPanel.setBackground(Color.darkGray);      
      
      JLabel navigationLabel = new JLabel("Navigation");
      navigationLabel.setFont(navigationLabel.getFont().deriveFont(16.0f));
      navigationLabel.setForeground(Color.white);
      navigationLabel.setHorizontalAlignment(SwingConstants.LEFT);
      
      navigationPanel.add(navigationLabel);
      navigationPanel.setPreferredSize(new Dimension(150, 30));
      
      sidebarPanel.add(navigationPanel, BorderLayout.NORTH);
   }
   
   private JPanel createModuleOptionsPanel(BJADNavModule module)
   {
      JPanel moduleOptions = new JPanel(true);
      moduleOptions.setLayout(new BoxLayout(moduleOptions, BoxLayout.Y_AXIS));
      moduleOptions.setBorder(new EmptyBorder(0, 8, 0, 0));
      for (BJADModuleEntry entry : module.getEntries())
      {
         LinkButtonWithModuleEntry entryLink = new LinkButtonWithModuleEntry(entry); 
         
         if (entry.getNavPanel() != null)
         {
            entryLink.addActionListener(this);
         }
         
         moduleOptions.add(entryLink);
      }
      return moduleOptions;
   }
   
   private JPanel createDefaultMainPanel()
   {
      JPanel defaultPane = new JPanel(new BorderLayout(), true);
      JLabel defaultLabel = new JLabel("Please select an option from the sidebar");
      defaultLabel.setHorizontalAlignment(SwingConstants.CENTER);
      defaultPane.add(defaultLabel, BorderLayout.CENTER);
      defaultPane.setBorder(new LineBorder(Color.BLACK, 2));
      
      // Mark this pane as the one showing in the content pane.
      panelShowing = defaultPane;
      
      return defaultPane;
   }

   @Override
   public void actionPerformed(ActionEvent e)
   {
      if (e.getSource() instanceof LinkButtonWithNavModule)
      {
         if (currentModulePanelShowing != null)
         {
            currentModulePanelShowing.setVisible(false);
         }
         
         currentModulePanelShowing = ((LinkButtonWithNavModule)e.getSource()).getEntryOptionsPanel();
         currentModulePanelShowing.setVisible(true);
      }
      else if (e.getSource() instanceof LinkButtonWithModuleEntry)
      {
         switchContentDisplay(((LinkButtonWithModuleEntry)e.getSource()).getModuleEntry());
      }      
   }
   
   private void switchContentDisplay(BJADModuleEntry entry)
   {
      if (moduleEntryInFocus != null)
      {
         if (moduleEntryInFocus.getNavPanel().canPanelClose())
         {
            moduleEntryInFocus.getNavPanel().onPanelClosed();
         }
         else
         {
            return;
         }
      }
      
      // Hide the current display and then remove from the panel.
      this.panelShowing.setVisible(false);
      this.remove(panelShowing);
      
      JPanel contentPanel = new JPanel(new BorderLayout(), true);
      
      JPanel navigationPanel = new JPanel(true);
      navigationPanel.setBackground(Color.darkGray);  
      
      JLabel navigationLabel = new JLabel(entry.getNavPanel().getPanelTitle());
      navigationLabel.setFont(navigationLabel.getFont().deriveFont(16.0f));
      navigationLabel.setForeground(Color.white);
      navigationLabel.setHorizontalAlignment(SwingConstants.LEFT);
      navigationPanel.add(navigationLabel);                  
      navigationPanel.setPreferredSize(new Dimension(1000, 30));
      
      AbstractBJADNavPanel navPane = entry.getNavPanel();
      
      contentPanel.add(navigationPanel, BorderLayout.NORTH);
      contentPanel.add(navPane, BorderLayout.CENTER);                  
      contentPanel.setBorder(new LineBorder(Color.darkGray));
      
      // Add some padding to the panel being shown.
      navPane.setBorder(new EmptyBorder(3, 3, 3, 3));
      
      // Replace the main panel with the newly selected one.
      panelShowing = contentPanel;
      this.add(panelShowing, BorderLayout.CENTER);
      panelShowing.setVisible(true);
      
      navPane.onPanelDisplay();
      
      // With components removed and added, invalidate the 
      // parent panel so its forced to re-draw everything.
      this.invalidate();
      this.repaint();
      
      if (navPane.getComponentForDefaultFocus() != null)
      {
         navPane.getComponentForDefaultFocus().requestFocusInWindow();
      }
      moduleEntryInFocus = entry;
   }
}

class LinkButtonWithNavModule extends LinkButton
{
   private static final long serialVersionUID = 6303110187316684688L;
   protected BJADNavModule navModule = null;
   protected JPanel entryOptionsPanel = null;
   
   /**
    * Constructor setting mav module for the 
    * link to represent. 
    * 
    * @param navModule
    *    The module entry associated to the 
    *    link button to display when option 
    *    is selected.
    * @param entryOptionsPanel
    *    The panel containing the module's various
    *    options for the user to select.   
    * 
    */
   public LinkButtonWithNavModule(BJADNavModule navModule, JPanel entryOptionsPanel)
   {
      super(navModule.getDisplayName(), navModule.getIcon());
      this.navModule = navModule;
      this.entryOptionsPanel = entryOptionsPanel;
      
      // Don't bold the text as the header for the module would be bolded.
      this.setFont(this.getFont().deriveFont(Font.BOLD));
      // Not a tabbing stop.
      this.setFocusable(false);
   }

   /**
    * Returns the value of the LinkButtonWithNavModule instance's 
    * navModule property.
    *
    * @return 
    *   The value of navModule
    */
   public BJADNavModule getNavModule()
   {
      return this.navModule;
   }
   
   /**
    * Sets the value of the LinkButtonWithNavModule instance's 
    * navModule property.
    *
    * @param navModule 
    *   The value to set within the instance's 
    *   navModule property
    */
   public void setNavModule(BJADNavModule navModule)
   {
      this.navModule = navModule;
   }

   /**
    * Returns the value of the LinkButtonWithNavModule instance's 
    * entryOptionsPanel property.
    *
    * @return 
    *   The value of entryOptionsPanel
    */
   public JPanel getEntryOptionsPanel()
   {
      return this.entryOptionsPanel;
   }

   /**
    * Sets the value of the LinkButtonWithNavModule instance's 
    * entryOptionsPanel property.
    *
    * @param entryOptionsPanel 
    *   The value to set within the instance's 
    *   entryOptionsPanel property
    */
   public void setEntryOptionsPanel(JPanel entryOptionsPanel)
   {
      this.entryOptionsPanel = entryOptionsPanel;
   }
}

/**
 * Wrapper for the link button class that will 
 * wrap a panel to display with the button so 
 * the action listener handling will have access
 * to the panel to display.
 *
 * @author 
 *    Ben Dougall
 */
class LinkButtonWithModuleEntry extends LinkButton
{
   private static final long serialVersionUID = 6303110187316684629L;
   protected BJADModuleEntry moduleEntry = null;
   
   /**
    * Constructor setting module entry for the 
    * link to represent. 
    * 
    * @param moduleEntry
    *    The module entry associated to the 
    *    link button to display when option 
    *    is selected.
    */
   public LinkButtonWithModuleEntry(BJADModuleEntry moduleEntry)
   {
      super(moduleEntry.getDisplayName(), moduleEntry.getIcon());
      this.moduleEntry = moduleEntry;
      
      // Don't bold the text as the header for the module would be bolded.
      this.setFont(this.getFont().deriveFont(Font.PLAIN));
      // Not a tabbing stop.
      this.setFocusable(false);
   }

   /**
    * Returns the value of the LinkButtonWithPanel instance's 
    * moduleEntry property.
    *
    * @return 
    *   The value of moduleEntry
    */
   public BJADModuleEntry getModuleEntry()
   {
      return this.moduleEntry;
   }

   /**
    * Sets the value of the LinkButtonWithPanel instance's 
    * moduleEntry property.
    *
    * @param moduleEntry 
    *   The value to set within the instance's 
    *   moduleEntry property
    */
   public void setModuleEntry(BJADModuleEntry moduleEntry)
   {
      this.moduleEntry = moduleEntry;
   }
}