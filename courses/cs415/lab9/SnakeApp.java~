/**
 * SnakeApp S09
 * 
 */

import wheelsunh.users.*;
import java.awt.Color;

public class SnakeApp extends Frame 
{   
   /**
    * The constructor 
    */
   public SnakeApp() 
   {
      Snake s = new Snake( 330, 250 );
    
      // Make the labels
      TextBox rightText = new TextBox("Turn Right");
      rightText.setSize( 80 , rightText.getHeight());
      rightText.setLocation( 200, 470);
      TextBox leftText = new TextBox("Turn Left");
      leftText.setSize( 80 , rightText.getHeight() );
      leftText.setLocation( 300, 470);
      TextBox slitherText = new TextBox("   Slither");
      slitherText.setSize( 80 , rightText.getHeight() );
      slitherText.setLocation( 400, 470);
     
      // Make the  buttons
       
      DrawButton rightButton = new RightButton( 230, 440, s );
      DrawButton leftButton  = new LeftButton( 330, 440, s );
      DrawButton moveButton  = new MoveButton( 430, 440, s );
     
   }
   
   public static void main ( String[] argv ) 
   {
      new SnakeApp();
   }
}
