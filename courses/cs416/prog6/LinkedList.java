/**
 * LinkedList - a generic linked list class with a public external Node class
 *              Objects stored in this list must implement the Comparable
 *              interface.
 * 
 *              Since the Node's are accessible to any one, there is little
 *              protection to insure that lists don't get corrupted. 
 * 
 * This is a skeleton framework for this class, including a main
 * program that tests it for LinkedList<String>
 * 
 * 
 * @author
 * rdb 10/03/10
 */
public class LinkedList<T extends Comparable<T>> 
{
  //---------------------- instance variables --------------------------
  private  Node<T> head;
  private  Node<T> tail;
  
  public int size;
  
  //----------------------- constructor --------------------------------
  public LinkedList() 
  {
    ////////////////////////////////////////////////////
    // Add any needed code
    ////////////////////////////////////////////////////
    
    head = null;
    tail = null;
  }
  
  //-------------------------- head() ------------------------------------
  /**
   * return the head of the list
   */
  public Node<T> head() 
  {
    return head;
  }
  //-------------------------- tail() ------------------------------------
  /**
   * return the tail of the list
   */
  public Node<T> tail() 
  {
    return tail;
  }
  //---------------------- size() ------------------------------------
  /**
   * return the number of entries in the list
   */
  public int size() 
  {
    return size;
  }
  //--------------------- add( T ) ---------------------
  /**
   * add to tail; 
   */
  public Node<T> add( T t ) 
  {
    return addTail( t );     
  }
  
  //----------------------- addTail( T ) ---------------------------------
  /**
   * Create a node for the passed-in object and add the node to the 
   * end of the list. 
   * Return the added node; update the size.
   */
  public Node<T> addTail( T t ) 
  {
    Node<T> newNode = new Node<T>( t );
    ///////////////////////////////////////////////////////
    // Complete the method
    ///////////////////////////////////////////////////////
    if( tail!= null )
    {
      newNode.prev = tail;
      tail.next = newNode;
      tail = newNode;
      size++;
    }
    else
    {
      head = newNode;
      tail = newNode;
      size++;
    }
    return newNode;
    
  }
  
  //--------------------- addBefore( Node, T ) ---------------------
  /**
   * Given a Node on the list and a new object, create a node for the
   * new object and add it in front of the existing node.
   */
  public Node<T> addBefore( Node<T> n, T t ) 
  {
    Node<T> newNode = new Node<T>( t );
    ///////////////////////////////////////////////////////
    // Complete the method
    ///////////////////////////////////////////////////////
    
    if( n == null ) //if the node I'm adding before doesn't exist
    {
      return newNode;
    }
    else if( n.prev == null ) //It's the head!
    {
      head.prev = newNode;
      newNode.next = head;
      head = newNode;
      size++;
    }
    else //normal case
    {
      newNode.prev = n.prev;
      n.prev.next = newNode;
      n.prev = newNode;
      newNode.next = n;
      size++;
    }
    return newNode;
  }
  
  //--------------------- addInOrder( T ) ---------------------
  /**
   * Create a Node for the argument, and insert that Node into the list
   * such that the list is ordered according the compareTo method of T.
   * The order should be from low to high.
   * Return the Node being added
   */
  public Node<T> addInOrder( T t ) 
  {
    Node<T> newNode = new Node<T>( t );
    Node curNode = head;
    ///////////////////////////////////////////////////////
    // Complete the method
    ///////////////////////////////////////////////////////
    if(curNode != null)
    {
      //Loops through until curNode is out of order
      while( curNode!= null && curNode.data.compareTo( newNode.data ) < 0 )
      {
        curNode = curNode.next;
      }
     
      if( curNode != null )
      {
        return addBefore( curNode , t ); //switch up the order
      }
      else
      {
        return add( t ); //if its null simply add it to the list
      }
    }
    else
    {
      return add( t );
    }
    
  }
  //--------------------- addAfter( Node, T ) ---------------------
  /**
   * Given a Node on the list and a new object, create a node for the
   * new object and add it in after the existing node.
   */
  public Node<T> addAfter( Node<T> n, T t ) 
  {
    Node<T> curNode = new Node<T>( t );
    if( n == null ) //if the node I'm adding after doesn't exist 
    {
      return null;
    }
    else if( n.next == null ) //It's the tail!
    {
      tail.next = curNode;
      curNode.prev = tail;
      tail = curNode;
      size++;
    }
    else //normal case
    {
      curNode.next = n.next;
      n.next.prev = curNode;
      n.next = curNode;
      curNode.prev = n;
      size++;
    }
    ///////////////////////////////////////////////////////
    // Complete the method
    ///////////////////////////////////////////////////////
    
    return curNode;
  }
  
