/*
 * Pellet.java
 * @author Stephen Chambers
 * 
 * Creates a pellet (small black ellipse) that knows about where it needs to go
 * and has the ability to move
 * 
 * 
 * */
import java.awt.*;
public class Pellet extends JEllipse
{
  double _dx; 
  double _dy;
  public Pellet()
  {
    super();
    setColor(Color.BLACK);
    setSize(10, 10);
  }
  public Pellet(int x, int y, double dx, double dy)
  {
    super(x, y);
    setColor(Color.BLACK);
    setSize(10, 10);
    _dx = dx;
    _dy = dy;
  }
  public void move(double dx, double dy)
  {
    setLocation(getXLocation() + (int)dx, getYLocation() + (int)dy);
  }
  public double getdX()
  {
    return _dx;
  }
  public double getdY()
  {
    return _dy;
  }
  
}