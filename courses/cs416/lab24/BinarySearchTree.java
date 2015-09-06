/**
 * BinarySearchTree -- this class uses a private inner class for
 *      tree nodes. (Although in this version it is public so I can 
 *      do a prefix walk in DisplayPanel.)
 *
 * @author rdb
 * April 2, 2008
 * 
 * Modified: April 6
 *      added iterator and node deletion code.
 * modified mlb Nov 2008 for Lab17
 */

import java.util.*;
import java.io.*;

public class BinarySearchTree implements Iterable<Data>
{
   //-------------------- instance variables ---------------------
   private Node   _root;
   private int    _size;
   
   //-------------------- constructors --------------------------
   /**
    * Construct an empty tree with no nodes
    */
   public BinarySearchTree()
   {
      _root = null;
   }
   /**
    * Construct a tree with a root 
    */
   public BinarySearchTree( Data rootData )
   {
      _root = new Node( rootData );
   }
   //--------------------- root() ----------------------------------
   /**
    * return the root of the tree; this is package access so that DisplayPanel
    * can do a prefix walk of the tree. Would be better to have multiple
    * iterators.
    */
   BinarySearchTree.Node root()
   {
      return _root;
   }
   //------------------ iterator() ---------------------------------
   public Iterator<Data> iterator()
   {
      return new InfixIterator();
   }
   //-------------------- findNode( Node, String ) -------------------------
   /**
    * Given a key value and a node in which to start the search, 
    * search the subtree rooted at the node to find the node that has
    * that key value, if it exists.
    * 
    * Return the Node found or null
    */
   public Node findNode( Node root, String key )
   {
      Node cur = root;
      Node found = null;
      while ( cur != null && found == null )
      {
         int cmp = key.compareTo( cur.data.key );
         if ( cmp == 0 )
            found = cur;
         else if ( cmp < 0 )
            cur = cur.left;
         else 
            cur = cur.right;
      }
      return cur;
   }
   //-------------------- find( String ) -------------------------
   /**
    * Given a key value, search the tree to find the node that has
    * that key value, if it exists.
    * 
    * Return the Data object from the node or null
    */
   public Data find( String key )
   {
      Data foundData = null;
      Node foundNode = findNode( _root, key );
      if ( foundNode != null )
         foundData = foundNode.data;
      return foundData;
   }
   //-------------------- height() ------------------------------------
   /**
    * return the depth of the tree
    */
   public int height()
   {
      return height( _root );
   }
   
   //-------------------- height(Node) --------------------------------
   /**
    * return the depth of the subtree rooted at node
    */
   private int height( Node node )
   {
      if ( node == null ) 
         return 0;
      else 
      {
         int leftHeight = height( node.left );
         int rightHeight = height( node.right );
         if ( leftHeight > rightHeight )
            return leftHeight + 1;
         else
            return rightHeight + 1;
      }         
   }
      
   //-------------------- removeNode( Node ) ------------------------------
   /**
    * Remove a specific node from the tree
    */
   public void removeNode( Node n )
   {
      if ( _root == n ) // n is the root
         removeRoot();
      else if ( n.parent.left == n )
         removeLeft( n.parent, n );
      else
         removeRight( n.parent, n );
   }
   //-------------------- removeRoot( ) ------------------------------
   /**
    * Remove the root node
    */
   private void removeRoot()
   {
      if ( _root.left != null )
      {
         addToFarRight( _root.left, _root.right );
         _root = _root.left;
         _root.parent = null;
      }
      else if ( _root.right != null )
      {
         addToFarLeft( _root.right, _root.left );
         _root = _root.right;
         _root.parent = null;
      }
      else
         _root = null;
   }
   //-------------------- removeLeft( Node, Node ) ----------------------------
   /**
    * Remove a node that is the left child of its parent
    */
   private void removeLeft( Node parent, Node n )
   {
      if ( n.left == null )
      {
         parent.left = n.right;
         if ( n.right != null ) 
            n.right.parent = parent;         
      }
      else
      {
         n.left.parent = parent;
         parent.left = n.left;
         addToFarRight( parent.left, n.right );
      }
   }
   //-------------------- removeRight( Node, Node ) ----------------------------
   /**
    * Remove a node that is the right child of its parent
    */
   private void removeRight( Node parent, Node n )
   {
      if ( n.right == null )
      {
         parent.right = n.left;
         if ( n.left != null ) 
            n.left.parent = parent;
      }
      else
      {
         parent.right = n.right;
         n.right.parent = parent;
         addToFarLeft( parent.right, n.left );
      }
   }
   //---------------------- addToFarRight( node, node ) ----------------
   /**
    * add the subtree Node as the right most descendant of the 1st argument
    */
   private void addToFarRight( Node n, Node subtree )
   {
      while ( n.right != null )
         n = n.right;
      n.right = subtree;
      if ( subtree != null )
         subtree.parent = n;
   }
   //----------------------- addToFarLeft( Node, Node ) -----------------------
   /**
    * add the subtree Node as the left most descendant of the 1st argument
    */
   private void addToFarLeft( Node n, Node subtree )
   {
      while ( n.left != null )
         n = n.left;
      n.left = subtree;
      if ( subtree != null )
         subtree.parent = n;
   }
    
   //------------------ preOrderPrint( PrintStream ) ---------------
   /**
    * Traverse the tree in in-order fashion to print the nodes of the tree
    * to the PrintStream parameter.
    * This method uses the private utility method to print a subtree rooted
    * at a particular node.
    */
   public void preOrderPrint( PrintStream out )
   {
      preOrderPrint( out, _root );
      out.println();
   }
   //------------------ preOrderPrint( PrintStream, node ) ---------------
   /**
    * Print the subtree rooted at "node" in in-order fashion.
    */
   private void preOrderPrint( PrintStream out, Node n )
   {
      if ( n != null )
      {
         out.print( " " + n.data + " ");
         preOrderPrint( out, n.left );
         preOrderPrint( out, n.right );
      }
   }
      
