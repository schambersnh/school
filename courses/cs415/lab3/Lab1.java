/** 
 * Lab1.java:
 * Displays a red circle
 *
 * @author cs415
 * Created 1/19/08
 * 
 */
public class Lab1 extends wheelsunh.users.Frame
{
    //---------------- instance variables ------------------------------
    private wheelsunh.users.Ellipse circle;
   
    
    // -----------------------------------------------------------------
    /** Constructor for the Lab1 class.
     */
    public Lab1( )
    {
        circle = new wheelsunh.users.Ellipse( java.awt.Color.RED );
     
    } 
    
    // -----------------------------------------------------------------
    /** main program just invokes the class constructor.
     */
    public static void main(String[] args)
    {
        Lab1 app = new Lab1();
    }
}//End of Class Lab1
