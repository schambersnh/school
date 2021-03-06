/**
 * DNAHeaderTask -- Find all lines in the file that are sequence headers
 * 
 * Return one String for each header line,
 * where the String starts with the sequence id followed by a ": ", 
 * followed by the rest of the header.
 * E.g. 
 *     >seqId3 more information
 * is returned as:
 *     seqId3: more informaton
 */
import java.util.regex.*;
import java.util.*;
import java.io.*;
import java.text.*;

public class DNAHeaderTask extends AbstractRegex
{
   //-------------------------- instance variables -----------------------
   ////////////////////////////////////////////////////////////////////////
   //  Define your pattern
   /////////////////////////////////////////////////////////////////////////
   //  remember that you must "escape" the \ because it is inside a Java
   //    literal string. 
   private String pattern = "(>\\S*)(.*)";
   
   //-------------------------- constructor ------------------------------
   public DNAHeaderTask()
   {
      setRegex( pattern );
      _pattern = Pattern.compile( pattern, 
                                 Pattern.COMMENTS | Pattern.MULTILINE );
   }
   //---------------------------- execute() -----------------------------
   /**
    * Find the lines representing sequence headers; and print just these
    * (after a little manipulation).
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
         results.append( _matcher.group( 1 ) + ":" 
                        + _matcher.group( 2 ) + "\n" );
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