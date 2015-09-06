   //++++++++++++++++++ DataNode class ++++++++++++++++++++++++++++++++
   /**
    * The DataNode class is public
    * 
    * DataNode stores a Data objects
    * 
    */
   public class DataNode 
   {
      private DataNode _next = null;
      private Data _data = null;
      
      public DataNode( Data newData, DataNode n )
      {
         _data = newData;  
         _next = n;
      }
      //---------------- next() ------------------
      public DataNode next()
      {
         return _next;
      }
      //---------------- data() ------------------
      public Data data()
      {
         return _data;
      }
      //---------------- setNext( DataNode ) ------------------
      public void setNext( DataNode n )
      {
         _next = n;
      }
   }