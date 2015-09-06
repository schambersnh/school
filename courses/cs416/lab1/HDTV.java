/* 
 * HDTV.java:
 *   
 * Displays a representation of a HDTV based on colors given
 * at construction time.  
 * 
 * @author Dan Bergeron
 * Modified by Stephen Chambers 1/27/2011 for cs416 Lab1
 * 
 */

import java.awt.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics2D;

public class HDTV implements AShape
{ 
   //---------------------- instance variables ------------------------------
   private int xLoc, yLoc;     // the location of the HDTV

   // HDTV component objects
   private ARectangle body;
   private ARectangle bezel;
   private AEllipse   onButton;
   private ALine      volSlider;
   private ALine      volLevel;
   
   // HDTV display variables
   private int bodyWidth = 80, bodyLength = 40;
   private int bodyX = 0, bodyY = 0;
   private Color bodyColor;  
   
   private int bezelWidth  = bodyWidth - 8;
   private int bezelLength = bodyLength - 16;
   private int bezelX = 4, bezelY = 4;
   
   private int onWidth = 5, onLength = 5;
   private int onX = bodyWidth / 2, onY = bodyLength / 2 + 14;
   
   private int volSliderX = onX - 30, volSliderY = onY + 2;
   private int volSliderLen = 20;
   
   private int volLevelX = volSliderX + 10, volLevelY = volSliderY - 2;
   private int volLevelLen = 6;
        
   //------------------------- constructor -------------------------------
   /**
    * constructor takes main body color as its argument.
    */
   public HDTV( int x, int y, Color theBodyColor ) 
   {
      xLoc = x; yLoc = y;    // default location
      bodyColor = theBodyColor;      
      makeScene();
      setLocation( x, y );
   }
   
   // ------------------- HDTV( int, int ) --------------------------------
   /**
    * This constructor includes an explicit location and default color (BLUE)
     */
   public HDTV( int x, int y )
   {   
       this( x, y, Color.BLUE );
   }
   
   // ---------------------- HDTV( Color ) -------------------------------
   /** 
    * This constructor includes explicit color and default location ( 0,0 )
    */
   public HDTV( Color c )
   {   
       this( 0, 0, c );
   }

   //--------------------- makeScene() ----------------------------------
   /**
    * create the graphical objects for the scene
    */
   private void makeScene()
   {
      body = new ARectangle( bodyColor);
      body.setSize( bodyWidth, bodyLength);
      
      bezel = new ARectangle( Color.BLACK );
      bezel.setSize( bezelWidth, bezelLength );
      bezel.setColor( Color.LIGHT_GRAY );
      
      onButton = new AEllipse( Color.BLACK );
      onButton.setSize( onWidth, onLength );
      
      volSlider = new ALine();
      volSlider.setColor( Color.BLACK );
      volSlider.setPoints( volSliderX, volSliderY,
                           volSliderX + volSliderLen, volSliderY );
      volSlider.setThickness( 2 );
      
      volLevel = new ALine();
      volLevel.setColor( Color.magenta );
      volLevel.setPoints( volLevelX, volLevelY,
                           volLevelX, volLevelY + volLevelLen );
      volLevel.setThickness( 2 );
   }
   //-------------------- setLocation( Point ) --------------------------
   /**
    * move the HDTV to the specified Point location.
    */
   public void setLocation( Point p ) 
   {
      setLocation( p.x, p.y );
   }
   
   //------------------ setLocation( int, int ) --------------------------
   /**
    * move the HDTV to the specified x,y location.
    */
   public void setLocation( int x, int y ) 
   {
      xLoc = x; yLoc = y;
      body.setLocation( x + bodyX, y + bodyY );
      bezel.setLocation( x + bezelX, y + bezelY );
      onButton.setLocation( x + onX, y + onY );
      volSlider.setLocation( x + volSliderX, y + volSliderY );
      volLevel.setLocation( x + volLevelX, y + volLevelY );
   }
   //----------------------- display ------------------------------------
   /** 
    * call display methods for all components
    */
   public void display( Graphics2D brush2D )
   {
     body.display( brush2D );
     bezel.display( brush2D );
     onButton.display( brush2D );
     volSlider.display( brush2D );
     volLevel.display( brush2D );
   }
   /**/
}
