/**
 * SwingApp.java -- CS416 Lab 1 -- Spring 2010
 * 
 * This is a Swing application based on the pattern described in Chapter 7 of
 * Sanders and van Dam, Object-Oriented Programming in Java.
 * 
 * All the application-specific code is in the DrawPanel class
 * 
 * @author rdb
 * Modified by Stephen Chambers 1/27/2011 for cs416 Lab1
 */

import javax.swing.*;

public class SwingApp extends JFrame
{
   //------------------ constructor ---------------------------------
   /**
    * Construct a JFrame for the application and put a JPanel in it.
    */
   public SwingApp( String title, String[] args )
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
      SwingApp app = new SwingApp( "Drawing in AWT/Swing", args );
   }
}