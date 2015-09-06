/** 
 * Lab1.java:
 * Displays a red circle
 *
 * @author Stephen Chambers
 * 8/31/10
 * -cs415
 */
public class Lab1 extends wheelsunh.users.Frame
{
    //---------------- instance variables ------------------------------
  
    private wheelsunh.users.Ellipse circle;
    private wheelsunh.users.Rectangle rect;
    private wheelsunh.users.Rectangle sqr;
   
    
    // -----------------------------------------------------------------
    /** Constructor for the Lab1 class.
     */
    public Lab1( )
    {
        circle = new wheelsunh.users.Ellipse( java.awt.Color.GREEN);
        circle.setLocation(140, 240);
        
         circle = new wheelsunh.users.Ellipse( java.awt.Color.RED );
        circle.setLocation(50, 50);
        
        /*Using the Rectangle Class, change the size to make the object a
         * traditional rectangle.*/
        rect = new wheelsunh.users.Rectangle( java.awt.Color.CYAN );
        rect.setLocation(200, 250);
        rect.setSize(43, 90);
        
        /*Using the Rectangle Class, do NOT set size so the 'rectangle'
         * will become a square */
        sqr = new wheelsunh.users.Rectangle( java.awt.Color.BLACK );
        rect.setLocation(400, 317);
    } 
    
    // -----------------------------------------------------------------
    /** main program just invokes the class constructor.
     */
    public static void main(String[] args)
    {
        Lab1 app = new Lab1();
    }
}//End of Class Lab1
