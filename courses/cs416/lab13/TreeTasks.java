/**
 * TreeTasks - the main class for controlling the application code. 
 * 
 * 
 * The application semantics including the graphics generated 
 * by the app are controlled from this class.
 */
import javax.swing.*;
import java.util.*;
   
public class TreeTasks
{
   //--------------------- instance variables ----------------------------
   private static DataList _list;
   private static BinarySearchTree _bst = new BinarySearchTree();;
   
   //------ "package" state variables; set by GUI and/or BSTdriver ---------
   static boolean printMode     = true;  // if set, print after each command
   static boolean guiMode       = true;  // true if running from BSTpractice
                                         // false if running from BSTdriver
   static boolean interactive   = true;  // true if interactive user
   static Scanner terminal      = null;
   
   //--- data generation constants with package access so GUI can change
   static int     dataSize        = 8;
   static int     minDataSize     = 0;
   static int     maxDataSize     = 100;
   static int     defaultDataSize = 8;
   
   static int     rngSeed         = 1;   // random seed
   static int     maxSeed         = 16;   // arbitrary number
   
   //--- subset constants
   private static int  subsetMin = 30; // arbitrary; all values are 0, 100
   private static int  subsetMax = 70;
   
   //+++++++++++++++++++++ methods invoked by GUI buttons +++++++++++++++++++
   //---------------------- makeData() -----------------------------
   /**
    * generate a new DataList 
    */
   public static void makeData( )
   {  
      _list = generateData( dataSize, rngSeed );
      
      _bst = new BinarySearchTree();
      
      Iterator<Data> iter = _list.iterator();
      while ( iter.hasNext() )
         _bst.add( iter.next() );
      
      showTree();
   }
   /**
    * generate a new DataList after first changing the size
    */
   public static void makeData( int newSize )
   {  
      if ( newSize >= minDataSize && newSize <= maxDataSize )
         dataSize = newSize;
      makeData();   // make data
   }
   //---------------------- showTree() -----------------------------
   /**
    * print the list for debugging; maybe more 
    */
   public static void showTree( )
   {  
      //printList( _list, "DebugList" );
      //if ( _list != null )
         //_list.checkList();

      if ( guiMode )  // convenient to have tree printed in gui  mode
         printTree( _bst, "DebugTree" );
      
      showResults( "Print: \n" + _bst );
   }
   //---------------------- maxFind() -----------------------------
   /**
    * find and report the max value Data item in the list
    */
   public static void maxFind( )
   { 
      String msg;
      Data max = _bst.maxFind();
      if ( max == null )
         msg = "No max; tree must be empty";
      else
         msg = "Max entry in tree is " + max;
      showResults( msg );
   }
   //---------------------- inOrderPrint() -----------------------------
   /**
    * print the tree using an in-order traversal
    */
   public static void inOrderPrint( )
   { 
      System.out.println( "-------------------------------------------" );
      System.out.println( "             in-Order print                " );
      
      _bst.inOrderPrint( System.out );
   }
   //---------------------- preOrderPrint() -----------------------------
   /**
    * print the tree using an pre-order traversal
    */
   public static void preOrderPrint( )
   { 
      System.out.println( "-------------------------------------------" );
      System.out.println( "             pre-Order print                " );
      
      _bst.preOrderPrint( System.out );
   }
   //---------------------- find() -----------------------------
   /**
    * find an item in tree with a given id
    */
   public static void find( )
   { 
      String outMsg;
      String key;
      key = readLine( "Enter key to search for" );
      if ( key != null && key.length() > 0 )
      {         
         Data found = _bst.find( key );
         if ( found != null )
            outMsg = "Found: " + found;
         else
            outMsg = key + " is not in the tree.";
         showResults( outMsg );
      }
   }
   /**
    * batch version specifies id on command input line
    */
   public static void find( String key )
   { 
      String outMsg;
      if ( key != null && key.length() > 0 )
      {         
         Data found = _bst.find( key );
         if ( found != null )
            outMsg = "Found: " + found;
         else
            outMsg = key + " is not in the tree.";
         showResults( outMsg );
      }
   }
   //--------------------- setGUI -----------------------------
   /**
    * called if in batch mode; opens a Scanner on System.in
    * takes input that way.
    */
   static void setGUI( boolean mode )
   {
      guiMode = mode;
      if ( mode )
         terminal = null;
      else
         terminal = new Scanner( System.in );
   }
   //--------------------- setInteractive -----------------------
   /**
    * set interactive mode, if not interactive, don't prompt 
    */
   static void setInteractive( boolean mode )
   {
      interactive = mode;
   }
   //--------------------- readLine --------------------------------
   /**
    * get input line from "user". If guiMode is true, use JOptionPanes,
    * else read from standard input. 
    */
   private static String readLine( String msg )
   {
      String line;
      if ( guiMode )
         line = JOptionPane.showInputDialog( null, msg );
      else
      {
         if ( interactive )
            System.out.println( msg + "\n> " );
         if ( terminal.hasNextLine() )
            line = terminal.nextLine();
         else
            line = "";
      } 
      return line;
   }
   
