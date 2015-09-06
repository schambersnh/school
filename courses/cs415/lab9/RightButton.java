/**
 * @Stephen Chambers
 * 9/29/2010
 * RightButton.java
 * A SubClass of DrawButton
 * 
 */
import wheelsunh.users.*;
import java.awt.Point;
import java.awt.Color;

public class RightButton extends DrawButton
{
  //---------------------- Constructor -------------------------------------
    /**
     * Simply passes parameters to the DrawButton constructor
     */
    public RightButton ( int x, int y, Snake s ) 
    {
       super(x, y, s);
    }
    
    //----------------------------MousePressed event------------------------
    public void mousePressed(java.awt.event.MouseEvent e)
    {
      snake1.turnRight();
    }
 
    //---------------- Testing Main ---------------------------
    public static void main( String a[] )
    {
        Frame f = new Frame();
        Snake mySnake = new Snake( 100, 100 );
        DrawButton myButton = new DrawButton(20, 20, mySnake);
        LeftButton myLeftButton = new LeftButton(200, 2000, mySnake);
        RightButton myRightButton = new RightButton(300, 300, mySnake);
    }    
}