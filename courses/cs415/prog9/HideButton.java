/**
 *HideButton.java
 * Creates a Hide Button 
 * 
 * @Author Stephen Chambers
 * 10/27/10
 * */
import java.awt.Color;
import wheelsunh.users.*;

public class HideButton extends TextBox
{
  protected MastermindApp app;

  //------------------------Constructor-------------------------------------
  public HideButton( int x, int y, MastermindApp app)
  {
    setLocation( x, y );
    setFillColor( Color.RED );
    setSize( 100, 70 );
    this.app = app;
 
      
  }
  //----------------------------------------------------------------------------
  /*
   * Shows the Code
   * */
  public void mousePressed( java.awt.event.MouseEvent e )
  {
    app.showCode();
  }
  public void mouseReleased(java.awt.event.MouseEvent e)
  {
    app.hideCode();
  }
} //End of Class