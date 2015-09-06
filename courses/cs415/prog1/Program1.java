/** 
 * Program1.java:
 * Displays a rectangle for building an ATV.
 *
 * mb 8/10
 * 
 */
import wheelsunh.users.*;
import java.awt.Color;

public class Program1 extends wheelsunh.users.Frame
{
    
    //---------------- instance variables ------------------------------
    
    private  Rectangle pad;
    private Rectangle body;
    private Ellipse wheel, wheel2;
    private Line line1;
    private Rectangle flag;
    private Ellipse inFlag;
    
    

    
    
    // -----------------------------------------------------------------
    /** Constructor for the Start class.bbbb
      */
    public Program1( )
    {
        pad = new wheelsunh.users.Rectangle( 200, 100 );
        pad.setFillColor( java.awt.Color.WHITE );
        pad.setFrameColor( java.awt.Color.BLACK );
        pad.setSize( 200, 180);
        //--------------------------------------------
        //--------------------------------------------
        
        // Creates body of the ATV
        body = new wheelsunh.users.Rectangle(250, 165);
        body.setFillColor(java.awt.Color.RED);
        body.setSize(100, 50);
        
        //Creates the two wheels of the ATV
        wheel = new wheelsunh.users.Ellipse(250,215);
        wheel.setFillColor(java.awt.Color.GREEN);
        wheel.setSize(30, 30);
        
        wheel2 = new wheelsunh.users.Ellipse(315,215);
        wheel2.setFillColor(java.awt.Color.GREEN);
        wheel2.setSize(30, 30);
        
        //Creates the Flagpole
        line1 = new wheelsunh.users.Line(320, 142, 320, 162);
        line1.setThickness(5);
        line1.setColor(java.awt.Color.BLACK);
        
        //Creates the Outisde of the flag
        flag = new wheelsunh.users.Rectangle(323, 140);
        flag.setFillColor(java.awt.Color.WHITE);
        flag.setFrameColor(java.awt.Color.BLACK);
        flag.setSize(25, 15);
        
        //Creates the circle inside of the flag
        inFlag = new wheelsunh.users.Ellipse(330, 143);
        inFlag.setSize(10, 10);
        
        
   
        
        
        
        
    } 
    
    // -----------------------------------------------------------------
    /** main program just invokes the class constructor.
      */
    public static void main(String[] args)
    {
        Program1 app = new Program1();
    }
}//End of Class 