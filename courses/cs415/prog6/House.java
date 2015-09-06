/** 
 * House.java
 * Shows the different seasons of a House
 from space
 *
 * Stephen Chambers
 * 10/12/10
 */
import wheelsunh.users.*;
import java.awt.Color;
import java.awt.event.MouseEvent;

public class House extends ShapeGroup implements Seasonal, Draggable
  
  
{
  
  //---------------- instance variables ------------------------------
  private Ellipse sun;
  private Rectangle myHouse, window1, window2, door;
  ;
  private int earthX, earthY, lastX, lastY, currentX, currentY;
  //------------------------------------------------------------------
  
  public House(int x, int y)
  {
    //Creates Body of the House
    myHouse = new Rectangle(x, y);
    myHouse.setSize(70, 70);
    myHouse.setColor(Color.RED);
    add(myHouse);
    
    //Creates first window
    window1 = new Rectangle(x + 5, y + 10);
    window1.setSize(10, 10);
    window1.setColor(Color.WHITE);
    add(window1);
    
    //Creates Second Window
    window2 = new Rectangle(x + 45, y + 10);
    window2.setSize(10, 10);
    window2.setColor(Color.WHITE);
    add(window2);
    
    //Creates Door
    door = new Rectangle(x + 30, y + 40);
    door.setSize(10, 30);
    door.setColor(Color.CYAN);
    add(door);
  }
  
  //Set the Color
  //////////////////////////////////////////////////////////////////////
  public void setColor(Color myColor)
  {
    myHouse.setColor(myColor);
  }
  //Get the Color
  ////////////////////////////////////////////////////////////////////
  public Color getColor()
  {
    return myHouse.getColor();
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
   * mouseDragged -- Drag the House

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
   myHouse.setColor(Color.BLACK);
  }
  /////////////////////////////////////////////////////////////////////////
  public void summer()
  {
    myHouse.setColor(Color.PINK);
    
  }
  /////////////////////////////////////////////////////////////////////////
  public void spring()
  {
    myHouse.setColor(Color.YELLOW);
    
  }
  /////////////////////////////////////////////////////////////////////////    
  public void fall()
  {
    myHouse.setColor(Color.ORANGE);
  }
} //End of Class