   //--------------------- add -----------------------------------
   /**
    * add a node to the tree in its proper position determined by the
    * "key" field of the Data object. This method uses the addNode 
    * recursive utility method.
    */
   public void add( Data data )
   {
      Node newNode = new Node( data );
      if ( _root == null )
         _root = newNode;
      else
         addNode( _root, newNode );
      _size++;
   }
 
   //------------------ addNode( Node, Node ) -----------------------
   /**
    * a recursive method to add a new Node (2nd argument) to the subtree
    * rooted at the first argument.
    */
   private void addNode( Node parent, Node newOne )
   {
      if ( newOne.data.compareTo( parent.data ) < 0 )
      {
         if ( parent.left != null )
            addNode( parent.left, newOne );
         else 
         {
            parent.left = newOne;
            newOne.parent = parent;
         }
      }
      else
      {
         if ( parent.right != null )
            addNode( parent.right, newOne );
         else 
         {
            parent.right = newOne;
            newOne.parent = parent;
         }
      }
   }
   
   //-------------------------- size() -------------------------
   /**
    * return tree size
    */
   public int size()
   {
      return _size;
   }
   //-------------------------- toString() -------------------------
   /**
    * Generate a string representation of the tree.
    */
   public String toString()
   {
      return toString( _root, "  ", "  " ) ;        
   }
   
   /**
    * recursively generate a string representation for a Node of a tree.
    * indent is increased for increasing depth.
    * branch is a short character string that prefixes each node indicating
    *        whether this node is a left (L) or right (R) child of its parent.
    */
   private String toString( Node n, String indent, String branch )
   {
      String s = "";
      String p = "";
      if ( n != null )
      {
         String prefix = indent.substring( 0, indent.length() - 2 ) + branch;
         if ( n.parent == null )
            p = " parent: null " ;
         else
            p = " parent: " + n.parent.data;
           
         s += prefix + n.data.toString() + p + "\n";
         if ( n.left != null )
            s += toString( n.left, indent + "  ", "L " );
         if ( n.right != null )
            s += toString( n.right, indent + "  ", "R " );
      }
      return s;
   }
   //+++++++++++++++++++++++ inner class Node ++++++++++++++++++++++
   /**
    * The Node class is now public, should be "package", probably.
    */
   public class Node
   {
      //-------------- instance variables ---------------------------
      public Data data;
      public Node left;
      public Node right;
      public Node parent;
      
      //--------------- constructor --------------------------------
      public Node( Data d )
      {
         data = d;
         left = null;
         right = null;
         parent = null;
      }
   }
   //++++++++++++++++++++++++ inner class InfixIterator ++++++++++++++++++
   /**
    * An iterator that walks the tree in prefix order
    */
   public class InfixIterator implements Iterator<Data>
   {
      //--------------------- instance variables ---------------------------
      private Stack<NodeVisit> stack;  // top of stack defines the next node
      private Node             nextNode;
      private Node             lastNode;
      
      //+++++++++++ private inner class Step +++++++++++++++++++++++++++++
      private class NodeVisit
      {
         public Node node;
         public int  visit;  // 0, 1, 2, or 3; but only 0, 1 and 2 in stack
         
         public NodeVisit( Node n, int v )
         {
            node = n; visit = v;
         }
      }
      
      //----------------------- constructor ------------------------------
      public InfixIterator()
      {
         nextNode = null;
         if ( _root == null )
            stack = null;
         else
         {
            stack = new Stack<NodeVisit>();
            stack.push( new NodeVisit( _root, 0 ) );
            nextNode = getNext();
         }
         lastNode = null;
      }
      //---------------------- hasNext() -----------------------------------
      public boolean hasNext()
      {
         return  nextNode != null;
      }
      //----------------------- Data next() ----------------------------------
      public Data next()
      {
         lastNode = nextNode;
         Data ret;
         if ( nextNode == null )
            throw new NoSuchElementException( "BinarySearchTree.iterator" );
         ret = nextNode.data;
         nextNode = getNext();
         return ret;
      }
         
      //----------------------- Node getNext() --------------------------------
      private Node getNext() 
      {
         Node ret = null;
         if ( stack.isEmpty() )
            return null;
         NodeVisit top = stack.peek();
         {
            switch ( top.visit++ )  
            {
               case 0:       // about to make first visit; push left onto stack
                  
                  if ( top.node.left != null )
                  {
                      stack.push( new NodeVisit( top.node.left, 0 )); 
                      ret = getNext();    // we'll invoke next recursively
                  }
                  else
                  {
                     ret = top.node;
                     top.visit++;
                  }
                  break;
               case 1:       // we've been down the left branch, return the node
                  // and set up to go down the right branch
                  ret = top.node;              
                  break;
               case 2:       // done the node, go down the right branch
                  
                  if ( top.node.right != null )
                  {
                      stack.push( new NodeVisit( top.node.right, 0 ));
                      ret = getNext();
                  }
                  else  // can pop this one off the stack and continue
                  {
                     stack.pop();
                     ret = getNext();
                  }
                  break;
                case 3:       // finished right branch, pop from stack and recurse
                  if ( !stack.isEmpty() )
                     stack.pop();
                  ret = getNext();
                  break;
               default:
                  if ( !stack.isEmpty() )
                      stack.pop();
                  return null;
            }
         }
         //System.out.println( stack.size() + " " + stack.peek().visit + " " + ret );  
         return ret;
      }
      //------------------ remove() ----------------------------------
      public void remove()
      {
         removeNode( lastNode );
         lastNode = null;
      }
   }     
}
