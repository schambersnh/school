/**
 * HashTable: Experiment with different hashing functions.
 * 
 * A generic HashTable implementation that implements Java's Collection
 * interface -- or at least part of it.
 * 
 * implemented methods of Collection:
 *   void add( T )          -- adds T object somewhere in the list
 * 
 *                             removed from the list and its data returned.
 *   boolean ifEmpty()      -- returns true if the list is empty.
 *   int     size()         -- returns the size of the list
 *   int     hashCode()     -- returns the hashCode for the HashTable
 *   boolean contains( Object ) -- returns true if the Object is in the table
 *   
 * 
 * unimplemented methods of Collection
 *   void  clear()
 *   boolean containsAll( Collection<?> c )
 *   boolean equals( Object o )
 *   Iterator<T>  iterator()
 *   boolean remove( Object )
 *   boolean removeAll( Collection<?> )
 *   boolean retainAll( Collection<?> )
 *   Object[] toArray()
 *   <T> T[] toArray( T[] a )
 *  
 * @author rdb
 * March 2009
 * 
 * Modified by Stephen Chambers for Lab 12 on 3/8/11
 *          
 * Oct 2010 rdb: Added String parameter to HashTable for clearer table output
 * Mar 2011 rdb: renamed and relabeled hash methods for clarity
 * 
 * ************************************************************************
 * Use the space below to report performance statistics:
 * ************************************************************************
 * 1. Execution times for HashSet and BinarySearch 
 *                    HashSet  BinarySearch
 *     traipse  time  0.028    0.032 
 *     estrange time  0.082    0.131 
 * 
 * 2. badHash statistics for various table sizes
 *                       53     211    1023
 *     max # entries  1309      747    378
 *     avg # entries  1024.0   257.0   53.0
 *     min # entries  753        3       0
 *     std dev        5.589   14.235   12.437
 * 
 *     traipse  time  0.149   0.176   0.156
 *     estrange time  1.16    0.794   0.354
 * 
 * 3. Java hashCode statistics for various table sizes
 *                       53     211    1023
 *     max # entries  1099      299    123
 *     avg # entries  1024.0   257.0   53.0
 *     min # entries  954       217    8
 *     std dev        1.064    0.975   3.02
 * 
 *     traipse  time  0.187  0.184   0.143
 *     estrange time  1.249  0.44    0.148
 * 
 * 4. myHash statistics for various table sizes
 *                       53     210    211     225   1023
 *     max # entries  1081      450    299     418    82
 *     avg # entries  1024.0   258.0   257.0   241.0  53.0
 *     min # entries  952        97    216     41     32
 *     std dev        1.023    5.817   1.032   5.429  0.991
 * 
 *     traipse  time  0.202   0.174   0.09  0.17  0.056
 *     estrange time  1.388   0.449   0.621 0.454 0.353
 * 
 * 5. Look at the statistics from myHash, especially the standard
 *    deviation values. Recall that standard deviation is a measure of how 
 *    close the numbers are to the average. Smaller standard deviation 
 *    means that values are generally closer to the average than with a larger 
 *    standard deviation. 1 is considered to be a small standard deviation
 *    and would characterize a "good" table; 5 is considered a very large 
 *    standard deviation and would characterize a "bad" table.
 * 
 *    a. What seems unusual about these numbers? 
 *    53 and 211, and 1023 have excellent standarad deviation values, but 210
 *    and 225 are considered bad tables. 210 and 211 are only one bucket off
 *    from each other, and yet one is an excellent table and one is a terrible
 *    table.
 * 
 * 
 * 
 * 
 *    b. What question(s) might you ask about them?
 *    I would ask how the amount of buckets change standard deviation values, 
 *    as well as if the amount of buckets depends on a special number, i.e
 *    the avg entries for the table sizes is 2^10.
 * 
 * 
 *  
 * 
 */

import java.util.*;

public class HashTable<T> implements Collection<T>
{
   //----------------- instance variables --------------------------
   private int            _tableSize;    // # buckets in hash table
   private char           _hashFunction;
   private String         _hashName;

   private ArrayList<T>[] _table; 
   
   private int            _numEntries;   // # entries added to hash table

