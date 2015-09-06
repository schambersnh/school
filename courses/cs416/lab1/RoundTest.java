/**
 * RoundTest.java 
 * 
 * This is a Swing application based on the SwingApp class made in cs416 lab1
 * 
 * All the application-specific code is in the DrawPanel class
 * 
 *@author Stephen Chambers
 */

import javax.swing.*;

public class RoundTest extends JFrame
{
   //------------------ constructor ---------------------------------
   /**
    * Construct a JFrame for the application and put a JPanel in it.
    */
   public RoundTest( String title, String[] args )
   {
      super( title );            // specify window title
      this.setSize( 700, 500 );  // define window size
      
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
 
      // All the application specific code is in the DrawPanel constructor.
      // DrawPanel extends JPanel. 

      DrawPanel panel = new DrawPanel( args );   // create graphics in JPanel
      this.add( panel );                         // add it to the frame
      
      this.setVisible( true );  // Initially, JFrame is not visible, make it so.
   }
   
   //------------------------ main -----------------------------------
   public static void main( String[] args )
   {
      RoundTest app = new RoundTest( "Drawing in AWT/Swing", args );
   }
}