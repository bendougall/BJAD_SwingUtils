package bjad.swing;

import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 * <p>
 * Wrapped label control that will wrap text
 * as needed but look like a JLabel.
 * </p>
 * <p>
 * <b>Note</b>: Due to using a JTextArea as 
 * the base of the class, html formatting is 
 * not available for these "labels".
 * </p>
 *
 * @author 
 *   Ben Dougall
 */
public class WrappedLabel extends JTextArea
{
   private static final long serialVersionUID = -3438081053300107388L;

   /**
    * Default constructor making a text-less wrapped label.
    */
   public WrappedLabel()
   {
      this("");
   }

   /**
    * Constructor setting the text to display in the 
    * wrapped label. 
    * 
    * @param text
    *    The text to show in the Wrapped Label.
    */
   public WrappedLabel(String text)
   {
      super(text);
    
      // Turn on wrapping by default.
      this.setWrapStyleWord(true);
      this.setLineWrap(true);
      
      // Make the text area super class act like
      // like a label by making it transparent,
      // not editable, and not focusable.
      this.setOpaque(false);
      this.setEditable(false);
      this.setFocusable(false);
      
      // Make the text area super class look
      // like a label by stealing all the 
      // label properties from the UIManager
      this.setBackground(UIManager.getColor("Label.background"));
      this.setFont(UIManager.getFont("Label.font"));
      this.setBorder(UIManager.getBorder("Label.border"));
   }
}
