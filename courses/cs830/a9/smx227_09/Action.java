import java.util.ArrayList;


public class Action {
	private int num;
	private ArrayList<Successor> successors;
	
	public Action(int num){
		successors = new ArrayList<Successor>();
		this.num = num;
	}
	
	public int getNum(){
		return num;
	}
	
	public ArrayList<Successor> getSuccessors(){
		return successors;
	}
	
	public void addSuccessor(int state, double prob){
		Successor newSuccessor = new Successor(state, prob);
		successors.add(newSuccessor);
	}
	
	public void printSuccessors(){
		for(Successor s: successors){
			System.out.println("State " + s.getState() + " with probability " + s.getProbability());
		}
	}

	public double getUtility() {
		ProblemInfo info = ProblemInfo.getInstance();
		double utilitySum = 0.0;
		for(Successor s: successors){
			int index = s.getState();
			utilitySum += s.getProbability() * info.getStateList().get(index).getUtility();
		}
		return utilitySum;
	}
}
