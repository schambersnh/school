/**
 * TreeApp - the main class for controlling the application code. 
 * 
 * 
 * The application semantics including the graphics generated 
 * by the app are controlled from this class.
 *********************************************************************
 *
 *   Step 4: Null descendents
 *           Test your method with the parameters listed below and 
 *           fill in the last column. You must click the Null Descendants
 *           button again after pressing both the "Balance" and "Data"
 *           buttons.
 *
 *   Fill in the list below:
 *          Seed  Data Size   Balanced?     value field of root node
 *            0       8          N          9   
 *            0       8          Y          9
 *            1       8          N          9
 *            1       8          Y          9
 *            2       8          N          9
 *            2       8          Y          9
 *
 *            2      12          N          13
 *            2      12          Y          13
 *            1      12          N          13
 *            1      12          Y          13
 *            0      12          N          13
 *            0      12          Y          13
 * -----------------------------------------------------------------
 * Do you have an explanation for these numbers?
 * 
 * The value field of the root node is always one more then the data 
 * size.
 * 
 * 
 */
import javax.swing.*;
import java.util.*;
   
public class TreeApp
{
   //--------------------- instance variables ----------------------------
   private DataList _list;
   private BinarySearchTree _bst = null;
   private DisplayPanel     _display;
   
   //------ "package" state variables; set by GUI ----------
   boolean printMode     = true;  // if set, print after each command
   
   //--- data generation constants with package access so GUI can change
   int     dataSize        = 8;
   int     minDataSize     = 0;
   int     maxDataSize     = 100;
   int     defaultDataSize = 8;
   int     minNodeWidth    = 1;
   int     maxNodeWidth    = 90;
   int     defaultNodeWidth = 90;
   
   int     rngSeed          = 0;   // random seed
   int     maxSeed          = 16;   // arbitrary number
   
   //----- duplicates flag
   private boolean _allowDuplicates = false;
   
