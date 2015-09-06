/**
 * @Stephen Chambers
 * 9/29/2010
 * DrawButton.java
 * Super Button Class
 */
import wheelsunh.users.*;
import java.awt.Point;
import java.awt.Color;

public class DrawButton extends Ellipse
{
    //-------------------- instance variables ------------------------------
  //Creates a snake that can be accessed by all *Button classes
   protected Snake snake1;
   
    
    //---------------------- Constructor -------------------------------------
    /**
     * 
     */
    public DrawButton ( int x, int y, Snake s ) 
    {
       setColor(Color.blue);
       setSize(20, 20);
       snake1 = s;
       setLocation(x,y);
    }
    //----------------------------MousePressed event------------------------
    public void mousePressed(java.awt.event.MouseEvent e)
    {
      setColor(Color.RED);
    }
    //----------------------------MouseReleased event----------------------
    public void mouseReleased(java.awt.event.MouseEvent e)
    {
      setColor(Color.BLUE);
    }
    
    
  
    //---------------- Testing Main ---------------------------
    public static void main( String a[] )
    {
        Frame f = new Frame();
        Snake mySnake = new Snake( 100, 100 );
        DrawButton myButton = new DrawButton(20, 20, mySnake);
    }    
}