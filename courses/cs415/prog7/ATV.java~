/** 
 * ATV.java
 * Builds an ATV extending ShapeGroup
 *
 * Stephen Chambers
 * 10/12/10
 */
import wheelsunh.users.*;
import java.awt.Color;
import java.awt.event.MouseEvent;

public class ATV extends ShapeGroup implements Seasonal, Draggable
  
  
{
  
  //---------------- instance variables ------------------------------
  
  
  protected Rectangle body;
  protected Ellipse wheel, wheel2, surfboard;
  protected Line line1;
  protected Rectangle flag;
  protected Ellipse inFlag;
  protected int x1, y1, x2, y2, lastX, lastY, currentX, currentY, origX, origY;
  protected int padX = 100, padY = 80;
  protected Color origColor = Color.RED;
  
  
  
  
  
  
  
  // -----------------------------------------------------------------
  /** Constructor accepting no parameters
    */
  public ATV()
  {
    
    // Creates body of the ATV1
    body = new wheelsunh.users.Rectangle( x1 + 50, y1 - 35 );
    body.setFillColor(Color.RED);
    body.setSize(padX, padY-30);
    add(body);
    
    //Creates the two wheels of the ATV1
    wheel = new wheelsunh.users.Ellipse( x1 + 50, y1 + 15 );
    wheel.setColor(Color.GREEN);
    wheel.setFillColor(java.awt.Color.GREEN);
    wheel.setSize(padX - 70, padY- 50);
    add(wheel);
    
    wheel2 = new wheelsunh.users.Ellipse( x1 + 115 , y1 + 15 );
    wheel2.setColor(Color.GREEN);
    wheel2.setFillColor(java.awt.Color.GREEN);
    wheel2.setSize( padX - 70 , padY - 50 );
    add(wheel2);
    
    //Creates the Flagpole of ATV1
    line1 = new wheelsunh.users.Line(x1 + 120, y1 - 58, x1+ 120, y1-38);
    line1.setThickness(5);
    line1.setColor(java.awt.Color.BLACK);
    add(line1);
    
    //Creates the Outisde of the flag of ATV1
    flag = new wheelsunh.users.Rectangle(x1 + 123, y1 - 60);
    flag.setFillColor(java.awt.Color.WHITE);
    flag.setFrameColor(java.awt.Color.BLACK);
    flag.setSize( padX - 75 , padY - 65 );
    add(flag);
    
    //Creates the circle inside of the flag of ATV1
    inFlag = new wheelsunh.users.Ellipse(x1 + 130, y1 - 57);
    inFlag.setSize( padX - 90 , padY - 70 );
    add(inFlag);
    
    //Creates a surfboard for the SummerATV
    surfboard = new Ellipse(getXLocation(), getYLocation());
    add(surfboard);
    
    setLocation( 0,0 );
    
  }
  //--------------------------------------------------------------------
  //Constructor accepting only a color
  public ATV(Color myColor)
  {
    
    // Creates body of the ATV1
    body = new wheelsunh.users.Rectangle(x1 + 50, y1 - 35);
    body.setFillColor(myColor);
    body.setSize(padX, padY-30);
    add(body);
    
    //Creates the two wheels of the ATV1
    wheel = new wheelsunh.users.Ellipse(x1 + 50,y1 + 15);
    wheel.setColor(Color.GREEN);
    wheel.setFillColor(java.awt.Color.GREEN);
    wheel.setSize(padX - 70, padY- 50);
    add(wheel);
    
    wheel2 = new wheelsunh.users.Ellipse(x1 + 115,y1 + 15);
    wheel2.setColor(Color.GREEN);
    wheel2.setFillColor(java.awt.Color.GREEN);
    wheel2.setSize(padX - 70, padY - 50);
    add(wheel2);
    
    //Creates the Flagpole of ATV1
    line1 = new wheelsunh.users.Line(x1 + 120, y1 - 58, x1+ 120, y1-38);
    line1.setThickness(5);
    line1.setColor(java.awt.Color.BLACK);
    add(line1);
    
    //Creates the Outisde of the flag of ATV1
    flag = new wheelsunh.users.Rectangle(x1 + 123, y1 - 60);
    flag.setFillColor(java.awt.Color.WHITE);
    flag.setFrameColor(java.awt.Color.BLACK);
    flag.setSize(padX - 75, padY - 65);
    add(flag);
    
    //Creates the circle inside of the flag of ATV1
    inFlag = new wheelsunh.users.Ellipse(x1 + 130, y1 - 57);
    inFlag.setSize(padX - 90, padY - 70);
    add(inFlag);
    
   //Creates a surfboard for the SummerATV
    surfboard = new Ellipse(getXLocation(), getYLocation());
    add(surfboard);
    
    setLocation(0,0);
    
  }
  //-----------------------------------------------------------------
  //Constructor accepting the location and color of the ATV
  public ATV(int x1, int y1 )
  {
    // Creates body of the ATV1
    body = new wheelsunh.users.Rectangle(x1 + 50, y1 - 35);
    body.setFillColor(Color.RED);
    body.setSize(padX, padY-30);
    add(body);
    
    //Creates the two wheels of the ATV1
    wheel = new wheelsunh.users.Ellipse(x1 + 50,y1 + 15);
    wheel.setColor(Color.GREEN);
    wheel.setFillColor(java.awt.Color.GREEN);
    wheel.setSize(padX - 70, padY- 50);
    add(wheel);
    
    wheel2 = new wheelsunh.users.Ellipse(x1 + 115,y1 + 15);
    wheel2.setColor(Color.GREEN);
    wheel2.setFillColor(java.awt.Color.GREEN);
    wheel2.setSize(padX - 70, padY - 50);
    add(wheel2);
    
    //Creates the Flagpole of ATV1
    line1 = new wheelsunh.users.Line(x1 + 120, y1 - 58, x1+ 120, y1-38);
    line1.setThickness(5);
    line1.setColor(java.awt.Color.BLACK);
    add(line1);
    
    //Creates the Outisde of the flag of ATV1
    flag = new wheelsunh.users.Rectangle(x1 + 123, y1 - 60);
    flag.setFillColor(java.awt.Color.WHITE);
    flag.setFrameColor(java.awt.Color.BLACK);
    flag.setSize(padX - 75, padY - 65);
    add(flag);
    
    //Creates the circle inside of the flag of ATV1
    inFlag = new wheelsunh.users.Ellipse(x1 + 130, y1 - 57);
    inFlag.setSize(padX - 90, padY - 70);
    add(inFlag);
    
    //Creates a surfboard for the SummerATV
    surfboard = new Ellipse(getXLocation(), getYLocation());
    add(surfboard);
  } 
  //Set the Color of the ATV
  public void setColor(Color myColor)
  {
    body.setColor(myColor);
  }
  //------------------------------------------------------------------
  //Get the Color of the ATV body
  public Color getColor()
  {
    return body.getColor();
  }
  