   //---------------------- constructor ----------------------------------
   /**
    * The app needs the display reference only so it can update the 
    * DisplayPanel with new data when needed and tell it to redraw
    * when things change.
    */
   public TreeApp( DisplayPanel display )
   {
      _display = display;
      makeData( _allowDuplicates );
   }
   //////////////////////////////////////////////////////////////////
   // Step 1 delByName
   //---------------------- delByName() -----------------------------
   /**
    * find all the nodes with a specified "name" field (not the search key)
    * and delete them from the tree.
    */
   public void delByName( )
   { 
      String name = JOptionPane.showInputDialog( null, "Enter a 'name' field" );
      //////////////////////////////////////////////////////////////
      // Step 1 -- use an iterator from BinarySearchTree
      //
      // delete nodes with a specified name field
      //////////////////////////////////////////////////////////////
      Iterator<Data> iterator = _bst.iterator();
      
      while(iterator.hasNext())
      {
        if(iterator.next().name.equals(name))
        iterator.remove();
      }
 
      
      
      ////////////////////////////////////////////////////////////////
      update();
   }
   /////////////////////////////////////////////////////////////////////
   // Step 2 nameCount
   //---------------------- nameCount() -----------------------------
   /**
    * Given a "name" field typed in by the user (NOT a key, but a name), 
    * use recursion to find all the nodes with this name field. 
    * Report how many nodes have the specified "name" field . 
    */
   public void nameCount( )
   { 
      String name = JOptionPane.showInputDialog( null, "Enter a 'name' field" );
      
      int count = 0;
      if ( name != null )
         count = nameCount( _bst.root(), name );
      
      
      
                           
      System.out.printf( "There are %d nodes with the name %s\n", 
                        count, name );
   }
   //-----------nameCount( BinarySearchTree.Node, String ) -------------
   /**
    * recursion part of nameCount(): count number of nodes in the tree
    * with the specified name.
    */
   public int nameCount( BinarySearchTree.Node node, String name )
   {
      int count = 0;
      /////////////////////////////////////////////////////////////////
      // Step 2 -- Use recursion to count occurrences of nodes with a given name
      //
      ////////////////////////////////////////////////////////////////
     if(node == null)
      {
       return 0;
     }
        if(node.data.name.equals(name))
        {
          count++;
        }
        
        count += nameCount(node.left, name);
        count += nameCount(node.right, name); 
       
 
      return count;
      
   }
   ///////////////////////////////////////////////////////////////////////
   // Step 3 swapData
   //---------------------- swapData() -----------------------------
   /**
    * For each node in the tree, from the bottom up,
    *    reorder the data in the nodes so that the value of the
    *    left child is always <= the value of the right node. 
    * I.e., if value of left child is > value of right child,
    *     exchange data between left and right, but DO NOT EXCHANGE 
    *     the key values -- that would mess up the tree order.
    * Make no changes unless both children exist.
    */
   public void swapData( )
   { 
      swapData( _bst.root() );
      _display.update();
   }
   //---------------------- swapData( BinarySearchTree.Node ) ------------
   /**
    *  Recursive helper: first recursive callIf a node has 2 children, 
    *  order them so that the left.value <= right.value
    */
   private void swapData( BinarySearchTree.Node node )
   { 
      ////////////////////////////////////////////////////////////////////
      // Step 3 -- code goes here
      //
      // Swap data in left and right nodes if left value >= right value
      //   make sure to recurse to the children as well as making the test here
      /////////////////////////////////////////////////////////////////////
      if (node!= null)
      {
        if(node.left != null && node.right != null)
        {
          if (node.left.data.value >= node.right.data.value)
          {
            int left = node.left.data.value;
            int right = node.right.data.value;
            node.left.data.value = right;
            node.right.data.value = left;
            
            String leftName = node.left.data.name;
            String rightName = node.right.data.name;
            node.left.data.name = rightName;
            node.right.data.name = leftName;
            
          }
            swapData(node.left);
            swapData(node.right);
        }
      }
     
      
      
      
      
   }
   /////////////////////////////////////////////////////////////////////
   // Step 4 nullDescendents
   //---------------------- nullDescendents() -----------------------------
   /**
    * For each node in the tree replace the value field of its Data object 
    * with the count of all its null descendents. A leaf node has 2 null
    * descendents. A node with 1 child has 1 null descendent plus the 
    * count from its non-null child, etc.
    */
   public void nullDescendents( )
   { 
      nullDescendents( _bst.root() );
      _display.update();
   }
   //-------------------- nullDescendents( Node ) -----------------------
   public int nullDescendents( BinarySearchTree.Node node )
   {
      //////////////////////////////////////////////////////////////
      // Step 4 code goes here
      //
      // 
      //////////////////////////////////////////////////////////////
      int count = 0;
      
      if(node.left == null && node.right == null)
      {
        count += 2;
        node.data.value = count;
      }
      else if (node.left == null)
      {
        count++;
        count += nullDescendents(node.right);
        node.data.value = count;
      }
      else if(node.right == null)
      {
        count++;
        count += nullDescendents(node.left);
        node.data.value = count;
      }
      else
      {
        count+= nullDescendents(node.left);
        count+= nullDescendents(node.right);
        node.data.value = count;
      }
      
      
      
      
      
      
      return count;   // don't for get to replace this line
   }
   /////////////////////////////////////////////////////////////////////
   // Step 5 findPath
   //---------------------- findPath( ) ---------------------------
   /**
    * Get a "name" field typed in by the user (NOT a key, but a name), 
    * use recursion to find a node with this name field.. 
    */
   public void findPath()
   {      
      String name = JOptionPane.showInputDialog( null, "Enter a 'name' field" );
      if ( name != null ) 
      {
         String result;
         Vector<BinarySearchTree.Node> path = findPath( _bst.root(), name );
         if ( path == null )
            result = name + " not found in tree. ";
         else
         {
            result = "----------- path to " + name + "------\n";
            for ( BinarySearchTree.Node node: path )
               result += node.data + "; ";   
         }
         JOptionPane.showMessageDialog( null, result );
      }
   }
   //----------- findPath( TreeNode, String ) -------------
   /**
    * recursion part of findPath(): 
    */
   public Vector<BinarySearchTree.Node> 
                           findPath( BinarySearchTree.Node node, String name )
   {
      /////////////////////////////////////////////////////////////////
      // ---------------- Step 5 code goes here ------------------
      //////////////////////////////////////////////////////////////
      // Use recursion to find a node with the given name field.
      //    if find a match, create a Vector<TreeNode> object and 
      //       assign it to the "path" variable and add this node to it.
      //    else recurse to left subtree
      //       if that recursion returns a non-null path, add this node
      //           to the front of the path and return the path
      //       else do the same for right subtree
      ////////////////////////////////////////////////////////////////
      Vector<BinarySearchTree.Node> path = null;
      
      if(node != null)
      {
        if(node.data.name.equals(name))
        {
          Vector<BinarySearchTree.Node> vect = new Vector<BinarySearchTree.Node>();
          path = vect;
          path.add(node);
        }
        else
        {
          if(node.left != null)
          {
            path = findPath(node.left, name);
            if(path != null)
            {
              path.add(0, node);
            }
            else if(node.right != null)
            {
              path = findPath(node.right, name);
            if(path != null)
            {
              path.add(0, node);
            }
            }
          }
         
        }
      }

      
      
      
      
      
      
      
      return path;
   }
   
   
   ////////////////////////////////////////////////////////////////////
   // DO NOT CHANGE ANY CODE BELOW HERE
   ////////////////////////////////////////////////////////////////////
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
   //---------------------- setDuplicates( boolean allow ) ---------------
   /**
    * if argument is true, duplicate keys are allowed in tree
    */
   public void setDuplicates( boolean allow )
   {
      _allowDuplicates = allow;
   }
   //+++++++++++++++++++++ methods invoked by GUI buttons +++++++++++++++++++
   //---------------------- makeData( boolean ) -----------------------------
   /**
    * generate a new DataList 
    */
   public void makeData( boolean allowDuplicates )
   {  
      _allowDuplicates = allowDuplicates;
      _list = generateData( this.dataSize, this.rngSeed );
      
      _bst = new BinarySearchTree();
      
      Iterator<Data> iter = _list.iterator();
      while ( iter.hasNext() )
         _bst.add( iter.next() );
      
      print();
      update();
   }
   //---------------------- toArray() -----------------------------
   /**
    * Generate an ordered list from tree
    */
   public void toArray( )
   { 
      Vector<Data> sorted = toVector();
      
      _bst = new BinarySearchTree();
      Iterator<Data> iter = sorted.iterator();
      while ( iter.hasNext() )
         _bst.add( iter.next() );
     
      _display.setTree( _bst );
      _display.update();
   }
   //----------------------- toVector() ----------------------------
   private Vector<Data> toVector()
   {
      Vector<Data> sorted = new Vector<Data>();
      
      Iterator<Data> iter = _bst.iterator();
      while ( iter.hasNext() )
         sorted.add( iter.next() );
      return sorted;
   }
   //---------------------- balanceTree() -----------------------------
   /**
    * balance the tree 
    */
   public void balanceTree( )
   {  
      Vector<Data> sorted = new Vector<Data>();
      
      Iterator<Data> iter = _bst.iterator();
      while ( iter.hasNext() )
         sorted.add( iter.next() );
      
      BinarySearchTree newTree  = new BinarySearchTree();
      
      binaryAdd( sorted, 0, sorted.size() - 1, newTree );
      _bst = newTree;
      print();
      update();
   }
   //------------------------ binaryAdd ------------------------
   private void binaryAdd( Vector<Data> order, int low, int high, 
                          BinarySearchTree bst )
   {
      if ( high >= low )
      {
         int mid = ( high + low ) / 2;
         bst.add( order.get( mid ) );
         binaryAdd( order, low, mid - 1, bst );
         binaryAdd( order, mid + 1, high, bst );
      }
   }

