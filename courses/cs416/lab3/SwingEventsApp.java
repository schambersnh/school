/**
 * SwingEventsApp.java - SwingApp for Animation lab
 * 
 */
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class SwingEventsApp extends JFrame 
{
   //----------------- Constructor -------------------------
   public SwingEventsApp( String title ) 
   {
      super( title );
      this.setSize( 600, 450 );
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      this.add( new EventsPanel( this ) );
      this.setVisible( true );
   }
   
   //------------------ main -------------------------------
   public static void main( String [ ] args ) 
   {
      SwingEventsApp app = new SwingEventsApp( "SwingEventsApp" );
   }
}