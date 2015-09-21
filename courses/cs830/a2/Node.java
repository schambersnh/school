/**
 * Node - a node in a tree
 */
import java.awt.Point;
import java.util.*;
public class Node
{
   public int  gscore = 0;
   public int hscore = 0;
   public State state;
   public Node parent;
   
   /**
    * Node constructor
    * @param g
    * The gscore this node should have
    * @param s
    * The world state this node should have
    */
   public Node( int g, int h, State s )
   {
      this.gscore = g;
      this.state = s;
      this.hscore = h;
      this.parent = null;
   }
   
   @Override
   public int hashCode() {
       int hash = 7;
       int xVals = 0;
       int yVals = 0;
       for(Point p: this.state.getDirtyCells()){
           xVals = xVals + p.x;
           yVals = yVals + p.y;
       }
       hash = 71 * hash + xVals;
       hash = 13 * hash + yVals;
       hash = 13 * hash + this.state.getLocation().x;
       hash = 71 * hash + this.state.getLocation().y;
       return hash;
   }

   
   @Override
   public boolean equals(Object obj) {
       if (obj == null) {
           return false;
       }
       if (getClass() != obj.getClass()) {
           return false;
       }
       final Node other = (Node) obj;
       
       //If they are not in the same location, they are not equal.
       if(!this.state.getLocation().equals(other.state.getLocation())){
    	   return false;
       }
       //If the amount of dirty cells remaining is not equal, then they are not equal.
       if(this.state.getDirtyCells().size() != other.state.getDirtyCells().size()){
    	   return false;
       }
       //Makes sure each point in "other" is contained in "this"
       int oSize = other.state.getDirtyCells().size();
       for(int i = 0; i < oSize; i++){
    	   if(!this.state.getDirtyCells().contains(other.state.getDirtyCells().get(i))){
    		   return false;
    	   }
       }
       //Makes sure each point in "this" is contained in "other"
       int tSize = this.state.getDirtyCells().size();
       for(int j = 0; j < tSize; j++){
    	   if(!other.state.getDirtyCells().contains(this.state.getDirtyCells().get(j))){
    		   return false;
    	   }
       }
       //If I left, charged, and came back to a spot I was previously, these states are not duplicates
       if(this.state.getNumCharges() != other.state.getNumCharges()){
    	   return false;
       }

       return true;
   }
      
    /**
    * Find all neighbors of this node
    */
   public ArrayList<Node> getChildren()
   {
	   ArrayList<Node> neighbors = new ArrayList<Node>();
	   ArrayList<State> states = state.getNeighbors();
	   for(State s: states){
		   Node n = new Node(gscore + 1, s.calcHScore(), s);
		   neighbors.add(n);
	   }
	   return neighbors;
   }
}
