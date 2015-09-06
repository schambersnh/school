/**
 * SubButton.java
 * Nick Hodgkiss
 * 11/9/10
 * Program10
 * 
 * This is a button that will call the CryptogramApp's substitution method.
 * 
 */

import wheelsunh.users.*;
import java.awt.Color;

public class SubButton extends TextBox
{
  //---------------- instance variables ------------------------------
  protected CryptogramApp app;  
  
  //----------------------------------------------------------------------------
  /**
   * Constructor sets the location and takes in a CryptogramApp.
   */
  public SubButton( int x, int y, CryptogramApp app )
  {
    super( x, y );
    super.setText( "Substitute." );
    this.app = app;
  }
  
  //----------------------------------------------------------------------------
  /**
   * This just calls CryptogramApp's substitute method when the button is
   * clicked.
   */
  public void mousePressed ( java.awt.event.MouseEvent e )
  {
   app.substitute();
  }
  
  
}