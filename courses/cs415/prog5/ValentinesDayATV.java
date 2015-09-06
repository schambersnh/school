/** 
 * ValentinesDayATV.java
 * Builds an ATV Subclass with ValentinesDay Colors
 *
 * Stephen Chambers
 * 10/05/10
 */
import wheelsunh.users.*;
import java.awt.Color;
import java.awt.event.MouseEvent;

public class ValentinesDayATV extends ATV

{
  //----------------Instance Variables-----------------------------------------
  private Rectangle rect;
  //---------------------------------------------------------------------------
  //Two Variable Constructor to set Location of HalloweenATV object
  public ValentinesDayATV(int x, int y)
  {
    super(x,y);
    body.setColor(Color.PINK);
    wheel.setColor(Color.RED);
    wheel2.setColor(Color.RED);
    inFlag.setColor(Color.PINK);
    flag.setFillColor(Color.RED);
  }
  
   //Method called when ValentinesDay Button is clicked
  public void activate()
  {
    rect = new Rectangle(body.getXLocation() + 40, body.getYLocation()+ 10);
    rect.setColor(Color.RED);
    rect.setRotation(45);
    rect.setSize(30,30);
  }
    //Method Called when ValentinesDay Button is unclicked
  public void deactivate()
  {
    rect.setFillColor(Color.PINK);
    rect.setFrameColor(Color.PINK);
  }
}//End of Class