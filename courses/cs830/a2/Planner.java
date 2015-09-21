
import java.awt.Point;
import java.util.ArrayList;
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
	int nodesGenerated;
	int nodesExpanded;
	
	/**
	 * Planner constructor
	 * @param blockedCells
	 * A list of the blocked cells locations
	 * @param dirtyCells
	 * A list of the dirty cell locations
	 * @param curLocation
	 * The current location of the robot
	 */
	public Planner(ArrayList<Point> blockedCells, ArrayList<Point> dirtyCells,
			Point curLocation, Point bounds, boolean useBattery, 
				ArrayList<Point> chargingStations, String heuristic) {
		State init = new State("", blockedCells, dirtyCells, curLocation, bounds,
					useBattery, chargingStations, heuristic);
		if(useBattery){
			int initialCharge = initBatteryPower(bounds);
			init.setBatteryPower(initialCharge);
		}
		_root = new Node(0, init.calcHScore(), init);
	}
	
	/**
	 * Finds the best path to a goal state
	 * @param searchAlg
	 * The search algorithm to use (depth-first, uniform-cost)
	 * @return
	 */
	public Node findPath(String searchAlg){
		if(searchAlg.equals("depth-first")){
			Stack<Node> openList = new Stack<Node>();
			openList.push(_root);
			return findPathDFS( openList );
		}
		else if(searchAlg.equals("uniform-cost")){
			PriorityQueue<Node> openList = new PriorityQueue<Node>(new GComparator());
			openList.add(_root);
			return findBestPathUC( openList );
		}
		else if (searchAlg.equals("depth-first-id")){
			Stack<Node> openList = new Stack<Node>();
			openList.add(_root);
			return findBestPathDFSID( openList );
		}
		else if(searchAlg.equals("a-star")){
			PriorityQueue<Node> openList = new PriorityQueue<Node>(new GHComparator());
			openList.add(_root);
			return findBestPathAStar( openList );
		}
		else if(searchAlg.equals("greedy")){
			PriorityQueue<Node> openList = new PriorityQueue<Node>(new HComparator());
			openList.add(_root);
			return findBestPathGreedy( openList );
		}
		else if(searchAlg.equals("ida-star")){
			Stack<Node> openList = new Stack<Node>();
			openList.add(_root);
			return findBestPathIDAStar( openList );
		}
		else{
			System.err.println("Cannot find best path with that search algorithm.");
			return null;
		}
	}
	
	
		
	/**
	 * Find the best path using a depth first search
	 * @param stack
	 * An open list of the path traversed so far represented by a stack. 
	 * @return
	 * The Node containing a possible solution, if found. Returns null if no solution found.*/
	 
	private Node findPathDFS( Stack<Node> openList ){
		nodesGenerated = 1;
		nodesExpanded = 0;
		while(!openList.isEmpty()){
			Node n = openList.pop();
			if(n.state.isGoalState()){
				return n;
			}
			if(isDuplicate(n)){
				continue;
			}
			nodesExpanded++;
			ArrayList<Node> children = n.getChildren();
			nodesGenerated += children.size();
			for(Node kid: children){
				kid.parent = n;
				openList.push(kid);
			}
		}
		//Didn't find a solution :(
		return null;
	}

	/**
	 * Find the best path through the uniform-cost algorithm
	 * @param openList
	 * An open list of the path traversed so far represented by a priority queue.
	 * @return
	 * The Node containing the optimal solution, if found. Returns null if no solution found.*/
	
	private Node findBestPathUC(PriorityQueue<Node> openList){
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
	 * Find the best path using a iterative deepening depth first search
	 * @param stack
	 * An open list of the path traversed so far represented by a stack. 
	 * @return
	 * The Node containing a possible solution, if found. Returns null if no solution found.*/
	 
	private Node findBestPathDFSID( Stack<Node> openList ){
		Node retval = null;
		int gBound = _root.gscore;
		int nextBound = Integer.MAX_VALUE;
		nodesGenerated = 1;
		nodesExpanded = 0;
		while(retval == null){
			if(openList.isEmpty()){
				/* Did not find anything at current depth, increasing bound */
				gBound = nextBound;
				nextBound = Integer.MAX_VALUE;
				openList.push(_root);
			}
			Node n = openList.pop();
			if(n.state.isGoalState()){
				retval = n;
				break;
			}
			if(isDuplicate(n)){
				continue;
			}
			nodesExpanded++;
			ArrayList<Node> children = n.getChildren();
			nodesGenerated += children.size();
			for(Node kid: children){
				if(n.gscore > gBound){
					/* This node will be pruned, it is past our bound */
					if(n.gscore < nextBound){
						nextBound = n.gscore;
					}
				} 
				else{
					kid.parent = n;
					openList.push(kid);
				}
			}
		}
		return retval;
	}
	
	/**
	 * Find the best path using a iterative deepening a star depth first search
	 * @param stack
	 * An open list of the path traversed so far represented by a stack. 
	 * @return
	 * The Node containing a possible solution, if found. Returns null if no solution found.*/
	 
	private Node findBestPathIDAStar( Stack<Node> openList ){
		Node retval = null;
		int fBound = _root.gscore + _root.hscore;
		int nextBound = Integer.MAX_VALUE;
		nodesGenerated = 1;
		nodesExpanded = 0;
		while(retval == null){
			if(openList.isEmpty()){
				/* Did not find anything at current depth, increasing bound */
				fBound = nextBound;
				nextBound = Integer.MAX_VALUE;
				openList.push(_root);
			}
			Node n = openList.pop();
			if(n.state.isGoalState()){
				retval = n;
				break;
			}
			if(isDuplicate(n)){
				continue;
			}
			nodesExpanded++;
			ArrayList<Node> children = n.getChildren();
			nodesGenerated += children.size();
			for(Node kid: children){
				int fScore = n.gscore + n.hscore;
				if(fScore > fBound){
					/* This node will be pruned, it is past our bound */
					if(fScore < nextBound){
						nextBound = fScore;
					}
				} 
				else{
					kid.parent = n;
					openList.push(kid);
				}
			}
		}
		return retval;
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
	 * Find the best path through the Greedy algorithm. The heuristic has already been
	 * passed at this point; each Node knows how to calculate its h value.
	 * @param openList
	 * An open list of the path traversed so far represented by a priority queue.
	 * @return
	 * The Node containing the optimal solution, if found. Returns null if no solution found.*/
	
	private Node findBestPathGreedy(PriorityQueue<Node> openList){
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
	
	/**
	 * Determine if a node is a duplicate or not
	 * @param n
	 * The node that may be a duplicate
	 * @return
	 */
	private boolean isDuplicate(Node n) {
		Node cur = n;
		while(cur.parent != null){
			if(n.equals(cur.parent)){
				return true;
			}
			cur = cur.parent;
		}
		return false;
	}
	
	/**
	 * Compute the initial battery charge of the robot
	 * @param bounds
	 * The bounds of the world, needed for the calculation
	 */
	private int initBatteryPower(Point bounds){
		return ((bounds.x + bounds.y) * 2) + 1;
	}

}
