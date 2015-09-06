/**
 * SwingApp -- template for Swing based application main class.
 */

import java.awt.*;
import javax.swing.*;

public class SwingApp extends JFrame
{
   //------ Constructor ---------------
   public SwingApp( String title )
   {
      super( title );
      this.setSize( 700, 500 );
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      
      // The JPanel class referenced here might be the only 
      //   application specific line in this class:
      this.add( new DrawPanel() );
      
      this.setVisible( true ); 
   } 
   
   //-------- main ---------------------
   public static void main(String[] args)
   {
      SwingApp app = new SwingApp( "A Swing Application" );
   }
}