  //----------------------remove( T ) ----------------------------
  /**
   * Find the node on the list that contains an object that compares
   * as equal to the parameter passed in. 
   * If such a node exists, remove it from the list, update list size
   * and return its object
   */
  public T remove( T d ) 
  {
    Node<T> curNode = head;
    if( curNode!= null )
    {
      //Loop through until curNode is the node that equals node w/ data passed 
      while( curNode!= null && !curNode.data.equals( d ) )
      {
        curNode = curNode.next;
      }
      if( curNode!= null )
      {
        if( curNode.prev == null ) //It's the head!
        {
          head = curNode.next;
          if( size >= 2 ) //only do this if theres more then one element
          {
          head.prev = null;
          }
          curNode.next = null;
          size--;
          return curNode.data;
        }
        else if( curNode.next == null ) //It's the tail!
        {
          tail = curNode.prev;
          if( size >= 2 ) //only do this if theres more then one element
          {
          tail.next = null;
          }
          curNode.prev = null;
          size--;
          return curNode.data;
        }
        
        else //normal case
        {
        curNode.prev.next = curNode.next;
        curNode.next.prev = curNode.prev;
        size--;
        return curNode.data;
        }
      }
      else
      {
        return null;
      }
    }
    else
    {
      return null;
    }
  }
  //----------------------- findNode( T ) --------------------------
  /**
   * Find the node on the list that contains an object that compares
   * as equal to the parameter passed in. Return the node or null.
   */   
  public Node<T> findNode( T d ) 
  {
    Node<T> curNode = head;
    if( curNode!= null )
    {
      //Loop through until curNode is the node that equals node w/ data passed 
      while( curNode!= null && !curNode.data.equals( d ) )
      {
        curNode = curNode.next;
      }
     if( curNode!= null )
     {
       return curNode; //return it
     }
     else
     {
       return null;
     }
    }
    else
    {
      return null;
    }
  }
  
  //---------------------- get( int ) --------------------------------
  /**
   * get the n-th Node in the list. If n-th element doesn't exist, return null
   */
  public Node<T> get( int n ) 
  {
    if ( n >= size )
      return null;
    ///////////////////////////////////////////////////////
    // Complete the method
    ///////////////////////////////////////////////////////
    Node<T> newNode = head;
    //loop through until newNode is at the index passed in
    for(int i = 1; i <= n; i++)
    {
      newNode = newNode.next;
    }
    
    return newNode;
  }
  
  //---------------------- clear() ----------------------------------
  /**
   * remove all elements from the List.
   */
  public void clear()
  {
    head = tail = null;
    size = 0;
  }
  
