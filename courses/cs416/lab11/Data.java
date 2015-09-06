/**
 * Data - the object to be stored in the data structure. It has
 *    String key        -- the key used in the StringComparable interface
 *    int    valie      -- an int
 */

public class Data 
{
   //------------ instance variables ----------------------------
   public   String key;
   public   int    value;
   //public   GNode  gNode;
   
   //------------------ constructor -----------------------------
   /**
    *  Construct a Data object from its components
    *
   public Data( String k, String r, GNode gn )
   {
      key = k;
      rest = r;
      gNode = gn;
      
   }
   /**
    *  Construct a Data object without a GNode
    */
   public Data( String k, int v )
   {
      key = k;
      value = v;
   }
   //--------------------- toString() ------------------------------------
   /**
    * toString created as a tuple; 
    */
   public String toString()
   {
      return key + ":" + value;
   }
}