  /*Draggable Methods
   * 
   * */
  //Saves the location of the mouse when clicked on the ATV
  public void mousePressed( MouseEvent me )
  {
    ///////////////////////////////////////////////////////////////////////
    //Saves the original location because lastX and lastY will change after
    //being dragged///////////////////////////////////////////////////////
    origX = me.getPoint().x;
    origY = me.getPoint().y;
    
    lastX = me.getPoint().x;
    lastY = me.getPoint().y;
  }
  
  //-------------------------------------------------------------------------
  /**
   * mouseDragged -- Drag the ATV
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
    lastX = currentX;
    lastY = currentY;
  }
  
  //-------------------------------------------------------------------------
  /**
   * MouseReleased Method is in interface, but does not need to be exectued
   */
  public void mouseReleased( MouseEvent me )
  {
    
  }
  
  
  
  /*
   * Seasonal methods changing the ATV
   * 
   * 
   * 
   * */
  public void winter()
  {
    surfboard.setSize(0,0);
    setColor(origColor);
    wheel.setSize(50, 50);
    wheel2.setSize(50, 50);
    wheel.setColor(Color.BLACK);
    wheel2.setColor(Color.BLACK);
  }
  /////////////////////////////////////////////////////////////////////////
  public void summer()
  {
   
    setColor(Color.PINK);
    surfboard.setSize(10, 70);
    surfboard.setRotation(35);
    surfboard.setColor(Color.GREEN);
    add(surfboard);
    
  }
  /////////////////////////////////////////////////////////////////////////
  public void spring()
  {
    surfboard.setSize(0,0);
    setColor(Color.YELLOW);
  }
  /////////////////////////////////////////////////////////////////////////    
  public void fall()
  {
    surfboard.setSize(0,0);
    setColor(Color.ORANGE);
  }     
}//End of Class 