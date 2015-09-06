/** 
 * Lab5
.java:
 * 
 * 
 * 
 * @author cs415
 * 
 */

// --------------- imports ------------------------------
import wheelsunh.users.*;
import java.awt.Color;

public class Lab5
 extends Frame
{
    //---------------- instance variables ------------------------------
       int    outerX   =  0,  outerY = 0;
       int    outerSize = 80;
       Ellipse    outer, inner, inner2, inner3, centeredCircle;
    // -----------------------------------------------------------------
    /** 
     * Constructor for the Lab5
 class.
     */
    public Lab5( )
    {
      makeTarget(0, 0);  
      makeTarget(55, 68);
      makeCenteredTarget(150, 200, 60);
      makeCenteredTarget(250, 300, 35);
      makeCenteredTarget(400, 400, 20);
      
    } 
    
   public void makeTarget(int x, int y)
    {
       
       outer = new Ellipse( x+ outerX, y + outerY );
       outer.setSize( outerSize,  outerSize );
       outer.setColor( Color.RED );
       
       inner = new Ellipse( x+ outerX + 5, y + outerY + 5 );
       inner.setSize( outerSize-10,  outerSize-10 );
       inner.setColor( Color.BLUE );
       
       inner2 = new Ellipse( x+ outerX + 10, y + outerY + 10 );
       inner2.setSize( outerSize-20,  outerSize-20 );
       inner2.setColor( Color.GREEN );
       
       inner3 = new Ellipse( x+ outerX + 15, y + outerY + 15 );
       inner3.setSize( outerSize-30,  outerSize-30 );
       inner3.setColor( Color.YELLOW );
    }
   public void makeCenteredCircle(int x, int y, int r, Color myColor)
    {
     centeredCircle = new Ellipse();
     centeredCircle.setLocation(x-r, y+-r);
     centeredCircle.setSize(2*r, 2*r);
     centeredCircle.setColor(myColor);
    
    }
   public void makeCenteredTarget(int x, int y, int r)
    {
    makeCenteredCircle(x, y, r, Color.BLACK);
    makeCenteredCircle(x, y, r/2, Color.RED);
    makeCenteredCircle(x, y, r/4, Color.BLUE);
    makeCenteredCircle(x, y, r/8, Color.YELLOW);
     
    }
   
        
    // -----------------------------------------------------------------
    /** main program just invokes the class constructor.
     */
    public static void main(String[] args)
    {
        Lab5 app = new Lab5();
    }
 
}//End of Class Lab5
