/** 
 * AWTPanel.java: Swing panel for AWT draw assignment.
 * 
 * This is a framework for the main display window for an 
 * awt application. 
 *      
 * @author rdb
 * 01/10/08 
 * 01/22/11 rdb modified for start code for P1, Spring 2011
 * Modified by Stephen Chambers for Program 1, 2/2/2011
 */

import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

public class AWTPanel extends JPanel
{ 
   //-------------------- instance variables ----------------------
   //
   // declare here (among other things) the variables that reference
   //   AWT display objects (ellipses, rectangles, lines, etc.)
   //private House     robot1, robot2;
  ARectangle myRect;
  ALine myLine;
  AEllipse myEllipse;
  ARoundRectangle myRoundRect, myRoundRect2;
  ArrayList<AShape> myShapes;
   
   //------------- Constructor ---------------------------------
   /**
    * Create awt objects to be displayed.
    */
   public AWTPanel( String [] args ) 
   {
      // Create the objects you want to display using awt graphical
      // objects. Use the wheels-like "wrapper" classes, ARectangle, AEllipse, 
      // and ALine. The ARectangle and AEllipse classes are minor variations 
      // of SmartRectangle and SmartEllipse from the Sanders and van Dam text.
      //
      // References to the objects you create need to be saved in an
      //   ArrayList or Vector of AShape objects.
      //
     myShapes = new ArrayList<AShape>();
     
     myRect = new ARectangle( 300 , 300 );
     myRect.setSize( 100 , 100 );
     myRect.setColor(Color.BLUE);
     myShapes.add(myRect);
     
     myLine = new ALine( 300 , 300 , 300 , 250 );
     myLine.setThickness( 5 );
     myShapes.add(myLine);
     
     myRoundRect = new ARoundRectangle( 304 , 250 , 50 , 20 , 0 , 0 );
     myRoundRect.setColor( Color.WHITE );
     myShapes.add( myRoundRect );
     
     myEllipse = new AEllipse( 323 , 254 );
     myEllipse.setSize( 10 , 10 );
     myEllipse.setColor( Color.RED );
     myShapes.add( myEllipse );
     
     myRoundRect2 = new ARoundRectangle( 320 , 320 , 50 , 50 , 30 , 30 );
     myRoundRect2.setColor( Color.GREEN );
     myShapes.add( myRoundRect2) ;
     
     House myHouse = new House( 400 , 100 );
     myShapes.add( myHouse );
     
     House myHouse2 = new House( 250, 100 );
     myHouse2.setColor( Color.GREEN );
     myShapes.add( myHouse2 );
     
     
     

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
      Graphics2D newBrush = (Graphics2D) aBrush;  // coerce arg to Graphics2D
      
      for(int i = 0; i < myShapes.size(); i++)
      {
        myShapes.get( i ).display( newBrush );
      }
      //////////////////////////////////////////////////////////////
      // invoke display( newBrush ) for all A-objects in scene
      //////////////////////////////////////////////////////////////
   }
   //-------------------- main --------------------------------------------
   /**
    * Invoke AWTApp.main
    */
   public static void main( String[] args )
   {
      AWTApp.main( args );
   }
 }