   //--------------------- showResults --------------------------------
   /**
    * Show results to "user". If guiMode is true, use JOptionPanes,
    * else print to standard output.
    */
   private static void showResults( String msg )
   {
      if ( guiMode )
         JOptionPane.showMessageDialog( null, msg );
      else
         System.out.println( msg );
   }
   //++++++++++++++++++++++ utility methods +++++++++++++++++++++++++++++
   // Should think about more of these
   //
   //------------------- loopAverage( _list ) ----------------------------
   /**
    * compute and return the average value of all the value fields of
    * the Data objects in the _list. Traverse the _list using the 
    * first() and next() methods of DataList. This is an iterative
    * solution.
    */
   private static double loopAverage( DataList _list )
   {
      Iterator<Data> iter = _list.iterator();
      double         sum = 0.0;
      
      while ( iter.hasNext() )
      {
         Data item = iter.next();
         sum += item.value;
      }
      return sum / _list.size();
   }
        
   //------------------- printTree( _bst, String ) ---------------------------
   private static void printTree( BinarySearchTree _bst, String title )
   {
      String dashes = " ------------------------ ";
      System.out.println( "\n" + dashes + title + dashes );
      if ( _bst == null || _bst.size() == 0 )
         System.out.println( "---Empty---" );
      else
      {
         System.out.println( _bst );
         System.out.println( "Tree has " + _bst.size() + " nodes." );
      }
   }
   //------------------- printList( _list, String ) ---------------------------
   private static void printList( DataList _list, String title )
   {
      String dashes = " ------------------------ ";
      System.out.println( "\n" + dashes + title + dashes );
      if ( _list == null || _list.size() == 0 )
         System.out.println( "---Empty---" );
      else
      {
         System.out.println( _list );
         double loopAve = loopAverage( _list );
         int total = (int) ( loopAve * _list.size() );
         System.out. println( "List average (loop) of " + _list.size() 
                                + " elements: " + loopAve + " Total: " + total ); 
      }
   }
   //------------------- generateData -----------------------------------
   /**
    * arguments are 
    *    numItems -- number items to generate
    *    seed     -- random number seed; -1 means let system pick it.
    */
   private static DataList generateData( int numItems, int seed )
   {
      String[] names = { "box", "cat", "dog", "ham", "bike", "bath", "beer", 
         "rose", "seal", "eggs", "iron", "brass" };
      DataList dl = new DataList();
      Random rng;
      if ( seed < 0 )
         rng = new Random();
      else 
         rng = new Random( seed );
      
      String        letters = "abcdefghijklmnopqrstuvwxyz";
      StringBuffer  keybuf  = new StringBuffer( "12" );
      
      while ( dl.size() < numItems )
      {
         // generate a key
         char letter1 = letters.charAt( rng.nextInt( letters.length() ));
         char letter2 = letters.charAt( rng.nextInt( letters.length() ));
         
         keybuf.setCharAt( 0, letter1 );
         keybuf.setCharAt( 1, letter2 );         
         String key = keybuf.toString();

         Data check = dl.find( key );
         if ( check == null )
         {            
            // generate a name field; these are allowed to duplicate
            int    nameIndex = rng.nextInt( names.length );
            String name = names[ nameIndex ];
                       
            // generate a value from 0 to 99
            int    val = rng.nextInt( 100 );
            dl.add( new Data( key, name, val ));
         }
      }
      return dl;
   }
}
