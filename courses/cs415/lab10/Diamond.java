/** 
 * Diamond.java
 * Creates Diamonds with the ability to fall  
 * 
 * @Stephen Chambers
 * 9/30/2010
 * 
 */
import java.awt.Color;
import wheelsunh.users.*;

public class Diamond extends ShapeGroup implements Faller
{
  //-------------------------------Instance Variables----------------------
private int fall = 50;
private Rectangle rect;
//-----------------------------------------------------------------------
  public Diamond(int x, int y)
  {
    rect = new Rectangle(x,y);
    rect.setRotation(45);
  }
  
   public void setFall(int f)
   {
     fall = f;
   }
   
   public void mouseClicked(java.awt.event.MouseEvent e)
   {
     setLocation(getXLocation(), getYLocation() + fall);
   }
  
   public static void main(String [] args)
   {
     Frame f = new Frame();
     Diamond diamond1 = new Diamond(50, 50);
     Diamond diamond2 = new Diamond(150,150);
   }
   
}