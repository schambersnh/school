import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A1.java
 * Does simple event parsing 
 */

/**
 * @author Stephen
 *
 */
public class A1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length == 0){
			System.err.println("Error: Must provide expansion algorithm.");
			System.exit(1);
		}
		else if(args.length > 3){
			System.err.println("Error: Too many arguments.");
			System.exit(1);
		}
		else
		{
			String searchAlg = "";
			String heuristic = "h0";
			boolean battery = false;
			for(int i = 0; i < args.length; i++){
				if(args[i].equals("-battery")){
					battery = true;
				} 
				else if(args[i].equals("uniform-cost")){
					searchAlg = "uniform-cost";
				}
				else if(args[i].equals("depth-first")){
					searchAlg = "depth-first";
				}
				else if(args[i].equals("depth-first-id")){
					searchAlg = "depth-first-id";
				}
				else if(args[i].equals("a-star")){
					searchAlg = "a-star";
					heuristic = args[i+1];
				}
				else if(args[i].equals("greedy")){
					searchAlg = "greedy";
					heuristic = args[i+1];
				}
				else if(args[i].equals("ida-star")){
					searchAlg = "ida-star";
					heuristic = args[i+1];
				}
			}
			/* Populate initial world state */
			Scanner stdin = new Scanner(System.in);
			String line;
			
			int cols = stdin.nextInt();
			int rows = stdin.nextInt();
			
			Point bounds = new Point(cols - 1, rows -1);
			ArrayList<Point> blockedCells = new ArrayList<Point>();
			ArrayList<Point> dirtyCells = new ArrayList<Point>();
			ArrayList<Point> chargingStations = new ArrayList<Point>();
			Point curLocation = new Point();
			for(int i = 0; i < rows; i++){
				line = stdin.next();
				for(int j = 0; j < cols; j++){
					char cur = line.charAt(j);
					if(cur == '#'){
						blockedCells.add(new Point(j, i));
					}
					else if(cur == '*'){
						dirtyCells.add(new Point(j, i));
					}
					else if(cur == '@'){
						curLocation.setLocation(new Point(j, i));;
					}
					else if(cur == ':'){
						chargingStations.add(new Point(j, i));
					}
				}
			}
			Planner planner = new Planner(blockedCells, dirtyCells, curLocation, 
						bounds, battery, chargingStations, heuristic);
			stdin.close();
			Node solution = planner.findPath(searchAlg);
			for(int i = 0; i < solution.state.getActionList().length(); i++){
				System.out.println(solution.state.getActionList().charAt(i));
			}
			
			System.out.println(planner.getNodesGenerated() + " nodes generated");
			System.out.println(planner.getNodesExpanded() + " nodes expanded");
		}
	}

}
