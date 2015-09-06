/**
 * StringUtilities.java
 * Static string manipulation methods
 * 
 * Stephen Chambers
 * 11/9/10
 *
 */

import wheelsunh.users.*;
import java.util.*;

public class StringUtilities
{
  //----------------------------------------------------------------------------
  /**
   * This method returns the letter of the given string at the given index.
   */
  public static String letterAt( String text, int n )
  {    
    if( n < text.length() && n >= 0 )
    {
      return text.substring( n, n+1 );
    }      
    else
      return null;    
  }
  
  //----------------------------------------------------------------------------
  /**
   * this method inserts the letter into the string at the index of n.
   */
  public static String insertAt( String text, String letter, int n )
  {
    String sub1, sub2;
    if( n < text.length() && n >= 0 )
    {
      sub1 = text.substring( 0, n );
      sub2 = text.substring( n, text.length() );
      text = sub1 + letter + sub2;
      return text;
    }
    else
      return null;
  }
  
  //----------------------------------------------------------------------------
  /**
   * This replaces the letter at text's index n witht the given letter.
   */
  public static String replaceAt( String text, String letter, int n )
  {
    String sub1, sub2;
    if( n < text.length() && n >= 0 )
    {
      sub1 = text.substring( 0, n );
      sub2 = text.substring( n+1, text.length() );      
      text = sub1 + letter + sub2;
      return text;
    }
    else
      return null;    
  }
  
  //----------------------------------------------------------------------------
  /**
   * This method will scrable the given text into a random order.
   */
  public static String scramble( String text )
  {
    String scramble = "";
    int index; 
    Random random = new Random();
    boolean[] wasUsed = new boolean[ text.length() ];
    while( scramble.length() < text.length() )
    {
      index = random.nextInt( text.length() );
      if( !wasUsed[index] )
      {
        scramble = scramble + text.charAt( index );
        wasUsed[index] = true;
      }
    }
    return scramble;
  }
  
  //----------------------------------------------------------------------------
  /**
   * This method will translate the given text into a new text of the same
   * length, but encrypted witht the given key.
   */
  public static String translate( String text, String key )
  {
    String alphabet = new String( "ABCDEFGHIJKLMNOPQRSTUVWXYZ" );
    String myUpKey = new String( key.toUpperCase() );    
    String s1 = new String( text );
    String letter = new String ();
    String newLetter = new String( "" );
    int index;
    for( int i = 0; i < s1.length(); i++ )
    {
      
      letter = s1.substring( i, i+1 );      
      index = alphabet.indexOf( letter );
      if( index != -1 )
      {
        newLetter = myUpKey.substring( index, index + 1 );
        s1 = StringUtilities.replaceAt( s1, newLetter, i );
      }
    }
    return s1;
  }
  
  //----------------------------------------------------------------------------
  /**
   * Testing main
   */
  public static void main(String arg[])
  {    
    String letter = "Madden rocks";
    String insert = "Patriots all the way";
    String replace = "Solitaire's the only game in town.";
    String scramble = "abcdefghijklmnopqrstuvwxyz";
    String translate = "ILL BE BACK";
    String key = "QWERTYUIOPLKJHGFDSAZXCVBNM";
    
    //scrambles the alphabet
    System.out.println( StringUtilities.scramble( scramble ) );
    
     //inserts go at the 3rd index
    System.out.println( StringUtilities.insertAt( insert, " go", 3 ) );
    
    //gets the letter at the 3rd index of letter
    System.out.println( StringUtilities.letterAt( letter, 3 ) );
    
   
    
    //replaces the i with an h
    System.out.println( StringUtilities.replaceAt( replace, "h", 3 ) );
    

    
    //translates the string 
    System.out.println( "Alphabet: " + "ABCDEFGHIJKLMNOPQRSTUVWXYZ" );
    System.out.println( "Key: " + key );
    System.out.println( "Before: " + translate );
    System.out.println( "After: " + StringUtilities.translate( translate, key ) );
    
    //All return null
    System.out.println( StringUtilities.letterAt( letter, 83 ) );
    System.out.println( StringUtilities.insertAt( insert, "z", 55 ) );
    System.out.println( StringUtilities.replaceAt( replace, "z", 90 ) );
    
  }
  
}