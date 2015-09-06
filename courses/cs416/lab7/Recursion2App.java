/**
 * Recursion2App.java -- 
 * A driver for testing recursive methods 
 * 
 * Last edited: 9/19/10 rdb
 *     Added loops for individual test, so can re-test same method
 *     many times. 
 */
import java.util.*;
import java.io.*;

public class Recursion2App 
{
   //---------------------- instance variables ----------------------
   private static boolean interactive = true;
   private static Scanner terminal;
   private static String  arrayLine;
   
   //--------------------------- runSum --------------------------------
   /**
    * get parameters and invoke the sum method to sum an int array recursively
    * Keep testing until an empty line is entered
    */
   public static void runSum()
   {
      String prompt = "Enter a list of ints on 1 line; empty line to quit";
      int[] temp = readIntArray( prompt );
      
      while ( temp.length > 0 )
      {
         System.out.println( "----------------------------------------------" );
         System.out.print( "Sum of " + array2String( temp ) + " = " );
         System.out.println( Recursion2.sum( temp, temp.length )) ;   
         temp = readIntArray( prompt );
      }
   }
 
   //--------------------- runPow ----------------------------------
   /**
    * get parameters for a number and an exponent and invoke the exp method
    * Keep testing until an empty line is entered
    */
   public static void runPow()
   {
      String prompt = "Enter number and exponent on 1 line; empty line to quit";
      String line = readLine( prompt );
      
      while ( line.length() > 0 )
      {
         int num;
         int exp;
         Scanner parse = new Scanner( line );
         if ( parse.hasNextInt() )
         {
            num = parse.nextInt();
            if ( parse.hasNextInt() )
            {
               exp = parse.nextInt();
               System.out.println( "----------------------------------------" );
               System.out.println( num + " raised to the " + exp + " power = " 
                               + Recursion2.pow( num, exp ));
            }
            else
               System.out.println( "Error: need a valid integer for exp" );
         }
         else
            System.out.println( "Error: need a valid integer for the number" );
         line = readLine( prompt );
      }
   }
   //----------------------- runOrdered --------------------------------
   public static void runOrdered()
   /**
    * get int input array and invoke the ordered method to check if the numbers
    * are in numerical order.
    * Keep testing until an empty line is entered
    */
   {
      String prompt = "Enter a list of ints on 1 line; empty line to quit";
      int[] temp = readIntArray( prompt );
      
      while ( temp.length > 0 )
      {
         System.out.println( "----------------------------------------------" );
         System.out.print( array2String( temp ) );
         if ( Recursion2.ordered( temp, temp.length )) 
            System.out.println( "  IS ordered." );
         else
            System.out.println( "  is NOT ordered." );
         temp = readIntArray( prompt );
      }
   }   //-------------------- runPairTest ----------------------------
   /**
    * get input string and invoke the pairTest method to check if the string
    * is composed of successive pairs of matching characters.
    * Keep testing until an empty line is entered
    */
   public static void runPairTest()
   {
      String prompt = "Enter a string to test for pairs; empty line to quit";
      String input = readLine( prompt );
      while ( input.length() > 0 )
      {
         System.out.println( "---------------------------------" );
         System.out.print( "\"" + input + "\""  );  
         if ( Recursion2.pairTest( input ) )
            System.out.println( " IS a sequence of pairs" );
         else 
            System.out.println( " IS NOT a sequence of pairs" );
         input = readLine( prompt );
      }
   }
      
   //--------------------- runExchangePairs ----------------------------------
   /**
    * Swap pairs of neighboring characters in the input string and return the
    * result. If there are an odd number of characters in the input, the last
    * one is left unchanged.
    */
   public static void runExchangePairs()
   {
      String prompt = "Enter a string for exchanging pairs of characters>";
      String input = readLine( prompt );
      while ( input != null && input.length() > 0 )
      {
         String f = Recursion2.exchangePairs( input );    
         System.out.println( "---------------------------------" );
         System.out.println( "exchangePairs( \"" + input + "\" ) = " + f );   
         input = readLine( prompt );
      }
   }
//    
//   //------------------------ runReverseComp -----------------------------
//   /**
//    * get String input and invoke the reverseComplement method to reverse
//    *   the order of a DNA string and complement it at the same time.
//    * Keep testing until an empty line is entered
//    */
//   public static void runReverseComp()
//   {
//      String prompt = "Enter a dna string of 'gcta'; empty line to quit";
//      String input = readLine( prompt );
//      while ( input.length() > 0 )
//      {
//         System.out.println( "---------------------------------" );
//         System.out.println( input + "  --->   " 
//                               + Recursion2.reverseComplement( input ) );
//         input = readLine( prompt );
//      }
//   }
   
   //------------------- array2String( int[] ------------------------------
   /**
    * a utility method to convert an int array to a String representation
    */
   private static String array2String( int[] array )
   {
      StringBuffer out = new StringBuffer();
      out.append( "[ " );
      for ( int i = 0; i < array.length; i++ )
         out.append( array[ i ] + ", " );
      out.replace( out.length() - 2, out.length(), " ]" );
      return out.toString();
   }
   
   //---------------------- readIntArray() -------------------------------
   /**
    * reads one line of input, parses to an int array and returns it.
    * A 0 length array means no input.
    */
   private static int[] readIntArray( String prompt )
   {
      String arrayLine = readLine( prompt );
     
      Scanner list = new Scanner( arrayLine );
      int[] temp = new int[ 1000 ];   // way bigger than possible to enter
      int n = 0;
      while ( list.hasNextInt() )
         temp[ n++ ] = list.nextInt();
      int[] ret = new int[ n ];
      for ( int i = 0; i < n; i++ )
         ret[ i ] = temp[ i ];
      return ret;
   }
  
   //---------------------- readLine( String ) -----------------------------
   /**
    * issue a prompt if we are in interactive mode and read a line
    * of input from the terminal
    */
   static String readLine( String prompt )
   {
      if ( interactive )
         System.out.println( "\n" + prompt );
      if ( terminal.hasNextLine() )
         return terminal.nextLine();
      else
         return "";
   }
   
   //----------------------- main ----------------------------------------
   public static void main( String[] args )
   { 
      if ( args.length > 0 ) // running in batch, don't prompt
         interactive = false;
      terminal = new Scanner( System.in );
      String choice = "";
      String outMessage;
      String inMessage = 
         "q (quit)          " +
         "s (array sum)     " +
         "p (pow)           " +
         "o (ordered)       " +
         "\n" +
         "t (pair test)     " +
         "x (exchangePairs) " +
         //"r (rev compl)     " +
         "\n***************";
      
      if ( interactive )
         System.out.println( inMessage );
      boolean done = false;
      while( !done && terminal.hasNextLine() ) 
      {
         choice = terminal.nextLine();
         choice = choice.trim().toLowerCase() + " "; // add blank for empty line        
         if( choice.startsWith(  "s" ) )
            runSum();
         else if ( choice.startsWith(  "p" ) )
            runPow();
         else if ( choice.startsWith(  "o" ) )
            runOrdered();
         else if ( choice.startsWith(  "t" ) )
            runPairTest();
         else if ( choice.startsWith(  "x" ) )
            runExchangePairs();
         else if ( choice.startsWith(  "q" ) )
            done = true;
         else if ( choice.trim().length() != 0 )
         {
            System.err.println( "Invalid choice: " + choice );
            System.out.println( "\n**************" );
         }
         if ( !done && interactive )
            System.out.println( inMessage );
      }
      
      System.out.println( "So long!");
   }
}