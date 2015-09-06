/**
 * TreeApp - the main class for controlling the application code. 
 * 
 * 
 * The application semantics including the graphics generated 
 * by the app are controlled from this class.
 */
import javax.swing.*;
import java.util.*;
   
public class TreeApp
{
   //--------------------- instance variables ----------------------------
   private BinTree _bst = null;
   private DisplayPanel     _display;
   private boolean          _autoBalance = false;
   
   //------ "package" state variables; set by GUI ----------
   boolean printMode     = true;  // if set, print after each command
   
   //--- data generation constants with package access so GUI can change
   int     dataSize        = 8;
   int     minDataSize     = 0;
   int     maxDataSize     = 20;
   int     defaultDataSize = 8;
   int     minNodeWidth    = 1;
   int     maxNodeWidth    = 70;
   int     defaultNodeWidth = 45;
   int     maxValue         = 99;
   
   int     rngSeed          = 2;   // random seed
   int     maxSeed          = 16;   // arbitrary number
   
   //--- subset constants
   private int     subsetMin       = 30;   // arbitrary; all values are 0, 99
   private int     subsetMax       = 70;
   
   //---------------------- constructor ----------------------------------
   /**
    * The app needs the display reference only so it can update the 
    * DisplayListPanel with new Lists when needed and tell it to redraw
    * when things change.
    */
   public TreeApp( DisplayPanel display )
   {
      _display = display;
      makeData();
   }
   //---------------------- update() -----------------------------
   /**
    * Something in gui has change; update whatever needs to be updated
    * The GUI calls the app when a GUI update occurs; the app (in this
    * case) only needs to pass this along to the DisplayListPanel.
    */
   public void update()
   { 
       _display.setTree( _bst );     
   }
   //----------------------  setNodeWidth( int ) -------------------
   public void setNodeWidth( int newW )
   {
      if ( newW > 0 )
         _display.setNodeWidth( newW );
   }
   //+++++++++++++++++++++ methods invoked by GUI buttons +++++++++++++++++++
   //---------------------- makeData() -----------------------------
   /**
    * generate a list of data values 
    */
   public void makeData( )
   {  
      ArrayList<Integer> dl = generateData( this.dataSize, this.rngSeed );
      
      _bst = new BinTree( new BiNode( 0, maxValue ));
      
      Iterator<Integer> iter = dl.iterator();
      while ( iter.hasNext() )
         _bst.add( iter.next() );
      update();
   }

   //---------------------- print() -----------------------------
   /**
    * print the list for debugging; maybe more 
    */
   public void print( )
   {  
      _bst.print();
   }
   
   //---------------------- split() -----------------------------
   /**
    * split the spatial region at a given value
    */
   public void split( )
   { 
      String outMsg;
      String input;
      input = JOptionPane.showInputDialog( null, "Enter value for split" );
      if ( input != null && input.length() > 0 )
      {  
         int value = 0;
         
         Scanner scan = new Scanner( input );
         while( scan.hasNextInt() )
         {
            value = scan.nextInt();            
            _bst.split( value );
            _display.update();
            print();
         }
      }
   }
   
   //---------------------- merge() -----------------------------
   /**
    * merge the spatial region contaiing a given value with its sibling
    */
   public void merge( )
   { 
      String outMsg;
      String input;
      input = JOptionPane.showInputDialog( null, "Enter value for merge" );
      if ( input != null && input.length() > 0 )
      {  
         int value = 0;
         
         Scanner scan = new Scanner( input );
         if ( scan.hasNextInt() )
         {
            value = scan.nextInt();            
            _bst.merge( value );
            _display.update();
            print();
         }
         else
            JOptionPane.showMessageDialog( null, "Bad input: need a number" );
      }
   }
   
   //---------------------- pointsInRange() -----------------------------
   /**
    * Find the leaf with a specified value and print its data 
    */
   public void pointsInRange( )
   { 
      String outMsg;
      String input;
      input = JOptionPane.showInputDialog( null, "Enter 2 values for the range" );
      if ( input != null && input.length() > 0 )
      {  
         int min = 0;
         int max = maxValue;
         
         Scanner scan = new Scanner( input );
         if ( scan.hasNextInt() )
         {
            min = scan.nextInt();
            if ( scan.hasNextInt() )
               max = scan.nextInt();
            if ( min > max )
               outMsg = "min > max: " + min + " " + max;
            else
            {
               ArrayList<Integer> data = _bst.pointsInRange( min, max );
               outMsg = BiNode.toString( data );
            }
         }
         else
            outMsg = "Bad input: need a number";

         JOptionPane.showMessageDialog( null, outMsg );
      }
   }
   
   //---------------------- balance() -----------------------------
   /**
    * do a heuristic rebalancing of the tree 
    */
   public void balance( )
   {  
      //_bst.balance();
   }
   
   
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
   
   
   //------------------- printTree( _bst, String ) ---------------------------
   private void printTree( BinTree _bst, String title )
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
   
   //------------------- generateData -----------------------------------
   /**
    * arguments are 
    *    numItems -- number items to generate
    *    seed     -- random number seed; -1 means let system pick it.
    */
   private ArrayList<Integer> generateData( int numItems, int seed )
   {
      Random rng;
      ArrayList<Integer> dl = new ArrayList<Integer>();
      if ( seed < 0 )
         rng = new Random();
      else 
         rng = new Random( seed );
      
      while ( dl.size() < numItems )
      {
         dl.add( new Integer( rng.nextInt( maxValue + 1 )));
      }
      return dl;
   }
   //----------------------- main ------------------------------------
   /**
    * A convenience method to invoke the main application: BinTreeLab
    */
   public static void main( String[] args )
   {
      BinTreeLab.main( args );
   }
}
