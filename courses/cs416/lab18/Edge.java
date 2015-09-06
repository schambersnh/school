/**
 * Edge -- Represent a edge in a graph
 *
 * @author rdb 
 * 10/31/10
 * 
 * derived from code written by Jonathan Brown for GraphLab Fall 2010
 */
public class Edge
{
   //----------------------- Instance Variables -------------------------------
   protected Node    start, end;
   
   //---------------------------- constructor -----------------------
   /**
    * Creates a new edge object from 2 nodes
    */
   public Edge( Node s, Node e )     
   {
      start = s;
      end = e;
   }
   //-------------------------- getEnd() -----------------------------
   /**
    * Returns the node this edge ends at
    */
   public Node getEnd()
   {
      return end;
   }
   
   //-------------------------- getStart() ----------------------
   /**
    * Returns the node this edge starts at
    */
   public Node getStart()
   {
      return start;
   }         
}
