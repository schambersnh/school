/**
 * AbstractRegex -- a framework for defining a particular task to be performed
 *              that uses a regular expression
 */
import java.util.regex.*;
import java.util.*;
import java.io.*;
import java.text.*;

abstract public class AbstractRegex
{
   //-------------------- instance variables ----------------------------
   protected String  _regex = null;   // the regular expression
   
   protected Matcher _matcher = null; // the Matcher is created by subclass
   protected Pattern _pattern = null; // the Pattern is created by subclass
   
   //--------------------- setRegex( String ) ------------------------------
   public void setRegex( String re )
   {
      _regex = re;
   }
   
   //-------------------- getPatternString() -------------------------------
   /**
    * return the regular expression as a String
    */
   public String getPatternString()
   {
      return _regex;
   }
    
   //---------------------- execute( String ) -------------------------------------
   /**
    * Perform the desired task. The result of the task is an array of
    * String that represents the "output" of the task.
    * 
    * Depending on the tast, it might be useful to call execute() many times
    * on the same input data. If it the task does not allow multiple calls, 
    * or if there is no longer any input left to be processed, execute should
    * return "null". 
    */
   abstract public String execute( String input );
   
   //--------------------- main -----------------------------------------
   public static void main( String[] args )
   {
      new RegexApp( "Regex in Java", args );
   }
}