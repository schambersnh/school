/**
 * Recursion2.java starter
 * Stephen Chambers
 */
import java.util.*;

public class Recursion2
{
   //------------------------- sum( int[], int ) --------------------
   /**
    * return the sum of the first n elements of the array.
    * 
    * Hint: the sum of the first n elements of the array is the 
    *       sum of the last element of the array plus the sum of the first
    *       n - 1 elements of the array.
    * 
    * Recursive solution means: NO LOOPS, NO INSTANCE VARIABLES.
    */
   public static int sum( int[] vals, int n )
   {
      
      if(n == 0)
      {
        return 0;
        
      }
      else
      {
        return vals[n-1] + sum(vals, n-1);
      }
      
    
   }
   
   //----------------------------- pow( float, int ) -----------------------
   /**
    * returns num raised to the power exp
    * 
    * Recursive solution means: NO LOOPS, NO INSTANCE VARIABLES.
    */
   public static float pow( float num, int exp )
   {
     if(exp == 0)
     {
       return 1;
     }
     else
     {
      return num * pow(num, exp -1);
     }
    
   }

   //------------------------- ordered( int[], int ) --------------------
   /**
    * check if the first n elements of the integer array are ordered
    *   from small to large.
    * 
    * Hint: The first n are ordered IF
    *                  the n-th is larger than or equal to the (n-1)st.
    *              AND the first n-1 are ordered.
    * 
    * Hint: There are 2 base cases: 
    *            a "success" case - we've tested all the elements
    *            a "failure" case when we've found a failure of order
    * 
    * Recursive solution means: NO LOOPS, NO INSTANCE VARIABLES.
    */
   public static boolean ordered( int[] vals, int n )
   {
     
      if(n <= 1)
      {
        return true;
      }
      else if(vals[n-1] < vals[n-2])
      {
        return false;
      }
      
      else
      {
        return ordered(vals, n-1);
      }
      
   }
   
   //------------------------- pairTest( String ) ----------------------
   /**
    * check if a String is composed of sequential pairs of identical letters.
    *     Examples:   aa,  aaBBccAAaa
    * 
    * A 1 character string should fail the pairTest; 
    * a 0-length string should pass it
    * 
    * Hint: A string passes the pairTest if the first 2 characters match 
    *            AND the rest of the string passes the pairTest
    * 
    * Hint: There are 3 base cases:
    *            a "success" case - we've tested all the elements
    *            a "failure" case when we've found a failure of a pair match
    *            a "failure" case when there is an "extra" character that cannot
    *              be paired with another.
    * 
    * Recursive solution means: NO LOOPS, NO INSTANCE VARIABLES.
    */
   public static boolean pairTest( String s )
   {
      if(s.length() == 0 )
      {
        return true;
      }
      else if(s.length() == 1)
      {
        return false;
      }
      else if(s.charAt(0) != s.charAt(1))
      {
        return false;
      }
       else
        {
         return pairTest(s.substring(0, s.length() - 2));
        }
      
   }
   
   //-------------------- exchangePairs( String ) --------------------------
   /**
    *  exchanges pairs of letters
    *  eg. input:  abcdefg
    *     output:  badcfeg
    * 
    * What is the base case(s?)?
    * What is the recursion step?
    */
   public static String exchangePairs( String s )
   {    
     if(s.length() <= 1)
     {
       return s;
     }
             
     else
     {
       return s.substring(1, 2) + s.substring(0, 1) + exchangePairs(s.substring(2));
     }
   
   }
//   //++++++++++++++++++++ convenience main test ++++++++++++++++++++++++++++
   /**
    * Invoke the main method of Recursion2App directly
    */
   public static void main( String[] args )
   {
      Recursion2App.main( args );
   }
}