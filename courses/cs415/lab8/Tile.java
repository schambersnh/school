/**
 * Tile.java - 
 *    A Tile extends the wheels Rectangle class, so it has all the 
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

public class Tile extends Rectangle
{
   //------------------------ instance variables --------------------------
  
   //------------------------- Constructors -------------------------------
   /**
    * Position tile at x,y; tile color is gray; tize is size 25x25 
    */
   public Tile( int x, int y )
   {
      setLocation(x,y);
      setColor(Color.WHITE);
      setFrameColor(Color.GRAY);
      setSize(25,25);
   }
   
   //------------------------- mousePressed ------------------------------
   /**
    * make border black on mouse down event
    */
   public void mousePressed( MouseEvent e )
   {
     setFrameColor(Color.BLACK);
   }
   
   //-------------------------- mouseReleased ----------------------------
   /**
    * make border gray on mouse up event
    */
   public void mouseReleased( MouseEvent e )
   {
      setFrameColor(Color.GRAY);
   }
   
   //--------------------------- main -----------------------------------
   /*
    * Unit test method for Tile class
    */
   public static void main( String args[] )
   {
     Frame f = new Frame();
     Tile tile1 = new Tile(50, 50);
     Tile tile2 = new Tile(150, 150);
   }
} 
