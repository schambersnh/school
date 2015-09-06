/**
 * RecursionApp.java -- 
 * A driver for Recursion lab
 * 
 * Last modified: 09/15/10 rdb
 *    Added loops in each test, so can do multiple tests of the same
 *    method without picking method each time
 * 
 */
import java.util.*;
import java.io.*;

public class RecursionApp 
{
   //---------------------- class variables ----------------------
   private static boolean interactive = true;
   private static Scanner terminal;
   
   //---------------------- instance variables ----------------------
   //--------------------------- runMax --------------------------------
   public static void runMax()
   {
      String prompt = "Enter a list of ints on 1 line. Empty list to quit.";      
      String input = readLine( prompt );
      while ( input != null && input.length() > 0 )
      {
         Scanner list = new Scanner( input);
         int  temp [] = new int[ 1000 ];   // way bigger than possible to enter
         int n = 0;
         while ( list.hasNextInt() )
            temp[ n++ ] = list.nextInt();
         
         if ( n == 0 )
            System.out.println( "****ERROR**** No numbers entered" );
         else 
         {
            System.out.println( "---------------------------------" );
            System.out.println( "Max of " + input );
            System.out.println( Recursion.maxValue( temp, n )) ;   
         }
         input = readLine( prompt );
      }
   }
   
   //----------------------------- runFind ----------------------------
   public static void runFind()
   {
      String prompt = "Enter a list of words on 1 line.";
      
      String   input = readLine( prompt );
      String[] words = input.split( " " );
      int n = words.length;
      
      if ( n <= 1 )
         System.out.println( "****ERROR**** Must enter at least 1 word" );
      else
      {
         System.out.println( "----------------Input array ---------------" );
         for ( int i = 0; i < n; i++ )
            System.out.print( "   " + words[ i ] );
         System.out.println();
         prompt = "Enter a word to find in list. Return to quit.";
         input = readLine( prompt );
         while ( input != null && input.length() > 0 )
         {
            Scanner parse = new Scanner( input );
            if ( parse.hasNext() )
            {
               String key = parse.next();
               int keyPos = Recursion.findLast( words, n, key );
               System.out.print( "Searching for " + key + ": "  );
               if ( keyPos >= 0 )
                  System.out.println( ": found at index " + keyPos );
               else
                  System.out.println( " NOT FOUND " );
            }
            else
               System.out.println( "Invalid input, no key: " + input );
            input = readLine( prompt );
         }
      }
   }
   
   //------------------------ runPalindrome -----------------------------
   public static void runPalindrome()
   {
      String prompt = "Enter a string to check for palindrome";
      String input = readLine( prompt );
      while ( input.length() > 0 )
      {
         
         System.out.println( "---------------------------------" );
         System.out.print( input + ": " );
         if ( Recursion.palindrome( input) ) 
            System.out.println( " IS a palindrome");  
         else
            System.out.println( " is NOT a palindrome");   
         input = readLine( prompt );
      }
   }
   
   //-------------------- runCountLetter ----------------------------
   public static void runCountLetter()
   {
      String prompt = "Enter a string for counting letters";
      String input = readLine( prompt );
      String letterPrompt = "Enter the letter to count ";
      String letterLine = readLine( letterPrompt );
      while ( letterLine.length() > 0 )
      {
         char letter = letterLine.charAt( 0 );
         
         System.out.println( "---------------------------------" );
         int count = Recursion.countLetter( input, letter );
         System.out.println( "\"" + input + "\"" + " has " 
                               + count + " " + letter + " 's" );     
         letterLine = readLine( letterPrompt );
      }
   }
   
   //----------------------- runSubstring --------------------------------
   public static void runSubstring()
   {
      String prompt = "Enter a string to search";
      String subPrompt = "Enter a substring to search for";
      String input = readLine( prompt );
      String subInput = readLine( subPrompt );
      while ( subInput.length() > 0 )
      {
         System.out.println( "----------------------------------------------" );
         if ( Recursion.isSubstring( input, subInput ))
            System.out.println( "\"" + subInput + "\"" + " is a substring of "
                                  + "\"" + input + "\"" )  ;    
         else     
            System.out.println( "\"" + subInput + "\"" + " is NOT a substring of " 
                                  + "\"" + input + "\"" )  ;  
         subInput = readLine( subPrompt );
      }
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
         "m (maxValue)      " +
         "f (findLast)      " +
         "p (palindrome)    " +
         "\n" +
         "c (countLetter)   " +
         "s (isSubstring)   " +
         "\n***************";
      
      if ( interactive )
         System.out.println( inMessage );
      boolean done = false;
      while( !done && terminal.hasNextLine() ) 
      {
         choice = terminal.nextLine();
         choice = choice.trim().toLowerCase() + " "; // add blank for empty line        
         char cmd = choice.charAt( 0 );
         switch ( cmd )
         {
            case 'm':  runMax(); break;
            case 'f':  runFind(); break;
            case 'p':  runPalindrome(); break;
            case 'c':  runCountLetter(); break;
            case 's':  runSubstring(); break;
            case 'q':  done = true; break;
            default:
               System.err.println( "Invalid choice" );
               System.out.println( "\n**************" );
         }
         System.out.println();
         if ( !done && interactive )
            System.out.println( inMessage );
      }
      
      System.out.println( "So long!");
   }
}
