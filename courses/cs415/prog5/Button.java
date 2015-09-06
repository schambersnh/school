/** 
 * Button.java
 * Builds a button used for calling methods of ATVs
 *
 * Stephen Chambers
 * 10/05/10
 */
import wheelsunh.users.*;
import java.awt.Color;
import java.awt.event.MouseEvent;

public class Button extends TextBox
{
  //-------------------------------Instance Variables--------------------------
private ATV myATV;
//-----------------------------------------------------------------------------
//Three parameter constructor accepting an ATV so the Button 'knows' which 
//activate or deactivate method to call

  public Button(int x, int y, ATV atv)
  {
   setLocation(x, y);
   myATV = atv;
  }
 //Calls the activate method of the respective ATV passed 
  public void mousePressed(java.awt.event.MouseEvent e)
  {
   myATV.activate();
  }
   //Calls the deactivate method of the respective ATV passed 
  public void mouseReleased(java.awt.event.MouseEvent e)
  {
   myATV.deactivate();
  }
}//End of Class
