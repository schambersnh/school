/** 
 * Target.java:
 * 
 * Displays a simple archery target using multiple Wheels Shapes.
 * The entire target is built in a method, makeTarget.
 * 
 * makeTarget has position arguments.
 *
 * Stephen Chambers
 * Created 9/16/10
 */

// --------------- imports ------------------------------
import wheelsunh.users.*;
import java.awt.Color;


public class Target 
{
    //---------------- instance variables ------------------------------
 
       int    level2X    = 15,  level2Y = 15;
       int    level3X    = 25,  level3Y = 25;
       int    level4X    = 30,  level4Y = 30;
       
       int    level1Size  = 80;
       int    level2Size = 50; 
       int    level3Size = 30; 
       int    level4Size  = 20;
       
       Ellipse    level1;
       Ellipse    level2;
       Ellipse    level3;
       Ellipse    level4;
    // -----------------------------------------------------------------
    /** 
     * Constructor for the Target class.
     */
    public Target( )
    {
       makeTarget( 0, 0 );
     
    } 
     public Target(int x, int y )
    {
       makeTarget( x, y );
     
    } 
   
    // -----------------------------------------------------------------

    private void setLocation( int x, int y )
    {
       level1.setLocation(x, y);
       level2.setLocation(x + level2X, y + level2Y);
       level3.setLocation(x + level3X, y + level3Y);
       level4.setLocation(x + level4X, y + level4Y);
    }
    
   
    private void move( int dx, int dy )
    {
       int x = level1.getXLocation();
       int y = level1.getYLocation();
       
       level1.setLocation(x + dx, y + dy);
       level2.setLocation(x + dx + level2X, y + dy + level2Y);
       level3.setLocation(x + dx + level3X, y + dy+ level3Y);
       level4.setLocation(x + dx + level4X, y + dy + level4Y);
    }

    // -----------------------------------------------------------------
    /**
     * makeTarget
     * encapsulates all the Wheels components needed to draw a target.
     */
    private void makeTarget( int x, int y )
    {
       
       
       // other local variables are used to references the Wheels objects
       // used to draw the target.
      
       
       // create the level1 circle
       level1 = new Ellipse( x, y );
       level1.setSize( level1Size, level1Size );
       
       // create the next level4 circle
       level2 = new Ellipse( x + level2X, y + level2Y );
       level2.setSize( level2Size, level2Size );
       level2.setColor( Color.BLUE );
       
       // create the next level4 circle
       level3 = new Ellipse( x + level3X, y + level3Y );
       level3.setSize( level3Size, level3Size );
       level3.setColor( Color.CYAN );
       
       // create the level4 circle
       level4 = new Ellipse( x + level4X, y + level4Y );
       level4.setColor( Color.BLACK );
       level4.setSize( level4Size, level4Size );
    }
    
    // -----------------------------------------------------------------
    /** main program just invokes the class constructor.
     */
    public static void main(String[] args)
    {
        Frame f = new Frame();
        Target t1 = new Target(0, 0);
        Target t2 = new Target(100, 100);
        Target t3 = new Target(200, 200);
        sleep(1000);
        t3.setLocation(400,400);
        sleep(1000);
        t3.move(20, 5);
        sleep(1000);
        t3.move(40, 5);
        sleep(1000);
        
       
    }
    
    /////////////// Don't worry about anything below this line! ////////
    /******************************************************************/
    /**
     * sleep for a while
     */
    public static void sleep( int milliseconds ) 
    {
       try 
       {
          Thread.sleep(milliseconds);
       }
       catch (java.lang.InterruptedException e) 
       {}
    }
}//End of Class Target
