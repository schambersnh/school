/**
 * RegexApp - this class manages the regular expression processing 
 * 
 * 
 * The application semantics including the graphics generated 
 * by the app are controlled from this class.
 */
import javax.swing.*;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.text.*;
   
public class RegexApp
{
   //--------------------- instance variables ----------------------------
   private JFileChooser     _chooser;
  
   private Pattern          _pattern;
   private Matcher          _matcher;
   
   private String           _regex;    // the currently active reg expr
   private String           _testString;
   
   private PrintWriter      _log;
   
   public boolean           dotFlag = false;
   public boolean           caseInsensitiveFlag = false;
   public boolean           multiLineFlag = true;
   public boolean           commentsFlag = false;
    
   //---------------------- constructor ----------------------------------
   /**
    * The app needs the display reference only so it can update the 
    * DisplayListPanel with new Lists when needed and tell it to redraw
    * when things change. It accesses the GUI.display class variable for this
    */
   public RegexApp( )
   {
      _pattern = null;
      
      File f = new File( "." );
      System.out.println( "File( . ): " + f.getAbsolutePath() );
      
      _chooser = new JFileChooser( new File( "." ));
      _chooser.setCurrentDirectory( new File( "." ));
      try 
      {
         File logFile = new File( "RESET.log" );
         if ( !logFile.exists() )
            logFile.createNewFile();
         
         FileWriter fw = new FileWriter( logFile, true );  // append
         _log = new PrintWriter( fw, true );
         
         log( "-------------------------------", "" );
      }
      catch ( IOException ioe ) 
      {
         System.out.println( "RegexApp catch: " + ioe.getMessage() );
      }
   }
   /**
    * log information in the log file: shorter version.
    */
   private void log( String data )
   {
      _log.println( "**** " + data );
   }
   /**
    * log information in the log file: this is the "long form" 
    * it has been replaced by the 1 argument version for most things.
    */
   private void log( String separator, String data )
   {
      _log.println( separator + dateString() + separator );
      _log.println( data );
      //_log.println( separator + separator + separator );
   }
   /**
    * return the current date/time as a string
    */
   private String dateString()
   {
         Date d = new Date();
         DateFormat fmt = new SimpleDateFormat();
         return fmt.format( d );
   }
      
   //+++++++++++++++++++++ methods invoked by GUI buttons +++++++++++++++++++
   //---------------------- newRegex() -----------------------------
   /**
    * get a new regular expression from dialog box
    */
   public void newRegex( )
   {  
      String regex = JOptionPane.showInputDialog( null, 
                                       "Enter regular expression" );
      setRegex( regex );
   }
   //--------------------- setRegex( String ) ---------------------------
   /**
    * define a new regular expression
    */
   public void setRegex( String regex )
   {
      if ( regex != null && regex.length() > 0 )
      {
         _regex = regex;
         compilePattern();

         GUI.setRegexLabel( _regex );
         GUI.display.setMatch( " " );
      }
      else 
         System.err.println( "Invalid regular expression: " + regex );
   }
   //------------------ compilePattern() ------------------------
   /**
    * The pattern or a flag has changed, compile the pattern.
    */
   public void compilePattern()
   {
      try
      {
         log( "Regex --> " + _regex + " <--" + "Flags: " + flagsToString() );
         _pattern = Pattern.compile( _regex, regexFlags() );
         if ( _testString != null )
            _matcher = _pattern.matcher( _testString );         
      }
      catch ( PatternSyntaxException pse )
      {
         JOptionPane.showMessageDialog( null, pse.getMessage() );
      }
   }
   //------------------ regexFlags() ------------------------
   /**
    * create the flag variable for regex compilation 
    */
   public int regexFlags()
   {
      int flags = 0;
      if ( multiLineFlag )
         flags |= Pattern.MULTILINE;
      if ( dotFlag )
         flags |= Pattern.DOTALL;
      if ( caseInsensitiveFlag )
         flags |= Pattern.CASE_INSENSITIVE;
      if ( commentsFlag )
         flags |= Pattern.COMMENTS;
      return flags;
   }   
   //------------------ flagsToString() ------------------------
   /**
    * create the flag variable for regex compilation 
    */
   public String flagsToString()
   {
      String flags = "";
      if ( multiLineFlag )
         flags += "MLine ";
      if ( dotFlag )
         flags += "DotAll ";
      if ( caseInsensitiveFlag )
         flags += "NoCase ";
      if ( commentsFlag )
         flags += "Comments";
      return flags;
   }
      
