/**
 * Log class: a static class to help testing
 * 
 * @author rdb
 * 11/11/10
 */
import java.io.*;

public class Log
{
   //-------------------- class variables -------------------------------
   public  enum Level { INFO, TRACE, WARNING, ERROR, SEVERE, FATAL };   
   public  static Level reportLevel = Level.INFO;  // report msgs at or above 
   
   public  static boolean      traceAll = false;
   public  static String       fileName = null;
   
   private static PrintWriter  fileOut = null;   // can be set to file output  
   private static StringBuffer traceKeys = new StringBuffer();
   
   //--------------------- openFile( String ) -------------------------
   public static void openFile( String logger )
   {
      try
      {
         fileOut = new PrintWriter( new File( logger ) );
         fileOut.println( "Open fileOut: " + logger );
      }
      catch ( IOException ioex )
      {
         output( Level.ERROR, System.err, "Can't open log file: " + logger );
      }
   }
   //--------------------- closeFile() -------------------------
   public static void closeFile()
   {
      fileOut.close();
      fileOut = null;
   }
   //--------------------- info ---------------------------------------
   public static void info( String msg )
   {
      output( Level.INFO, System.out, msg );
   }
   //--------------------- trace ---------------------------------------
   public static void trace( String key, String msg )
   {
      if ( traceAll || traceKeys.indexOf( ":" + key + ":" ) >= 0 )
         output( Level.TRACE, System.out, msg );
   }
   //--------------------- warnng ---------------------------------------
   public static void warning( String msg )
   {
      output( Level.WARNING, System.err, "*** Warning *** " + msg );
   }
   //--------------------- error ---------------------------------------
   public static void error( String msg )
   {
      output( Level.ERROR, System.err, "*** ERROR *** " + msg );
   }
   //--------------------- fatal ---------------------------------------
   public static void fatal( String msg )
   {
      output( Level.FATAL, System.err, "*** FATAL ERROR *** " + msg );
      System.exit( -1 );
   }
   //--------------------- addTraceKey ---------------------------------
   public static void addTraceKey( String key )
   {
      traceKeys.append( ":" + key + ":" );
   }
   //--------------------- removeTraceKey ------------------------------
   public static void removeTraceKey( String key )
   {
      int start = traceKeys.indexOf( ":" + key + ":" );
      if ( start >= 0 )
         traceKeys.replace( start, start + key.length() + 2, "" );
   }
   //-------------------- output ---------------------------------------
   private static void output( Level level, PrintStream out, String msg )
   {
      //if ( level.compareTo( reportLevel ) >= 0 )
      {
         if ( out != null )  // terminal output
            out.println( msg );
         if ( fileOut != null )
         {
            fileOut.println( msg ); 
            fileOut.flush();
         }
      }
   }
   //--------------------- main ---------------------------------------
   public static void main( String[] args )
   {
      addTraceKey( "a" );
      addTraceKey( "able" );
      addTraceKey( "happy" );
      
      removeTraceKey( "happy" );
      trace( "able", "Able test" );
   }
}