/**
 * MinedTile.java - 
 *    A MinedTile extends the wheels Rectangle class, so it has all the 
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

public class MinedTile extends Tile
{
   //------------------------ instance variables --------------------------
  
   //------------------------- Constructors -------------------------------
   /**
    * Position tile at x,y; tile color is gray; tize is size 25x25 
    */
   public MinedTile( int x, int y )
   {
     super(x,y);
   }
   
   //------------------------- mousePressed ------------------------------
   /**
    * make border black on mouse down event
    */
   public void mouseClicked( MouseEvent e )
   {
     setFillColor(Color.RED);
    
   }
   //--------------------------- main -----------------------------------
   /*
    * Unit test method for MinedTile class
    */
   public static void main( String args[] )
   {
     Frame f = new Frame();
     MinedTile tile1 = new MinedTile(50, 50);
     MinedTile tile2 = new MinedTile(150, 150);
   }
} 