   //---------------- constructor ----------------------------------
   /**
    * create an empty hash table
    */
   public HashTable( int tableSize, char hashFcn, String hashName ) 
   {
      _numEntries = 0;
      _tableSize = tableSize;
      _hashFunction = hashFcn;
      _hashName     = hashName;
      _table = (ArrayList<T>[])new ArrayList[_tableSize];
      
      for(int i = 0; i < _tableSize; i++)
      {
        _table[i] = new ArrayList<T>();
      }
      /////////////////////////////////////////////////////////////////////
      // Create an array of ArrayList objects; the array should be 
      // _tableSize length. 
      //
      // Because of esoteric Java semantic complications, you cannot specify
      //  a generic array in the "new", so you must create a non-generic
      //  ArrayList array and then cast the result to ArrayList<T>[]
      //  before assigning it to _table.
      //
      // This generates a warning. Putting try-catch around doesn't help.
      //-----------------

      
      
      //++++++++++++++++++++
      // Now initialize the _table entries to objects of type ArrayList<T>
      //     a for loop is the easiest.
      //--------------------
      
      
   }
   //----------------- tableIndex( String ) ------------------------
   /**
    * map a String based key to a table index;
    * first map the String to a hash value, then take the
    * modulus (remainder) of the hash value by the size of the
    * ArrayList we are using. 
    * 
    * Hence every object will be mapped to a particular entry in
    * the table; the same String will always map to the same index,
    * but if the hash function is good, all the data will be evenly
    * distributed among the table entries.
    */
   private int tableIndex( String s )
   {
      int hash;
      switch ( _hashFunction )
      {
         case 'x':  // our bad hash method
            hash = badHash( s );
            break;
         case 'm':  // your better hash method
            hash = myHash( s );
            break;
         case 'j':  // java's hash method
            hash = Math.abs( s.hashCode() );
            break;
         default:
            hash = badHash( s );
      }
                  
      return hash % _table.length;
   }
   //----------------- badHash( String ) ------------------------
   /**
    * This is not a very good hash function. It just sums the integer
    * values of all the characters in the string, so all words with the
    * same characters map to the same index.
    */
   private int badHash( String s )
   {
      int sum = 0;
      for ( int i = 0; i < s.length(); i++ )
         sum += s.charAt( i );
      return sum;
   }
   //----------------- myHash( String ) ------------------------
   /**
    * Using badHash as a model, write a better hashing function.
    * Instead of just using the sum of the character values
    * multiply each character's value by 10 raised to the power i
    * where i is the position of the character in the string.
    * Java has a math function to raise a number to a power:
    *    Math.pow( num, exponent )
    */
   private int myHash( String s )
   {
      int sum = 0;
      for ( int i = 0; i < s.length(); i++ )
         sum += s.charAt( i ) * Math.pow(10, i);
      return sum;

   }
   //----------------- add( T ) -------------------------------
   /**
    * add a T object to the hash table -- as long as it is not already there.
    */
   public boolean add( T newOne )
   {
      if ( contains( newOne ))
         return false;
      else
      {
        int index = tableIndex(newOne.toString());
        _table[index].add(newOne);
         /////////////////////////////////////////////////////////////////////
         // Convert "newOne" to a String (use the toString() method ).
         // Get its corresponding table index by calling the tableIndex method
         // Add "newOne" to the ArrayList at that index.
         /////////////////////////////////////////////////////////////////////

         
         _numEntries++;
         return true;
      }
   }
   //----------------- contains( Object ) -------------------------------
   /**
    * returns true if the object passed as an argument is contained
    * in the hash table. Note Collection ADT requires that the contains
    * method accept an Object parameter, rather than a T.
    */
   public boolean contains( Object search )
   {
     int index = tableIndex(search.toString());
   
      ////////////////////////////////////////////////////////////
      // Convert "search" to a String (use the toString() method ).
      // Get its corresponding table index (tableIndex method)
      // Invoke the "contains" method of the list at that index,
      //    using "search" as its argument.
      // Return the result of the contains invocation.
      /////////////////////////////////////////////////////////////
         
      
      
      return _table[index].contains(search);  // <---- replace this
   }
   //----------------- isEmpty( ) -------------------------------
   /**
    * returns true if the list is empty
    */
   public boolean isEmpty()
   {
      return _numEntries == 0;
   }
   //----------------- size( ) -------------------------------
   /**
    * returns the size of the list
    */
   public int size()
   {
      return _numEntries;
   }
 
   //-------------------- report() ----------------------------
   /**
    * printout out lengths of each of bucket lists
    */
   public void report()
   {
      if ( _table == null )
      {
         System.err.println( "HashTable.report: No table created yet!" );
         return;
      }
      
      float avg = _numEntries / _tableSize;
      int   max = 0, min = _numEntries + 1;
      float sdSum = 0;
      System.out.println( "---------- " + _hashName + " HashTable info ---------" );
      System.out.println( _numEntries + " words in " 
                            + _tableSize + " buckets." );
      for ( int i = 0; i < _tableSize; i++ )
      {
         int bucketSize = _table[ i ].size();
         if ( bucketSize < min )
            min = bucketSize;
         if ( bucketSize > max )
            max = bucketSize;
         sdSum += Math.pow( avg - bucketSize, 2 );
      }
      double stdDev = Math.sqrt( sdSum / _numEntries );
      stdDev = ((int)( stdDev * 1000 )) / 1000.0;
      System.out.println( "Max   Ave     Min   Std deviation" );
      System.out.println( max + "  " + avg + "  " + min + "   " + stdDev );
   }
            
   //+++++++++++++++++ unimplemented methods of Collection
  
   public boolean addAll( Collection< ? extends T > c ) { return false; }
   public void  clear(){};
   public boolean containsAll( Collection<?> c ){ return false; }
   public boolean equals( Object o ) { return false; }
   public Iterator<T>  iterator() { return null; }
   public boolean remove( Object o ) { return false; }
   public boolean removeAll( Collection<?> c ) { return false; }
   public boolean retainAll( Collection<?> c ) { return false; }
   public Object[] toArray() { return null; }
   public <T> T[] toArray( T[] a )  { return null; }

   //--------------------- main ----------------------------------
   /**
    * A very basic main testing program; it just adds a few entries to
    * a small hash table and then tries to find them and some that aren't
    * in the table.
    */
   public static void main( String[] args )
   {
      HashTable<String> ht = new HashTable<String>( 5, 'm', "m: My Hash" );
      
      ht.add( "A" );
      ht.add( "B" );
      ht.add( "C" );
      ht.add( "D" );
      ht.add( "E" );
      
      System.out.println( "List size (5) = " + ht.size() );
      
      System.out.println( "contains( A ) = true:  " + ht.contains( "A" ));
      System.out.println( "contains( C ) = true:  " + ht.contains( "C" ));
      System.out.println( "contains( d ) = false: " + ht.contains( "d" ));
      System.out.println( "contains( E ) = true:  " + ht.contains( "E" ));
      System.out.println( "contains( B ) = true:  " + ht.contains( "B" ));
      System.out.println( "contains( G ) = false: " + ht.contains( "G" ));
      
      System.out.println( "#entries in table (5) = " + ht.size() );  
      
      ht.report();
    }
}
         
