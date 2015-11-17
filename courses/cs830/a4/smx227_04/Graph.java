import java.util.ArrayList;

/**
 * A simple graph class, holding an array of vertexes.
 * @author Stephen
 *
 */
public class Graph {
	private Vertex[] graph;
	private int numVertices;
	
	/**
	 * The graph constructor
	 * Note that graph[0] is null
	 */
	public Graph( int numVertices, int numColors ){
		this.numVertices = numVertices;
		graph = new Vertex[numVertices + 1];
		for(int i = 1; i < numVertices + 1; i++){
			graph[i] = new Vertex(i, numColors);
		}
	}
		
	/**
	 * Add an edge to the graph
	 * @param source the originating vertex
	 * @param dest the end vertex
	 */
	public void addEdge(int source, int dest){
		graph[source].addNeighbor(graph[dest]);
	}
	
	/**
	 * Return the edges of the specified vertex
	 * @param val
	 * @return
	 */
	public ArrayList<Vertex> edgesOf(Vertex v){
		return v.getNeighbors();
	}
	
	/**
	 * Return the vertex with the smallest domain for MCV
	 * @return the vertex with the smallest domain
	 */
	public Vertex getNextVertex(){
		Vertex minVert = null;
		int min = Integer.MAX_VALUE;
		for(int i = 1; i < numVertices + 1; i++){
			if(this.getVertex(i).getColor() == 0){
				int size = this.getVertex(i).getPossibleColors().size();
				if(size < min){
					min = size;
					minVert = this.getVertex(i);
				}
			}
		}
		return minVert;
	}
	
	/**
	 * Returns if the graph is fully colored in or not
	 * @return
	 */
	public boolean isColored(){
		for(int i = 1; i < numVertices + 1; i++){
			if(graph[i].getColor() == 0){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Get Vertex
	 * @param val the key of the vertex
	 * @return the vertex specified by val
	 */
	public Vertex getVertex(int val){
		return graph[val];
	}

	public int getNumVertices() {
		return numVertices;
	}

}
