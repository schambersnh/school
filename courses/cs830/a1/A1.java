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
		//String filename = "spec_input.txt";
		//String filename = "tiny-1.vw";
		//String filename = "tiny-2.vw";
		String filename = "small-1.vw";
		//String filename = "hard-1.vw";
		//String filename = "hard-2.vw";
		//String filename = "charger-1.vw";
		//String filename = "tester";
		 //********** DELETE THIS DELETE THIS DELETE THIS ***************
		try {
			System.setIn(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 //********** DELETE THIS DELETE THIS DELETE THIS ***************

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
			String searchAlg = "";
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
				else{
					System.err.println("Warning: Unrecognized command line argument '" + args[i] + "'");
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
						bounds, battery, chargingStations);
			Node solution = planner.findPath(searchAlg);
			for(int i = 0; i < solution.state.getActionList().length(); i++){
				System.out.println(solution.state.getActionList().charAt(i));
			}
			
			System.out.println(planner.getNodesGenerated() + " nodes generated");
			System.out.println(planner.getNodesExpanded() + " nodes expanded");
			System.out.println("path cost: " + solution.state.getActionList().length());
		}
	}

}
