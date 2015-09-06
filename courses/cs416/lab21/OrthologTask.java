/**
 * OrthologTask -- Find all lines in the file that contain 2 consecutive
 *     words that both start with capital letters and are separated by a ","
 *     Add a line to the output for each such name, but reverse the order
 *     of the names and leave out the ,.
 * 
 * Intended to be used with "names.txt".
 * 
 */
import java.util.regex.*;
import java.util.*;
import java.io.*;
import java.text.*;

public class OrthologTask extends AbstractRegex
{
   //-------------------------- instance variables -----------------------
   ////////////////////////////////////////////////////////////////////////
   //  Define your pattern
   /////////////////////////////////////////////////////////////////////////
   //  remember that you must "escape" the \ because it is inside a Java
   //    literal string. 
   private String pattern = null;
   
   //-------------------------- constructor ------------------------------
   public OrthologTask()
   {
      setRegex( pattern );
      _pattern = Pattern.compile( pattern, 
                                 Pattern.COMMENTS | Pattern.MULTILINE );
   }
   //---------------------------- execute() -----------------------------
   /**
    * Parse lines of the ortholog file to produce output of the form:
    * 
    * brenneri: g1678, g2452, ...
    * briggsae: g24, g567, ....
    * elegans: C27H6.8, R07H5.3, ...
    * remanei: g14185, g26903, ....
    */
   public String execute( String input )
   {    
      ////////////////////////////////////////////////////////////////////
      // Initial code is same as Sample.java
      ////////////////////////////////////////////////////////////////////
      if ( input == null )
         return null;
      StringBuffer results = new StringBuffer();
      _matcher = _pattern.matcher( input );
      while ( _matcher.find() )
      {
         results.append( _matcher.group( 2 ) + " " 
                        + _matcher.group( 1 ) + "\n" );
      }
      _matcher.reset();
      return results.toString();
  }
   //--------------------- main -----------------------------------------
   public static void main( String[] args )
   {
      new RegexApp( "Regex in Java", args );
   }
}  