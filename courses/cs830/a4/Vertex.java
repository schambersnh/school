import java.util.ArrayList;

/**
 * A simple vertex class. Each vertex has a value, color, and list of connected vertexes.
 * @author Stephen
 *
 */
public class Vertex  {
	private int value;
	private int color;
	private ArrayList<Vertex> neighbors;
	private ArrayList<Integer> removed;
	
	
	/**
	 * Default constructor
	 * @param value
	 * The value of the vertex
	 * @param color
	 * The color of the vertex
	 */
	public Vertex(int value, int numColors){
		removed = new ArrayList<Integer>();
		for(int i = 1; i <= numColors; i++){
			removed.add(0);
		}
		neighbors = new ArrayList<Vertex>();
		this.value = value;
		this.color = 0;
	}
		
	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}
	/**
	 * @return the color
	 */
	public int getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(int color) {
		this.color = color;
	}
	
	/**
	 * @param newNeighbor the vertex neighbor
	 */
	public void addNeighbor(Vertex newNeighbor){
		neighbors.add(newNeighbor);
	}
	
	/**
	 * 
	 * @return the neighbors
	 */
	public ArrayList<Vertex> getNeighbors(){
		return this.neighbors;
	}
		
	/**
	 * 
	 * @return the possible colors
	 */
	public ArrayList<Integer> getPossibleColors(){
		/* Find the colors this vertex can use */
		ArrayList<Integer> possibleColors = new ArrayList<Integer>();
		int size = this.getPrunedColors().size();
		for(int i = 0; i < size; i++){
			if(this.getPrunedColors().get(i) == 0)
				possibleColors.add(i+1);
		}
		return possibleColors;
	}
	
	/**
	 * 
	 * @return the pruned colors
	 */
	public ArrayList<Integer> getPrunedColors(){
		return this.removed;
	}

	public boolean domainWipeout() {
		int size = removed.size();
		for(int i = 0; i < size; i++){
			if(i == 0){
				return false;
			}
		}
		return true;
	}
}
