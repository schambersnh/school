/**
 * Node - a node in a tree
 */
import java.util.*;
public class Node
{
   public int  gscore = 0;
   public double hscore = 0;
   public State state;
   public Node parent;
   private double weight;
   
   /**
    * Node constructor
    * @param g
    * The gscore this node should have
    * @param s
    * The world state this node should have
    */
   public Node( int g, int h, double weight, State s )
   {
      this.gscore = g;
      this.state = s;
      this.hscore = h * weight;
      this.parent = null;
      this.weight = weight;
   }
   
   @Override
   public int hashCode() {
       return state.hashCode();
   }

   
   @Override
   public boolean equals(Object obj) {
       final Node other = (Node) obj;
       
       return state.equals(other.state);
   }
      
    /**
    * Find all neighbors of this node
    */
   public ArrayList<Node> getChildren()
   {
	   ArrayList<Node> neighbors = new ArrayList<Node>();
	   ArrayList<State> states = state.getChildren();
	   for(State s: states){
		   Node n = new Node(gscore + 1, s.calcHScore(), weight, s);
		   neighbors.add(n);
	   }
	   return neighbors;
   }
}
