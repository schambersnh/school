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
public class A8 {

	public static boolean localTesting = false;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(localTesting){
			//String filename = "groceryworld.in";
			//String filename = "coffeeworld-1.in";
			//String filename = "coffeeworld-2.in";
			//String filename = "impossible.in";
			String filename = "study2.in";
			//String filename = "switches.in";
			//String filename = "switches_impossible.in";
			 //********** DELETE THIS DELETE THIS DELETE THIS ***************
			try {
				System.setIn(new FileInputStream(filename));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 //********** DELETE THIS DELETE THIS DELETE THIS ***************

		if(args.length == 0){
			System.err.println("Usage: <weight> <heuristic>");
			System.exit(1);
		}
		else if(args.length > 2){
			System.err.println("Error: Too many arguments.");
			System.exit(1);
		}
		else
		{
			double weight = 0.0;
			String heuristic = "h0";
			for(int i = 0; i < args.length; i++){
				if(args[i].startsWith("h")){
					heuristic = args[i];
				}
				else{
					weight = Double.parseDouble(args[i]);
				}
			}
			/* Populate initial world state */
			Scanner stdin = new Scanner(System.in);
			String configFile = removeComments(stdin);
			Scanner confScan = new Scanner(configFile);
			
			/* skip predicates */
			confScan.nextLine();
			
			/* scan in the constants */
			ArrayList<Constant> constants = new ArrayList<Constant>();
			Scanner constScan = new Scanner(confScan.nextLine());
			constScan.next();
			while(constScan.hasNext()){
				Constant temp = new Constant(constScan.next());
				constants.add(temp);
			}
			constScan.close();
			//System.out.println(constants);

			/* skip number of actions */
			confScan.nextLine();
			
			/* parse the actions */
			ArrayList<Action> actionList = new ArrayList<Action>();
			String line = "";
			while(confScan.hasNextLine()){
				/* parse an action */
				line = confScan.nextLine();
				if(line.startsWith("constants:")){
					/* parse extra constants and break out */
					constScan = new Scanner(line);
					constScan.next();
					while(constScan.hasNext()){
						Constant temp = new Constant(constScan.next());
						constants.add(temp);
					}
					constScan.close();
					break;
				}
				Scanner actionScan = new Scanner(line);
				StringBuilder sb = new StringBuilder();
				sb.append(actionScan.next());
				sb.append("(");
				while(actionScan.hasNext()){
					sb.append(actionScan.next());
					if(actionScan.hasNext())
						sb.append(",");
				}
				sb.append(")");
				Action newAction = new Action(
						GrammarListener.parseSentence(sb.toString()).getPredicates().get(0));
				actionScan.close();
				
				/* parse the preconditions for the action */
				Scanner preScan = new Scanner(confScan.nextLine());
				preScan.next();
				while(preScan.hasNext()){
					String next = preScan.next();
					newAction.addPrecondition(
							GrammarListener.parseSentence(next).getPredicates().get(0));
				}
				preScan.close();
				
				/* parse the negative preconditions for the action */
				Scanner preNegScan = new Scanner(confScan.nextLine());
				preNegScan.next();
				while(preNegScan.hasNext()){
					newAction.addNegPrecondition(
							GrammarListener.parseSentence(preNegScan.next()).getPredicates().get(0));
				}
				preNegScan.close();
				
				/* parse the delete list for the action */
				Scanner delScan = new Scanner(confScan.nextLine());
				delScan.next();
				while(delScan.hasNext()){
					newAction.addToDeleteList(
							GrammarListener.parseSentence(delScan.next()).getPredicates().get(0));
				}
				delScan.close();
				actionList.add(newAction);
				
				/* parse the add list for the action */
				Scanner addScan = new Scanner(confScan.nextLine());
				addScan.next();
				while(addScan.hasNext()){
					newAction.addToAddList(
							GrammarListener.parseSentence(addScan.next()).getPredicates().get(0));
				}
				addScan.close();
			}
			
			/* parse the initial state */
			State init = new State();
			Scanner stateScan = new Scanner(confScan.nextLine());
			stateScan.next();
			while(stateScan.hasNext()){
				init.addPredicate(
						GrammarListener.parseSentence(stateScan.next()).getPredicates().get(0));
			}
			stateScan.close();
			
			/* parse the goal state */
			State goal = new State();
			Scanner goalScan = new Scanner(confScan.nextLine());
			goalScan.next();
			while(goalScan.hasNext()){
				goal.addPredicate(
						GrammarListener.parseSentence(goalScan.next()).getPredicates().get(0));
			}
			goalScan.close();
			
			/* add negative goal conditions */
			Scanner goalNegScan = new Scanner(confScan.nextLine());
			goalNegScan.next();
			while(goalNegScan.hasNext()){
				goal.addNegPredicate(
						GrammarListener.parseSentence(goalNegScan.next()).getPredicates().get(0));
			}
			goalNegScan.close();
			confScan.close();
			
			HashMap<Predicate, List<Action>> groundList = groundActions(actionList, constants);
			
			ProblemInfo.setInstance(weight, heuristic, goal, actionList, groundList);
			init.updateInfo();
			goal.updateInfo();
			Planner planner = new Planner(init);
			Node result = planner.findPath();
			if(result == null){
				System.out.println("No plan found.");
			}
			else{
				int i = 0;
				for(Action a: result.state.getActionsSoFar()){
					String resultString = a.toString();
					resultString = resultString.replaceAll(",|\\(|\\)", " ");
					System.out.println(i + " " + resultString);
					i++;
				}
			}
			System.out.println(planner.getNodesGenerated() + " nodes generated");
			System.out.println(planner.getNodesExpanded() + " nodes expanded");
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
			line = line.replaceAll(", ", ",");
			sb.append(line);
			sb.append('\n');
		}
		return sb.toString();
	}
	
	/**
	 * Ground the actions
	 * @param actionList
	 * @param constants
	 * @return
	 */
	private static HashMap<Predicate,List<Action>> 
	  groundActions(ArrayList<Action> actionList, ArrayList<Constant> constants){
		HashMap<Predicate,List<Action>> retval = new HashMap<Predicate,List<Action>>();
		List<Action> nullList = new ArrayList<Action>();
		retval.put(null, nullList);
		ArrayList<Constant> constsCopy = new ArrayList<Constant>(constants);
		List<List<Constant>> permutations = generatePermutations(constsCopy);
		for(Action action: actionList){
				int pSize = action.getAction().getTermList().size();
				HashSet<List<Constant>> possibleConsts = prunePermutations(pSize, permutations, constants);
				for(List<Constant> possibleCombo: possibleConsts){
					Action copy = new Action(action);
					for(int i = 0; i < pSize; i++){
						copy.substituteConstant(possibleCombo.get(i), 
								(Variable)action.getAction().getTermList().get(i));
					}
					for(Predicate preCond: copy.getPreconditions()){
						if(retval.containsKey(preCond)){
							retval.get(preCond).add(copy);
						}
						else{
							List<Action> newList = new ArrayList<Action>();
							newList.add(copy);
							retval.put(preCond, newList);
						}
					}
					retval.get(null).add(copy);
				}
		}
		return retval;
	}
	
	private static HashSet<List<Constant>> prunePermutations(int pSize,
			List<List<Constant>> permutations, ArrayList<Constant> origConstants) {
		HashSet<List<Constant>> hashSet = new HashSet<List<Constant>>();
		for(List<Constant> constants: permutations){
			List<Constant> smallerPerm = new ArrayList<Constant>();
			for(int i = 0; i < pSize; i++){
				smallerPerm.add(constants.get(i));
			}
			hashSet.add(smallerPerm);
		}
		/* add reflexive actions */
		for(Constant c: origConstants){
			List<Constant> reflexive = new ArrayList<Constant>();
			for(int i = 0; i < origConstants.size(); i++){
				reflexive.add(c);
			}
			hashSet.add(reflexive);
		}
		return hashSet;
	}
	
	public static List<List<Constant>> generatePermutations(List<Constant> original) {
		     if (original.size() == 0) { 
		       List<List<Constant>> result = new ArrayList<List<Constant>>();
		       result.add(new ArrayList<Constant>());
		       return result;
		     }
		     Constant firstElement = original.remove(0);
		     List<List<Constant>> returnValue = new ArrayList<List<Constant>>();
		     List<List<Constant>> permutations = generatePermutations(original);
		     for (List<Constant> smallerPermutated : permutations) {
		       for (int index=0; index <= smallerPermutated.size(); index++) {
		         List<Constant> temp = new ArrayList<Constant>(smallerPermutated);
		         temp.add(index, firstElement);
		         returnValue.add(temp);
		       }
		     }
		     return returnValue;
	}	
}
