import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ProblemInfo { 
	  private static ProblemInfo instance;
	  private double weight;
	  private String heuristic;
	  private State goal;
	  private ArrayList<Action> actionSet;
	  private HashMap<Predicate,List<Action>> groundList;
	  
	  private ProblemInfo(double weight, String heuristic, State goal, 
			  ArrayList<Action> actionSet, HashMap<Predicate, List<Action>> groundList) {
		  this.weight = weight;
		  this.heuristic = heuristic;
		  this.goal = goal;
		  this.actionSet = actionSet;
		  this.groundList = groundList;
	  }
	  
		public static void setInstance(double weight, String heuristic, State goal, 
				  ArrayList<Action> actionList, HashMap<Predicate, List<Action>> groundList){
			  if(instance == null){
				  instance = new ProblemInfo(weight, heuristic, goal, actionList, groundList);
			  }
		  }
		  public static ProblemInfo getInstance() {
		    return instance;
		  }	

	  
	  /**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}


	/**
	 * @return the heuristic
	 */
	public String getHeuristic() {
		return heuristic;
	}


	/**
	 * @return the goal
	 */
	public State getGoal() {
		return goal;
	}


	/**
	 * @return the actionList
	 */
	public ArrayList<Action> getActionSet() {
		return actionSet;
	}


	/**
	 * @return the groundList
	 */
	public HashMap<Predicate, List<Action>> getGroundList() {
		return groundList;
	}
}
