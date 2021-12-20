package bjad.swing;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * Clickable link button that hides the normal
 * button's background, border, and hover behaviour
 * and shows the link with an underline when the 
 * mouse is over the button.
 *
 * @author 
 *   Ben Dougall
 */
public class LinkButton extends JButton implements MouseListener
{
   private static final long serialVersionUID = -6293334168864202320L;

   /**
    * Default constructor.  
    */
   public LinkButton()
   {
      super();
      setDefaultProperties();
   }

   /**
    * Constructor setting the action for the button
    * to invoke.
    * @param a
    *    The action for the button.
    */
   public LinkButton(Action a)
   {
      super(a);
      setDefaultProperties();
   }

   /**
    * Constructor setting the icon for the 
    * button to display.
    * @param icon
    *    The icon to display in the button.
    */
   public LinkButton(Icon icon)
   {
      super(icon);
      setDefaultProperties();
   }

   /**
    * Constructor setting the text and icon to 
    * display within the button.
    * @param text
    *    The text to display in the link
    * @param icon
    *    The icon to display in the link
    */
   public LinkButton(String text, Icon icon)
   {
      super(text, icon);
      setDefaultProperties();
   }

   /**
    * Constructor setting the text to display
    * within the link button.
    * @param text
    *    The text to display.
    */
   public LinkButton(String text)
   {
      super(text);
      setDefaultProperties();
   }

   /**
    * Sets all the properties within the button 
    * to make the button appear as a text link 
    * instead of a full button.
    */
   private void setDefaultProperties()
   {
      // Remove the shading logic when the mouse is hovering the button.
      this.setRolloverEnabled(false);
      // Remove the border
      this.setBorderPainted(false);
      // Remove the extra padding from the button.
      this.setMargin(new Insets(0, 1, 0, 0));
      // Remove the filling of the button so the background is shown.
      this.setContentAreaFilled(false);
      // Align the text to the left of the control by default. 
      this.setHorizontalAlignment(SwingConstants.LEFT);
      // Add the mouse listener that will add the underline or remove it
      // when the user's mouse is above the button.
      this.addMouseListener(this);
   }

   @Override
   public void mouseClicked(MouseEvent e)
   {
      ; //unused  
   }

   @Override
   public void mousePressed(MouseEvent e)
   {
      ; //unused
   }

   @Override
   public void mouseReleased(MouseEvent e)
   {
      ; //unused
   }

   /**
    * Adds the underline to the text in the 
    * button when the mouse is over the
    * button. 
    * 
    * @param e
    *    The mouse event triggered by the 
    *    swing library when the mouse is 
    *    moved over the button.
    */
   @Override
   public void mouseEntered(MouseEvent e)
   {  
      adjustUnderline(true);
   }

   /**
    * Removes the underline to the text in the 
    * button when the mouse is moved away
    * from the button. 
    * 
    * @param e
    *    The mouse event triggered by the 
    *    swing library when the mouse is 
    *    moved away from the button.
    */
   @Override
   public void mouseExited(MouseEvent e)
   {  
      adjustUnderline(false);
   }
   
   /**
    * Adjusts the underline attribute for the font 
    * within the button. 
    * 
    * @param underlineOn
    *    True to underline the text, false to 
    *    suppress the underline.
    */
   private void adjustUnderline(boolean underlineOn)
   {
      Font buttonFont = this.getFont();
      Map<TextAttribute, Object> attributes = new HashMap<>(buttonFont.getAttributes());
      attributes.put(TextAttribute.UNDERLINE, underlineOn ? TextAttribute.UNDERLINE_ON : -1);
      this.setFont(buttonFont.deriveFont(attributes));
   }
}
