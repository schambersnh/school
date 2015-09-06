import wheelsunh.users.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.util.*;

public class Missile extends ShapeGroup
{
  //class variables
  public static int width = 4;
  public static int height = 10;
  
  //instance
  private Rectangle body;
  private int dY;
  
  public Missile(int x, int y, int diffY)
  {
    body = new Rectangle(x, y);
    body.setSize(width, height);
    body.setColor(Color.BLACK);
    add(body);
    setLocation(x, y);
    dY = diffY;
  }
  public Point getTop()
  {
    return getLocation(); 
  }
  public Point getBottom()
  {
    Point p = getLocation();
    Point returnPoint = new Point(p.x, p.y + height);
    return returnPoint;
  }
  public void step()
  {
    setLocation(getXLocation(), getYLocation() + dY); 
  }

public static void main(String[] args)
{
  Frame f = new Frame();
  Missile m = new Missile(100, 100, 10);
  System.out.println(m.getLocation());
  m.step();
  m.step();
  System.out.println(m.getLocation());
}
}