   //---------------------- print() -----------------------------
   /**
    * print the list for debugging; maybe more 
    */
   public void print( )
   {  
      printList( _list, "DebugList" );
      if ( _list != null )
         _list.checkList();
      printTree( _bst, "DebugTree" );
   }
   //++++++++++++++++++++++ utility methods +++++++++++++++++++++++++++++++++++++
   // You should think about more of these
   //
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
   private void printTree( BinarySearchTree _bst, String title )
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
   private void printList( DataList _list, String title )
   {
      String dashes = " ------------------------ ";
      System.out.println( "\n" + dashes + title + dashes );
      if ( _list == null || _list.size() == 0 )
         System.out.println( "---Empty---" );
      else
      {
         System.out.println( _list );
      }
   }
   //------------------- generateData -----------------------------------
   /**
    * arguments are 
    *    numItems -- number items to generate
    *    seed     -- random number seed; -1 means let system pick it.
    */
   private DataList generateData( int numItems, int seed )
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
      if ( _allowDuplicates )
         letters = "abc";
      StringBuffer  keybuf  = new StringBuffer( "12" );
      
      while ( dl.size() < numItems )
      {
         // generate a key
         char letter1 = letters.charAt( rng.nextInt( letters.length() ));
         char letter2 = letters.charAt( rng.nextInt( letters.length() ));
         
         keybuf.setCharAt( 0, letter1 );
         keybuf.setCharAt( 1, letter2 );         
         String key = keybuf.toString();

         if ( _allowDuplicates || dl.find( key ) == null )
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
   //--------------------- main -----------------------------------------
   public static void main( String[] args )
   {
      OLETreeLab app = new OLETreeLab( "TreeApp", args );
   }
}
