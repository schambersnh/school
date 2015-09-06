/**
 * Node - a node in a tree
 */
import java.util.*;
public class Node
{
   public Node parent = null;
   public ArrayList<Node> children;
   public int  data   = 0;
   public State state;
   
   //------------------ Node constructor ---------------------
   public Node( int i, State s )
   {
      this.data = i;
      this.state = new State(s);
      children = new ArrayList<Node>();

   }
   public void addChild( Node n )
   {
     children.add(n);
   }
   public void makeChildren()
   {
     ArrayList<Integer> plays = this.state.getPlays();
     for(int i = 0; i < plays.size(); i++)
     {
       this.addChild(new Node(plays.get(i), this.state));
       children.get(i).state.play(children.get(i));
       children.get(i).makeChildren();
     }
   }
}
