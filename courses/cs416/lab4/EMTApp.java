/**
 * EMTApp.java -- CS416 P2 -- Spring 2009
 * 
 * This is a Swing application based on the pattern described in Chapter 7 of
 * Sanders and van Dam, Object-Oriented Programming in Java.
 * 
 * All the application-specific code is in the DrawPanel class
 * 
 * @author rdb
 * January 2008
 */

import javax.swing.*;

public class EMTApp extends JFrame
{
   //-------------------- class variables -------------------------
   //---- These are actually "application" parameters, specified
   //---- on the command line and available to any class
   //
   public static int  highSpeed    = 30;
   public static int  normalSpeed  = 10;
   public static int  numHospitals = 3; 
   
   //------------------ constructor ---------------------------------
   /**
    * Construct a JFrame for the application and put a JPanel in it.
    */
   public EMTApp( String title, String[] args )
   {
      super( title );            // specify window title
      this.setSize( 700, 500 );  // define window size
      
      this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
 
      // All the application specific code is in the DrawPanel constructor.
      // DrawPanel extends JPanel. 

      highSpeed    = getArg( args, 0, 30 ); // arg 0 is high speed
      normalSpeed  = getArg( args, 1, 10 ); // arg 1 is normal speed
      numHospitals = getArg( args, 2, 3 );  // arg 2 is number of hospitals
              
      EMTPanel panel = new EMTPanel( this ); // Put graphics in JPanel
      this.add( panel );                         // add it to the frame
      
      this.setVisible( true );  // Initially, JFrame is not visible, make it so.
   }
   
   //------------- class methods ---------------------------------
   /**
    * This is a utility method for accessing one of the command line
    * string arguments and converting it to an int. It accepts the 
    * entire array of arguments in String form, an integer indicating 
    * which is to be converted to an int and a default value to be 
    * returned if this argument was not on the command line, or if 
    * the specified String is not a valid integer representation.
    */
   private static int getArg( String[ ] args, int which, int defaultVal )
   {
      try
      {
         // The Integer class has a class method that will convert a String
         //   to an int -- if it is a valid representation of an  integer. 
         return Integer.parseInt( args[ which ] );
      }
      catch ( ArrayIndexOutOfBoundsException oob )
      {
         // If there is no args[ which ] element, Java "throws" an exception.
         // This code "catches" the exception and handles it gracefull.
         // In this case, it is not an error. The parameter is optional
         // and there is a specified default value that is returned.
      }
      catch ( NumberFormatException nfe )
      {
         // If the string is not a valid representation of an integer
         // (such as 4Qd3), Integer.parseInt throws a "NumberFormatException".
         // Again, this code catches the exception, gives an error message
         // and uses the default value.
         
         System.err.println( "Error: improper command line argument " 
                           + which + " = " + args[ which ] 
                           + ".  Should be an integer; using default value: "
                           + defaultVal );
      }  
      return defaultVal;
   }
   //------------------------ main -----------------------------------
   public static void main( String[] args )
   {
      EMTApp app = new EMTApp( "Drawing in AWT/Swing", args );
   }
}
