/**
 * Data - the object to be stored in the data structure. It has
 *    String key    -- not really a key, but string representing range of data 
 *    String values     -- a stub representing actual data
 */

public class Data 
{
   //------------ instance variables ----------------------------
   public   String  key;  
   public   int     count;   // # values in this node
   public   String  values;
   
   //------------------ constructor -----------------------------
   /**
    *  Construct a Data object from its components
    */
   public Data( String k, String v, int c )
   {
      key   = k;
      values = v; 
      count = c;
   }
   //-------------------- toString() ------------------------
   public String toString()
   {
      return  key  + ":" + count ;
   }
}