import java.util.ArrayList;
import java.util.Random;

public class ProblemInfo { 
	  private static ProblemInfo instance;
	  private String algorithm;
	  private double discount;
	  private double termCrit;
	  private int startState;
	  private Random rng;
	  private ArrayList<State> stateList;
	  
	  private ProblemInfo(ArrayList<State> stateList, String algorithm, double discount, double termCrit, int startState) {
		  this.stateList = stateList;
		  this.algorithm = algorithm;
		  this.discount = discount;
		  this.termCrit = termCrit;
		  this.startState = startState;
		  this.rng = new Random();
	  }
	  
	  public static void setInstance(ArrayList<State> stateList, String algorithm, double discount, double termCrit, int startState){
		  if(instance == null){
			  instance = new ProblemInfo(stateList, algorithm, discount, termCrit, startState);
		  }
	  }
	  
	  public static ProblemInfo getInstance() {
		  return instance;
	  }

	public String getAlgorithm() {
		return algorithm;
	}

	public double getDiscount() {
		return discount;
	}

	public double getTermCrit() {
		return termCrit;
	}	
	
	public int getStartState() {
		return startState;
	}
	
	public ArrayList<State> getStateList(){
		return stateList;
	}
	
	public Random rng(){
		return rng;
	}

	  
}
