/**
 * DataList: a linked _list implementation using a public inner Node class. 
 * 
 * This has several methods to add and remove objects
 *   void add( Data )          -- adds Data somewhere in the _list
 *   void addHead( Data )      -- adds Data to _list at the head
 *   void addTail( Data )      -- adds Data to _list at the tail
 *   Data remove()             -- removes the head of the _list and returns value
 *   Data remove( String key ) -- looks for an object whose key equals "key", 
 *                                  if found, it removes it from _list 
 *                                  and returns its value, else return null
 *   Data find( String key )   -- looks for an object whose key equals "key", 
 *                                  if found, returns its value, else return null
 * 
 *   void foreach( Action act ) -- invoke the execute( Data ) method of act for each
 *                                 element on the _list. 
 *   Iterator iterator()        -- returns an Iterator object for this _list
 * 
 * @author rdb
 * February 2008
 */
import java.util.Iterator;
import java.util.NoSuchElementException;

public class DataList implements Iterable
{
   //----------------- instance variables --------------------------
   //-- these are "package" access, but they would not normally be!
   Node _head;
   Node _tail;
   int  _size;
   
   //---------------- constructor ----------------------------------
   public DataList() 
   {
      _head = null;
      _tail = null;
      _size = 0;
   }
   //----------------- isEmpty( ) -------------------------------
   public boolean isEmpty()
   {
      return _head == null;
   }
   //----------------- foreach( Action act ) ----------------------
   /***
   public void foreach( Action act )
   {
      Node cur = _head;
      while ( cur != null )
      {
         act.execute( cur.data );
         cur = cur.next;
      }
   }
   /*******************************/
   //----------------- toString( ) -------------------------------
   public String toString()
   {
      Node walk = _head;
      String str = "";
      while ( walk != null )
      {
         str += walk.data + ", ";
         walk = walk.next;
      }
      return str;
   }
   //----------------- size( ) -------------------------------
   public int size()
   {
      return _size;
   }
   //----------------- add( Data ) -------------------------------
   /**
    * add a node to the _list with the data defined by the Data object
    * It is not specified where the node is added; this implementation
    * adds it "after" the head if there is one, else it becomes the head.
    */
   public void add( Data newOne )
   {
      Node newNode = new Node( newOne, null, null );
      if ( _head == null )
         _head = _tail = newNode;
      else          
      {
         newNode.next = _head.next;
         newNode.prev = _head;
         if ( _head.next != null )
            _head.next.prev = newNode;
         _head.next = newNode;  
         if ( _head == _tail )
            _tail = newNode;
      }
      _size++;
   }
   //----------------- addHead( Object ) -------------------------------
   public void addHead( Data newOne )
   {
      Node newNode = new Node( newOne, _head, null );
      if ( _head == null )
         _tail = newNode;
      else
         _head.prev = newNode;
      _head = newNode;
      _size++;
   }
   //----------------- addTail( Data ) -------------------------------
   /**
    * adds the data to a node that becomes the new _list tail.
    */
   public void addTail( Data newOne )
   {
      if ( _tail == null )
         addHead( newOne );
      else
      {
         Node newNode = new Node( newOne, null, _tail );
         _tail.next = newNode;
         _tail = newNode;
         _size++;
      }
   }
   //----------------- remove() ----------------------------------
   /**
    * remove and return the head of the _list
    */
   public Data remove()
   {
      Data  retObj = null;
      if ( _head != null )
      {
         retObj = _head.data;
         _head = _head.next;
         _size--;
      }
      if ( _head == null )  // if last element, update tail
         _tail = null;
      else                 // otherwise update its prev pointer
         _head.prev = null;
      return retObj;
   }
   //----------------- remove( String ) -----------------------------
   public Data remove( String key )
   {
      Data    retObj = null;
      Node cur = _head;
      while ( cur != null && cur.data.getKey().compareTo( key ) != 0 )
      {
         cur = cur.next;
      }
      if ( cur != null )  // remove node from the _list and return the object
      {
         if ( cur == _head )    // found node is head of _list
         {
            _head = cur.next;
            if ( _head == null )
               _tail = null;
            else
               _head.prev = null;
         }
         else   // previous node now points to deleted node's next 
         {
            cur.prev.next = cur.next; 
            if ( cur.next != null )
               cur.next.prev = cur.prev;
            if ( cur == _tail )
            {
               //System.out.println( "tail " + cur.prev.data.value );
               _tail = cur.prev;
               //System.out.println( "            " + _tail.next );
            }
         }
         
         retObj = cur.data;
         _size--;
     }
     checkList();
     return retObj;
   }
   //----------------- find( String ) -----------------------------
   public Data find( String key )
   {
      Node cur = _head;
      while ( cur != null 
           && cur.data.getKey().compareTo( key ) != 0 )
         cur = cur.next;
      if ( cur == null )
         return null;     // not found
      else
         return cur.data;
   }
   //----------------- Data first() --------------------------------
   /**
    * return the object that is at the start of the _list and set the
    * "current" node to the head of the _list. Returns null if there are 
    * no elements on the _list.
    */
   public Data first()
   {
      if ( _head == null )
         return null;
      else
      {
         return _head.data;
      }
   }
   //----------------- Node head() --------------------------------
   /**
    * return the node that is at the start of the _list
    */
   public Node head()
   {
      return _head;
   }
   
