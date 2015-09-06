/**
 *  Log: Simple logging tools for debugging
 */
import java.io.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.*;


public class Log 
{ 
   private static PrintStream out = System.out;
   private static PrintStream fileOut = null;
   private static boolean consoleLogging, fileLogging, pausing = true;  
   private static String header = "*LOG-> ";
   private static boolean on = false;
   private static int pauseCounter = 0;
   
   
   //--------------------------------------------------------------------
   /**
    *   Print out message with method and line number information for this call.
    * 
    */ 
   public static void println( String s)
   {
      if( !on )
         return;
      // get info on code that sent mesage
      StackTraceElement codeLine =  new Throwable().getStackTrace()[ 1 ];
      String msg = header + codeLine + ": " + s; 
      
      display( msg );
   }
   
   
   //--------------------------------------------------------------------
   /**
    *   Print out  line number information for this call.
    * 
    */ 
   public static void println( )
   {
      if( !on )
         return;
      // get info on code that sent mesage
      StackTraceElement codeLine =  new Throwable().getStackTrace()[ 1 ];
      String msg = header + codeLine;
      
      display( msg );
   }
   
   //--------------------------------------------------------------------
   /**
    *  Log messages to file with name filename only. 
    * 
    */ 
   public static void file( String fileName )
   {
      if( !on )
         return;
      
      PrintStream p; 
      
      // get info on code that sent mesage
      StackTraceElement codeLine =  new Throwable().getStackTrace()[ 1 ];
      
      if ( fileName != null && !fileLogging )
      {
         try{ 
            p = new PrintStream( new FileOutputStream(fileName) );
            fileOut = p;
            out.println( header  + codeLine + 
                        ": Logging to file \"" + fileName + "\"" );
            fileLogging = true;
            consoleLogging = false;
            fileOut.println( now( ) );
            
         }catch(Exception e)
         {
            System.err.println(header +  codeLine  + 
                               ": LOG ERROR: could not open " + 
                               fileName + " for output!");
            fileOut = null;
         }
      }
   } 
   
   
   //--------------------------------------------------------------------
   /**
    *   Log messages to System.out and file with name filename
    * 
    */ 
   public static void both( String fileName )
   {
      if( !on )
         return;
      
      PrintStream p; 
      
      // get info on code that sent mesage
      StackTraceElement codeLine =  new Throwable().getStackTrace()[ 1 ];
      
      consoleLogging = true;
      
      if ( fileName != null )
      {
         try{ 
            p = new PrintStream( new FileOutputStream( fileName ) );
            fileOut = p;
            out.println( header + codeLine +
                        ": Logging to console and file \"" + fileName + "\"" );
            fileOut.println( now( ) );
            fileLogging = true;
            
         }catch( Exception e )
         {
            System.err.println(header +  codeLine + 
                               "LOG ERROR: could not open " + fileName );
         }
      }
   }
   
   
   //--------------------------------------------------------------------
   /**
    *   Print a stack trace.
    * 
    */ 
   public static void trace( )
   {
      if( !on )
         return;
      
      StackTraceElement[ ] codeLines =  new Throwable().getStackTrace();
      String msg = header + "stack trace:\n                   "; 
      
      for(int i = 1; i< codeLines.length ; i++)
      {
         msg += codeLines[ i ].toString()  ;
         if( i != codeLines.length - 1 )
            msg += "\n                   ";
         // stop when we get back to main
         if( codeLines[ i ].toString().contains(".main") )
            break;
      }
      display( msg );
   }
   
   
   
   // --------------------- pause ----------------------------------
   /**
    * Pause execution until the user hits return on the keyboard.
    */
   static public void pause( String str ) 
   {
      if( !on )
         return;
      if( ! pausing )
         return;
      if(pauseCounter > 0 )
         pauseCounter--;
      else
      {
      // get info on code that sent mesage
      StackTraceElement codeLine =  new Throwable().getStackTrace()[ 1 ];
      
      try
      {
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String msg = header + codeLine + " PAUSED: " + str; 
      
              display( msg );
         
        // out.println( header +  codeLine + ": PAUSED");
         String s = br.readLine().trim();
         if( s != null && s.length() > 0)
            parse ( s );
         
      }
      catch( java.io.IOException ioe )
      {
         System.err.println(header +  codeLine + ": LOG ERROR: Pause failed");
      }
      }
   }
   
    // --------------------- pause ----------------------------------
   /**
    * Pause execution until the user hits return on the keyboard.
    */
   static public void pause() 
   {
      if( !on )
         return;
      if( ! pausing )
         return;
      if(pauseCounter > 0 )
         pauseCounter--;
      else
      {
      // get info on code that sent mesage
      StackTraceElement codeLine =  new Throwable().getStackTrace()[ 1 ];
      
      try
      {
         BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

         
         out.println( header +  codeLine + ": PAUSED");
         String s = br.readLine().trim();
         
         if( s != null && s.length() > 0)
            parse ( s );
         
      }
      catch( java.io.IOException ioe )
      {
         System.err.println(header +  codeLine + ": LOG ERROR: Pause failed");
      }
      }
   }
   
   
   //--------------------------------------------------------------------
   /**
    *   
    * 
    */ 
   private static void parse( String s )
   {
      if( s != null)
         s = s.trim();
      if(s.length() == 0 )
         return;
      String cmd = s.trim().substring(0,1).toUpperCase( );
      
      
      if ( cmd.equals("Q") )
      {
         on = false;
      }
      else   if ( cmd.equals("P") )
      {
         if( s.length() > 1)
         {
            
            Scanner scan = new Scanner( s.substring(1 ) );
            if( scan.hasNextInt() )
            {
               pauseCounter = scan.nextInt();
            }
         }
          else
               pausing = false;
         
      }
   }
      
      //--------------------------------------------------------------------
      /**
       *   Log messages to System.out only
       * 
       */ 
      public static void console(  )
      {
         
         if( !on )
            return;
         // get info on code that sent mesage
         StackTraceElement codeLine =  new Throwable().getStackTrace()[ 1 ];
         
         fileLogging = false;
         if( fileOut != null )
         { 
            fileOut.close();
            fileOut = null;
         }
         consoleLogging = true;
         display( header +  codeLine +  ": Logging to console");
      } 
      
      //--------------------------------------------------------------------
      /**
       *   
       * 
       */ 
      public static void off(  )
      {
         on = false;
      }
      
      //--------------------------------------------------------------------
      /**
       *   
       * 
       */ 
      public static void on(  )
      {
         on = true;
      }
      
  //--------------------------------------------------------------------
   /**
    *   private helper, display to active output
    * 
    */ 
   private static void display( String msg )
   {
      if( !on )
         return;
      if( consoleLogging && out != null )
         out.println( msg );
      if( fileLogging && fileOut != null )
         fileOut.println( msg );
   }
      
      
      
      //--------------------------------------------------------------------
      /**
       *   Get the formatted time and date
       * 
       */ 
      private static String now( ) 
      {
         String dateFormat = "MM/dd/yyyy HH:mm:ss";
         Calendar cal = Calendar.getInstance( );
         SimpleDateFormat sdf = new SimpleDateFormat( dateFormat );
         return sdf.format( cal.getTime() );
      }
   }
