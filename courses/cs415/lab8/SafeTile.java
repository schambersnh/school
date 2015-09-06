/**
 * SafeTile.java - 
 *    A SafeTile extends the wheels Rectangle class, so it has all the 
 *       functionality of a Rectangle.
 *    We add to the Rectangle functionality: the ability to respond to
 *       mouse press and mouse release events.
 * 
 * @Stephen Chambers
 * 9/23/10
 */
import java.awt.Color;
import java.awt.event.*;
import wheelsunh.users.*;

public class SafeTile extends Tile
{
   //------------------------ instance variables --------------------------
  
   //------------------------- Constructors -------------------------------
   /**
    * Position tile at x,y; tile color is gray; tize is size 25x25 
    */
   public SafeTile( int x, int y )
   {
     super(x,y);
   }
   
   //------------------------- mousePressed ------------------------------
   /**
    * make border black on mouse down event
    */
   public void mouseClicked( MouseEvent e )
   {
     setFillColor(Color.BLACK);
    
   }
   //--------------------------- main -----------------------------------
   /*
    * Unit test method for SafeTile class
    */
   public static void main( String args[] )
   {
     Frame f = new Frame();
     SafeTile tile1 = new SafeTile(50, 50);
     SafeTile tile2 = new SafeTile(150, 150);
   }
} 
