import java.awt.Color;
import wheelsunh.users.*;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class Slider  
{
  private Rectangle rect1, rect2;
  Point lastMousePosition;
  int myX = 0;
  int myY = 0;
  
  public Slider(int x, int y)
  {
    rect1 = new Rectangle(x, y);
    rect1.setSize(100, 10);
    rect1.setColor(Color.WHITE);
    
    rect2 = new Rectangle(x, y);
    rect2.setSize(100, 10);
    rect2.setColor(Color.RED);
    
    
  }
  public void mousePressed(MouseEvent me)
  {
    lastMousePosition = me.getPoint();
  }
  public void mouseDragged(MouseEvent me)
  {
    Point curMouse = me.getPoint();
    myX =  curMouse.x - lastMousePosition.x;
    
    rect2.setSize( rect2.getXLocation() + myX, rect2.getYLocation() );   
    lastMousePosition = curMouse; 
  }
  
}