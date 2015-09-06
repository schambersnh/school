/** 
 * Lab4.java:
 * 
 * 
 * 
 * @author Stephen Chambers
 * 9/9/10
 * 
 */

// --------------- imports ------------------------------
import wheelsunh.users.*;
import java.awt.Color;

public class Lab4 extends Frame
{
    //---------------- instance variables ------------------------------
    
    // -----------------------------------------------------------------
    /** 
     * Constructor for the Lab4 class.
     */
    public Lab4( )
    {
       // local "constant" variables define location/size of each circle
       int    outerX   =  0,  outerY = 0;
       int    outerSize = 80;
       
       //Add x and y variable to translate target horizontally
       int outer2X = 100;
       int outer2Y = 100;
       
       // <------ Add declarations for data needed for inner circles ---------->
       
       // local variables to reference the Wheels objects used to draw target.
       Ellipse    outer, inner1, inner2, inner3, inner4;
       int innerSize, innerX, innerY;
       innerSize = 80;
       innerX = 0;
       innerY = 0;
       
       // <------- Add declarations for Wheels variables for inner circles ---->
       
       // create the outer circle
       
       outer = new Ellipse( outerX , outerY);
       outer.setSize( outerSize, outerSize );
       outer.setColor( Color.RED );
       
       //create the second outer circle
       outer = new Ellipse( outerX + outer2X, outerY+ outer2Y);
       outer.setSize( outerSize, outerSize );
       outer.setColor( Color.RED );
       
      
       
       // create the inner circles
       // <-------- Add code here to create the inner circles

       inner1 = new Ellipse( (innerX + 7) + outer2X, (innerY + 7) + outer2Y);
       inner1.setSize( innerSize - 15, innerSize - 15 );
       inner1.setColor( Color.BLUE );
       
       inner2 = new Ellipse( (innerX + 15) + outer2X, (innerY + 15) + outer2Y );
       inner2.setSize( innerSize - 30, innerSize - 30);
       inner2.setColor( Color.YELLOW );
       
       inner3 = new Ellipse( (innerX + 25) + outer2X, (innerY + 25)  + outer2Y);
       inner3.setSize( innerSize - 50, innerSize - 50);
       inner3.setColor( Color.YELLOW );
       
       inner4 = new Ellipse( (innerX + 35) + outer2X, (innerY + 35)  + outer2Y);
       inner4.setSize( innerSize - 70, innerSize - 70);
       inner4.setColor( Color.GREEN );

       //Create Second Target
       inner1 = new Ellipse( innerX + 7 , innerY + 7) ;
       inner1.setSize( innerSize - 15, innerSize - 15 );
       inner1.setColor( Color.BLUE );
       
       inner2 = new Ellipse( innerX + 15 , innerY + 15  );
       inner2.setSize( innerSize - 30, innerSize - 30);
       inner2.setColor( Color.YELLOW );
       
       inner3 = new Ellipse( innerX + 25 , innerY + 25 );
       inner3.setSize( innerSize - 50, innerSize - 50);
       inner3.setColor( Color.YELLOW );
       
       inner4 = new Ellipse( innerX + 35, innerY + 35);
       inner4.setSize( innerSize - 70, innerSize - 70);
       inner4.setColor( Color.GREEN );
       
       //create third target
       
       outer2X = 400;
       outer2Y = 400;

       outer = new Ellipse( outerX + outer2X, outerY+ outer2Y);
       outer.setSize( outerSize, outerSize );
       outer.setColor( Color.RED );
       
       inner1 = new Ellipse( (innerX + 7) + outer2X, (innerY + 7) + outer2Y);
       inner1.setSize( innerSize - 15, innerSize - 15 );
       inner1.setColor( Color.BLUE );
       
       inner2 = new Ellipse( (innerX + 15) + outer2X, (innerY + 15) + outer2Y );
       inner2.setSize( innerSize - 30, innerSize - 30);
       inner2.setColor( Color.YELLOW );
       
       inner3 = new Ellipse( (innerX + 25) + outer2X, (innerY + 25)  + outer2Y);
       inner3.setSize( innerSize - 50, innerSize - 50);
       inner3.setColor( Color.YELLOW );
       
       inner4 = new Ellipse( (innerX + 35) + outer2X, (innerY + 35)  + outer2Y);
       inner4.setSize( innerSize - 70, innerSize - 70);
       inner4.setColor( Color.GREEN );

                
    } 
        
    // -----------------------------------------------------------------
    /** main program just invokes the class constructor.
     */
    public static void main(String[] args)
    {
        Lab4 app = new Lab4();
    }
}//End of Class Lab4