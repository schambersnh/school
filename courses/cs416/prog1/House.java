/** 
 * House.java: A simple 2D image of a house
 * Modified from AWTBot by Stephen Chambers on 2/2/2011
 */

import java.awt.*;
import java.awt.Color;
import java.util.*;

public class House implements AShape
{ 
   //---------------- instance variables ------------------------------
private AEllipse doorknob;
private ALine pane, pane2, pane3, pane4;
private ARoundRectangle door;
private ARectangle body, window, window2;
private ArrayList<AShape> myShapes;
private Color bodyColor = Color.RED;
private int x, y;


   
   
   // ---------------------- House() ----------------------------------
   /** The constructor creates all the house components
     */
   public House()
   {   
     //Create House components
      
     myShapes = new ArrayList<AShape>();
     
     body = new ARectangle();
     body.setSize(100, 100);
     body.setColor(Color.BLUE);
     myShapes.add(body);

     window = new ARectangle();
     window.setSize( 20 , 20 );
     window.setColor( Color.YELLOW );
     myShapes.add( window );
     
     window2 = new ARectangle();
     window2.setSize(20, 20);
     window2.setColor( Color.YELLOW );
     myShapes.add( window2 );
     
     pane = new ALine();
     pane.setThickness( 3 );
     pane.setColor( Color.BLACK );
     myShapes.add( pane );
     
     pane2 = new ALine();
     pane2.setThickness( 3 );
     pane2.setColor( Color.BLACK );
     myShapes.add( pane2 );
     
     
     pane3 = new ALine();
     pane3.setThickness( 3 );
     pane3.setColor( Color.BLACK );
     myShapes.add( pane3 );
     
     pane4 = new ALine();
     pane4.setThickness( 3 );
     pane4.setColor( Color.BLACK );
     myShapes.add( pane4 );
     
     door = new ARoundRectangle();
     door.setColor( Color.BLACK );
     door.setArcSize( 30 , 30 );
     door.setSize( 30, 60 );
     myShapes.add( door );
     
     doorknob = new AEllipse();
     doorknob.setColor( Color.YELLOW );
     doorknob.setSize( 10, 10 );
     myShapes.add( doorknob );
     
     
     setLocation( 0, 0 );

   }
   
   // ------------------- House( int, int ) --------------------------------
   /**
    * This constructor includes an explicit location 
     */
   public House ( int x, int y )
   {   
       this();
       setLocation( x, y );
       setColor( bodyColor );

   }
   
   // ---------------------- House( Color ) -------------------------------
   /** 
    * This constructor includes explicit color and default location ( 0,0 )
    */
   public House( Color c )
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
      body.setColor( c );
   }
      
   //------------------- setLocation( int, int ) --------------------------- 
   /**
    * set the location of the house to the paramters.
    */
   public void setLocation( int x, int y )
   {
      this.x = x;
      this.y = y;
      body.setLocation(x, y);
      window.setLocation(x + 10, y + 10 );
      window2.setLocation( x + 60, y + 10 );
      pane.setPoints( x + 20 , y + 10 , x + 20 , y + 30 );
      pane2.setPoints( x + 10 , y + 20 , x + 30 , y + 20 );
      pane3.setPoints( x + 70 , y + 10 , x + 70 , y + 30 );
      pane4.setPoints( x + 60 , y + 20 , x + 80, y + 20 );
      door.setLocation( x + 30 , y + 40 );
      doorknob.setLocation(x + 35 , y + 70);

   }
   
   //--------------------- int getXLocation() ---------------------------------
   /**
    * return the x location of the house.
    */
   public int getXLocation()
   {
      return x;       
   }
   
   //--------------------- int getYLocation() ---------------------------------
   /**
    * return the y location of the house.
    */
   public int getYLocation()
   {
      return y;    
   }
   
   //--------------------- int getColor() ---------------------------------
   /**
    * return the body color of the house.
    */
   public Color getColor()
   {
      return bodyColor;    
   }
   
   //----------------------- display( Graphics2D ) -------------------------
   /**
    * display - calls draw and fill awt methods (this is an rdb method).
    */
   public void display( java.awt.Graphics2D brush2D )
   {
     for(int i = 0; i < myShapes.size(); i++)
     {
       myShapes.get( i ).display( brush2D );
     }
      
   }
} //End of Class