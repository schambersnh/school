/**
 * NameReplaceTask -- Find all lines in the file that contain 2 consecutive
 *     words that both start with capital letters and are separated by a ","
 *     Return those lines, but with the 2 matched words reversed and the ","
 *     removed.
 * 
 * Intended to be used with "names.txt".
 * 
 */
import java.util.regex.*;
import java.util.*;
import java.io.*;
import java.text.*;

public class NameReplaceTask extends AbstractRegex
{
   //-------------------------- instance variables -----------------------
   ////////////////////////////////////////////////////////////////////////
   //  Define your pattern
   /////////////////////////////////////////////////////////////////////////
   //  remember that you must "escape" the \ because it is inside a Java
   //    literal string. 
   private String pattern = " ([A-Z][a-z]+), \\s+ ([A-Z][a-z]+) ";
   
   //-------------------------- constructor ------------------------------
   public NameReplaceTask()
   {
      setRegex( pattern );
      _pattern = Pattern.compile( pattern, 
                                 Pattern.COMMENTS | Pattern.MULTILINE );
   }
   //---------------------------- execute() -----------------------------
   /**
    * Find apparent Lastname, Firstname patterns, 
    *         replace with Firstname Lastname
    * Reproduce the entire file with these name patters reversed.
    */
   public String execute( String input )
   {    
      ////////////////////////////////////////////////////////////////////
      // Initial code is same as Sample.java
      //   Now, you should use the matcher.appendReplacement and
      //   matcher.appendTail methods to reproduce the input with edits
      ////////////////////////////////////////////////////////////////////
      if ( input == null )
         return null;
      StringBuffer results = new StringBuffer();
      _matcher = _pattern.matcher( input );
      while ( _matcher.find() )
      {
       _matcher.appendReplacement(results, "$2 $1");
      }
      _matcher.appendTail(results);
      _matcher.reset();
      return results.toString();
  }
   //--------------------- main -----------------------------------------
   public static void main( String[] args )
   {
      new RegexApp( "Regex in Java", args );
   }
}  