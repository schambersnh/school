/**
 * Log class: a static class to help testing
 * 
 * @author rdb
 * Created: 11/11/10
 * 01/21/11 rdb: Added file feature
 *               Added type counts, but don't report them yet
 */
import java.io.*;

public class Log
{
   //-------------------- class variables -------------------------------
   // Supports 5 kinds of messages to be logged, TRACE is different from 
   //    the other 4:
   //   TRACE - each trace message has an associated String "key"; the
   //           logging of a trace message is controlled by 
   //           enabling/disabling all messages with the same key.
   //   INFO - intended for messages that reflect the status of the
   //           system, or input.
   //   WARNING - indication of possible error
   //   ERROR   - a clear error, but we assume that execution can continue
   //   FATAL   - a serious error that precludes andy further execution.
   //           
   public  enum Level { TRACE, INFO, WARNING, ERROR, FATAL };
   //
   ////////////////////////////////////////////////////////////////////

   //--- reportLevel does not affect TRACE messages
   public  static Level reportLevel = Level.INFO;  // report msgs at or above

   public  static boolean traceAll = false;
   
   private static StringBuffer traceKeys    = new StringBuffer();
   private static PrintWriter  logFile      = null;   // for log file output
   private static PrintStream  termOut      = System.err;
   private static int          errorCount   = 0;
   private static int          warningCount = 0;
   private static int          infoCount    = 0;
   
   //---------------------openLogFile( String name ) ----------------------
   /**
    * open a log file with the given name
    */
   public static boolean openLogFile( String fileName )
   {
      try 
      {
         logFile = new PrintWriter( fileName );
      }
      catch ( FileNotFoundException fnf )
      {
         System.err.println( "Unable to open log file: " + fileName );
      }
      return logFile != null; 
   }
   //--------------------- closeLogFile --------------------------------
   /**
    * close the log file
    */
   public static void closeLogFile()
   {
      if ( logFile != null )
         logFile.close();
      logFile = null;
   }
   //---------------------- log -----------------------------------------
   /**
    * log a message if the current reportLevel allows it
    */
   public static void log( Level level, String msg )
   {
      if ( level.compareTo( reportLevel ) >= 0 )
         logMessage( level, msg );
   }
   //---------------------- info ---------------------------------------
   /**
    * INFO level log message
    */
   public static void info( String msg )
   {
      logMessage( Level.INFO, msg );
   }
   //---------------------- warning ---------------------------------------
   /**
    * WARNING level log message
    */
   public static void warning( String msg )
   {
      logMessage( Level.WARNING, "*** Warning *** " + msg );
   }
   //---------------------- error ---------------------------------------
   /**
    * ERROR level log message
    */
   public static void error( String msg )
   {
      logMessage( Level.ERROR, "*** ERROR *** " + msg );
   }
   //---------------------- fatal ---------------------------------------
   /**
    * Fatal level log message; report then fail
    */
   public static void fatal( String msg )
   {
      logMessage( Level.FATAL, "*** FATAL ERROR *** " + msg );
      System.exit( -1 );
   }
   //---------------------- trace ---------------------------------------
   /**
    * trace message - report it if key is enabled
    */
   public static void trace( String key, String msg )
   {
      if ( traceAll || traceKeys.indexOf( ":" + key + ":" ) >= 0 )
         logMessage( Level.TRACE, key + ": " + msg );
   }
   //----------------------- traceOn( String ) --------------------------
   /**
    * keys parameter defines multiple keys separated by ":" 
    *    All trace logs will be reported for the keys included
    */
   public static void traceOn( String keys )
   {
      traceKeys.append( ":" + keys + ":" );
   }
   //----------------------- traceOff( String ) --------------------------
   /**
    * keys parameter defines multiple keys separated by ":" 
    *    All trace logs will be disabled for the keys included
    */
    public static void traceOff( String keys )
    {
      String[] key = keys.split( ":" );
      for ( int i = 0; i < key.length; i++ )
      {
         int start = traceKeys.indexOf( ":" + key[ i ] + ":" );
         if ( start >= 0 )
            traceKeys.replace( start, start + key[ i ].length() + 2, "" );
      }
   }
   //----------------------- logMessage -------------------------------
   /**
    * private method to do the actual prining
    */
   private static void logMessage( Level level, String msg )
   {
      if ( logFile != null )  
         logFile.println( msg );
      if ( termOut != null )
         termOut.println( msg );
   }
   //------------------- main --------------------------------------
   /** 
    * very primitive unit test to test the trace keys
    */
   public static void main( String[] args )
   {
      traceOn( "a" );
      traceOn( "able" );
      traceOn( "happy" );
      
      traceOff( "happy" );
      trace( "able", "Able test" );
   }
}
