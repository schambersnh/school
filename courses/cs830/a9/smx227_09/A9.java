import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * A8.java
 * Does simple event parsing 
 */

/**
 * @author Stephen
 *
 */
public class A9 {

	public static boolean localTesting = false;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(localTesting){
			//String filename = "spec_input.mdp";
			//String filename = "sample.mdp";
			//String filename = "track-10-10-3-2.mdp";
			//String filename = "track-10-10-3-3.mdp";
			//String filename = "track-20-20-3-3.mdp";
			String filename = "track-20-20-7-5.mdp";

			 //********** DELETE THIS DELETE THIS DELETE THIS ***************
			try {
				System.setIn(new FileInputStream(filename));
			} catch (FileNotFoundException e) {
				System.err.println("Warning, did not find file");
			}
		}
		 //********** DELETE THIS DELETE THIS DELETE THIS ***************

		if(args.length >= 0 && args.length < 3){
			System.err.println("Usage: <algorithm> <discount> <term-criterion>");
			System.exit(1);
		}
		else if(args.length > 3){
			System.err.println("Error: Too many arguments.");
			System.exit(1);
		}
		else
		{
			String algorithm = args[0];
			double discount = Double.parseDouble(args[1]);
			double termCrit = Double.parseDouble(args[2]);
			
			
			/* Populate initial world state */
			Scanner stdin = new Scanner(System.in);
			String configFile = removeComments(stdin);
			Scanner confScan = new Scanner(configFile);
			
			confScan.skip("number of states:");
			int numStates = confScan.nextInt();
			confScan.skip("\nstart state:");
			int startState = confScan.nextInt();
			//burn a new line
			confScan.nextLine();
			ArrayList<State> stateList = new ArrayList<State>();
			for(int i = 0; i < numStates; i++){
				/* parse the state */
				Scanner stateScan = new Scanner(confScan.nextLine());
				State s = new State(i, Double.parseDouble(stateScan.next()), Integer.parseInt(stateScan.next()));
				int numActions = Integer.parseInt(stateScan.next());
				for(int j = 0; j < numActions; j++){
					Action newAction = new Action(j);
					Scanner actionScan = new Scanner(confScan.nextLine());
					int numSuccessors = Integer.parseInt(actionScan.next());
					for(int k = 0; k < numSuccessors; k++){
						newAction.addSuccessor(Integer.parseInt(actionScan.next()), Double.parseDouble(actionScan.next()));
					}
					s.addAction(newAction);
					actionScan.close();
				}
				stateScan.close();
				stateList.add(s);
			}
			//System.out.println("yo");
			ProblemInfo.setInstance(stateList, algorithm, discount, termCrit, startState);
			Planner planner = new Planner(stateList);
			ArrayList<Action> policy = planner.getPolicy();
			for(Action a: policy){
				if(a == null){
					System.out.println();
				}
				else{
					System.out.println(a.getNum());
				}
			}
			System.out.println(planner.getNumBackups() + " backups performed.");
			confScan.close();
		}
	}
	/**
	 * Remove all comments and extraneous white space
	 *  and return a string for easier parsing
	 * @return
	 */
	private static String removeComments(Scanner in){
		StringBuilder sb = new StringBuilder("");
		while(in.hasNext()){
			String line = in.nextLine();
			if(line.equals("") || line.charAt(0) == '#')
				continue;
			sb.append(line);
			sb.append('\n');
		}
		return sb.toString();
	}
	
	private static void printStates(ArrayList<State> stateList){
		for(State s: stateList){
			System.out.println("State " + s.getNum());
			System.out.println("Reward: " + s.getReward());
			System.out.println("Possible actions:");
			for(Action a: s.getActions()){
				System.out.println("Action " + a.getNum());
				a.printSuccessors();
			}
		}

	}
}
