/** 
 * Program3.java:
 * Makes a method for Creating an instnace of an ATV
 * Stephen Chambers
 * 9/21/10
 */
import wheelsunh.users.*;
import java.awt.Color;

public class Program3

 extends wheelsunh.users.Frame
{
    
    //---------------- instance variables ------------------------------
    

    private Rectangle body;
    private Ellipse wheel, wheel2;
    private Line line1;
    private Rectangle flag;
    private Ellipse inFlag;
    private int x1, y1, x2, y2;
    private int padX = 100, padY = 80;
    

    
    
    // -----------------------------------------------------------------
    /** Constructor for the Program3 Class, makes the makeATV method calls
      */
    public Program3( )
    {
        makeATV(100, 300, Color.BLUE);
        makeATV(250, 250, Color.CYAN);
        makeATV(400, 250, Color.GREEN);
        makeATV(300, 100, Color.BLACK);
    } 
    //----------------------------------------------------------------
    //Method for making the ATV
    public void makeATV(int x1, int y1, Color myColor)
    {
        // Creates body of the ATV1
        body = new wheelsunh.users.Rectangle(x1 + 50, y1 - 35);
        body.setFillColor(myColor);
        body.setSize(padX, padY-30);
        
        //Creates the two wheels of the ATV1
        wheel = new wheelsunh.users.Ellipse(x1 + 50,y1 + 15);
        wheel.setFillColor(java.awt.Color.GREEN);
        wheel.setSize(padX - 70, padY- 50);
        
        wheel2 = new wheelsunh.users.Ellipse(x1 + 115,y1 + 15);
        wheel2.setFillColor(java.awt.Color.GREEN);
        wheel2.setSize(padX - 70, padY - 50);
        
        //Creates the Flagpole of ATV1
        line1 = new wheelsunh.users.Line(x1 + 120, y1 - 58, x1+ 120, y1-38);
        line1.setThickness(5);
        line1.setColor(java.awt.Color.BLACK);
        
        //Creates the Outisde of the flag of ATV1
        flag = new wheelsunh.users.Rectangle(x1 + 123, y1 - 60);
        flag.setFillColor(java.awt.Color.WHITE);
        flag.setFrameColor(java.awt.Color.BLACK);
        flag.setSize(padX - 75, padY - 65);
        
        //Creates the circle inside of the flag of ATV1
        inFlag = new wheelsunh.users.Ellipse(x1 + 130, y1 - 57);
        inFlag.setSize(padX - 90, padY - 70);
   }
    
    // -----------------------------------------------------------------
    /** Main program just invokes the class constructor.
      */
    public static void main(String[] args)
    {
       Program3 app = new Program3();
    }
}//End of Class 