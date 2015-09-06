/**
 * Recursion.java - a collection of recursive static methods that 
 *                 need to be completed.
 * 
 * Last modified: 09/15/10 rdb
 */
import java.util.*;
import java.io.*;

public class Recursion
{  
  //------------------- maxValue -------------------------
  //////////////////////////////////////////////////////////////
  // find the max value in array list[0,...,n-1]
  //  n is number of elements in the array
  ////////////////////////////////////////////////////////////////
  // Algorithm:
  //   if there is 1 entry in the array, it must be the max
  //      return it (base case)
  //   else
  //      return the max of:
  //         -the last entry in the array, and
  //         -the max of the first n-1 entries in the array (recursion)
  //
  // Note: this algorithm fails if the array has 0 length! That's ok.
  //////////////////////////////////////////////////////////////
  
  public static int maxValue( int[] list, int n)
  {
    int maxVal = -1;

    // base case
    if ( n == 1 )
      maxVal = list[ 0 ];
    else // recursion case
    {    
        maxVal = Math.max(list[n-1], maxValue(list, n-1));
        return maxVal;
      }
      // find maximum of first n-1 elements ( 0 ... n-2 )
      // return maximum of that number and the nth
    
    return maxVal;
  }
  
  //--------------------- findLast( words, n, key ) -----------------------------
  //-------------------------------------------------------
  //  find the last "key" in array words[ 0,...,n-1 ]
  //   n is number of elements in the array
  //   return the index of a found element ( or -1 if not found)
  ////////////////////////////////////////////////////////////////////
  // Algorithm:
  //    if n == 0 (remaining array has zero length) 
  //       return -1 (not found)
  //    else if last element in array == key
  //       return n-1 (index of last element)
  //    else
  //       return result of calling search with first n-1 elements of list
  //////////////////////////////////////////////////////////////////
  
  public static int findLast( String[] words, int n, String key)
  {
    // base case 1: array has 0 length, key isn't there, return -1
    if ( n <= 0 )
      return -1;
    // base case 2: last element of array matches key
    else if ( words[ n - 1 ].equals( key ))
      return n - 1;
    else // recursion case
    {
      
      ///////////////////////////////////////////////////////////
      // invoke find on the first n-1 elements of the array
      //    return whatever that returns.
      ///////////////////////////////////////////////////////////      
     return findLast(words, n-1, key);
    }
   
  }
  
  //---------------------  palindrome( String ) ---------------------------
  ////////////////////////////////////////////////////////////////
  //  returns true if this String is a palindrome
  //     A palindromic string is a mirrored about its center, such as
  //          abba, otto, civic
  ////////////////////////////////////////////////////////////////
  // Algorithm:
  //    if string length <=1 
  //       return true
  //    compare the first and last characters in the string
  //    if they are not equal, 
  //       return false
  //    else
  //       this string is a palindrome if and only if 
  //       the substring between the first and last is
  //       a palindrome, so return the result of recursively
  //       calling the method with that substring
  //////////////////////////////////////////////////////////////     
  public static boolean palindrome( String input)
  {
    // base cases:  empty string (length 0) is a palindrome
    //              string of length 1 is a palindrome
    if ( input.length() <= 1 ) 
    {
      return true;
    }
    else if (input.charAt(0) != input.charAt(input.length()-1))         
    {
      return false;
    }
    
    else
    {
       return palindrome(input.substring(1, input.length() - 1));
    }
  
}

//-------------------- countLetter( String, char ) -------------------------
//////////////////////////////////////////////////////////////////
// returns the number of occurrences of the letter "letter"
// in the string  
//////////////////////////////////////////////////////////////////
// Algorithm:
//    if string length is 0 
//       return 0
//    count the occurrences of the letter in the substring that
//        excludes the first character
//    if  first character == the letter we are counting add 1 to the count
//    return the count
/////////////////////////////////////////////////////////////////
public static int countLetter( String input, char letter)
{
  // base case, the string is the empty string, so it has 0
  //   occurrences of the letter
  int count = 0;
  int tryCount = 0;
  if ( input.length() == 0 )
    return 0;
  else // recursion case
  {

    if (input.charAt(0) == letter)
    {
      
      System.out.println("String were finding: " + input.substring(tryCount, input.length()));
      return 1+ countLetter(input.substring(tryCount+ 1, input.length()), letter); 
    }
    else
    {
      return countLetter(input.substring(tryCount+ 1, input.length()), letter); 
    }
   
  } 
 
} 

//-------------- isSubstrng( String s, String t ) ------------------------  
// returns true if t is a substring of s
////////////////////////////////////////////////////////////////////
// Algorithm:
//    if length of t > length of s
//       return false (a larger string can't be a substring of a smaller)
//    else if s startsWith t
//       return true
//    else
//       recurse using the substring of s that excludes the first char
////////////////////////////////////////////////////////////////////

public static boolean isSubstring( String s, String t)
{ 
  // base case: the length of t > length of s
  //      return false; t cannot match a string shorter than itself
  int count = 0;
  if ( t.length() > s.length() )
    return false;
  else if( s.startsWith(t))
  {  
    return true;
  }
  else
  {
    return isSubstring(s.substring(count+1, s.length()), t);
  }
  
} 

//+++++++++++++++++++++++++ convenience main +++++++++++++++++++++++++
//----------------------- main ----------------------------------------
public static void main( String[] args )
{
  RecursionApp.main( args );
}
}