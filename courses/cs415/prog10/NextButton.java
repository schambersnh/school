/**
 * NextButton.java
 * Calls Cryptograms next method
 * 
 * Stephen Chambers
 * 11/9/10
 *
 * 
 */

import wheelsunh.users.*;
import java.awt.Color;

public class NextButton extends TextBox
{
  //---------------- instance variables ------------------------------
  protected CryptogramApp app;  
  
  //----------------------------------------------------------------------------
  /**
   * Constructor that sets the location and takes in a CryptogramApp.
   */
  public NextButton( int x, int y, CryptogramApp app )
  {
    super( x, y );
    super.setText( "Next Quote." );
    this.app = app;
  }
  
  //----------------------------------------------------------------------------
  /**
   * This calls the CryptogramApp's next method when clicked.
   */
  public void mousePressed ( java.awt.event.MouseEvent e )
  {
   app.next();
  }
  
  
}