  //------------------------ toString() ----------------------------  
  /**
   * return a String representation of the list; while we're at it it,
   * count the number of elements and verify that that is the same 
   * number that is reported to be the size of the list. Report an
   * error if there is something wrong with the list.
   */
  public String toString() 
  {
    StringBuffer sb = new StringBuffer();
    int checkSize = 0;
    if ( head == null ) 
      sb.append( "Empty List" );
    else
    {
      Node<T> cur = head.next;
      
      sb.append( head + "->" );
      checkSize++;
      
      while ( cur != null ) 
      {
        sb.append( cur + "->" );
        cur = cur.next;
        checkSize++;
      }
    } 
    
    if ( checkSize != size )
      System.err.println( "List error: #nodes != size: " 
                           + checkSize + " != " + size );
    
    return sb.toString();
  }
  //------------------------ checkList() ----------------------------  
  /**
   * Traverse the list to check the size value and the prev references:
   * At each step save the previous nodes reference then compare that
   * to the "prev" reference in the next node.
   */
  public void checkList() 
  {
    int index = 0;
    int checkSize = 0;
    Node<T> cur = head;
    Node<T> back = null;
    
    while ( cur != null ) 
    {
      if ( cur.prev != back )
        System.err.println( "List error: bad prev @ index: " + index );
      back = cur;
      cur = cur.next;
      checkSize++;
      index++;
    }
    if ( checkSize != size )
      System.err.println( "List error: #nodes != size: " 
                           + checkSize + " != " + size );
    
  }
  //+++++++++++++++++++++++++++ main Unit tester ++++++++++++++++++++++
  public static void main( String[] args )
  {
    LinkedList<String> list = new LinkedList<String>();
    Node<String> node;
    String[] names = { "a", "c", "e", "g", "i", "k", "m" };
    for ( int i = 0; i < names.length; i++ )
    {
      System.out.println( names[ i ] );
      list.addTail( names[ i ] );
    }
    System.out.println( "List: " + list );
    list.checkList();
    
    // test get
    node = list.get( 3 );
    System.out.println( "------ get( 3 ) ------------------" );
    System.out.println( "Size: 7 =? " + list.size() );
    System.out.println( "item 3 should be g: " + node );
    list.checkList();
    
    // test addBefore in middle
    System.out.println( "------ add f before g ------------------" );
    list.addBefore( node, "f" );
    System.out.println( "Size: 8 =? " + list.size() );
    System.out.println( "a c e f g i k m  : " + list );
    list.checkList();
    
    // test addAfter in middle
    System.out.println( "------ add h after g ------------------" );
    list.addAfter( node, "h" );
    System.out.println( "Size: 9 =? " + list.size() );
    System.out.println( "a c e f g h i k m  : " + list );
    list.checkList();
    
    // test addBefore head
    System.out.println( "------ add 1 before a ------------------" );
    node = list.head;
    list.addBefore( node, "1" );
    System.out.println( "Size: 10 =? " + list.size() );
    System.out.println( "1 a c e f g h i k m  : " + list );
    list.checkList();
    
    // test addAfter tail
    System.out.println( "------ add n after m ------------------" );
    node = list.tail;
    list.addAfter( node, "n" );
    System.out.println( "Size: 11 =? " + list.size() );
    System.out.println( "1 a c e f g h i k m n  : " + list );
    list.checkList();
    
    // test findNode
    System.out.println( "-------- search for 'k' ------- " );
    node = list.findNode( "k" );
    if ( node != null )
      System.out.println( "Found: " + node );
    
    System.out.println( "-------- search for 'z' ------- " );
    node = list.findNode( "z" );
    if ( node == null )
      System.out.println( "z isn't in list! correct!" );
    
    System.out.println( "--------- remove( f ) -----------" );
    String s =  list.remove( "f" );      
    System.out.println( "Size: 10  =? " + list.size() );
    System.out.println( "1 a c e g i k m n  :" + list );
    list.checkList();
    
    System.out.println( "------------ clear() --------------" );
    list.clear();
    System.out.println( "Size: 0  =? " + list.size() );
    System.out.println( " " + list );
    
    System.out.println( "------------ addInOrder():  q b z r c a" );
    list.addInOrder( "q" );
    list.addInOrder( "b" );
    list.addInOrder( "z" );
    list.addInOrder( "r" );
    list.addInOrder( "c" );
    list.addInOrder( "a" );
    System.out.println( "Size: 6  =? " + list.size() );
    System.out.println( "a b c q r z  : " + list );
    list.checkList();
    
    System.out.println( "------------ remove all one at a time ------" );
    while ( list.head() != null )
    {
      node = list.get( 0 );
      list.remove( node.data );
      System.out.println( "List: " + list );
      list.checkList();
    }
    
  }
}