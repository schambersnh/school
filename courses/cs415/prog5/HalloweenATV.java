/** 
 * HalloweenATV.java
 * Builds an ATV subclass with Halloween Colors
 *
 * Stephen Chambers
 * 10/05/10
 */
import wheelsunh.users.*;
import java.awt.Color;
import java.awt.event.MouseEvent;

public class HalloweenATV extends ATV
{
  //------------------------Instance Variables---------------------------------
  private Ellipse pumpkin;
  //---------------------------------------------------------------------------
  //Two Variable Constructor to set Location of HalloweenATV object
  public HalloweenATV(int x, int y)
  {
    super(x,y);
    body.setColor(Color.BLACK);
    wheel.setColor(Color.ORANGE);
    wheel2.setColor(Color.ORANGE);
    inFlag.setColor(Color.ORANGE);
    flag.setFillColor(Color.BLACK);
  }
  //Method called when HalloweenButton is clicked
  public void activate()
  {
   pumpkin = new Ellipse(body.getXLocation() + 35, body.getYLocation()+ 5);
   pumpkin.setColor(Color.ORANGE);
   pumpkin.setSize(40, 40);
  }
  
  //Method Called when HalloweenButton is unclicked
  public void deactivate()
  {
    pumpkin.setFrameColor(Color.BLACK);
    pumpkin.setFillColor(Color.BLACK);
  }
}//End of Class