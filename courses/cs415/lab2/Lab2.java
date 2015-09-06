/** 
 * Lab2.java:
 * Displays a red circle
 *
 * Stephen Chambers cs415
 * 9/2/2010
 * 
 */
public class Lab2 extends wheelsunh.users.Frame
{
    //---------------- instance variables ------------------------------
    private wheelsunh.users.Ellipse circle;
   
    
    // -----------------------------------------------------------------
    /** Constructor for the Lab2 class.
     */
    public Lab2( )
    {
        circle = new wheelsunh.users.Ellipse( java.awt.Color.RED );
    } 
    
    // -----------------------------------------------------------------
    /** main program just invokes the class constructor.
     */
    public static void main(String[] args)
    {
        Lab2 app = new Lab2();
    }
}//End of Class Lab2
