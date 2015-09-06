/**
 * AnagramTest.java -- Anagram
 * 
 * This program gets a string from the user (of length 3-8), generates all
 * permutations of the string and looks up each permutation in a Dictionary.
 * The Dictionary is the OPTED public domain English word list dictionary. 
 * This dictionary is based on the public domain portion of "The Project 
 * Gutenberg Etext of Webster's Unabridged Dictionary" which is in turn based
 * on the 1913 US Webster's Unabridged Dictionary. (See Project Gutenburg)
 *             http://msowww.anu.edu.au/~ralph/OPTED/
 * 
 * Words of length 3 to 8 were extracted from this dictionary into a file 
 * named opted3to8.txt. There are no plurals in the dictionary.
 * 
 * The entire dictionary is read into a Java Collection object and then
 * the user is allowed to enter a list of 3-8 characters, from which all
 * valid anagrams are extracted. 
 * 
 */


///////////////////////////////////////////////////////////////////////////
////////////////// Enter results of timing tests below ////////////////////
///////////////////////////////////////////////////////////////////////////
/* 
 * 3. ------------------------ 7 character test ----------------------------
 * Record 3 execution times for each of 3 options for 3 different words
 *                           message      escaper       relapse
 *                        +-------------+------------+-------------+
 * 1. Vector:             |      5.677  |    6.57        4.968     |
 *                        +-------------+------------+-------------+
 * 2. Hashset:            |     0.064      0.0080        .0099     |
 *                        +-------------+------------+-------------+
 * 3. Binary search tree: |      0.055  |    0.109        0.01     |
 *                        +-------------+------------+-------------+
 *
 * 4. ------------------------ 8 character test ----------------------------
 * Record 1 execution time for Vector and 3 execution times for the others.
 * Use the same word:        strainer
 *                        +-------------+
 * 1. Vector:             |   38.717    |  strainer     strainer
 *                        +-------------+------------+-------------+
 * 2. Hashset:            |   0.222          0.056       0.049     |
 *                        +-------------+------------+-------------+
 * 3. Binary search tree: |   0.153          0.078       0.051     |
 *                        +-------------+------------+-------------+
 *
 * 5. ----------------------- 8 character worst case test ------------------
 * Record 1 execution time for Vector and 3 execution times for the others.
 * Use the string:           qwertiop
 *                        +-------------+
 * 1. Vector:             |   56.463       qwertiop     qwertiop
 *                        +-------------+------------+-------------+
 * 2. Hashset:            |   0.22          0.06          0.042    |
 *                        +-------------+------------+-------------+
 * 3. Binary search tree: |   0.142          0.076         0.053   |
 *                        +-------------+------------+-------------+ 
 */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class AnagramTest //extends JFrame
{
   //---------------------- class variables -------------------------
   //private static String dictionaryName = "optedTiny.txt";
   private static String dictionaryName = "opted3to8.txt";
   
   //---------------------- instance variables ----------------------
   Collection<String> _dictionary;   
   private char       _option;

   //--------------------------- constructor -----------------------
   /**
    * Test the anagram algorithm using different search strategies
    */
   public AnagramTest( String title )     
   {      
      Collection<String> validWords = new Vector<String>();      

      String option = chooseAlgorithm();
     
      while ( option != null && option.length() > 0 )
      {        
         readDictionary( _dictionary );
         String inMessage = "Enter a string of letters";
         String input = JOptionPane.showInputDialog( null,  inMessage );
         
         String outMessage;
         
         while ( input != null && input.length() > 0 )
         {
            if ( input.length() < 3 || input.length() > 8 )
               outMessage = "Program limited to 3-8 letter words. Try another";
            else
            {   
               long start = System.currentTimeMillis();
               
               lookup( validWords, "", input );
               if ( validWords.size() == 0 )
                  outMessage = input + ": leads to no words";
               else 
                  outMessage = makeMessage( input, validWords );
               
               validWords.clear();
               
               long time = System.currentTimeMillis() - start;
               float seconds = time / 1000.0f;
               
               String timeMsg = option + ": " + input + " -> "
                                     + seconds + " seconds\n";
               System.out.print( timeMsg );
               outMessage += "\n\n" + timeMsg;
            }         
            JOptionPane.showMessageDialog( null, outMessage );
            input = JOptionPane.showInputDialog( null,  inMessage ); 
         }
         option = chooseAlgorithm();
      }
   }
   //--------------------- search( String, int, int ) ---------------------
   /**
    * Do a binary search looking for the word.
    */
   private boolean search( String word, Vector<String> dict, int lo, int hi )
   {
        
     int middle = (hi + lo)/2;
     if(lo > hi)
     {
       return false;
     }   
     else if (word.compareTo(dict.get(middle)) < 0)
     {
       return search(word, dict, lo, middle - 1);
     }
     else if(word.compareTo(dict.get(middle)) > 0)
     {
       return search(word, dict, middle + 1, hi);
     }
     else 
     {
       return true;
     }
      //////////////////////////////////////////////////////////////////////
      //      
      // Add code here to do the binary search of the Vector
      //    find the mid point
      //    compare word to element at the midpoint
      //    if they are equal 
      //        return true -- you've found it
      //    else if word is < the midpoint word
      //        recurse: return the result from searching the portion of 
      //            the array from 'lo' UPTO, BUT NOT INCLUDING 'mid'
      //    else
      //        recurse: return the result from searching the portion of
      //             the array from AFTER 'mid' through 'hi'
      //////////////////////////////////////////////////////////////////////
     
   }
   //---------------------- chooseAlgorithm -----------------------------
   /**
    * Open a dialog box to let user choose among the 3 algorithm options.
    * Only the 1st character of the user's input is used to determine
    * the chosen option. The returned string is just a term referring to the
    * chosen algorithm.
    */
   private String chooseAlgorithm()
   {    
      String outMessage;
      String inMessage = "Choose: V (vector), H (hash), or B (Binary search)";
      String input = JOptionPane.showInputDialog( null,  inMessage ); 
      if ( input == null || input.length() == 0 )
         return input;
      
      _option = input.toLowerCase().charAt( 0 );
      switch ( _option )
      {
         default:
            System.err.println( "Invalid choice: you're getting Hashset!" );
            _option = 'h';
         case 'h': 
            _dictionary = new HashSet<String>();
            outMessage = "Hashset ";
            break;
         case 'v': 
            outMessage = "Vector ";
            _dictionary = new Vector<String>();
            break;

         case 'b': 
            outMessage = "Binary Tree ";
            _dictionary = new Vector<String>();
            break;
      }   
      return outMessage;
   }
   //------------------- lookup( Collection, String, String ------------------
   /**
    * This is the key recursive call 
    *   The "head" parameter is the fixed portion of the current permutation;
    *   the "tail" parameter is set of characters for which all permutations
    *   must still be generated for this "head". 
    *  
    *   Permutations that represent valid words are added to wordsFound.
    */
   private void lookup( Collection<String> wordsFound, String head, String tail )
   {
      String newHead, newTail;
      
      // the "Base case" is when there is nothing left in the tail.
 
      if ( tail.length() == 0 ) // no tail means we've got a possible word
      {
         if ( ifWord( head )  && !wordsFound.contains( head ))
            wordsFound.add( head );
      }
      else
      {  // for each character in "tail" add it to the head and
         //    generate permutations for the remainder of the tail.
         for ( int i = 0; i < tail.length(); i++ )
         {
            // add next character of the tail to the head 
            newHead = head + tail.substring( i, i + 1 );
            
            // remove that character from the tail
            newTail = tail.substring( 0, i ) + tail.substring( i + 1 );
            
            // and recurse
            lookup( wordsFound, newHead, newTail );
         }
      }
   }
   //--------------------- ifWord( String ) ------------------------------
   /**
    * check if the argument string is in the English word dictionary.
    */
   private boolean ifWord( String word )
   {
      if ( _option == 'h' || _option == 'v' )
         return _dictionary.contains( word );
      else
      {      
         return search( word, (Vector<String>)_dictionary, 
                         0, _dictionary.size() - 1 );
      }
   }
   
   //------------------- readDictionary -------------------------------
   /**
    * read the opted3to8 dictionary into a Collection object
    */
   private void readDictionary( Collection<String> dictionary )
   {
      Scanner scanner = null;
      try
      {
         scanner = new Scanner( new File( dictionaryName ));
      }
      catch ( IOException ioe )
      {
         System.err.println( "***Error -- can't open " + dictionaryName );
         System.exit( -1 );
      }
 
      while ( scanner.hasNext() )
      {
         dictionary.add( scanner.next() );
      }
   }
   //--------------------- makeMessage( String, Collection ) ---------------
   /**
    * create the message that reports the results
    */
   private String makeMessage( String start, Collection<String> validWords ) 
   {
      String message = start + ": makes " + validWords.size() + " words.";
      int i = 0;
      
      for ( Iterator iter = validWords.iterator(); iter.hasNext(); )
      {
         if ( i++ % 4 == 0 )
            message = message + "\n" + iter.next();
         else
            message = message + "    " + iter.next();
      }
      return message;
   }
   //----------------------- main ----------------------------------------
   public static void main( String[] args )
   {
      if ( args.length > 0 )
         AnagramTest.dictionaryName = args[ 0 ];
      
      new AnagramTest( "AnagramTest" );
   }
}
