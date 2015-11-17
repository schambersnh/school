import java.util.ArrayList;

/**
 * This class performs the dfs, fc, and mcv algorithms.
 * @author Stephen
 *
 */
public class Planner {	
	private Graph graph;
	private String searchAlg;
	private int numColors;
	private int nodesExplored = 0;
	
	/**
	 * Planner constructor
	 * @param blockedCells
	 * A list of the blocked cells locations
	 * @param dirtyCells
	 * A list of the dirty cell locations
	 * @param curLocation
	 * The current location of the robot
	 */
	public Planner(Graph g, String searchAlg, int numColors) {
		this.graph = g;
		this.searchAlg = searchAlg;
		this.numColors = numColors;
	}
	
	/**
	 * Finds the best path to a goal state
	 * @param searchAlg
	 * The search algorithm to use (depth-first, uniform-cost)
	 * @return a boolean value, true if graph was successfully colored in, false if otherwise
	 */
	public boolean colorGraph(){
		if(searchAlg.equals("dfs")){
			return colorGraphDFS(1);
		}
		else if(searchAlg.equals("fc")){
			return colorGraphFC(1);
		}
		else if(searchAlg.equals("mcv")){
			return colorGraphMCV(1);
		}

		else{
			System.err.println("Cannot find best path with that search algorithm.");
			return false;
		}
	}
		
		
	/**
	 * Find the best path using a depth first search
	 * @return
	 * The final graph colored in, if found. Returns null if no solution found.*/
	 
	private boolean colorGraphDFS(int k){
		do{
			int color = setNodeColorDFS(k);
			if(color == 0){
				return false;
			}
			graph.getVertex(k).setColor(color);
			if(k == graph.getNumVertices()){
				return true;
			}
			else{
				if(colorGraphDFS(k+1)){
					return true;
				} 
			}
		} while(true);
	}

	/**
	 * Set the node color according to the DFS alogorithm
	 * @param k the current node being colored in
	 * @return the color this node was set to
	 */
	private int setNodeColorDFS(int k) {
		do{
			Vertex cur = graph.getVertex(k);
			int color = (cur.getColor() + 1) % (numColors + 1);
			cur.setColor(color);
			nodesExplored++;
			/* Tried all the colors, and none of them worked! */
			if(color == 0){
				return 0;
			}
			if(validColor(color, cur))
				return color;
		} while(true);
	}
	
	/**
	 * Find the best path using forward checking
	 * @return
	 * The final graph colored in, if found. Returns null if no solution found.*/
	 
	private boolean colorGraphFC(int k){
		Vertex cur = graph.getVertex(k);
		
		/* Find the colors this vertex can use */
		ArrayList<Integer> possibleColors = cur.getPossibleColors();
		/* Try to set colors */
		int possibleSize = possibleColors.size();
		if(possibleSize > 1){
			nodesExplored += possibleSize;
		}
		for(int i = 0; i < possibleSize; i++){
			int color = setNodeColorFC(k, possibleColors.get(i));
			if(color == 0){
				return false;
			}
			cur.setColor(color);
			if(k == graph.getNumVertices()){
				return true;
			}
			else{
				 if(colorGraphFC(k+1)){
					 return true;
				 }
				 else{
					 /* color failed, reset */
					 for(Vertex v: graph.edgesOf(graph.getVertex(k))){
						 int val = v.getPrunedColors().get(color - 1);
						 v.getPrunedColors().set(color - 1, val - 1);
					 }
					 graph.getVertex(k).setColor(0);
				 }
			}
		}
		return false;	
	}

	/**
	 * Set the node color according to the FC algorithm
	 * @param k the node to be colored in
	 * @return the color this node was set to
	 */
	private int setNodeColorFC(int k, int color) {
		Vertex cur = graph.getVertex(k);
		if(validColor(color, cur)){
			if(checkConsistency(color, cur)){
				return color;
			}
		}
		/* Could not set the color */
		return 0;
	}
	
	/**
	 * Returns true if no adjacent vertexes share 'color'
	 * @param color the color being compared against
	 * @param cur the initial vertex
	 * @return
	 */
	private boolean validColor(int color, Vertex cur){
		for(Vertex v: graph.edgesOf(cur)){
			if(color == v.getColor()){
				return false;
			}
		}
		return true;
	}
	/**
	 * Returns true if adjusting the domain of 'cur's neighbors does
	 * not result in a domain wipeout.
	 * @param color
	 * @param cur
	 * @return
	 */
	private boolean checkConsistency(int color, Vertex cur){
		for(Vertex v: graph.edgesOf(cur)){
			int val = v.getPrunedColors().get(color-1);
			v.getPrunedColors().set(color-1, val + 1);
			if(v.domainWipeout()){
				System.out.println("ERROR: DOMAIN WIPEOUT");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Find the best path using most constrained variable
	 * @return
	 * The final graph colored in, if found. Returns null if no solution found.*/
	 
	private boolean colorGraphMCV(int k){		
		Vertex cur = graph.getVertex(k);
		
		/* Find the colors this vertex can use */
		ArrayList<Integer> possibleColors = cur.getPossibleColors();
		/* Try to set colors */
		int possibleSize = possibleColors.size();
		if(possibleSize > 1){
			nodesExplored += possibleSize;
		}
		for(int i = 0; i < possibleSize; i++){
			int color = setNodeColorFC(k, possibleColors.get(i));
			if(color == 0){
				return false;
			}
			cur.setColor(color);
			if(graph.isColored()){
				return true;
			}
			else{
				 Vertex next = graph.getNextVertex();
				 if(colorGraphMCV(next.getValue())){
					 return true;
				 }
				 else{
					 /* color failed, reset */
					 for(Vertex v: graph.edgesOf(graph.getVertex(k))){
						 int val = v.getPrunedColors().get(color - 1);
						 v.getPrunedColors().set(color - 1, val - 1);
					 }
					 graph.getVertex(k).setColor(0);
				 }
			}
		}
		return false;	
	}
	
	/**
	 * Get the nodes explored for the last algorithm run
	 * @return the nodes explored
	 */
	public int getNodesExplored(){
		return nodesExplored;
	}	
}