   //------------------ newInputString() ------------------------
   /**
    * Enter an input string using dialog box
    */
   public void newInputString()
   {
      String newInput = JOptionPane.showInputDialog( null, 
                                       "Enter input string" );
      newData( newInput );
      log( "Input --> " + newInput + " <--" );
   }
   //------------------- newData( String ) -------------------
   /**
    * set up new input data
    */
   public void newData( String input )
   {
      if ( input != null && input.length() > 0 )
      {
         _testString = input;
         GUI.display.setInput( _testString );   
         GUI.display.setMatch( " " );
         if ( _pattern != null )
            _matcher = _pattern.matcher( _testString );
      }      
   }
   //------------------ findAll() ------------------------
   /**
    * find all occurrences of the pattern in the input
    */
   public void findAll()
   {
      if ( _pattern != null )
      {
         _matcher = _pattern.matcher( _testString );
         
         String results = "";
         boolean found = _matcher.find();
         int count = 0;
         while ( found )
         {
            results += _matcher.group() + "\n--------------------\n";
            found = _matcher.find();
            count++;
         }
         GUI.setFindCountLabel( "" + count );
         GUI.display.setMatch( results );
         //log( "@@@@@@@@@@@@@@@@@@@", results );
         log( "FindAll match count: " + count );
      }
   }
   //------------------ split() ------------------------
   /**
    * split the input string according to the r.e.
    */
   public void split()
   {
      if ( _pattern != null )
      {
         String[] parts = _pattern.split( _testString );
         String splitMatch = "";
         for ( int i = 0; i < parts.length; i++ )
            splitMatch += parts[ i ] + "\n";
         GUI.display.setMatch( splitMatch );
         //log( "====================", splitMatch );
         log( "Split" );
      }
   }
   //---------------------- newInputFile() -----------------------------
   /**
    * read input strings from a file 
    */
   public void newInputFile( )
   {
      int returnval = _chooser.showOpenDialog( null );
      if ( returnval == JFileChooser.APPROVE_OPTION )
      {
         File newIn = _chooser.getSelectedFile();
         log( "File --> " + newIn.getName() + " <--" );
         StringBuffer contents = new StringBuffer();
         try
         {
            Scanner in = new Scanner( newIn );
            while ( in.hasNextLine() )
               contents.append( in.nextLine() + "\n" );
            newData( new String( contents ));
         }
         catch ( IOException iox )
         {
            System.out.println( "newInputFile::IOException " 
                                  + iox.getMessage() );
         }
      }
   }
   //------------------ find() ------------------------
   /**
    * Find next match
    */
   public void find()
   {
      if ( _pattern != null && _testString != null )
      {         
         log( "Find" );
         
         String results = "";
         if ( _matcher.find() )
            results = _matcher.group();
         GUI.display.setMatch( results );
      }
   }
   //------------------ groups() ------------------------
   /**
    * Return the groups matched in previous find
    */
   public void groups()
   {
      log( "Groups" );
      if ( _matcher != null )
      {
         String results;
         System.out.println( "Matcher group count: " + _matcher.groupCount() );
         if ( _matcher.hitEnd() )
            results = "**** No match ****";
         else if ( _matcher.groupCount() <= 0  )
            results = "**** No group match ****";
         else
         {
            String wholeMatch = _matcher.group( 0 );
            results = "Entire match: " + wholeMatch + "\n";
            // matcher.groupCount does not include "group" 0
            //   so start at 1 and continuation condition is i <= count
            for ( int i = 1; i <= _matcher.groupCount(); i++ )  
            {
               results += i + ": " + _matcher.group( i ) + "\n";
            }
         }
         GUI.display.setMatch( results );
      }
   }
   //---------------------- reset() -----------------------------
   /**
    * reset the regular expression processing on the current input string.
    */
   public void reset( )
   {  
      if ( _matcher == null )
         System.err.println( "*** reset Error *** matcher is not defined." );
      else
         _matcher.reset();
      GUI.display.setMatch( " " );
   }
   
   //++++++++++++++++++++++ utility methods ++++++++++++++++++++++++++++++
   //----------------------- stringToInt( String ) -----------------------
   /**
    * Convert string to integer
    */
   private int stringToInt( String str, int defaultVal )
   {
      try 
      {
         return Integer.parseInt( str );
      }
      catch ( NumberFormatException nfe )
      {
         String input = JOptionPane.showInputDialog( null, 
                           "Invalid integer input: " + str + ". No change." );
         return defaultVal;
      }
   }
   //--------------------- main -----------------------------------------
   public static void main( String[] args )
   {
      new RESET( "RESET", args );
   }
}
