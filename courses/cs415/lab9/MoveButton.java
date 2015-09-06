/**
 * @Stephen Chambers
 * 9/29/2010
 * MoveButton.java
 * A SubClass of DrawButton
 *
 */
import wheelsunh.users.*;
import java.awt.Point;
import java.awt.Color;

public class MoveButton extends DrawButton
{
    //---------------------- Constructor -------------------------------------
    /**
     * Simply passes parameters to the DrawButton constructor
     */
    public MoveButton ( int x, int y, Snake s ) 
    {
        super(x, y, s);
    }
    
    public void mousePressed(java.awt.event.MouseEvent e)
    {
      snake1.move(5);
    }
 
    //---------------- Testing Main ---------------------------
    public static void main( String a[] )
    {
       Frame f = new Frame();
        Snake mySnake = new Snake( 100, 100 );
        DrawButton myButton = new DrawButton(20, 20, mySnake);
        MoveButton myMoveButton = new MoveButton(350, 350, mySnake);
    }    
}