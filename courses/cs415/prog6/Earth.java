/** 
 * Earth.java
 * Shows the different seasons of Earth from space
 *
 * Stephen Chambers
 * 10/12/10
 */
import wheelsunh.users.*;
import java.awt.Color;
import java.awt.event.MouseEvent;

public class Earth extends ShapeGroup implements Seasonal, Draggable
  
  
{
  
  //---------------- instance variables ------------------------------
  private Ellipse sun;
  private Ellipse myEarth;
  private int earthX, earthY, lastX, lastY, currentX, currentY;
  //------------------------------------------------------------------
  
  public Earth(int x, int y)
  {
    //Creates an Ellipse representing the Earth
    myEarth = new Ellipse( x , y );
    myEarth.setColor(Color.BLUE);
    myEarth.setSize( 50 , 50 );
    add(myEarth);
    earthX = x;
    earthY = y;
    
    //Creates an Ellipse representing the Sun
    sun = new Ellipse( x-30, y + 50 );
    sun.setColor(Color.YELLOW);
    sun.setSize(40, 40);
    add(sun);
  }
  
  //Set the Color
  //////////////////////////////////////////////////////////////////////
  public void setColor(Color myColor)
  {
    myEarth.setColor(myColor);
  }
  //Get the Color
  ////////////////////////////////////////////////////////////////////
  public Color getColor()
  {
    return myEarth.getColor();
  }
  
  
  //Mouse Pressed, gets location of the mouse when clicked
  //////////////////////////////////////////////////////////////////////
  public void mousePressed( MouseEvent me )
  {
    lastX = me.getPoint().x;
    lastY = me.getPoint().y;
  }
  
  //////////////////////////////////////////////////////////////////////////
  /**
   * mouseDragged -- Drag the Earth and Sun
   * Use mouse position saved in mousePressed; update the saved mouse position
   * for next drag event.
   */
  public void mouseDragged( MouseEvent me )
  {
    currentX = me.getPoint().x;
    currentY = me.getPoint().y;
    
    int diffX = currentX - lastX;
    int diffY = currentY - lastY; 
    
    setLocation( getLocation().x + diffX, getLocation().y + diffY ); 
    
    earthX += diffX;
    earthY += diffY;
    
    lastX = currentX;
    lastY = currentY;
  }
  
  ////////////////////////////////////////////////////////////////////////
  /**
   * MouseReleased Method is in interface, but does not need to be exectued
   */
  public void mouseReleased( MouseEvent me )
  {
    
    
  }
   /*
   * Seasonal methods changing the Sun
   * 
   * 
   * 
   * */
  public void winter()
  {
    sun.setLocation(earthX - 40, earthY - 20);
  }
  /////////////////////////////////////////////////////////////////////////
  public void summer()
  {
    sun.setLocation(earthX + 40, earthY - 20);
    
  }
  /////////////////////////////////////////////////////////////////////////
  public void spring()
  {
    sun.setLocation(earthX + 50, earthY + 20);
    
  }
  /////////////////////////////////////////////////////////////////////////    
  public void fall()
  {
    sun.setLocation(earthX - 40, earthY + 10);
  }
} // End of Class