   //----------------- Iterator iterator() --------------------------------
   /**
    * return an iterator for this _list
    */
   public Iterator<Data> iterator()
   {
      return new ListIterator();
   }

   //+++++++++++++++++++++ Iterator inner class ++++++++++++++++++++++++++++++
   //---------------------------------------------------------------------
   /**
    * Note: "static" is used here in order to properly "export" the generic
    * part of the inner class specification.
    */
   public class ListIterator implements Iterator<Data>
   {
      //----------------------- instance variables ----------------------
      private Node _cur  = null;
      private Node _next = null;
      //----------------------- constructor -----------------------------
      public ListIterator()
      {
         _next = (DataList.Node) _head;
      }
      //------------------------ hasNext() ------------------------------
      /**
       * returns true if the Collection has a next element.
       */
      public boolean hasNext()
      {
         return _next != null;
      }
      //------------------------ next ------------------------------------
      /** 
       * return next Node in _list (if there is one)
       */
      public Data next()
      {
         if ( !hasNext() )
            throw new NoSuchElementException( "No 'next' element" );
         _cur = _next;
         _next = _next.next;
         return  (Data) _cur.data();
      }
      //---------------------- remove() -----------------------------------
      /** 
       * remove last element returned by next()
       */
      public void remove()
      {
         checkList();
         DataList.Node temp;
         if ( _cur != null )
         {
            if ( _cur == _head ) // should be head
            {
               temp = _cur.next;
               _head = temp;
               if ( _head == null )
                  _tail = null;
               else
                  _head.prev = null;             
            }
            else
            {
               _cur.prev.next = _cur.next;
               if ( _cur.next != null )
                  _cur.next.prev = _cur.prev;
               if ( _cur == _tail )
               {
                  _tail = _cur.prev;
                  _tail.next = null;
               }
            }
            _cur = null;
            _size--;
            checkList();
         }
         else
            System.out.println( "iter.remove null " );
      }
   }
     
   //----------------- checkList -----------------------------------------
   public void checkList()
   {
      Node cur = _head;
      Node prev = null;
      Node next = null;
      
      if ( cur == null )
         return;
      if ( _head.prev != null )
         System.err.println( "************************************ " +
                            "head.prev NOT null: \n" + _head.prev.data );
      if ( _tail.next != null )
         System.err.println( "************************************ \n" +
                            "tail.next NOT null: \n" + _tail.next.data );
      // test links
      while ( cur != null )
      {
         if ( prev != null )
         {
            if ( cur.prev != prev )
                System.err.println( "******************************** "
                      + " Incorrect links: cur :: cur.prev != prev \n" + 
                      cur.data + " :: " + cur.prev.data + " != " + prev.data );
            if ( cur != prev.next )
                System.err.println( "************************************* "
                      + "Incorrect links: prev::  cur != prev.next \n" + 
                      prev.data + " :: " + cur.data + " != " + prev.next.data );
         }
         prev = cur;
         cur = cur.next;
      }
      if ( prev != _tail )
      {
         System.err.println( "**********************************" +
                            "Last node is not tail: \n" + 
                            prev.data + " != " + _tail.data );
      }
   }
            
         
   //+++++++++++++++++++++ Node inner class ++++++++++++++++++++++++++++++
   //---------------------------------------------------------------------
   /**
    * 
    */
   public class Node 
   {
      public Node next = null;
      public Node prev = null;
      public Data data = null;
      
      public Node( Data adder, Node n, Node p )
      {
         data = adder;  next = n; prev = p;
      }
      public Data data( ) 
      {  
         return data; 
      }
   }
   //------------------------------ unit test code ---------------------------
   //--------------------- main ----------------------------------
   public static void main( String[] args )
   {
         
      DataList l = new DataList();
      
      System.out.println( l.find( "bogus" ));
      l.add( new Data( "A", "asp", 1 ) );
      l.add( new Data( "B", "box", 1 ) );
      l.add( new Data( "C", "cat", 2 ));
      l.add( new Data( "D", "dog", 3 ));
      l.add( new Data( "E", "easel", 3 ));
      
      System.out.println( "Searching for A: " + l.find( "A" ));
      System.out.println( "Searching for C: " + l.find( "C" ));
      System.out.println( "Searching for d: " + l.find( "d" ));
      System.out.println( "Searching for E: " + l.find( "E" ));
      
      l.addTail( new Data( "F", "fox", 123 ) );
      l.addTail( new Data( "G", "gael", 124 ) );

      System.out.println( "Searching for F: " + l.find( "F" ));
      System.out.println( "Searching for G: " + l.find( "G" ));   
      
      System.out.println( "List size (7) = " + l.size() );
      
      // now remove some
      l.remove( "F" );
      System.out.println( "List size (6) = " + l.size() );
      
      l.remove( "A" );
      System.out.println( "List size (5) = " + l.size() );
      
      l.remove( "Not There" );
      System.out.println( "List size (5) = " + l.size() );
      
      while ( l.size() > 0 )
         l.remove();
      System.out.println( "List size (0) = " + l.size() );

   }
}
/**************************/
         
