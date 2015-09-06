/** 
 * Balloon.java:
 *  
 * mlb 10/2008
 * rdb 10/25/2009
 * 
 * Stephen Chambers
 * 10/26/10
 */

import wheelsunh.users.*;
import java.awt.Color;



public class Balloon extends Ellipse
{
  //------------ class variables ----------------------------
  public static int size = 30;  // diameter of all balloons
  
  //------------ instance variables -------------------------
  private int speed;
  private boolean isVisible;
  //-------------- constructor ------------------------------
  /**
   * create a balloon at x,y with floating speed of s
   */
  public Balloon( int x, int y, int s )
  {
    super(x,y);
    setSize(size, size);
    speed = s; 
  }
  
  //---------------------- isVisible() ---------------------------------
  /**
   * returns true if the balloon is still visible in the display area.
   */
  public boolean isVisible()
  {
    if(getYLocation() > -size)
    {
      isVisible = true;
    }
    
    
    return isVisible;     
  }   
  
  //----------------- rise() ----------------------------
  /**
   * change the y value by subtracting the speed (saved in an instance var)
   * if the balloon is at least partially visible (its y > -size)
   *    return true
   * otherwise
   *    return false
   */
  public boolean rise()
  {
    setLocation(getXLocation(), getYLocation() - speed);  
    return isVisible(); 
  }
  
  //---------------------------------------------------------
  public void floatAway(  )
  {    
    while(rise())
    {
      BalloonUtilities.sleep(100);
    }

  }
  
  //---------------------------------------------------------
  public static void main( String[ ] args )
  {
    Frame f = new Frame();
    Balloon b = new Balloon( 200, 450, 20 ); 
    Balloon c = new Balloon( 300, 450, 40 ); 
    b.rise();
    BalloonUtilities.sleep(100);
    b.rise();
    c.floatAway(  );
    b.rise();
    
  }  
}//End of Class Balloon