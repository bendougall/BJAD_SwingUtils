package bjad.swing;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * <p>
 * Helper used to position fields within a screen 
 * that is not using a layout manager. 
 * </p>
 * <p>
 * Used by passing a control that will have a key 
 * listener added to it, and a unbound list of controls
 * that will have its position modified by using
 * the arrow keys, and the size modified while 
 * holding CTRL key down
 * </p>
 *
 * @author 
 *   Ben Dougall
 */
public class BJADFieldPositionHelper extends KeyAdapter
{
   private ComponentWrapper[] componentsToMove = null;
   
   /**
    * Wrapper for component in the position helper
    * that contains the original position for the 
    * component. 
    *
    * @author 
    *   Ben Dougall
    */
   private class ComponentWrapper
   {
      /**
       * The component being moved. 
       */
      private Component component;
      /**
       * The original position of the component before all the moves. 
       */
      private Rectangle originalBounds; 
      
      /**
       * Constructor, setting the component and the original 
       * position of the component. 
       * 
       * @param c
       *    The component that will be held by the wrapper.
       */
      public ComponentWrapper(Component c)
      {
         this.component = c;
         this.originalBounds = component.getBounds();
      }
      
      /**
       * Provides the position string for the original
       * position of the component. 
       * 
       * @return  
       *    The string outlining the original position 
       *    of the component in the wrapper.
       */
      public String originalPositionText()
      {
         return getBoundsText(this.originalBounds);
      }
      
      /**
       * Provides the position string for the current
       * position of the component. 
       * 
       * @return  
       *    The string outlining the current position 
       *    of the component in the wrapper.
       */
      public String currentPositionText()
      {
         return getBoundsText(this.component.getBounds());
      }
      
      /**
       * Provides the string for the Rectangle object 
       * passed, which will outline the x1, y1, x2, y2,
       * width, and height of the rectangle. 
       * 
       * @param r
       *    The rectangle to create the string for.
       * @return
       *    The string with the rectangle and size information.
       */
      private String getBoundsText(Rectangle r)
      {
         return String.format("(%s, %s, %s, %s) Size: (%s, %s)", 
               r.x,
               r.y,
               r.x + r.width,
               r.y + r.height,
               r.width,
               r.height);
      }
      
      /**
       * Provides the name of the component in the wrapper, or if the 
       * field is not named, the text returned by the fields "getText()"
       * method (if has one). 
       * 
       * No Name and No Text will return "<Nameless/Textless>"
       * @return
       *    The name of the component, or the text from the component,
       *    or the default text if name and text could not be found.
       */
      private String getNameOrText()
      {
         StringBuilder sb = new StringBuilder();
         
         boolean nameOrTextFound = false;
         
         // Get the name of the field if it has a name.
         if (component.getName() != null && !component.getName().trim().isEmpty())
         {
            sb.append("Name=\"").append(component.getName()).append('\"');
            nameOrTextFound = true;
         }
         // Get the text from the field if it has a getText method. 
         else
         {
            try
            {
               String textResult = component.getClass().getMethod("getText").invoke(component).toString();
               if (textResult != null && !textResult.trim().isEmpty())
               {
                  sb.append("Text=\"").append(textResult).append('\"');
                  nameOrTextFound = true;
               }
            }
            catch (Exception ex)
            {            
            }
         }
         // No name, no text, return default text.
         if (!nameOrTextFound)
         {
            sb.append("<Nameless/Textless>");
         }
         
         return sb.toString();
      }
   }
   
   /**
    * Constructor, setting the field to add the key listener to, and the 
    * fields that will be adjusted by the arrow keys (or ctrl+arrow for size).
    * 
    * @param triggerField
    *    The field that will have the key listener that will adjust the 
    *    position and size of the fields passed
    * @param fieldsToMove
    *    The fields to re-position and/or re-size.
    */
   public BJADFieldPositionHelper(Component triggerField, Component... fieldsToMove)
   {
      if (triggerField != null)
      {
         // Create the wrappers from the components passed.
         this.componentsToMove = new ComponentWrapper[fieldsToMove.length];
         for (int i = 0; i != fieldsToMove.length; ++i)
         {
            this.componentsToMove[i] = new ComponentWrapper(fieldsToMove[i]);
         }
         // Validate the fields to move has a value. 
         if (this.componentsToMove == null || this.componentsToMove.length == 0)
         {
            System.err.println(this.getClass().getSimpleName() + " :: No Fields to move provided.");
            return;
         }
         // We have fields, add the key listener to the trigger field, and output
         // the wired fields including their original position.
         else
         {
            triggerField.addKeyListener(this);
            
            // Output the instructions and the original positions of the fields.
            System.out.println(String.format(
                  "%s: Control %s has been wired to control the positions of the fields below. " + System.lineSeparator() +
                  "While in focus the arrow keys will move the field(s) in their container, and CTRL+Arrow Keys will resize the controls.", 
                  this.getClass().getSimpleName(),
                  getControlText(new ComponentWrapper(triggerField), true)));
            for (ComponentWrapper cw : componentsToMove)
            {
               System.out.println(" - " + getControlText(cw, false));
            }
         }
      }
      else
      {
         System.err.println(this.getClass().getSimpleName() + " :: Triggering field cannot be null");
      }
   }
   /**
    * Invoked when a key has been pressed on the control
    * passed to the constructor of this class.
    * 
    * @param e
    *    The event to handle. 
    */
   @Override
   public void keyPressed(KeyEvent e) 
   {
      boolean ctrl = e.isControlDown();
      for (ComponentWrapper cw : componentsToMove)
      {
         Rectangle r = cw.component.getBounds();
         switch (e.getKeyCode())
         {
         case KeyEvent.VK_UP:
            if (ctrl)
            {
               r.height--;
            }
            else
            {
               r.y--;
            }
            break;
         case KeyEvent.VK_DOWN:
            if (ctrl)
            {
               r.height++;
            }
            else
            {
               r.y++;
            }
            break;
         case KeyEvent.VK_LEFT:
            if (ctrl)
            {
               r.width--;
            }
            else
            {
               r.x--;
            }
            break;
         case KeyEvent.VK_RIGHT:
            if (ctrl)
            {
               r.width++;
            }
            else
            {
               r.x++;
            }
            break;
         }
         cw.component.setBounds(r);
         System.out.println(getControlText(cw, true));
      }
   }
   
   /**
    * Creates a string about the control, including the original or current 
    * position of the field. 
    * 
    * @param cw 
    *    The wrapper for the component controlled by the helper.
    * @param withCurrentPosition
    *    True for the fields current position, false for the original position.
    * @return
    *    The string with the field type, name or text, and the position.
    */
   private static String getControlText(ComponentWrapper cw, boolean withCurrentPosition)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("Type=").append(cw.component.getClass().getSimpleName()).append(" :: ");
      
      sb.append(cw.getNameOrText());
      sb.append(" :: ");
      
      String boundsText = withCurrentPosition ? cw.currentPositionText() : cw.originalPositionText();
      sb.append(withCurrentPosition ? "Position=" : "Original Position=").append(boundsText);
      
      return sb.toString();
   }
}
