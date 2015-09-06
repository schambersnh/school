/**
 * RegexApp - this class manages the regular expression processing 
 * 
 * 
 * The application semantics including the graphics generated 
 * by the app are controlled from this class.
 */
import javax.swing.*;
import javax.swing.filechooser.*;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import java.text.*;
   
public class RegexApp
{
   //--------------------- instance variables ----------------------------
   private JFileChooser     _chooser;
  
   private Pattern          _pattern = null;
   private String           _regexString;    // the currently active reg expr
   private String           _testString;
   
   private PrintWriter      _log;
   
   private Regex            _regex;
   
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
      _regex = new Regex();     // create student's class
      
      File f = new File( "." );
      System.out.println( "File( . ): " + f.getAbsolutePath() );
      
      _chooser = new JFileChooser( new File( "." ));
      _chooser.setCurrentDirectory( new File( "." ));
      FileNameExtensionFilter filter = 
         new FileNameExtensionFilter( "Text files", "txt" );
      _chooser.setFileFilter( filter );
      
      //_chooser.setAcceptAllFileFilterUsed( false );
      //_chooser.setFileHidingEnabled( true );
      
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
      _regexString = JOptionPane.showInputDialog( null, 
                                       "Enter regular expression" );
      setRegex( _regexString );
   }
   //---------------------- setRegex( String ) -----------------------------
   /**
    * get a new regular expression from GUI
    */
   public void setRegex( String regexString )
   {  
      _regexString = regexString;
      if ( _regexString != null && _regexString.length() > 0 )
      {
         _regex.setRegex( _regexString );
         
         GUI.setRegexLabel( _regexString );
         log( "Regex --> " + _regexString + " <--" );
         GUI.display.setMatch( " " );
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
   //------------------ setFlags() ------------------------
   /**
    * The pattern or a flag has changed, compile the pattern.
    */
   public void setFlags()
   {
      try
      {
         log( "Regex --> Flags: " + flagsToString() );
         _regex.setFlags( regexFlags() );
      }
      catch ( PatternSyntaxException pse )
      {
         JOptionPane.showMessageDialog( null, pse.getMessage() );
      }
      reset();
   }
      
   //------------------ newInputString() ------------------------
   /**
    * Enter an input string using dialog box
    */
   public void newInputString()
   {
      _testString = JOptionPane.showInputDialog( null, 
                                       "Enter input string" );
      if ( _testString != null && _testString.length() > 0 )
      {
         GUI.display.setInput( _testString );   
         GUI.display.setMatch( " " );
      }
      _regex.setInput( _testString );
      log( "Input --> " + _testString + " <--" );
    }
    //------------------ findAll() ------------------------
   /**
    * find all occurrences of the pattern in the input
    */
   public void findAll()
   {
      log( "FindAll" );
      GUI.display.setMatch( _regex.findAll() );
   }
   //------------------ split() ------------------------
   /**
    * split the input string according to the r.e.
    */
   public void split()
   {
      log( "Split" );
      String results = _regex.split();
      GUI.display.setMatch( results );
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
            _testString = new String( contents );
            GUI.display.setInput( _testString );
            GUI.display.setMatch( " " );
            _regex.setInput( _testString );
          }
         catch ( IOException ioe )
         {
            System.out.println( "newInputFile::IOException " + ioe.getMessage() );
         }
      }
   }
   //------------------ find() ------------------------
   /**
    * Find next match
    */
   public void find()
   {
      log( "Find" );
      GUI.display.setMatch( _regex.find() );
   }
   //------------------ groups() ------------------------
   /**
    * Return the groups matched in previous find
    */
   public void groups()
   {
      log( "Groups" );
      String results;
      
      String wholeMatch = _regex.group( 0 );
      if ( wholeMatch == null )
         results = "**** No match ****";
      else
      {
         results = "Entire match: " + wholeMatch + "\n";
         int i = 1;
         String group = _regex.group( i );
         while ( group != null )   
         {
            results += i + ": " + group + "\n";
            group = _regex.group( ++i );
         }
      }
      GUI.display.setMatch( results );
   }
   //---------------------- reset() -----------------------------
   /**
    * reset the regular expression processing on the current input string.
    */
   public void reset( )
   {  
      _regex.reset();
      GUI.display.setMatch( " " );
   }
   
   //++++++++++++++++++++++ utility methods +++++++++++++++++++++++++++++++++++++
   //----------------------- stringToInt( String ) -----------------------------
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
      new RegexInJava( "Regex in Java", args );
   }
}
