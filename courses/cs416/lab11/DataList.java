/**
 * A primitive list of Data objects using a public DataNode class. 
 * 
 * All insertion and deletion operations are the responsibility of the user.
 *
 * 
 * public interface:
 *   DataList()              -- create an empty list
 *   DataNode head()         -- return the node that is the first element on list
 *                              if list is empty, returns null;
 *   DataNode tail()         -- return the node that is the last element on list
 *                              if list is empty, returns null;
 *   void addHead( Data )    -- add the Data object to the start of the list
 *   void addTail( Data )    -- add the Data object to the end of the list
 *   void add( Data )        -- add the Data object to the end of the list
 * 
 *   int     size()          -- returns the size of the list
 *  
 *   DataNode find( String ) -- search the list for the first node whose key
 *                              field matches the parameter to find. return
 *                              a reference to that node -- or return null if
 *                              no node in the list has a matching key field.
 * 
 * @author rdb
 * February 2009
 */

public class DataList
{
   //----------------- instance variables --------------------------
   protected DataNode _head;
   protected DataNode _tail;
   
   //---------------- constructor ----------------------------------
   /**
    * create an empty linked list
    */
   public DataList() 
   {
      _head = null;
      _tail = null;
   }
   //----------------- DataNode head() --------------------------------
   /**
    * return the object that is at the start of the list and set the
    * "current" node to the head of the list. Returns null if there are 
    * no elements on the list.
    */
   public DataNode head()
   {
      return _head;
   }
   //----------------- DataNode tail() --------------------------------
   /**
    * return the object that is at the end of the list and set the
    * "current" node to the tail of the list. Returns null if there are 
    * no elements on the list.
    */
   public DataNode tail()
   {
      return _tail;
   }
   //----------------- toString( ) -------------------------------
   /**
    * creates a string representation of the entire list. Only works for
    * very short lists; but it can be a useful debugging tool.
    */
   public String toString()
   {
      DataNode walk = _head;
      String str = "";
      while ( walk != null )
      {
         str += walk.data() + ", ";
         walk = walk.next();
      }
      return str;
   }
   //----------------- find( String ) -------------------------------
   /**
    * find and return the first node whose data key matches the search key
    *   returns null if not found
    */
   public DataNode find( String searchKey )
   {
      DataNode node = _head;
      while ( node != null && !searchKey.equals( node.data().key ))
         node = node.next();
      return node;
   }
   //----------------- add( Data ) -------------------------------
   /**
    * add a node to the end of the list with the data defined by the Data object
     */
   public void add( Data newOne )
   {
      addTail( newOne );
   }
   //----------------- addHead( Data ) -------------------------------
   /**
    * adds the data to a node that becomes the new list head.
    */
   public void addHead( Data newOne )
   {
      _head = new DataNode( newOne, _head );
      if ( _tail == null )
         _tail = _head;
   }
   //----------------- addTail( Data ) -------------------------------
   /**
    * adds the data to a node that becomes the new list tail.
    */
   public void addTail( Data newOne )
   {
      if ( _tail == null )
         addHead( newOne );
      else
      {
         DataNode temp = new DataNode( newOne, null );
         _tail.setNext( temp );
         _tail = temp;
      }
   }
   //------------------- size() -----------------------------------
   /**
    * computes the size of the list; since the nodes are available to
    *    other programs, it's not possible to guarantee that whatever
    *    has been added is still part of the list. 
    * Hence, every time size is called, we traverse the list and count.
    * Of course, since nodes are accessible, there is no guarantee that
    *    the links are correct! But, we'll try
    */
   public int size()
   {
      int count = 0;
      DataNode walk = _head;
      while ( walk != null )
      {
         count++;
         walk = walk.next();
      }
      return count;
   }
         
   //--------------------- main ----------------------------------
   public static void main( String[] args )
   {
      DataList l = new DataList();
      
      l.add( new Data( "A", 1 ) );
      l.add( new Data( "B", 2 ) );
      l.add( new Data( "C", 3 ));
      l.add( new Data( "D", 4 ));
      l.add( new Data( "E", 5 ));
      
      System.out.println( l );
      
      l.addTail( new Data( "F", 6 ) );
      l.addTail( new Data( "G", 7 ) );
      
      System.out.println( l );
      /***
      System.out.println( "Searching for F: " + l.find( "F" ));
      System.out.println( "Searching for G: " + l.find( "G" ));   
      
      System.out.println( "List size (7) = " + l.size() );
      
      // now remove some
      
      l.remove();  // removes A
      System.out.println( "List size (6) = " + l.size() );
      
      l.remove( "G" ); // remove one on the end
      System.out.println( "List size (5) = " + l.size() );
      
      System.out.println( l );
      l.remove( "E" ); // remove one in the middle
      System.out.println( "List size (4) = " + l.size() );
      System.out.println( l );
      
      l.remove( "Not There" );
      System.out.println( "List size (4) = " + l.size() );
      
      while ( l.size() > 0 )
         l.remove();
      System.out.println( "List size (0) = " + l.size() );
      
      //---------------------Timing ----------------------------------------
      int numAdd = 50000;    // number pushes
      
      System.out.println( "------------------ Timing test --------------" );
      System.out.println( numAdd + "    adds and removes" );
      System.out.println( "----------------------------------------------" );
                            
      
      System.out.println(  " Remove them using size()." );
      long start = System.currentTimeMillis();
      
      //------------- add lots of values onto stack ----------------
      for ( int i = 0; i < numAdd; i++ )
      {
          l.add( new Data( "A" + i, "rest" ) );
      }
      //------------- now remove them all ----------------
      while ( l.size() > 0 )
      {
         l.remove();
      }
      float time = ( System.currentTimeMillis() - start ) / 1000.0f;
      System.out.println( "Time : " + time );     

      System.out.println(  " Remove them using isEmpty (tests head)" );
      start = System.currentTimeMillis();
      
      //------------- add lots of values onto stack ----------------
      for ( int i = 0; i < numAdd; i++ )
      {
          l.add( new Data( "A" + i, "rest" ) );
      }
      //------------- now remove them all off ----------------
      while ( ! l.isEmpty() )
      {
         l.remove();
      }
      time = ( System.currentTimeMillis() - start ) / 1000.0f;
      System.out.println( "Time : " + time );   
      /*******************************/

   }
}
         