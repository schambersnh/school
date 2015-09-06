/**
 * PowerRectangle.java 
 *   This class extends Rectangle to add 
 *    - a peer object (a TextBox)
 *    - code to display the area, circumference, and diagonal length of
 *      the rectangle in the TextBox. 
 *      Use the ShapeFunctions class methods to compute these values.
 *    - Use drag to change size of the rectangle.
 *    - Use mouse clicked to print size info to Interactions pane
 *   
 * @author Stephen Chambers
 * 10/12/10
 */
import wheelsunh.users.*;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Point;

public class PowerRectangle extends Rectangle
{
  //--------------- instance variables --------------------
  TextBox label;
  int lastX, lastY, currentX, currentY, diffX, diffY;
  Color origColor;
  /**-------------------------------------------------------------------------
    * Constructor requires the x,y location of the rectangle
    * */
  
  public PowerRectangle( int x, int y )
  {
    super(x, y);
    label = new TextBox(x-100, y-70);
    updateInfo();
    
  }
  
  //-------------------------------------------------------------------------
  /**
   * setSize( int, int ) -- defines the width and height of the rectangle. 
   * */
  
  
  public void setSize( int width, int height )
  {
    super.setSize(width, height);
    if(label != null)
    {
      updateInfo();
    }
    
  }
  
  //------------ utility methods --------------------------
  /**
   * updateInfo -- computes the current area and circumference of the 
   * rectangle (using methods in the ShapeFunctions class) and updates
   * the text string in the TextBox.
   */
  private void updateInfo()
  {
    label.setText("A: " + ShapeFunctions.area(this) + "\n" + "C: " + 
                  ShapeFunctions.circumference(this) + "\n" + "D: " +
                  ShapeFunctions.diagonal(this));
  }
  
  //-------------------------------------------------------------------------
  /**
   * changeSizeBy( int, int ) -- update size of Rectangle by an encremental
   *                       amount in x and y. 
   */
  private void changeSizeBy( int dX, int dY )
  {
    setSize(getWidth() + dX, getHeight() + dY);
  }
  
  //-------------------------------------------------------------------------
  /**
   * mousePressed - save the position of the mouse and the current color of the 
   * rectangle, and change the color of the rectangle to red.
   */
  public void mousePressed( MouseEvent me )
  {
    origColor = getColor();
    setColor(Color.RED);
    
    lastX = me.getPoint().x;
    lastY = me.getPoint().y;
  }
  
  //-------------------------------------------------------------------------
  /**
   * mouseDragged -- change the rectangle size by the same delta that the 
   *                 mouse moved.
   * Use mouse position saved in mousePressed; update the saved mouse position
   * for next drag event.
   */
  public void mouseDragged( MouseEvent me )
  {
      
      
      currentX = me.getPoint().x;
      currentY = me.getPoint().y;
      
      int diffX = currentX - lastX;
      int diffY = currentY - lastY; 
      
      changeSizeBy(diffX, diffY);
      
  }
  
  //-------------------------------------------------------------------------
  /**
   * mouseReleased -- restore the original color.
   */
  public void mouseReleased( MouseEvent me )
  {
    setColor(origColor);
    
  }
  
  //-------------------------------------------------------------------------
  /**
   * mouseClicked - print the width/height to System.out
   */
  public void mouseClicked( MouseEvent me )
  {  
    System.out.println("Height: " + getHeight());
    System.out.println("Width: " + getWidth());
    
  }
  
  //------------------ main ----------------------------------
  // unit test code
  //
  public static void main( String args[] )
  {
    Frame f = new Frame();
    PowerRectangle pr1 = new PowerRectangle( 400, 400 );
    PowerRectangle pr2 = new PowerRectangle(250, 250);
    PowerRectangle pr3 = new PowerRectangle(100, 100);
    
    pr1.setColor( Color.BLUE );
    pr1.setSize( 20, 25 );
    
    pr2.setColor( Color.CYAN );
    pr2.setSize( 10, 30 );
    
    pr3.setColor( Color.GREEN );
    pr3.setSize( 30, 15 );
  }
}