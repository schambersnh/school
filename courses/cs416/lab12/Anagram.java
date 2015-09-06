/**
 * Anagram.java
 * 
 *   This is an interactive program that lets a user find all permutations
 * of a string of letters that are valid English words as determined by 
 * the OPTED public domain English word list dictionary. This dictionary is
 * based on the public domain portion of "The Project Gutenberg Etext of 
 * Webster's Unabridged Dictionary" which is in turn based on the 1913 US 
 * Webster's Unabridged Dictionary. (See Project Gutenburg)
 *             http://msowww.anu.edu.au/~ralph/OPTED/
 * 
 * Words of length 3 to 8 were extracted from this dictionary into a file 
 * named opted3to8.txt. There are no plurals in the dictionary.
 * 
 * The entire dictionary is read into a Java Collection object and then
 * the user is allowed to enter a list of 3-8 characters, from which all
 * valid anagrams are extracted.
 * 
 * At startup, the user is prompted to choose which of several data
 * structures should be used to store the dictionary:
 *    v - Vector
 *    l - LinkedList
 *    h - HashSet
 *    b - an alphabetized Vector with a binary search lookup
 *    x - our own "bad" hash table
 *    m - my hash: your improvement on our bad hash function in our table
 *    j - java's hash function with our table
 */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class Anagram //extends JFrame
{
   //---------------------- class variables ----------------------
   private static Collection<String> _dictionary;
   
   private static char      _option;
   private static String    _dsName;
   private static int       _tableSize = 53;   // size for our HashTable object
   private static char      _hashFunction = 'x';  // code for bad hash function
   private static Scanner   _batchIn = null;

   //--------------------------- constructor -----------------------
   public Anagram( String title )     
   {      
      Collection<String> validWords = new Vector<String>();
      
      if ( _dictionary == null )
         readDictionary( _dictionary );
      
      String outMessage;
      String input = readWord();
      while ( input != null && input.length() > 0 )
      {
         if ( input.length() < 3 || input.length() > 8 )
            outMessage = "Program limited to 3-8 letter words. Try another";
         else
         {   
            long start = System.currentTimeMillis();
            
            lookup( validWords, "", input );
           
            float secs = ( System.currentTimeMillis() - start ) / 1000.0f;
            
            outMessage = makeMessage( input, validWords, secs );
            
            validWords.clear();
         }  
         if ( _batchIn != null )       
            System.out.println( outMessage );
         else
            JOptionPane.showMessageDialog( null, outMessage );
         input = readWord(); 
      }
   }
   //--------------------- readWord() ---------------------------------------
   /**
    * read the next word from user or standard input
    */
   private String readWord()
   {
      String word;
      if ( _batchIn != null )
      {
         if ( _batchIn.hasNextLine() )
            word = _batchIn.nextLine();
         else
            word = null;
      }
      else  // interactive mode
      {
         String inMessage = "Enter a word";
         word = JOptionPane.showInputDialog( null,  inMessage );
      }
      return word;
   }
   //--------------------- search( String, int, int ) ------------------------
   /**
    * Do a binary search looking for the word.
    */
   private boolean search( String word, Vector<String> dict, int lo, int hi )
   {
      //return false;
      if ( lo > hi ) 
         return false;
      int mid = ( hi + lo ) / 2;
      
      int comp = word.compareTo( dict.get( mid )  );
      
      if ( comp < 0 )   // word belongs between lo and mid
      {
         return search( word, dict, lo, mid - 1 );
      }
      else if ( comp > 0 )      // word is between mid and hi  
      {
         return search(  word, dict, mid + 1, hi );
      }
      else
      {
         return true;
      }
   }
   //---------------------- lookup( String, String ----------------------
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
      if ( _option == 'b' )
      {      
         return search( word, (Vector<String>)_dictionary, 
                         0, _dictionary.size() - 1 );
      }
      else
         return _dictionary.contains( word );
   }
   
   //--------------------- makeMessage( String, Collection ) ------------------------
   private String makeMessage( String start, Collection<String> validWords, 
                              float time ) 
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
      
      message += "\n\n" + _dsName + " time: " + time + " seconds";
      return message;
   }
   
   //+++++++++++++++++++++++++++ class methods +++++++++++++++++++++++++++++++
   //------------------- readDictionary -------------------------------
   private static void readDictionary( Collection<String> dictionary )
   {
      Scanner scanner = null;
      try
      {
            scanner = new Scanner( new File( "opted3to8.txt" ));
      }
      catch ( IOException ioe )
      {
         System.err.println( "***Error -- can't open 'opted.txt'" );
         System.exit( -1 );
      }
 
      while ( scanner.hasNext() )
      {
         dictionary.add( scanner.next() );
      }
   }
   //---------------------- setTableSize( int ) --------------------
   /**
    * set the table size for future hash tables
    */
   public static void setTableSize( int size )
   {  
      _tableSize = size;
      setDataStructure( _dsName );
   }
   
   //---------------------- setDataStructure ---------------------------------
   /**
    * set the data structure to be used based on a character code passed
    */
   public static void setDataStructure( String option )
   {    
      _dsName = option;
      _option = option.toLowerCase().charAt( 0 );
      switch ( _option )
      {
         default:
            //JOptionPane.showMessageDialog( null, "Unimplemented choice: " +
                                     //option + " : you're getting HashSet!" );
            System.err.println( "Unimplemented choice: " +
                                     option + " : you're getting HashSet!" );
            _option = 'h';
         case 'h': 
            _dictionary = new HashSet<String>();
            break;
         case 'v': 
            _dictionary = new Vector<String>();
            break;

         case 'l': 
            _dictionary = new LinkedList<String>();
            break;

         case 'b': 
            _dictionary = new Vector<String>();
            break;

         case 'x':   // bad hash function with our hash table
         case 'm':   // your (much better) hash with our hash table
         case 'j':   // java's hash function with our hash table
            _dictionary = new HashTable<String>( _tableSize, _option, option );
            break;
      }
      readDictionary( _dictionary );
      if ( _dictionary instanceof HashTable )
         report();
      //new Anagram( "Anagram" );
   }
   //---------------------- report() ---------------------------------------
   /**
    * report the statistics for HashTable if we have one
    */
   public static void report()
   {
      if ( _dictionary instanceof HashTable )
         ((HashTable)_dictionary).report();
      else
         System.out.println( "Current dictionary is not a HashTable object" );
   }
   
   //---------------------- chooseDataStructure -----------------------------
   /**
    * Open a dialog box to let user choose among the 3 algorithm options.
    * Only the 1st character of the user's input is used to determine
    * the chosen option. The returned string is just a term referring to the
    * chosen algorithm.
    */
   private static void chooseDataStructure()
   {    
      String option;
      String inMessage = "Which algorithm: " +
                        "v (vector), h (hashSet), l (list), " +
                        "b (Binary search), x (badHash), m (myHash)?";
      String input = JOptionPane.showInputDialog( null,  inMessage ); 
      
      char code = input.toLowerCase().charAt( 0 );
      switch ( code )
      {
         default:
            System.err.println( "Invalid choice: you're getting HashSet!" );
         case 'h': 
            option = "h: HashSet";
            break;
         case 'v': 
            option = "v: Vector";
            break;
         case 'l': 
            option = "l: Linked List";
            break;
         case 'b': 
            option = "b: Binary Tree";
            break;
         case 'x': 
            option = "x: Bad hash function with our hash table";
            break;
         case 'm': 
            option = "m: My hash function with our hash table";
            break;
         case 'j': 
            option = "j: Java hash function with our hash table";
            break;
      }
      setDataStructure( option );
   }
   //----------------------- main ----------------------------------------
   public static void main( String[] args )
   {
      if ( args.length == 0 )
         AnagramGUI.main( args );         
      else if ( ! args[ 0 ].equals( "-b" ))
         System.err.println( "Invalid command line arguments" );
      else
      {
         if ( args.length > 1 )
            Anagram.setDataStructure( args[ 1 ] );
         else
            Anagram.setDataStructure( "m" );
         if ( args.length > 2 )
            Anagram.setTableSize( getArg( args, 2, 53 ));
         _batchIn = new Scanner( System.in );
         new Anagram( "Anagram" );
      }
   }
   //-------------------- getArg( String[], int, int ) -----------------
   /**
    * Utility to convert a command line string argument to an int. 
    * Parameters: args array, index of argument, default value
    * Default value if argument not present or not an int.
    */
   private static int getArg( String[ ] args, int which, int defaultVal )
   {
      try
      {
         return Integer.parseInt( args[ which ] ); // string to int
      }
      catch ( ArrayIndexOutOfBoundsException oob )
      {
         // Not an error. Parameter is optional; return default
      }
      catch ( NumberFormatException nfe )
      {         
         System.err.println( "Error: invalid command line argument " 
                               + which + " = " + args[ which ] 
                               + ".  Should be integer; using default value: "
                               + defaultVal );
      }  
      return defaultVal;
   }
}