
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * This class performs the depth-first search and breadth-first search algorithms
 * @author Stephen
 *
 */
public class Planner {
	private Node _root;
	private ProblemInfo info;
	int nodesGenerated;
	int nodesExpanded;
		
	public Planner(State init) {
		info = ProblemInfo.getInstance();
		_root = new Node(0, init.calcHScore(), info.getWeight(), init);
	}

	/**
	 * Finds the best path to a goal state
	 * @param searchAlg
	 * The search algorithm to use (depth-first, uniform-cost)
	 * @return
	 */
	public Node findPath(){
		PriorityQueue<Node> openList = new PriorityQueue<Node>(new GHComparator());
		openList.add(_root);
		return findBestPathAStar( openList );
	}
	
	
	/**
	 * Find the best path through the A-Star algorithm. The heuristic has already been
	 * passed at this point; each Node knows how to calculate its h value.
	 * @param openList
	 * An open list of the path traversed so far represented by a priority queue.
	 * @return
	 * The Node containing the optimal solution, if found. Returns null if no solution found.*/
	
	private Node findBestPathAStar(PriorityQueue<Node> openList){
		nodesGenerated = 1;
		nodesExpanded = 0;
		HashSet<Node> closedList = new HashSet<Node>();
		while(!openList.isEmpty()){
			Node n = openList.poll();
			if(n.state.isGoalState()){
				return n;
			}
			if(closedList.contains(n)){
				continue;
			}
			//Add n to the closed list (hash table)
			closedList.add(n);
			nodesExpanded++;
			ArrayList<Node> children = n.getChildren();
			nodesGenerated += children.size();
			for(Node kid: children){
				openList.add(kid);
			}
		}
		//Didn't find a solution :(
		return null;
	}	
	
	/**
	 * Get the number of nodes generated.
	 * @return
	 * The number of nodes generated for the last search fun.
	 */
	public int getNodesGenerated(){
		return nodesGenerated;
	}
	
	/**
	 * Get the number of nodes expanded.
	 * @return
	 * The number of nodes expanded for the last search fun.
	 */
	public int getNodesExpanded(){
		return nodesExpanded;
	}	
}
