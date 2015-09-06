/**
 * StringUtilities: Some useful static methods for dealing with Strings.
 */

import java.util.*;

public class StringUtilities
{
   //-------------------- indexAll( String, String ) ----------------------
   /**
    * indexAll - find all occurrences of the 2nd argument in the first.
    *            overlapping matches count separately; i.e., there are
    *            2 matches of "AA" in "AAA".
    */
   public static Vector<Integer> indexAll( String target, String pattern ) 
   {
      Vector<Integer> locations = new Vector<Integer>();
        
      for(int i = 0; i < target.length(); i++)
      {
        if(target.substring(0, pattern.length()).equals(pattern))
        {
          
        }
      }
      
      return locations;
   }
}