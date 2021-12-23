package bjad.swing.nav;

/**
 * Enum outlining the behaviour the the sidebar will
 * use when it comes to the module headings being 
 * clicked. 
 *
 *
 * @author 
 *   Ben Dougall
 */
public enum SidebarSectionBehaviour
{
   /**
    * One module's options will be shown at a time, 
    * defaulting in the first module showing its options
    * at startup.
    */
   ONE_SECTION_AT_A_TIME,
   /**
    * Show all the options from all the modules, allowing
    * the user to collapse the section if they click it.
    */
   ALL_SHOWN_AND_USER_CAN_COLLAPSE,
   /**
    * Always show each of the sections.
    */
   ALL_SECTIONS_ALWAYS_EXPANDED
}
