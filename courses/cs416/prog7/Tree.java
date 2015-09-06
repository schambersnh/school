public class Tree 
{
   //-------------------- instance variables ---------------------
   private Node   _root;
   private int    _size;
   
   //-------------------- constructors --------------------------
   /**
    * Construct an empty tree with no nodes
    */
   public Tree()
   {
      _root = null;
   }
   /**
    * Construct a tree with a root 
    */
   public Tree( int rootData )
   {
      _root = new Node( rootData, new State(null, null, null));
   }
   public int getNumNodes()
   {
     return getNumNodes( _root );
   }
    public int getNumNodes(Node n)
   {
      int count = 0;
     if(n != null)
     {
       count++;
       for(int i = 0; i < n.children.size(); i++)
       {
         count += getNumNodes(n.children.get(i));
       }      
     }
     return count;
   }
}