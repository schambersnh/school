/**
 * RegexManager - this class manages the regular expression processing 
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
import javax.swing.filechooser.*;
   
public class RegexManager
{
   //--------------------- instance variables ----------------------------
   private JFileChooser     _chooser;
  
   private Pattern          _pattern;
   
   private String           _regexString;    // the currently active reg expr
   private String           _testString;
   
   private PrintWriter      _log;
   
   private AbstractRegex     _regex;
        
   //---------------------- constructor ----------------------------------
   /**
    * The app needs the display reference only so it can update the 
    * DisplayListPanel with new Lists when needed and tell it to redraw
    * when things change. It accesses the GUI.display class variable for this
    */
   public RegexManager( )
   {
      File f = new File( "." );
      System.out.println( "File( . ): " + f.getAbsolutePath() );
      
      _chooser = new JFileChooser( new File( "." ));
      _chooser.setCurrentDirectory( new File( "." ));
      _chooser.setFileFilter( new TextFilter() );
   }
   //+++++++++++++++++++++ methods invoked by GUI buttons +++++++++++++++++++
   //---------------------- definePattern( int ) -----------------------------
   /**
    * define a new regular expression 
    */
   public void definePattern( int code )
   {
      switch ( code )
      {
         case 0:
            System.out.println( "Sample" );
            _regex = new Sample();
            GUI.setRegexLabel( _regex.getPatternString() );          
            break;
         case 1:
            System.out.println( "Name reverse" );
            _regex = new NameReverseTask();
            GUI.setRegexLabel( _regex.getPatternString() );          
            break;
         case 2:
            System.out.println( "Name replace" );
            _regex = new NameReplaceTask();
            GUI.setRegexLabel( _regex.getPatternString() );          
            break;
         case 3:
            System.out.println( "DNAHeader" );
            _regex = new DNAHeaderTask();
            GUI.setRegexLabel( _regex.getPatternString() );          
            break;
         case 4:
            System.out.println( "Ortholog" );
            _regex = new OrthologTask();
            GUI.setRegexLabel( _regex.getPatternString() );          
            break;
         default:
            System.out.println( "Undefined regex processor" );
      }
   }
   //------------------ newInputString() ------------------------
   /**
    * Enter an input string in a dialog box
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
   }
   //---------------------- newInputFile() -----------------------------
   /**
    * read input strings from a file, concatenate them into a single
    * input string.
    */
   public void newInputFile( )
   {
      int returnval = _chooser.showOpenDialog( null );
      if ( returnval == JFileChooser.APPROVE_OPTION )
      {
         File newIn = _chooser.getSelectedFile();
         StringBuffer contents = new StringBuffer();
         try
         {
            Scanner in = new Scanner( newIn );
            while ( in.hasNextLine() )
               contents.append( in.nextLine() + "\n" );
            _testString = new String( contents );
            GUI.display.setInput( _testString );
            GUI.display.setMatch( " " );
         }
         catch ( IOException ioe )
         {
            System.out.println( "newInputFile::IOException " + ioe.getMessage() );
         }
      }
   }
   //------------------ execute() ------------------------
   /**
    * Find next match
    */
   public void execute()
   {
      System.out.println( "Executing: " + _regex.getPatternString());
      String results = _regex.execute( _testString );
      GUI.display.setMatch( results );
   }
   //+++++++++++++++++++++++++ inner class +++++++++++++++++++++++++++++++
   //---------------------------- TextFilter -----------------------------
   /**
    * This class is used with FileChooser to limit the choice of files
    * to those that end in *.txt
    */
   public class TextFilter extends javax.swing.filechooser.FileFilter
   {
      public boolean accept( File f ) 
      {
         if ( f.isDirectory() || f.getName().matches( ".*txt" ) )
            return true;
         return false;
      }
      public String getDescription()
      {
         return "*.txt files";
      }
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
      new RegexApp( "Regex in Java", args );
   }
}
