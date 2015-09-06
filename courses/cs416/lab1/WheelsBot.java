/** 
 * WheelsBot.java: A simple 2D image of a robot using wheels
 *  
 */

import wheelsunh.users.*;
import java.awt.Color;

public class WheelsBot
{ 
   //---------------- instance variables ------------------------------
   Ellipse    body;
   Rectangle  wheel1;
   Rectangle  wheel2;
   Rectangle  caster;
   Rectangle  head;
   Rectangle  sensor;
   Line       antenna;
   int        x, y;
   Color      bodyColor;
   
   // ---------------------- WheelsBot() ----------------------------------
   /** The constructor creates all the robot parts, and sets a default
     * location and body color (BLUE).
     */
   public WheelsBot()
   {   
      wheel1 = new Rectangle();   
      wheel1.setSize( 20, 30 );
      wheel1.setFrameColor( java.awt.Color.BLACK );
      wheel1.setFillColor( java.awt.Color.BLACK );
      
      wheel2 = new Rectangle();     
      wheel2.setSize( 20, 30 );
      wheel2.setFillColor( java.awt.Color.BLACK );
      wheel2.setFrameColor( java.awt.Color.BLACK );
      
      caster = new Rectangle();    
      caster.setSize( 16, 20 );
      caster.setFillColor( java.awt.Color.BLACK );
      caster.setFrameColor( java.awt.Color.BLACK );
      
      body = new Ellipse(); 
      body.setSize( 60, 60 );
      body.setFrameColor( java.awt.Color.BLACK );
      body.setFillColor( java.awt.Color.BLUE );
      
      head = new Rectangle();   
      head.setSize( 50, 5);
      head.setFillColor( java.awt.Color.GREEN );
      head.setFrameColor( java.awt.Color.BLACK );
      
      sensor = new Rectangle();  
      sensor.setSize( 10, 10 );
      sensor.setFillColor( java.awt.Color.RED );
      sensor.setFrameColor( java.awt.Color.BLACK );
      
      antenna = new Line( x + 40, y + 30 , x + 40, y + 20);  
      antenna.setColor( java.awt.Color.BLUE);
      
      setLocation( 0, 0 );
   }
   
   // ------------------- WheelsBot( int, int ) --------------------------------
   /**
    * This constructor includes an explicit location and default color (BLUE)
     */
   public WheelsBot ( int x, int y )
   {   
       this();
       setLocation( x, y );
       setColor( Color.BLUE );
   }
   
   // ---------------------- WheelsBot( Color ) -------------------------------
   /** 
    * This constructor includes explicit color and default location ( 0,0 )
    */
   public WheelsBot ( Color c )
   {   
       this();
       setLocation( 0, 0 );
       setColor( c );
   }
   
   //--------------------- setColor( Color ) --------------------------------- 
   /**
    * set the body color to the parameter and default colors for all other parts
    */
   public void setColor( Color c )
   {
      bodyColor = c;
      body.setFillColor( c );
   }
      
   //------------------- setLocation( int, int ) --------------------------- 
   /**
    * set the location of the robot to the paramters.
    */
   public void setLocation( int x, int y )
   {
      this.x = x;
      this.y = y;
      
      wheel1.setLocation( x, y + 60 );
      wheel2.setLocation( x + 60, y + 60 );
      caster.setLocation( x + 32, y );
      body.setLocation( x + 10, y + 15 );
      head.setLocation( x + 15, y + 40 );
      sensor.setLocation( x + 35, y + 30 );
      antenna.setPoints( x + 40, y + 30 , x + 40, y - 20);
   }
   
   //--------------------- int getXLocation() ---------------------------------
   /**
    * return the x location of the robot.
    */
   public int getXLocation()
   {
      return x;       
   }
   
   //--------------------- int getYLocation() ---------------------------------
   /**
    * return the y location of the robot.
    */
   public int getYLocation()
   {
      return y;    
   }
   
   //--------------------- int getColor() ---------------------------------
   /**
    * return the body color of the robot.
    */
   public Color getColor()
   {
      return bodyColor;    
   }

   //------------------------  main -------------------------------------
   public static void main( String[] args )
   {
      new Frame();
      WheelsBot myWheelsBot = new WheelsBot();
      myWheelsBot.setColor( Color.ORANGE );
      myWheelsBot.setLocation( 200, 400 );
   }
}

