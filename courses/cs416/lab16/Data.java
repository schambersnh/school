/**
 * Data - the object to be stored in the data structure. It has
 *    String key       -- the key used in the Comparable interface
 *    int    value      -- a stub representing other data
 * 
 * toString shows only keys for this assignment mb 11/09
 */

public class Data 
{
   //------------ instance variables ----------------------------
   public   String  key;      // key must be unique
   public   String  name;     // name can be duplicated
   public   int     value;
   
   //------------------ constructor -----------------------------
   /**
    *  Construct a Data object from its components
    */
   public Data( String k, String n, int v )
   {
      key   = k;
      name  = n;
      value = v;      
   }
   //-------------------- getKey() -------------------------
   /**
    * get the key
    */
   public String getKey()
   {
      return key;
   }
   //-------------------- toString() ------------------------
   public String toString()
   {
      return  key;//  + name + ":" + value ;
   }
   //------------------- compareTo -----------------------------------
   /**
    * compare a Data object to another Data object based on the key field
    */
   int compareTo( Data other )
   {
      return key.compareTo( other.key );
   }
}