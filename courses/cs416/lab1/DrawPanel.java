/** 
 * DrawPanel.java: Swing panel for AWT draw assignment.
 * 
 * This is a skeleton for the main display window for an awt application. 
 *      
 * @author rdb
 * 01/10/08 
 * Modified by Stephen Chambers 1/27/2011 for cs416 Lab1
 */

import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

public class DrawPanel extends JPanel
{ 
   //-------------------- instance variables ----------------------
   // Instance variables to reference all the objects created in the scene.
   ArrayList<AShape> myList;
      
   //------------- Constructor ---------------------------------
   /**
    * Create awt objects to be displayed.
    */
   public DrawPanel( String [ ] args ) 
   {  
      myList = new ArrayList<AShape>();
      
      
      makeScene();
   }  
   //------------------- makeScene() ----------------------------
   /**
    * Create the wheels objects that make up the scene
    */
   private void makeScene()
   {  
      ARoundRectangle myRoundRect1 = new ARoundRectangle(30, 30, 40, 40, 100, 100);
      myList.add(myRoundRect1);
      
      //Changing Location, width, height and arc size
      ARoundRectangle myRoundRect2 = new ARoundRectangle(100, 100, 60, 60, 50, 50);
      myList.add(myRoundRect2); 
      
      //Changing Location, keeping height and width parameters from myRoundRect2
      //but changing arc size via method
      ARoundRectangle myRoundRect3 = new ARoundRectangle(300, 300, 60, 60, 50, 50);
      myRoundRect3.setArcSize(10, 10);
      myList.add(myRoundRect3);
      
     
   }
   //------------- paintComponent ---------------------------------------
   /**
    * This method is called from the Java environment when it determines
    * that the JPanel display should be updated; you need to 
    * make appropriate calls here to re-draw the graphical images on
    * the display. Each object created in the constructor above should 
    * have its "fill" and/or "draw" methods invoked with a Graphics2D 
    * object. The Graphics object passed to paintComponent will be a 
    * a Graphics2D object, so it can be coerced to that type and
    * passed along to the "display" method of the objects you created.
    * 
    * Note that the "display" method is not an awt method, but a convenience
    * method defined by the "wrapper" classes. The display method usually
    * passes the graphical objects to both the Graphics2D.fill and
    * Graphics2D.draw methods, except in the case of ALine graphical objects 
    * which cannot be "filled".
    */
   public void paintComponent( Graphics aBrush )
   {
      super.paintComponent( aBrush );
      Graphics2D brush2D = (Graphics2D) aBrush;  // coerce arg to Graphics2D
      
      for(int i = 0; i < myList.size(); i++)
      {
       myList.get( i ).display( brush2D );
      }

   
   
   
   }
   //-------------------- main --------------------------------------------
   /**
    * Invoke SwingApp.main
    */
   public static void main( String[] args )
   {
      SwingApp.main( args );
   }
}