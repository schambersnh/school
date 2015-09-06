/**
 * HideShowButton.java
 * Displays a button that shows/hides
 * 
 * Stephen Chambers
 * 11/9/10
 * 
 * 
 * 
 */

import wheelsunh.users.*;
import java.awt.Color;

public class HideShowButton extends TextBox
{
  //----------------Instance Variables ------------------------------
  protected CryptogramApp app;  
  protected int count = 0;
  
  //----------------------------------------------------------------------------
  /**
   * Constructor
   */
  public HideShowButton( int x, int y, CryptogramApp app )
  {
    super( x, y );
    super.setText( "Show / Hide  Answer." );
    this.app = app;
  }
  
  //----------------------------------------------------------------------------
  /**
   * Shows and Hides button
   */
  public void mousePressed ( java.awt.event.MouseEvent e )
  {
    if( count % 2 == 0 )    
      app.show();
    else
      app.hide();
    
  }
  
  
}