import wheelsunh.users.*;
import java.awt.Color;
public class Display
{
  private Rectangle rect1, rect2;
  
  public Display(int x, int y, double p)
  {
    rect1 = new Rectangle(x, y);
    rect1.setSize(100, 10);
    rect1.setColor(Color.WHITE);
    rect2 = new Rectangle(x, y);
    rect2.setSize(100, 10);
    rect2.setColor(Color.RED);
  }
  
  public void setValue(double p)
  {
    rect2.setSize((int)(p/100.0), 10);
  }
  
  
}

