/** 
 * BalloonUtilities.java:
 *  
 *mlb 10/2008
 */

import java.util.*;
import javax.swing.JOptionPane;

public class BalloonUtilities 
{  

  public static final int DEFAULT_SPEED = 30;
   
  //---------------------------------------------------------
  //
  //  Reads and returns an integer from a JInputDialog
  //  If the is no integer input then return 0
  //
   public static int readInt( String prompt )
   {
      int returnValue = 0;
      
      String dialogInput =  JOptionPane.showInputDialog( null, prompt ); 
      if ( dialogInput != null )
      {   
         Scanner scan = new Scanner( dialogInput );
                  
         if ( scan != null && scan.hasNextInt( ) )
            returnValue = scan.nextInt( );
      }
      
      return returnValue;  
   }
   
   
  //---------------------------------------------------------
  // sleep
  // 
  public static void sleep( int milliseconds ) 
  {
      try
      {
        Thread.sleep( milliseconds );
      }
      catch ( java.lang.InterruptedException e ) 
      {  }
  }
  //---------------------- intArg( String[], int, int ) -----------------
    /**
     * This is a utility method for accessing one of the command line
     * string arguments and converting it to an int. It accepts the 
     * entire array of arguments in String form, an integer indicating 
     * which is to be converted to an int and a default value to be 
     * returned if this argument was not on the command line, or if 
     * the specified String is not a valid integer representation.
     */
    public static int intArg( String[ ] args, int which, int defaultVal )
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
                                   + ".  It should be an integer; using default value: "
                                   + defaultVal );
        }  
        return defaultVal;
    }


}//End of Class BalloonUtilities