/**
 * Dots.java
 * Provides a prototype 'dot' on a standard Die 
 * 
 * @Author Stephen Chambers
 * 10/27/10
 * */
import java.awt.Color;
import wheelsunh.users.*;

public class Dots extends Ellipse
{
  public Dots(int x, int y)
  {
    setLocation(x, y);
    setColor(Color.BLACK);
    setSize(15, 15);
  }
  
}//End of Class