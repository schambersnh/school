/** 
 * Target.java
 *    Initial implementation where grouping is done "by hand".
 *    Used in 09-Interface notes
 * 
 * rdb - September 2009 
 *    derived from earlier targets
 * 
 * * @Stephen Chambers
 * 10/19/10
 */

import java.awt.Color;
import wheelsunh.users.*;
import java.util.Random;

public class Target 
{
  //---------------- instance variables --------------------------------
  private Ellipse level0 = null; // 4 circles comprise the target
  private Ellipse level1 = null; 
  private Ellipse level2 = null;  
  private Ellipse level3 = null;  
  
  //---------------- magic constants -----------------------------------
  
  private int dx1   = 10;    // next inner circle constants
  private int dy1   = 10;
  
  private int dx2   = 20;    // next smaller circle constants
  private int dy2   = 20;
  
  private int dx3   = 30;    // innermost circle constants
  private int dy3   = 30;
  
  //----------------- constructor ------------------------------
  public Target( int x, int y, int size )
  {
    makeTarget( x, y, size );  
  }
  //------------------- makeTarget( int, int, int ) ------------------------
  /**
   * create the target
   */
  private void makeTarget( int x, int y, int size )
  {
    // inner circle sizes
    int size1 = size - dx1 * 2;
    int size2 = size - dx2 * 2;
    int size3 = size - dx3 * 2;
    
    if (size > 10)
    {
      level0 = new Ellipse( x, y );
      level0.setSize( size, size );
      level0.setColor( Color.BLACK );
    }
    
    if (size1 > 10)
    {
      level1 = new Ellipse( x + dx1, y + dy1 );
      level1.setSize( size1, size1 );
      level1.setColor( Color.RED );
    }
    
    if (size2 > 10)
    {
      level2 = new Ellipse( x + dx2, y + dy2 );
      level2.setSize( size2, size2 );
      level2.setColor( Color.GREEN );
    }
    
    if (size3 > 10)
    {
      level3 = new Ellipse( x + dx3, y + dy3 );
      level3.setSize( size3, size3 );
      level3.setColor( Color.BLUE );
    }
    
    if(this != null)
    {
      this.setLocation( x, y );
    }
    
    
  }
  
  //------------------- setLocation( int, int ) ------------------------
  /**
   * set the target's location by setting each of the component circles.
   */
  public void setLocation( int x, int y )
  {
    if (level0 != null)
    {
      level0.setLocation( x, y );
    }
    if (level1 != null)
    {
      level1.setLocation( x + dx1, y + dy1 );
    }
    if (level2 != null)
    {
      level2.setLocation( x + dx2, y + dy2 );
    }
    if (level3 != null)
    {
       level3.setLocation( x + dx3, y + dy3 );
    }
    
    
  }
  /********************************************************************/
  //----------------- main --------------------------------------------
  /**
   * Unit test code for Target
   */
  public static void main( String[] args )
  {
    new Frame();
    Random rng = new Random( 2 );
    
    int size = 20 + rng.nextInt( 130 );
    Target t1 = new Target( 50, 50, size );
    
    size = 30 + rng.nextInt( 130 );
    Target t2 = new Target( 300, 100, size );
    
    size = 30 + rng.nextInt( 130 );
    Target t3 = new Target( 150, 150, size );
    
    size = 30 + rng.nextInt( 130 );
    Target t4 = new Target( 450, 350, size );
  }
} //End of Class
