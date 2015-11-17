import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A3.java
 * Does simple event parsing 
 */

/**
 * @author Stephen
 *
 */
public class A4 {
	private static boolean local = false;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int numColors = 0;
		String searchAlg = "";
		if(args.length == 0){
			System.err.println("Error: Must provide expansion algorithm.");
			System.exit(1);
		}
		else if(args.length > 2){
			System.err.println("Error: Too many arguments.");
			System.exit(1);
		}
		else
		{
			if(args[0].equals("dfs")){
				searchAlg = "dfs";
			} 
			else if(args[0].equals("fc")){
				searchAlg = "fc";
			}
			else if(args[0].equals("mcv")){
				searchAlg = "mcv";
			}
			numColors = Integer.parseInt(args[1]);
		}
		/* Populate initial world state */

		//String filename = "k3";
		//String filename = "k4";
		//String filename = "k5";
		//String filename = "k6";
		//String filename = "k7";
		//String filename = "k8";
		//String filename = "k9";
		//String filename = "k10";
		//String filename = "k30";
		//String filename = "queen5_5.col.txt";
		//String filename = "queen6_6.col.txt";
		//String filename = "queen7_7.col.txt";
		String filename = "queen8_12.col.txt";
		//String filename = "queen8_8.col.txt";
		//String filename = "queen8_8.col.txt";

		 //********** DELETE THIS DELETE THIS DELETE THIS ***************
		if(local){
			try {
				System.setIn(new FileInputStream(filename));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 //********** DELETE THIS DELETE THIS DELETE THIS ***************

		/* Populate initial world state */
		Scanner stdin = new Scanner(System.in);
		String line;
		int numVertices = 0;
		Graph graph = null;
		while(stdin.hasNextLine()){
			line = stdin.nextLine();
			Scanner subscan = new Scanner(line);
			if(line.charAt(0) == 'c'){
				continue;
			}
			else if(line.startsWith("p edge")){
				subscan.next();
				subscan.next();
				numVertices = subscan.nextInt();
				graph = new Graph(numVertices, numColors);
			}
			else if(line.charAt(0) == 'e'){
				subscan.next();
				if(graph == null){
					System.err.println("Error in input, exiting");
					System.exit(1);
				}
				int from = subscan.nextInt();
				int to = subscan.nextInt();
				graph.addEdge(from, to);
				graph.addEdge(to, from);
			}
			subscan.close();
		}
		stdin.close();
		Planner planner = new Planner(graph, searchAlg, numColors);
		if(planner.colorGraph()){
			printResults(numVertices, graph, planner);
		}
		else{
			System.out.println("No solution.");
			System.out.println(planner.getNodesExplored() + " branching nodes explored.");
		}
	}
	private static void printResults(int numVertices, Graph graph, Planner planner){
		int colorsUsed = 0;
		for(int i = 1; i <= numVertices; i++){
			if(graph.getVertex(i).getColor() > colorsUsed){
				colorsUsed = graph.getVertex(i).getColor();
			}
		}
		System.out.println("s col " + colorsUsed);
		for(int i = 1; i <= numVertices; i++){
			System.out.println("l " + i + " " + graph.getVertex(i).getColor());
		}
		System.out.println(planner.getNodesExplored() + " branching nodes explored.");
	}
}